package biz.belcorp.consultoras.feature.ficha.common

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.ShareCompat
import android.support.v4.content.ContextCompat
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import biz.belcorp.consultoras.BuildConfig
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.base.SafeLet
import biz.belcorp.consultoras.common.dialog.MessageDialog.MessageDialogListener
import biz.belcorp.consultoras.common.model.client.ClienteModel
import biz.belcorp.consultoras.common.model.error.ErrorModel
import biz.belcorp.consultoras.common.model.orders.OrderModel
import biz.belcorp.consultoras.common.model.product.ProductCUVModel
import biz.belcorp.consultoras.common.model.product.ProductItem
import biz.belcorp.consultoras.common.model.promotion.PromotionOfferModel
import biz.belcorp.consultoras.common.model.search.OrigenModel
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.util.OfferTypes
import biz.belcorp.consultoras.domain.util.OfferTypes.getOfferTypeForAnalytics
import biz.belcorp.consultoras.feature.ficha.FichaPresenter
import biz.belcorp.consultoras.feature.ficha.adapter.OfferOptionTonesListener
import biz.belcorp.consultoras.feature.ficha.di.FichaComponent
import biz.belcorp.consultoras.feature.home.fest.FestActivity
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.anotation.OfferTypeCode.KIT_NUEVA_CODE
import biz.belcorp.consultoras.util.TimeUtil
import biz.belcorp.consultoras.util.analytics.Ficha
import biz.belcorp.consultoras.util.anotation.*
import biz.belcorp.library.util.CountryUtil
import biz.belcorp.library.util.NetworkUtil
import biz.belcorp.mobile.components.core.helpers.ImagesHelper
import biz.belcorp.mobile.components.design.boxsection.BoxSection
import biz.belcorp.mobile.components.design.button.Button
import biz.belcorp.mobile.components.design.counter.Counter
import biz.belcorp.mobile.components.dialogs.bottom.BottomDialog
import biz.belcorp.mobile.components.offers.MiniOffer
import biz.belcorp.mobile.components.offers.model.OfferModel
import biz.belcorp.mobile.components.offers.model.ProductModel
import kotlinx.android.synthetic.main.fragment_ficha_base.*
import java.text.DecimalFormat
import javax.inject.Inject

