package biz.belcorp.consultoras.feature.search.single

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.support.v4.app.ActivityCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.base.SafeLet
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.model.client.ClienteModel
import biz.belcorp.consultoras.common.model.error.ErrorModel
import biz.belcorp.consultoras.common.model.mensajeprol.MensajeProlDataMapper
import biz.belcorp.consultoras.common.model.orders.OrderModel
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.util.OfferTypes
import biz.belcorp.consultoras.feature.client.di.ClientComponent
import biz.belcorp.consultoras.feature.ficha.common.BaseFichaActivity
import biz.belcorp.consultoras.feature.ficha.common.BaseFichaActivity.Companion.ACCESS_FROM_CAROUSEL_SUGERIDOS
import biz.belcorp.consultoras.feature.ficha.producto.FichaProductoActivity
import biz.belcorp.consultoras.feature.home.fest.FestActivity
import biz.belcorp.consultoras.feature.home.ganamas.GanaMasFragment
import biz.belcorp.consultoras.feature.search.VoiceRecognitionDialog
import biz.belcorp.consultoras.util.Belcorp
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.ToastUtil
import biz.belcorp.consultoras.util.UXCamUtils
import biz.belcorp.consultoras.util.analytics.AddOrder as AnalyticsAddOrder
import biz.belcorp.consultoras.util.anotation.*
import biz.belcorp.consultoras.util.anotation.OffersOriginType.ORIGEN_RECOMENDADO_FICHA
import biz.belcorp.consultoras.util.analytics.*
import biz.belcorp.consultoras.util.broadcast.FestBroadcast
import biz.belcorp.library.util.*
import biz.belcorp.mobile.components.core.helpers.ImagesHelper
import biz.belcorp.mobile.components.design.counter.Counter
import biz.belcorp.mobile.components.dialogs.bottom.BottomDialog
import biz.belcorp.mobile.components.offers.MiniOffer
import biz.belcorp.mobile.components.offers.Offer
import biz.belcorp.mobile.components.offers.model.OfferModel
import com.uxcam.UXCam
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_search_product.*
import java.text.DecimalFormat
import javax.inject.Inject


class SearchProductFragment : BaseFragment(),
    SearchProductView, SuggestList.SuggestListener,
    VoiceRecognitionDialog.VoiceRecognitionListener, SafeLet, MiniOffer.Listener {

    @Inject
    lateinit var presenter: SearchProductPresenter
    private var moneySymbol: String = ""
    private var clienteModel: ClienteModel? = null
    private var tooltipProduct: ProductTooltip? = null
    private var tooltipError: ProductTooltip? = null
    private var listener: Listener? = null
    private var decimalFormat: DecimalFormat = DecimalFormat()
    private var extraCuv: String = ""
    private var cantidad: Int = 1
    private var isVoiceSearch = false
    private var configuration: List<UserConfigData?>? = null
    var imageDialog: Boolean = false
    private var mensajes: Collection<MensajeProl?>? = null
    private var configFest: FestivalConfiguracion? = null
    private var listTagItemOrder: MutableMap<Int, String?> = mutableMapOf()
    private var orderModel: OrderModel? = null

    private var user: User? = null

    private val regex = "-?\\d+(\\.\\d+)?".toRegex()
    private val quantityText = "cantidad"

    private var productCUV: ProductCUV? = null
    private var type: Int = TYPE_SUGGEST_PRODUCT
    var addedRelated = false
    lateinit var listofferx:List<ProductCUV?>
    private lateinit var imageHelper: ImagesHelper

    private var suggestListener: SuggestList.SuggestListener? = null
    private var nameListTracker: String? = null
    private var nameListTrackerSuggest: String? = null

    private var handler = Handler()
    private val delay: Long = 1200

    @Throws(IllegalStateException::class)
    override fun onInjectView(): Boolean {
        getComponent(ClientComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        this.presenter.attachView(this)
        presenter.data()
        presenter.getRelatedConfig(UserConfigAccountCode.RECOMDS)
        init()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        UXCam.tagScreenName(UXCamUtils.SearchProductFragmentName)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search_product, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Listener) { listener = context }
        if (context is SuggestList.SuggestListener) { suggestListener = context }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            BaseFichaActivity.RESULT -> {
                val returnIntent = Intent()
                mensajes?.let { messages ->
                    if (messages.isNotEmpty()) {
                        val bundle = Bundle()
                        bundle.putParcelableArrayList(BaseFichaActivity.EXTRA_MENSAJE_PROL,
                            MensajeProlDataMapper().transformToDomainModel(messages) as java.util.ArrayList<out Parcelable>)
                        returnIntent.putExtra(BaseFichaActivity.EXTRA_BUNDLE, bundle)
                    }
                }

                activity?.setResult(Activity.RESULT_OK, returnIntent)
                activity?.finish()

            }
        }
    }

    override fun setImageEnabled(imageDialog: Boolean) {
        this.imageDialog = imageDialog
    }

    @SuppressLint("SetTextI18n")
    private fun init() {

        activity?.let {
            imageHelper = ImagesHelper(it)
        }

        presenter.getImageEnabled()

        arguments?.getString(SearchProductActivity.EXTRA_COUNTRYISO)?.let {
            this.decimalFormat = CountryUtil.getDecimalFormatByISO(it, true)
        }

        arguments?.getString(SearchProductActivity.EXTRA_TITLE)?.let {
            activity?.tvw_toolbar_title?.text = it
        }

        moneySymbol = arguments?.getString(SearchProductActivity.EXTRA_MONEYSYMBOL) ?: ""
        clienteModel = arguments?.getParcelable(SearchProductActivity.EXTRA_CLIENTEMODEL)
        orderModel = arguments?.getParcelable(SearchProductActivity.EXTRA_ORDER_MODEL)

        if(clienteModel != null){
            tvwClientFilter.text = clienteModel?.nombres +
                if (clienteModel?.apellidos != null) " " + clienteModel?.apellidos else ""
        }

        presenter.getNameListTracker(OffersOriginTypeLocation.ORIGEN_PEDIDO, OffersOriginTypeSection.ORIGEN_RECOMENDADO, true)

        presenter.getNameListTracker(OffersOriginTypeLocation.ORIGEN_PEDIDO, OffersOriginTypeSection.ORIGEN_DIGITADO, false)

        edtProductFilter.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(cs: CharSequence, start: Int, before: Int, count: Int) {
                if(tooltipProduct != null) tooltipProduct?.remove()
                if(tooltipError != null) tooltipError?.remove()
                if(lnlEmpty.visibility == View.VISIBLE) lnlEmpty.visibility = View.GONE

                handler.removeCallbacksAndMessages(null)
                handler = Handler()
                handler.postDelayed({
                    context?.let {context ->
                        if (Belcorp.isValidCuv(context, cs.toString())) {
                            val searched = cs.toString()
                            search(searched)
                            extraCuv = searched
                        }
                    }
                }, delay)

                if(cs.isEmpty()){
                    lnlClose.visibility = View.GONE
                    lnlVoice.visibility = View.VISIBLE
                }else{
                    lnlClose.visibility = View.VISIBLE
                    lnlVoice.visibility = View.GONE
                }

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // EMPTY
            }

            override fun afterTextChanged(s: Editable) {
                // EMPTY
            }
        })

        tvwClientFilter.setOnClickListener {
            listener?.onFilter()
        }

        lnlClose.setOnClickListener{
            edtProductFilter.setText("")
            if(tooltipProduct != null) tooltipProduct?.remove()
            if(tooltipError != null) tooltipError?.remove()
            if(lnlEmpty.visibility == View.VISIBLE) lnlEmpty.visibility = View.GONE
            lnlClose.visibility = View.GONE
        }

        lnlVoice.setOnClickListener {
            Tracker.trackEvent(GlobalConstant.SCREEN_ORDERS_GPR,
                GlobalConstant.EVENT_CATEGORY_PEDIDO, GlobalConstant.EVENT_ACTION_BUTTON,
                GlobalConstant.EVENT_LABEL_PEDIDO_VOZ,
                GlobalConstant.EVENT_NAME_VIRTUAL_EVENT, user)
            showVoiceRecognition()
        }

        arguments?.getBoolean(GlobalConstant.FROM_CLIENT_CARD, false)?.let {
            if(it){
                clienteModel?.let {
                    rltCliente.visibility = View.GONE
                    tvwClientFilterDisabled.text = """${it.nombres} ${it.apellidos}""".trim()
                    tvwClientFilterDisabled.isEnabled = false
                    rltClienteDisabled.visibility = View.VISIBLE
                }

            }
        }

        edtProductFilter.requestFocus()

        Handler().postDelayed({
            if (arguments?.getBoolean(SearchProductActivity.EXTRA_VOICE) == true) {
                showVoiceRecognition()
            } else {
                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(edtProductFilter, InputMethodManager.SHOW_FORCED)
            }
        }, 300)

    }

    override fun context(): Context? {
        val activity = activity
        return activity?.applicationContext
    }

    fun trackBackPressed() {
        presenter.trackBackPressed()

    }

    fun getClientModel(): ClienteModel? {
        return clienteModel
    }

    /**  SearchProductView   */

    override fun setData(user: User) {
        this.user = user
    }

    override fun getRelatedOffers(productCUV: ProductCUV) {

        configuration?.let { config ->

            val minimoResultados = config.find { it?.code == SuggestConfigCode.MINIMO_RESULTADOS }?.value1?.toInt()
            val maximoResultados = config.find { it?.code == SuggestConfigCode.MAXIMO_RESULTADOS }?.value1?.toInt()
            val caracteresDescripcion = config.find { it?.code == SuggestConfigCode.CARACTERES_DESCRIPCION }?.value3?.toInt()

            productCUV.let {
                presenter.getRelatedOffers(it, minimoResultados, maximoResultados, caracteresDescripcion)
            }
        }

    }

    override fun setRelatedConfig(config: List<UserConfigData?>?) {
        this.configuration = config
    }

    override fun initScreenTrack(model: LoginModel) {
        Tracker.trackScreen(GlobalConstant.SCREEN_ORDERS_GPR, model)
    }

    override fun trackBackPressed(model: LoginModel) {
        Tracker.trackBackEvent(GlobalConstant.SCREEN_ORDERS_GPR, model)
    }

    override fun productNotFound() {
        rlt_product.removeAllViews()
        tooltipError = ProductTooltip.Builder(activity)
            .into(rlt_product)
            .withAnchor(edtProductFilter)
            .setError(null, null)
            .build()
        tooltipError?.showError()
        cantidad = 1
        hideLoading()
    }

    override fun showCUV(productCUV: ProductCUV, msg: String) {
        rlt_product.removeAllViews()
        this.productCUV = productCUV
        tooltipProduct = ProductTooltip.Builder(activity)
            .into(rlt_product)
            .withAnchor(edtProductFilter)
            .withQuantity(cantidad)
            .withSupport(activity!!.supportFragmentManager)
            .withParameters(ProductParams().apply {
                prdCUV = productCUV
                clntMdl = clienteModel
                prdPrsntr = presenter
                dcmlFrmt = decimalFormat
                mnySymb = moneySymbol
                listTagOrder = listTagItemOrder
                order = orderModel
            })
            .setMessagePopup(msg)
            .build()
        tooltipProduct?.show()
        cantidad = 1
        hideLoading()

    }

    override fun showCUVWithError(t: BasicDto<ProductCUV>) {
        rlt_product.removeAllViews()
        tooltipProduct =
            t.message?.let {
                ProductTooltip.Builder(activity)
                    .into(rlt_product)
                    .withAnchor(edtProductFilter)
                    .setError(it, "")
                    .build()
            }

        tooltipProduct?.showError()
        hideLoading()
    }

    override fun showCUVWithError(t: String?) {

        rlt_product.removeAllViews()
        t?.let {
            tooltipProduct = ProductTooltip.Builder(activity)
                .into(rlt_product)
                .withAnchor(edtProductFilter)
                .setError(it, "")
                .build()

            tooltipProduct?.showError()
        }
        cantidad = 1
        hideLoading()
    }

    override fun onError(errorModel: ErrorModel) {
        processGeneralError(errorModel)
    }

    override fun errorAddingProduct(mensaje: String) {
        tooltipProduct?.showErrorAdding(mensaje)
        hideLoading()
    }

    override fun showToolTipError(messaje: String?) {
        val returnIntent = Intent()
        val bundle = Bundle()
        bundle.putString(SearchProductActivity.MESSAGE_ADDING,messaje)
        returnIntent.putExtra(SearchProductActivity.EXTRA_BUNDLE, bundle)
        activity?.setResult(Activity.RESULT_OK, returnIntent)
        activity?.finish()
    }

    override fun onProductAdded(message: String?) {
        onAdded(message, AddedAlertType.DEFAULT)
    }

    override fun onFestivalAwardReached(message: String?) {
        onAdded(message, AddedAlertType.FESTIVAL)
    }

    fun onAdded(message: String?, typeAlert: Int){

        // Analytics
        user?.let { user ->
            productCUV?.let {

                val onAnalyticsLoaded: (descriptionPalanca: String) -> Unit = { descriptionPalanca ->
                    AnalyticsAddOrder.addToCartOffer(user, it, if(it.isSugerido) nameListTrackerSuggest else nameListTracker, descriptionPalanca)
                }

                presenter.getAnalytics(it.tipoPersonalizacion, onAnalyticsLoaded)
            }
        }

        activity?.let { FestBroadcast.sendAddUpdateToCart(it) }

        if (this.type == TYPE_RELATED_OFFER) {
            addedRelated = true
            // Borrar esto para evitar que vuelva a la pantalla de atras
            Handler().postDelayed({ sendBack(message, true, typeAlert) }, 500)
        }
        else {
            sendBack(message, true, typeAlert)
        }
    }

    override fun onProductNotAdded(message: String?) {
        message?.let {
            context?.let { context ->
                BottomDialog.Builder(context)
                    .setIcon(R.drawable.ic_mano_error)
                    .setTitle("¡Ups!")
                    .setTitleBold()
                    .setContent(it)
                    .setNeutralText(getString(R.string.msj_entendido))
                    .onNeutral(object : BottomDialog.ButtonCallback {
                        override fun onClick(dialog: BottomDialog) {
                            dialog.dismiss()
                        }
                    })
                    .setNeutralBackgroundColor(R.color.magenta)
                    .show()
            }
        }
    }

    fun sendBack(message : String? = null, fromOnProductAdded: Boolean? = null, typeAlert: Int? = AddedAlertType.DEFAULT){
        val returnIntent = Intent()
        val bundle = Bundle()
        if(clienteModel != null){
            bundle.putParcelable(SearchProductActivity.EXTRA_CLIENTEMODEL, clienteModel)
        }
        bundle.putString(SearchProductActivity.EXTRA_CUV, extraCuv)
        productCUV?.let {
            bundle.putInt(SearchProductActivity.EXTRA_CUV_QT, it.cantidad!!)
            bundle.putInt(SearchProductActivity.EXTRA_CUV_BRAND_ID, it.marcaId!!)
            bundle.putBoolean(SearchProductActivity.EXTRA_VOICE, isVoiceSearch)
        }

        if(fromOnProductAdded == true){
            returnIntent.putExtra(SearchProductActivity.FROM_ONPRODUCT_ADDED,  true)
        }

        mensajes?.let {
            if(it.isNotEmpty()) {
                bundle.putParcelableArrayList(SearchProductActivity.EXTRA_MENSAJE_PROL, MensajeProlDataMapper().transformToDomainModel(it) as ArrayList<out Parcelable>)
            }
        }

        returnIntent.putExtra(SearchProductActivity.EXTRA_BUNDLE, bundle)
        returnIntent.putExtra(SearchProductActivity.EXTRA_MESSAGE_ADDING, message)
        returnIntent.putExtra(SearchProductActivity.EXTRA_TYPE_ALERT, typeAlert)
        activity?.setResult(Activity.RESULT_OK, returnIntent)
        activity?.finish()
    }

    private fun checkSelectionType(codigoEstrategia: Int): Boolean {
        return (codigoEstrategia == GanaMasFragment.COD_EST_COMPUESTA_VARIABLE)
    }

    private fun formatWithMoneySymbol(precio: String): String {
        return "$moneySymbol ${decimalFormat.format(precio.toBigDecimal())}"
    }

    private fun transformListProductCUV(offers: List<ProductCUV?>): ArrayList<OfferModel> {
        val list = arrayListOf<OfferModel>()

        for (offer in offers) {

            var showAmount = true
            if (offer?.tipoPersonalizacion == OfferTypes.HV) {
                showAmount = false
            }

            safeLet(offer?.cuv, offer?.descripcionMarca ?: "", offer?.description,
                offer?.precioValorizado?:0, offer?.precioCatalogo, offer?.fotoProductoSmall ?: "",
                "", "#000000", offer?.codigoEstrategia ?: 0,
                offer?.marcaId ?: 0, offer?.tipoPersonalizacion ?: "")
            { cuv, nombreMarca, nombreOferta, precioValorizado, precioCatalogo, imagenURL,
              imagenFondo, colorTexto, codigoEstrategia, marcaID, tipoPersonalizacion ->
                list.add(Offer.transform(cuv, nombreMarca, nombreOferta,
                    formatWithMoneySymbol(precioCatalogo.toString()),
                    formatWithMoneySymbol(precioValorizado.toString()),
                    imagenURL, "", checkSelectionType(codigoEstrategia), marcaID,
                    imageHelper.getResolutionURL(imagenFondo), colorTexto, showAmount, "", tipoPersonalizacion))
            }

        }

        return list
    }

    override fun showAlternative(type:Int, mensaje: String? ,productList: Collection<ProductCUV?>, total:Int?) {

        if(productList.isNotEmpty()){

            listofferx = productList.toList()

            suggestOffers.tag = type
            suggestOffers.listener = this
            suggestOffers.updateItemPlaceHolder(resources.getDrawable(R.drawable.ic_container_placeholder))

            if (productList.size == 1) suggestOffers.updateSimpleOfert()

            suggestOffers.setProducts(transformListProductCUV(listofferx))
        }

        if (type == TYPE_SUGGEST_PRODUCT) {

            rlt_product.removeAllViews()
            suggestOffers.updateTitle(mensaje ?: resources.getString(R.string.search_agotado))
            tooltipProduct = ProductTooltip.Builder(activity)
                .into(rlt_product)
                .withAnchor(edtProductFilter)
                .setError("Este producto no cuenta con Stock", "")
                .build()

            tooltipProduct?.showError()
            lnlEmpty.visibility = View.VISIBLE

        } else {
            suggestOffers.updateTitle(resources.getString(R.string.search_oferta_relacionada))
            configuration?.let { config ->
                val minimoResultados = config.find { it?.code == SuggestConfigCode.MINIMO_RESULTADOS }?.value1?.toInt() ?: 0
                total?.let {
                    if(it >= minimoResultados && productList.isNotEmpty()) lnlEmpty.visibility = View.VISIBLE
                }
            }
        }

        hideLoading()
    }

    @SuppressLint("SetTextI18n")
    fun onComplete(clienteModel: ClienteModel) {
        this.clienteModel = clienteModel
        tooltipProduct?.changeClienteModel(clienteModel)
        tvwClientFilter.text = clienteModel.nombres + if (clienteModel.apellidos != null) " " + clienteModel.apellidos else ""
    }

    /**  SuggestList.SuggestListener  */

    override fun addSuggestOffer(type:Int, productCUV: ProductCUV) {

        if (NetworkUtil.isThereInternetConnection(context())) {

            this.productCUV = productCUV
            this.type = type

            clienteModel?.let {
                productCUV.clienteId = it.clienteID
                productCUV.clienteLocalId = it.id
            }
            productCUV.identifier = DeviceUtil.getId(activity)
            productCUV.isSugerido = true
            productCUV.limiteVenta = 0
            extraCuv = productCUV.cuv ?: ""

            if (suggestOffers.tag == TYPE_RELATED_OFFER) {

                val codigo = OffersOriginType.ORIGEN_RECOMENDADO_CARRUSEL

                val palanca = if (productCUV.tipoPersonalizacion == OfferTypes.OPM) {

                    when (productCUV.isMaterialGanancia) {
                        true -> OfferTypes.MG
                        else -> OfferTypes.RD

                    }

                } else {
                    productCUV.tipoPersonalizacion.toString()
                }

                presenter.getValorOrigen(palanca, codigo, productCUV)

            } else {

                presenter.insertHomologado(productCUV, DeviceUtil.getId(requireContext()))

            }

        } else {
            showNetworkError()
        }
    }

    override fun updateOriginValue(value: String, product: ProductCUV) {
        product.apply {
            origenPedidoWeb = value
        }
        presenter.insertHomologado(product, DeviceUtil.getId(requireContext()))
    }

    /**  VoiceRecognitionListener  */

    override fun onRecognitionSuccess(found: String) {
        checkVoiceResult(found.trim(), true)
    }

    override fun onRecognitionCancel(retryActive: Boolean) {
        if (retryActive) {
            Tracker.trackEvent(GlobalConstant.SCREEN_PEDIDO_VOZ_ERROR,
                GlobalConstant.CATEGORY_PEDIDO_VOZ, GlobalConstant.EVENT_ACTION_BUTTON,
                GlobalConstant.LABEL_CERRAR_POPUP_VOLVER_INTENTARLO,
                GlobalConstant.EVENT_NAME_VIRTUAL_EVENT, user)
        }else {
            Tracker.trackEvent(GlobalConstant.SCREEN_PEDIDO_VOZ,
                GlobalConstant.CATEGORY_PEDIDO_VOZ, GlobalConstant.EVENT_ACTION_BUTTON,
                GlobalConstant.LABEL_CERRAR_AGREGAR_POR_VOZ,
                GlobalConstant.EVENT_NAME_VIRTUAL_EVENT, user)
        }
    }

    override fun onRecognitionRetry() {
        Tracker.trackEvent(GlobalConstant.SCREEN_PEDIDO_VOZ_ERROR,
            GlobalConstant.CATEGORY_PEDIDO_VOZ, GlobalConstant.EVENT_ACTION_BUTTON,
            GlobalConstant.LABEL_VOLVER_INTENTARLO,
            GlobalConstant.EVENT_NAME_VIRTUAL_EVENT, user)
    }

    override fun onRecognitionDismiss() {
        // EMPTY
    }

    /**  Private Functions  */

    private fun search(cuv: String) {
        KeyboardUtil.dismissKeyboard(context, edtProductFilter)
        if (NetworkUtil.isThereInternetConnection(context)) {
            showLoading()
            AnalyticsAddOrder.buttonSearch(if(this.isVoiceSearch) GlobalConstant.EVENT_LABEL_INGRESAR_PRODUCTO_VOZ else GlobalConstant.EVENT_LABEL_INGRESAR_PRODUCTO)
            presenter.buscarCUV(cuv)
        }
    }

    private fun checkVoiceResult(result: String, tryAgain: Boolean) {

        val message = resources.getString(R.string.search_order_error)
        val data = result.split(" ")
        val check = checkWords(result)

        if (check[0] && check[1] && check[2]) {
            edtProductFilter.setText(data[0].trim())
            edtProductFilter.setSelection(data[0].trim().length)
            data[2].trim().toInt().let {
                cantidad = when {
                    it <= 0 -> 1
                    it >= 100 -> 99
                    else -> it
                }
            }

            Tracker.trackEvent(GlobalConstant.SCREEN_PEDIDO_VOZ,
                GlobalConstant.CATEGORY_PEDIDO_VOZ, GlobalConstant.ACTION_AGREGAR_PEDIDO_VOZ,
                data[0], GlobalConstant.EVENT_NAME_VIRTUAL_EVENT, user)

            return
        }

        if(check[0] && (!check[1] || check[1])  && !check[2]){
            edtProductFilter.setText(data[0].trim())
            edtProductFilter.setSelection(data[0].trim().length)

            Tracker.trackEvent(GlobalConstant.SCREEN_PEDIDO_VOZ,
                GlobalConstant.CATEGORY_PEDIDO_VOZ, GlobalConstant.ACTION_AGREGAR_PEDIDO_VOZ,
                data[0], GlobalConstant.EVENT_NAME_VIRTUAL_EVENT, user)

            return
        }

        if(!check[0]){
            if(tryAgain) checkVoiceResult(checkAgain(result), false)
        }

        if(!tryAgain) ToastUtil.show(context!!, message, Toast.LENGTH_LONG)
    }

    private fun checkWords(words: String): Array<Boolean>{
        val results = arrayOf(false, false, false)
        context?.let {context ->
            words.split(quantityText).let {
                if (it.size >= 1 && Belcorp.isValidCuv(context, it[0].trim())) results[0] = true
                if (it.size >= 2 && it[1].trim().matches(regex)) {
                    results[1] = true
                    results[2] = true
                }
            }
        }
        return results
    }

    private fun checkAgain(words: String): String{
        var (first, second, third) = arrayOf("", "", "")
        val concat = words.replace("\\s".toRegex(), "")

        context?.let {context ->
            concat.split(quantityText).let {
                if (it.size >= 1 && Belcorp.isValidCuv(context, it[0].trim())){
                    first = it[0].trim()
                    second = quantityText
                }
                if (it.size >= 2 && it[1].trim().matches(regex)) third = it[1].trim()
            }
        }

        return "$first $second $third".trim()
    }

    fun showVoiceRecognition(){

        isVoiceSearch = true

        context?.let{ context ->
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(PermissionUtil.isRecordAudioGranted(context)){
                    VoiceRecognitionDialog(context,
                        context.resources.getString(R.string.voice_recognition_order),
                        context.resources.getString(R.string.voice_recognition_order_sub),
                        this).show()
                }else{
                    ActivityCompat.requestPermissions(activity!!,
                        arrayOf(Manifest.permission.RECORD_AUDIO),
                        SearchProductActivity.RECORD_REQUEST_CODE)
                }
            }else{
                VoiceRecognitionDialog(context,
                    context.resources.getString(R.string.voice_recognition_order),
                    context.resources.getString(R.string.voice_recognition_order_sub),
                    this).show()
            }
        }


    }

    override fun showErrorExcedido(message: String) {
        if(tooltipProduct != null && message != null){
            tooltipProduct!!.showErrorAdding(message)
        }
    }

    override fun onMensajeProl(mensajes: Collection<MensajeProl?>?) {
        this@SearchProductFragment.mensajes = mensajes;
    }

    /**
     * MiniOffer Listeners
     */

    override fun didPressedItemButtonAdd(typeLever: String, keyItem: String, quantity: Int, counterView: Counter, type: Any?) {
        val indexFilter = listofferx.indexOfFirst { it?.cuv == keyItem }

        if (indexFilter >= 0) {
            listofferx[indexFilter]?.let { producto ->
                producto.cantidad = quantity
                addSuggestOffer(type as Int, producto)
            }
        }
    }

    override fun didPressedItem(item: OfferModel, pos: Int, type: Any?) {
        if (type == TYPE_RELATED_OFFER) {

            val onAnalyticsLoaded: (descriptionPalanca: String) -> Unit = { descriptionPalanca ->
                AnalyticsAddOrder.clickOffer(user?.countryISO, item, nameListTrackerSuggest, descriptionPalanca)
            }

            presenter.getAnalytics(item.tipoPersonalizacion, onAnalyticsLoaded)

            goToFichaResumida(item.tipoPersonalizacion ?: "", item.key, item.marcaID, item.brand, type as Int)
        }
    }

    override fun didPressedItemButtonShowOffer(item: OfferModel, pos: Int, type: Any?) {
        if (type == TYPE_RELATED_OFFER) {
            goToFichaResumida(item.tipoPersonalizacion ?: "", item.key, item.marcaID, item.brand, type as Int)
        }
    }

    private fun goToFichaResumida(type: String, keyItem: String, marcaID: Int, marca: String, typeCarousel:Int) {

        val extras = Bundle()
        extras.putInt(BaseFichaActivity.EXTRA_MARCA_ID, marcaID)
        extras.putInt(BaseFichaActivity.EXTRA_ACCESS_FROM, ACCESS_FROM_CAROUSEL_SUGERIDOS)
        extras.putInt(BaseFichaActivity.EXTRA_FICHA_OFFER_TYPE, CarouselType.DEFAULT)
        extras.putString(BaseFichaActivity.EXTRA_KEY_ITEM, keyItem)
        extras.putString(BaseFichaActivity.EXTRA_TYPE_OFFER, type)
        extras.putString(BaseFichaActivity.EXTRA_MARCA_NAME, marca)
        extras.putString(BaseFichaActivity.EXTRA_ORIGEN_PEDIDO_WEB_FROM, ORIGEN_RECOMENDADO_FICHA)
        extras.putString(BaseFichaActivity.EXTRA_TYPE_FICHA, FichaType.PRODUCT_SIMPLE)
        startActivityForResult(FichaProductoActivity.getCallingIntent(requireContext(), extras), SearchProductActivity.SEARCH_RESULT)

    }

    override fun didPressedItemButtonSelection(item: OfferModel, pos: Int, type: Any?) {}

    override fun impressionItems(typeLever: String, list: ArrayList<OfferModel>, type: Any?) {
        val listResult = listofferx.filter { item -> list.any { it.key == item?.cuv } }

        val onAnalyticsLoaded: (list: List<ProductCUV?>) -> Unit = { listx ->
            AnalyticsAddOrder.impressionOffers(user, listx.filterNotNull(), this.nameListTrackerSuggest)
        }

        presenter.getAnalyticsCarousel(listResult, onAnalyticsLoaded)
    }

    override fun setNameListTracker(nameList: String?) {
        this.nameListTracker = nameList
    }

    override fun setNameListTrackerSuggest(nameList: String?) {
        this.nameListTrackerSuggest = nameList
    }

    override fun setConfigFest(configFest: FestivalConfiguracion?) {
        this.configFest = configFest
        this.listTagItemOrder[OrderItemTag.TAG_FEST] = configFest?.Titulo
    }

    interface Listener {
        fun onFilter()
    }

    companion object {
        const val TYPE_SUGGEST_PRODUCT = 0
        const val TYPE_RELATED_OFFER = 1
        const val TYPE_FESTIVAL_ADD = 2
    }

}