abstract class BaseFichaFragment : BaseFragment(),
    BaseFichaView,
    MessageDialogListener,
    Counter.OnChangeQuantityListener,
    OfferOptionTonesListener,
    MiniOffer.Listener,
    BoxSection.OnBoxSectionListener,
    SafeLet {

    @Inject
    protected lateinit var presenter: FichaPresenter

    protected lateinit var oferta: Oferta
    protected lateinit var imageHelper: ImagesHelper

    protected val scrollBounds = Rect()

    protected var listener: BaseListener? = null

    protected var visibleList = arrayListOf<Oferta>()
    protected var mensajes: Collection<MensajeProl?>? = null
    protected var ofertaMap = mutableMapOf<String, Oferta>()
    protected var origenesBuscador: List<OrigenModel>? = null
    protected var productItem: List<ProductCUVModel>? = null

    protected var mUser: User? = null
    protected var clienteModel: ClienteModel? = null
    protected var decimalFormat: DecimalFormat = DecimalFormat()
    protected var productoEncabezado: ProductModel = ProductModel()
    protected var orderItem: ProductItem? = null
    protected var productOrder: OrderModel? = null

    protected var fichaType: String = FichaType.PRODUCT_SIMPLE
    protected var marca: String? = null
    protected var moneySymbol: String = ""
    protected var origenPedidoWeb: String? = null
    protected var origenPedidoWebFrom: String? = null
    protected var tipoPersonalizacion: String? = null
    protected var type: String? = null

    protected var agotado: Boolean = false
    protected var canBack: Boolean = false
    protected var fromGanaMas: Boolean = false
    protected var tieneCompartir: Boolean = false
    protected var isTimerEnabled: Boolean = false

    protected var accessFrom: Int? = null
    protected var fichaOfferType: Int = CarouselType.DEFAULT
    protected var fichaOfferTypeCarousel: Int = CarouselType.DEFAULT
    protected var marcaID: Int? = null

    protected var isKitNueva: Boolean = false

    var cuv: String? = null
    var mImageMaxFicha: Long? = 0

    override fun onAttach(context: Context?) {

        super.onAttach(context)
        if (context is BaseListener) {
            this.listener = context
        }

    }

    override fun onResume() {

        super.onResume()
        presenter.initScreenTrack()
        startTimer()

    }

    override fun onDestroy() {

        super.onDestroy()
        presenter.destroy()

    }

    override fun context(): Context? {

        val activity = activity
        return activity?.applicationContext

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_ficha_base, container, false)

    @Throws(IllegalStateException::class)
    override fun onInjectView(): Boolean {

        getComponent(FichaComponent::class.java).inject(this)
        return true

    }

    override fun onViewInjected(savedInstanceState: Bundle?) {

        super.onViewInjected(savedInstanceState)
        this.presenter.attachView(this)
        setup()

    }

    private fun setup() {

        setupExtras()   // obtener los valores del bundle
        init()          // inicializar las variables / vistas del fragment
        setupUI()       // recyclers, listas, etc.
        getData()       // usar presenter para obtener la data dependiendo del tipo de ficha

    }

    protected open fun setupExtras() {

        cuv = arguments?.getString(BaseFichaActivity.EXTRA_KEY_ITEM)
        fichaType = arguments?.getString(BaseFichaActivity.EXTRA_TYPE_FICHA)
            ?: FichaType.PRODUCT_SIMPLE
        marcaID = arguments?.getInt(BaseFichaActivity.EXTRA_MARCA_ID)
        marca = arguments?.getString(BaseFichaActivity.EXTRA_MARCA_NAME)
        origenPedidoWeb = arguments?.getString(BaseFichaActivity.EXTRA_ORIGEN_PEDIDO_WEB)
        origenPedidoWebFrom = arguments?.getString(BaseFichaActivity.EXTRA_ORIGEN_PEDIDO_WEB_FROM, BaseFichaActivity.DEFAULT_VALUE)
        tipoPersonalizacion = arguments?.getString(BaseFichaActivity.EXTRA_TYPE_PERSONALIZATION)
        type = arguments?.getString(BaseFichaActivity.EXTRA_TYPE_OFFER)

        fromGanaMas = arguments?.getBoolean(BaseFichaActivity.FROM_GANAMAS) ?: false

        accessFrom = arguments?.getInt(BaseFichaActivity.EXTRA_ACCESS_FROM, BaseFichaActivity.ACCESS_FROM_GANAMAS)  // Por default viene de ganamas
        fichaOfferType = arguments?.getInt(BaseFichaActivity.EXTRA_FICHA_OFFER_TYPE)
            ?: CarouselType.DEFAULT

        origenesBuscador = arguments?.getParcelableArrayList(BaseFichaActivity.EXTRAS_ORIGENES_PEDIDO_SEARCH)   // Lista de Origines que vienen desde el buscador
        productItem = arguments?.getParcelableArrayList(BaseFichaActivity.EXTRAS_PRODUCTO_ITEM)

        orderItem = arguments?.getParcelable(BaseFichaActivity.EXTRA_PRODUCTO)
        productOrder = arguments?.getParcelable(BaseFichaActivity.EXTRA_PRODUCTO_ORDER)

    }

    open fun init() {

        nsvContent.getHitRect(scrollBounds)

        bsClient.listener = this
        simpleCounter.quantityListener = this

        activity?.let { context ->
            imageHelper = ImagesHelper(context)
            viewProduct.setTagBackground(ContextCompat.getColor(context, R.color.tag_new_start), ContextCompat.getColor(context, R.color.tag_new_end), 3)
            viewProduct.setImagePlaceholder(ContextCompat.getDrawable(context, R.drawable.ic_container_placeholder))
        }

        viewProduct.setTagText(getString(R.string.ficha_new))

        viewProduct.visibility = View.GONE
        lnlContent.visibility = View.GONE
        lnlExtraSection.visibility = View.GONE

    }

    protected open fun setupUI() {

        rvwOffersTone.layoutManager = LinearLayoutManager(context)
        rvwOffersTone.isNestedScrollingEnabled = false

        // Asignar listener para evitar que se ejecuten acciones onclick de un contenedor "oculto"
        lnlButtons.setOnClickListener { }
        lnlStatic.setOnClickListener { }
        lnlFloat.setOnClickListener { }

        simpleButton.buttonClickListener = object : Button.OnClickListener {

            override fun onClick(view: View) {

                addFromFicha()
                BaseFichaActivity.PRODUCT_ADDED = true//EAAR

            }

        }

    }

    fun refreshInit() {

        init()
        setupUI()
        getData()

    }

    fun getShareURL() {
        presenter.getShareURL(cuv, type, marcaID, marca, this.oferta)
    }

    fun goToOffers() {
        presenter.getMenuActive(MenuCodeTop.ORDERS, MenuCodeTop.ORDERS_NATIVE)
    }

    fun goToFest(extras: Bundle) {
        activity?.let { mContext ->
            if (!NetworkUtil.isThereInternetConnection(context())) {
                showNetworkError()
            } else {
                val intentToLaunch = FestActivity.getCallingIntent(mContext)
                intentToLaunch.putExtras(extras)
                mContext.startActivity(intentToLaunch)
            }
        }
    }

    fun disableWithClicSimpleButton(){
        simpleButton.setText(getString(R.string.ficha_add))
        simpleButton.addBackgroundColor(resources.getColor(R.color.gray_3))
        simpleButton.addIconLeft(R.drawable.ic_comp_add_to_cart)
        simpleButton.addIconRight(-1)
        simpleButton.textColor(resources.getColor(R.color.gray_4))
        simpleButton.isDisable(false)
        simpleButton.setTag(getString(R.string.disableWithClic))
    }

    fun restoreSimpleButton() {

        simpleButton.setText(getString(R.string.ficha_add))
        simpleButton.addBackgroundColor(resources.getColor(R.color.magenta))
        simpleButton.addIconLeft(R.drawable.ic_comp_add_to_cart)
        simpleButton.textColor(resources.getColor(R.color.white))
        simpleButton.addIconRight(-1)
        simpleButton.isDisable(false)
        simpleButton.setTag("")

        simpleButton.buttonClickListener = object : Button.OnClickListener {

            override fun onClick(view: View) {

                addFromFicha()
                BaseFichaActivity.PRODUCT_ADDED = true//EAAR

            }

        }

    }

    fun setRemoveButton() {

        simpleButton.setText(getString(R.string.ficha_remove))
        simpleButton.addBackgroundColor(Color.BLACK)
        simpleButton.addIconLeft(-1)
        simpleButton.addIconRight(-1)
        simpleButton.isDisable(false)

        simpleButton.buttonClickListener = object : Button.OnClickListener {

            override fun onClick(view: View) {

                removeFromFicha()

            }

        }

    }

    private fun removeFromFicha() {

        oferta.pedido?.let { formattedOrder ->

            presenter.deleteItem(formattedOrder, formattedOrder.productosDetalle?.get(0))

        }

    }

    protected fun showCounter(isShow: Boolean) {

        if (!isShow) {

            simpleCounter.visibility = View.GONE

            val params = simpleButton.layoutParams as LinearLayout.LayoutParams
            params.leftMargin = 0
            simpleButton.layoutParams = params

        }

    }

    /********************* Analytics *******************/

    private fun trackClicItem(keyItem: String, pos: Int, typeCarousel: Int) {
        val item = ofertaMap[keyItem]

        val currentLocation = calcularOfferOriginTypeLocation(OffersOriginType.ORIGEN_CONTENEDOR_FICHA_CARRUSEL)
        val currentSection = calcularOffersOriginTypeSection(typeCarousel.toString())
        val sectionDesc = getOfferTypeForAnalytics(arguments?.getString(BaseFichaActivity.EXTRA_TYPE_OFFER))
        val subsection = calcularCarouselTypeSubSection(GlobalConstant.CARRUSEL)
        val originLocation = calcularOfferOriginTypeLocation(arguments?.getString(BaseFichaActivity.EXTRA_ORIGEN_PEDIDO_WEB_FROM))
        val originSection = calcularOffersOriginTypeSection(arguments?.getString(BaseFichaActivity.EXTRA_TYPE_OFFER))

        val onAnalyticsLoaded: (analytics: Analytics) -> Unit = { analytics ->
            item?.let {
                mUser?.let { u -> Ficha.clicItemCarousel(u, it, pos, analytics) }
            }
        }

        presenter.getAnalytics(currentLocation, currentSection, sectionDesc, subsection,
            originLocation, originSection, onAnalyticsLoaded)

    }

    /** Función para añadir vistas a la sección extra (carruseles, promocion, enriquecida) **/

    protected fun setupExtraSection(list: List<View>) {

        lnlExtraSection.visibility = View.VISIBLE
        lnlExtraSection.removeAllViews()
        for (view in list) {
            lnlExtraSection.addView(view)
        }

    }

    /********************* Encabezado de la Ficha ******************/

    protected fun updateFichaHeader(hasMultiImages: Boolean = false,
                                    showTimer: Boolean = false,
                                    showTag: Boolean = false) {

        viewProduct.visibility = View.VISIBLE
        viewProduct.showTimer(showTimer)
        viewProduct.showTag(showTag)

        if (hasMultiImages) {

            viewProduct.carouselShowArrows = false
            viewProduct.carouselShowDots = true
            viewProduct.carouselShowLimit = (this.imagesMaxFicha ?: 0).toInt()

        }

        viewProduct.setValues(productoEncabezado)

        if (showTimer) {

            isTimerEnabled = true
            startTimer()

        }
    }

    private fun startTimer() {

        if (!isTimerEnabled) return

        // Modificar esto para que utilice el time correcto (getdevicetime es solo para pruebas)
        if (viewProduct.isShowTimer())
            viewProduct.setProductTime(TimeUtil.setServerTime(BuildConfig.TIME_SERVER))

    }

    /** Función para modificar la sección de botones de la ficha, para que sea estática o no **/

    protected fun addButtonVisible() {
        if (elementIsVisible(lnlStatic, scrollBounds)) {
            if (lnlFloat.childCount > 0 && lnlStatic.childCount == 0) {
                bnvBottomButton.visibility = View.GONE
                lnlFloat.removeView(lnlButtons)
                lnlStatic.addView(lnlButtons)
            }
        } else {
            lnlButtons.invalidate()
            if (lnlStatic.childCount > 0 && lnlFloat.childCount == 0) {
                lnlStatic.removeView(lnlButtons)
                lnlFloat.addView(lnlButtons)
                bnvBottomButton.visibility = View.VISIBLE
            }
        }
    }

    protected fun elementIsVisible(view: View, screenRect: Rect): Boolean {

        val scrollBounds2 = Rect()
        nsvContent.getLocalVisibleRect(scrollBounds2)

        if (!canViewScroll(nsvContent)) {
            return false
        }

        if (view.getLocalVisibleRect(screenRect)) {
            return if (!view.getLocalVisibleRect(screenRect) || screenRect.height() < view.height) {
                screenRect.top > 0
            } else {
                true
            }
        } else {
            if (screenRect.top < 0) {
                return true
            } else {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                    if (screenRect.bottom > view.height && (scrollBounds2.top < screenRect.top)) {
                        return false
                    }
                    if (screenRect.top > view.height && (scrollBounds2.top > screenRect.top)) {
                        return true
                    }
                    return false
                } else {
                    return false
                }
            }
        }
    }

    private fun canViewScroll(nsv: NestedScrollView): Boolean {
        val child: View = nsv.getChildAt(0)
        return nsv.height < child.height
    }

    /** Función para mostrar la experiencia de producto agotado (no para ficha de pase pedido) **/

    protected fun setFichaAgotadaButtons() {

        simpleCounter.visibility = View.GONE
        simpleText.visibility = View.VISIBLE

        simpleButton.addBorderRadius(resources.getDimensionPixelSize(R.dimen.ficha_btn_corner))

        simpleButton.layoutParams =
            LinearLayout.LayoutParams(resources.getDimensionPixelSize(R.dimen.ficha_btn_size_width_agotado),
                resources.getDimensionPixelSize(R.dimen.simple_default_button_height), 0F)
        simpleButton.addTextColorDisable(resources.getColor(R.color.white))
        simpleButton.addColorDisable(resources.getColor(R.color.gray_4))
        simpleButton.setText(resources.getString(R.string.ficha_btn_text_agotado))
        simpleButton.addIconLeft(-1)
        simpleButton.addIconRight(-1)
        simpleButton.isDisable(true)

    }

    /** Función para modificar los precios de la ficha (para todos los tipos de ficha) **/

    protected fun updatePrices(offer: Oferta, isUnique: Boolean = false) {

        if (isUnique) {

            var price = "  1x  $moneySymbol  ${decimalFormat.format(offer.precioCatalogo)}\n"

            offer.ofertaNiveles?.let {
                it.forEach { levelPrice ->
                    price += "  ${levelPrice?.cantidad}x  $moneySymbol  ${decimalFormat.format(levelPrice?.precio)}\n"
                }
                productoEncabezado.price = price
                productoEncabezado.priceClient = null
                productoEncabezado.again = null
            }

        } else {
            if (offer.precioValorizado?.compareTo(offer.precioCatalogo ?: 0.0) ?: 0 > 0) {
                setClientPrice(offer)
            } else {
                removeClientPrice(offer)
            }

        }

    }

    protected fun setClientPrice(offer: Oferta) {

        setPrice(offer)

        offer.precioValorizado?.let {
            if(it > 0){
                val price = "$moneySymbol ${decimalFormat.format(it)}"
                productoEncabezado.priceClient = price
            }
            else {
                productoEncabezado.priceClient = null
            }
        }?: run {
            productoEncabezado.priceClient = null
        }

        offer.ganancia?.let {
            if (it > 0) {
                val gain = "$moneySymbol ${decimalFormat.format(it)}"
                productoEncabezado.again = gain
            } else
                productoEncabezado.again = null


        }?: run {
            productoEncabezado.again = null
        }



    }

    protected fun formatWithMoneySymbol(precio: String): String {
        return "$moneySymbol ${decimalFormat.format(precio.toBigDecimal())}"
    }

    private fun removeClientPrice(offer: Oferta) {

        setPrice(offer)

        productoEncabezado.priceClient = null
        productoEncabezado.again = null

    }

    private fun setPrice(offer: Oferta) {
        offer.precioCatalogo?.let {
            if(it > 0){
                val price = "$moneySymbol ${decimalFormat.format(it)}"
                productoEncabezado.price = price
            }
            else {
                productoEncabezado.price = null
            }
        }?: run {
            productoEncabezado.price = null
        }
    }

    /** Funciones para obtener la descripción de origen de pedido **/

    protected fun getFichaOrderOrigin(webFrom: String, offerType: Int): String {

        var originType = ""

        when (offerType) {

            CarouselType.DEFAULT -> {
                originType = when (webFrom) {
                    OffersOriginType.ORIGEN_CONTENEDOR -> {
                        OffersOriginType.ORIGEN_CONTENEDOR_FICHA
                    }
                    OffersOriginType.ORIGEN_LANDING_CATEGORIA -> {
                        OffersOriginType.ORIGEN_LANDING_CATEGORIA_FICHA
                    }
                    OffersOriginType.ORIGEN_LANDING -> {
                        OffersOriginType.ORIGEN_LANDING_FICHA
                    }
                    OffersOriginType.ORIGEN_RECOMENDADO_FICHA -> {
                        OffersOriginType.ORIGEN_RECOMENDADO_FICHA
                    }
                    OffersOriginType.ORIGEN_SUBCAMPANIA_FICHA -> {
                        OffersOriginType.ORIGEN_SUBCAMPANIA_FICHA
                    }

                    else -> webFrom
                }
            }
            CarouselType.UP_SELLING -> {
                originType = OffersOriginType.ORIGEN_UPSELLING_FICHA
            }
            CarouselType.CROSS_SELLING -> {
                originType = OffersOriginType.ORIGEN_CROSSSELLING_FICHA
            }
            CarouselType.SUGERIDOS -> {
                originType = OffersOriginType.ORIGEN_SUGERIDOS_FICHA

            }

        }

        return originType

    }

    protected fun getCarouselOrderOrigin(webFrom: String, offerType: Int): String {

        var originType = ""

        when (offerType) {

            CarouselType.DEFAULT -> {
                originType = when (webFrom) {
                    OffersOriginType.ORIGEN_CONTENEDOR -> {
                        OffersOriginType.ORIGEN_CONTENEDOR_FICHA_CARRUSEL
                    }
                    OffersOriginType.ORIGEN_LANDING_CATEGORIA -> {
                        OffersOriginType.ORIGEN_LANDING_CATEGORIA_FICHA_CARRUSEL
                    }
                    OffersOriginType.ORIGEN_LANDING -> {
                        OffersOriginType.ORIGEN_LANDING_FICHA_CARRUSEL
                    }
                    SearchOriginType.ORIGEN_BUSCADOR_LANDING -> {
                        SearchOriginType.ORIGEN_BUSCADOR_LANDING_FICHA_CARRUSEL
                    }
                    SearchOriginType.ORIGEN_BUSCADOR_DESPLEGABLE -> {
                        SearchOriginType.ORIGEN_BUSCADOR_DESPLEGABLE_FICHA_CARRUSEL
                    }
                    else -> webFrom
                }
            }
            CarouselType.UP_SELLING -> {
                originType = OffersOriginType.ORIGEN_UPSELLING_CARRUSEL
            }
            CarouselType.CROSS_SELLING -> {
                originType = OffersOriginType.ORIGEN_CROSSSELLING_CARRUSEL
            }
            CarouselType.SUGERIDOS -> {
                originType = OffersOriginType.ORIGEN_SUGERIDOS_CARRUSEL
            }

        }

        return originType

    }

    /************************ BaseFichaView ***********************/

    override var imagesMaxFicha: Long?
        get() = this.mImageMaxFicha
        set(value) {
            this.mImageMaxFicha = value
        }

    override fun initScreenTrack(user: User) {

        val screenName = getScreenName()
        Tracker.trackView(screenName, user)

    }

    override fun setUser(user: User?) {

        user?.let { us ->
            this.mUser = us
            this.decimalFormat = CountryUtil.getDecimalFormatByISO(us.countryISO, true)
            this.moneySymbol = us.countryMoneySymbol?.let { it } ?: ""

            if (us.isMostrarBuscador == true)
                (activity as? BaseFichaActivity)?.showToolbarIcons(BaseFichaActivity.FichaMenuItems.SEARCH_ITEM)
            else
                (activity as? BaseFichaActivity)?.hideToolbarIcons(BaseFichaActivity.FichaMenuItems.SEARCH_ITEM)

        }

    }

    override fun load(oferta: Oferta) {
        isKitNueva = oferta.codigoTipoOferta == KIT_NUEVA_CODE
        loadOffer(oferta)
    }

    override fun load(component: Componente?) {
        loadComponent(component)
    }

    override fun load(promotion: PromotionOfferModel?) {
        loadPromotion(promotion)
    }

    override fun share(url: String) {

        Ficha.share()
        ShareCompat.IntentBuilder
            .from(activity)
            .setType("text/plain")
            .setChooserTitle(getString(R.string.offers_share_chooser_title))
            .setText(url)
            .startChooser()

    }

    override fun addComplete(quantity: Int, productCUV: ProductCUV, showImage: Boolean, message: String?, codeAlert: String?) {
        onAddComplete(quantity, productCUV, showImage, message, codeAlert)
    }

    override fun validateFestCondition(oferta: Oferta, quantity: Int, counterView: Counter, identifier: String, codigo: String?, palanca: String?, valor: String?, editable: Boolean, id: Int, clientID: Int, reemplazarFestival: Boolean?, message: String?) {
        message?.let {
            context?.let { context ->
                BottomDialog.Builder(context)
                    .setIcon(R.drawable.ic_mano_error)
                    .setTitle(getString(R.string.fest_confirt_message_p1))
                    .setTitleBold()
                    .setContent(it)
                    .setNegativeText(R.string.fest_confirt_calcel)
                    .setPositiveText(R.string.fest_confirt_ok)
                    .setNegativeTextColor(R.color.black)
                    .setNegativeBorderColor(R.color.black)
                    .setNegativeBackgroundColor(R.color.white)
                    .onNegative(object : BottomDialog.ButtonCallback {
                        override fun onClick(dialog: BottomDialog) {
                            dialog.dismiss()
                        }
                    })
                    .onPositive(object : BottomDialog.ButtonCallback {
                        override fun onClick(dialog: BottomDialog, chbxConfirmacion: CheckBox) {

                            presenter.agregar(oferta, quantity, counterView, identifier, codigo, palanca, valor, editable, id, clientID, reemplazarFestival)

                        }
                    })
                    .setPositiveBackgroundColor(R.color.magenta)
                    .show()

            }
        }
    }

    override fun updateProlMessages(mensajes: Collection<MensajeProl?>?) {
        this.mensajes = mensajes
    }

    override fun showError(canBack: Boolean) {

        if (!NetworkUtil.isThereInternetConnection(context)) {
            this.canBack = canBack
            showNetworkErrorWithListener(this)
            return
        }

    }

    override fun showError(stringId: Int) {
        showDialogError(getString(stringId))
    }

    override fun showError() {

        context?.let {
            BottomDialog.Builder(it)
                .setIcon(R.drawable.ic_mano_error)
                .setContent(getString(R.string.offers_no_result_default_message))
                .setNeutralText(getString(R.string.msj_entendido))
                .onNeutral(object : BottomDialog.ButtonCallback {
                    override fun onClick(dialog: BottomDialog) {
                        dialog.dismiss()
                        requireActivity().finish()
                    }
                })
                .setNeutralBackgroundColor(R.color.magenta)
                .show()

        }

    }

    override fun showError(message: String?) {

        val errorMsg = message ?: getString(R.string.error_share_message)

        showDialogError(errorMsg)
    }

    private fun showDialogError(message: String) {
        context?.let {
            BottomDialog.Builder(it)
                .setIcon(R.drawable.ic_mano_error)
                .setContent(message)
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

    override fun setCarouselOffers(offers: List<Oferta>, type: Int, promotion: PromotionResponse?) {
        updateCarouselOffers(offers, type, promotion)
    }

    override fun onGetMenu(menu: Menu) {
        listener?.goToOrders(menu)
    }

    override fun setTextTitles(resIdPrice: Int?, strikethroughPrice: Boolean, resIdPriceClient: Int?,
                               strikethroughPriceClient: Boolean, resIdGain: Int?, strikethroughGain: Boolean) {

        resIdPrice?.let { productoEncabezado.labelPrice = resources.getString(it) }
        resIdPriceClient?.let { productoEncabezado.labelPriceClient = resources.getString(it) }
        resIdGain?.let { productoEncabezado.labelAgain = resources.getString(it) }

       viewProduct.setTextTitles(resIdPrice, strikethroughPrice, resIdPriceClient, strikethroughPriceClient, resIdGain, strikethroughGain)

    }

    override fun setupSello(sello: FestivalSello?) {
        setupSelloFestival(sello)
    }

    /************************ MessageDialogListener ***********************/

    override fun aceptar() {

        if (this.canBack) {
            listener?.onBackPressedFragment()
        }

    }

    override fun cancelar() { /* EMPTY */
    }

    /************************ OnChangeQuantityListener ***********************/
    /** Solo para Ficha de Pase Pedido **/

    override fun onChange(quantity: Int) {
        onQuantityChange(quantity)
    }

    /************************ OfferOptionTonesListener ***********************/

    override fun completeTones(complete: Boolean) {
        if (!this.agotado) {
            when {
                complete -> {
                    restoreSimpleButton()
                }
            }
        }
    }

    override fun selectOption(item: Componente, option: Opciones) {

        accessFrom?.let { Ficha.select(it, item, option) }
        checkOptionsForPasePedido()
        changeMainImage(option?.imagenURL)
    }

    override fun changeOption(item: Componente) {
        accessFrom?.let { Ficha.change(it, item) }
    }

    override fun chooseOption(item: Componente) {
        Ficha.choose(item)
    }

    override fun selectOption(item: Opciones) {
        // EMPTY
    }

    override fun applySelection(item: Componente) {

        accessFrom?.let { Ficha.applyOption(it, item) }
        checkOptionsForPasePedido()

        if (rvwOffersTone.adapter.itemCount == 1){
            item.opciones?.firstOrNull { it?.selected == true }?.apply {
                changeMainImage(this.imagenURL)
            }
        }

    }

    override fun didPressedShowDetail(item: Componente, position: Int) {

        showDetail(item)
        Ficha.viewDetailfromGanaMas(item)

    }

    private fun showDetail(component: Componente) {
        val extras = Bundle()
        extras.putString(BaseFichaActivity.EXTRA_KEY_ITEM, cuv)
        extras.putString(BaseFichaActivity.EXTRA_TYPE_OFFER, type)
        extras.putInt(BaseFichaActivity.EXTRA_MARCA_ID, marcaID ?: 0)
        extras.putString(BaseFichaActivity.EXTRA_MARCA_NAME, marca)

        extras.putBoolean(BaseFichaActivity.EXTRA_ENABLE_SHARE, false)
        extras.putString(BaseFichaActivity.EXTRA_TYPE_FICHA, FichaType.PRODUCT_COMPONENT)
        extras.putSerializable(BaseFichaActivity.EXTRA_PRODUCT_COMPONENT, component)

        extras.putBoolean(BaseFichaActivity.FROM_GANAMAS, true)

        listener?.showFichaComponente(extras)
    }

    /************************ MiniOffer.Listener *************************/

    override fun didPressedItem(item: OfferModel, pos: Int, type: Any?) {

        setupNewFicha(getOfferTypeForFicha(item.leverName), item.key, item.marcaID, item.brand, type as Int)
        trackClicItem(item.key, pos, type)

    }

    override fun didPressedItemButtonAdd(typeLever: String, keyItem: String, quantity: Int, counterView: Counter, type: Any?) {

        addFromCarousel(quantity, keyItem, counterView, type as Int)
        BaseFichaActivity.PRODUCT_ADDED = true // EAAR

    }

    override fun didPressedItemButtonSelection(item: OfferModel, pos: Int, type: Any?) {

        setupNewFicha(getOfferTypeForFicha(item.leverName), item.key, item.marcaID, item.brand, type as Int)
        trackClicItem(item.key, pos, type)

    }

    override fun didPressedItemButtonShowOffer(item: OfferModel, pos: Int, type: Any?) {

        setupNewFicha(getOfferTypeForFicha(item.leverName), item.key, item.marcaID, item.brand, type as Int)
        trackClicItem(item.key, pos, type)

    }

    override fun impressionItems(typeLever: String, list: ArrayList<OfferModel>, type: Any?) {

        val visibleInstantList = getVisibleOfertas(list, type as Int)

        if (visibleInstantList.isNotEmpty()) {

            if (visibleList.size == 0) {

                visibleList.addAll(visibleInstantList)

                trackProductItems(visibleList, type)


            } else {

                val filterList = visibleInstantList.minus(visibleList).toMutableList()
                visibleList.clear()
                visibleList.addAll(visibleInstantList)

                if (filterList.isNotEmpty()) {

                    trackProductItems(filterList, type)

                }

            }

        }

    }

    private fun trackProductItems(list: MutableList<Oferta>, type: Any?) {

        val currentLocation = calcularOfferOriginTypeLocation(OffersOriginType.ORIGEN_CONTENEDOR_FICHA_CARRUSEL)

        val currentSection = if (this.type == OfferTypes.LAN)
            calcularOffersOriginTypeSection(this.type)
        else
            calcularOffersOriginTypeSection(type.toString())

        val subsection = calcularCarouselTypeSubSection(GlobalConstant.CARRUSEL)

        val originLocation = calcularOfferOriginTypeLocation(arguments?.
            getString(BaseFichaActivity.EXTRA_ORIGEN_PEDIDO_WEB_FROM))

        val originSection = calcularOffersOriginTypeSection(this.type)

        for (item in list) {

            val sectionDesc = getOfferTypeForAnalytics(item.tipoOferta)

            val onAnalyticsLoaded: (analytics: Analytics) -> Unit = { analytics ->
                mUser?.let { Ficha.viewItemsCarousel(it, item, analytics) }
            }

            presenter.getAnalytics(currentLocation, currentSection, sectionDesc, subsection,
                originLocation, originSection, onAnalyticsLoaded)

        }

    }

    protected fun exists(cuv: String?, list: ArrayList<OfferModel>): Boolean {

        val model = list.firstOrNull { it.key == cuv }
        return model != null

    }

    private fun setupNewFicha(subType: String, keyItem: String, marcaID: Int, marca: String, typeCarousel: Int) {

        val extras = Bundle()
        extras.putString(BaseFichaActivity.EXTRA_KEY_ITEM, keyItem)
        extras.putString(BaseFichaActivity.EXTRA_TYPE_OFFER, subType)
        extras.putInt(BaseFichaActivity.EXTRA_MARCA_ID, marcaID)
        extras.putString(BaseFichaActivity.EXTRA_MARCA_NAME, marca)

        fichaOfferType = typeCarousel

        accessFrom = BaseFichaActivity.ACCESS_FROM_GANAMAS

        origenPedidoWebFrom?.let { webOfferOrigin ->

            val offerOriginType = getCarouselOrderOrigin(webOfferOrigin, fichaOfferType)
            extras.putString(BaseFichaActivity.EXTRA_ORIGEN_PEDIDO_WEB_FROM, offerOriginType)

            extras.putInt(BaseFichaActivity.EXTRA_FICHA_OFFER_TYPE, fichaOfferType)

            extras.putInt(BaseFichaActivity.EXTRA_ACCESS_FROM, accessFrom ?: 0)
            origenesBuscador?.let {
                extras.putParcelableArrayList(BaseFichaActivity.EXTRAS_ORIGENES_PEDIDO_SEARCH,
                    it as ArrayList<out Parcelable>)
            }

            listener?.changeFicha(extras)

        }

    }

    /******************** BoxSection.OnBoxSectionListener ********************/

    override fun onButtonClick(view: View) {
        onClientBoxClick()
    }

    /************************ Abstract functions ***********************/
    /** Todos los tipos de ficha deben implementar éstas funciones **/

    abstract fun getData()
    abstract fun addFromFicha()
    abstract fun addFromCarousel(quantity: Int, keyItem: String, counterView: Counter, typeCarousel: Int)
    abstract fun getScreenName(): String

    /************************ Open functions ***********************/

    protected open fun loadOffer(offer: Oferta) {}
    protected open fun loadComponent(component: Componente?) {}
    protected open fun loadPromotion(promotion: PromotionOfferModel?) {}
    protected open fun updateCarouselOffers(offers: List<Oferta>, type: Int, promotion: PromotionResponse?) {}
    protected open fun onAddComplete(quantity: Int, productCUV: ProductCUV, showImage: Boolean, message: String?, codeAlert: String?) {}
    protected open fun onQuantityChange(quantity: Int) {}
    protected open fun onClientBoxClick() {}
    protected open fun checkOptionsForPasePedido() {}
    protected open fun changeMainImage(imagenURL: String?) {}
    protected open fun getOfferTypeForFicha(type: String): String = type
    protected open fun getVisibleOfertas(list: ArrayList<OfferModel>, type: Int): List<Oferta> = arrayListOf()
    protected open fun setupSelloFestival(selloConfig: FestivalSello?) {}

    override fun setDataAward(listaFestivalProgressResponse: List<FestivalProgressResponse?>?, oferta: Oferta?, festivalResponse: FestivalResponse?) { }

    override fun onFormattedOrderReceived(order: OrderModel?, clientModelList: List<ClienteModel?>?, callFrom: Int) {}

    override fun onProductNotAdded(message: String?){}
    override fun onError(errorModel: ErrorModel){}
    override fun close(){}

    @SuppressLint("SetTextI18n")
    open fun onComplete(clienteModel: ClienteModel) {
        this.clienteModel = clienteModel
        clienteModel.apellidos?.let {
            bsClient.setContent("${clienteModel.nombres} $it")
        } ?: bsClient.setContent(clienteModel.nombres)
    }

    open fun refreshShare() {
        listener?.showShare(tieneCompartir)
    }

    interface BaseListener {

        fun onBackPressedFragment()
        fun showShare(show: Boolean)
        fun goToOrders(menu: Menu)
        fun updateOrders(count: Int, isKitNueva: Boolean? = null)
        fun showFichaComponente(extras: Bundle)
        fun changeFicha(extras: Bundle)
        fun showClients()

    }

    open fun calcularOfferOriginTypeLocation(offerOriginTypeLocation: String?): String {
        var originTypeLocation = ""


        when (offerOriginTypeLocation) {

            OffersOriginType.ORIGEN_CONTENEDOR -> {
                originTypeLocation = OffersOriginTypeLocation.ORIGEN_CONTENEDOR
            }

            OffersOriginType.ORIGEN_LANDING,
            OffersOriginType.ORIGEN_LANDING_CATEGORIA -> {
                originTypeLocation = OffersOriginTypeLocation.ORIGEN_LANDING
            }

            SearchOriginType.ORIGEN_BUSCADOR_DESPLEGABLE,
            SearchOriginType.ORIGEN_BUSCADOR_LANDING,
            SearchOriginType.ORIGEN_BUSCADOR_DESPLEGABLE_FICHA,
            SearchOriginType.ORIGEN_BUSCADOR_LANDING_FICHA,
            SearchOriginType.ORIGEN_BUSCADOR_DESPLEGABLE_FICHA_CARRUSEL,
            SearchOriginType.ORIGEN_BUSCADOR_LANDING_FICHA_CARRUSEL,
            SearchOriginType.ORIGEN_BUSCADOR_FICHA -> {
                originTypeLocation = OffersOriginTypeLocation.ORIGEN_BUSCADOR
            }

            OffersOriginType.ORIGEN_PEDIDO -> {
                originTypeLocation = OffersOriginTypeLocation.ORIGEN_PEDIDO
            }

            OffersOriginType.ORIGEN_CONTENEDOR_FICHA,
            OffersOriginType.ORIGEN_LANDING_FICHA,
            OffersOriginType.ORIGEN_LANDING_CATEGORIA_FICHA,
            OffersOriginType.ORIGEN_CONTENEDOR_FICHA_CARRUSEL,
            OffersOriginType.ORIGEN_LANDING_FICHA_CARRUSEL,
            OffersOriginType.ORIGEN_LANDING_CATEGORIA_FICHA_CARRUSEL,
            OffersOriginType.ORIGEN_UPSELLING_FICHA,
            OffersOriginType.ORIGEN_SUGERIDOS_FICHA,
            OffersOriginType.ORIGEN_CROSSSELLING_FICHA,
            OffersOriginType.ORIGEN_RECOMENDADO_FICHA -> {
                originTypeLocation = OffersOriginTypeLocation.ORIGEN_FICHA
            }
        }

        return originTypeLocation
    }

    open fun calcularOffersOriginTypeSection(origenSectionDescription: String?): String {
        var originType = ""
        when (origenSectionDescription) {
            OfferTypes.ODD ->{
                originType = OffersOriginTypeSection.ORIGEN_ODD
            }
            OfferTypes.OPT, OfferTypes.RD -> {
                originType = OffersOriginTypeSection.ORIGEN_OPT
            }
            OfferTypes.SR -> {
                originType = OffersOriginTypeSection.ORIGEN_SR
            }
            OfferTypes.LIQ -> {
                originType = OffersOriginTypeSection.ORIGEN_LIQUIDACION
            }
            OfferTypes.ATP -> {
                originType = OffersOriginTypeSection.ORIGEN_ATP
            }
            OfferTypes.LMG -> {
                originType = OffersOriginTypeSection.ORIGEN_LMG
            }
            OfferTypes.MG -> {
                originType = OffersOriginTypeSection.ORIGEN_LMG
            }
            OfferTypes.LAN -> {
                originType = OffersOriginTypeSection.ORIGEN_LAN
            }
            OfferTypes.DP -> {
                originType = OffersOriginTypeSection.ORIGEN_DUO_PERFECTO
            }
            OfferTypes.PN -> {
                originType = OffersOriginTypeSection.ORIGEN_PACK_NUEVAS
            }
            OfferTypes.HV -> {
                originType = OffersOriginTypeSection.ORIGEN_HV
            }
            CarouselType.DEFAULT.toString() -> {
                originType = OffersOriginTypeSection.ORIGEN_CAROUSEL_DEFAULT
            }
            CarouselType.RECOMENDADOS.toString() -> {
                originType = OffersOriginTypeSection.ORIGEN_RECOMENDADO
            }
            CarouselType.SUGERIDOS.toString() -> {
                originType = OffersOriginTypeSection.ORIGEN_SUGERIDO
            }
            CarouselType.CROSS_SELLING.toString() -> {
                originType = OffersOriginTypeSection.ORIGEN_CROSSELLING
            }
            CarouselType.UP_SELLING.toString() -> {
                originType = OffersOriginTypeSection.ORIGEN_UPSELLING
            }
        }
        return originType
    }

    open fun calcularCarouselTypeSubSection(SubectionDescription: String): String {
        var originType = ""

        when (SubectionDescription) {
            GlobalConstant.EVENT_ACTION_FICHA -> {
                originType = OffersOriginTypeSubSection.ORIGEN_FICHA
            }
            GlobalConstant.CARRUSEL -> {
                originType = OffersOriginTypeSubSection.ORIGEN_CARRUSEL
            }
            GlobalConstant.SUBSECCION_NULL -> {
                originType = OffersOriginTypeSubSection.ORIGEN_NULL
            }
        }
        return originType
    }

    fun setProductTypePhoto(url: String): Unit {
        productoEncabezado.imageUrl[0] = url
        updateFichaHeader(true)
        oferta.imagenURL = url
    }
}
