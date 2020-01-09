package biz.belcorp.consultoras.feature.search.list

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import android.speech.RecognizerIntent
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.Toast
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.base.SafeLet
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.model.product.ProductCUVModel
import biz.belcorp.consultoras.common.model.search.OrigenModel
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.data.manager.SessionManager
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.feature.ficha.common.BaseFichaActivity
import biz.belcorp.consultoras.feature.ficha.producto.FichaProductoActivity
import biz.belcorp.consultoras.feature.home.fest.FestActivity
import biz.belcorp.consultoras.feature.search.VoiceRecognitionDialog
import biz.belcorp.consultoras.feature.search.di.SearchComponent
import biz.belcorp.consultoras.feature.search.results.SearchResultsActivity
import biz.belcorp.consultoras.util.*
import biz.belcorp.consultoras.util.analytics.Search
import biz.belcorp.consultoras.util.anotation.AnalyticsCategoryType
import biz.belcorp.consultoras.util.anotation.OffersOriginTypeLocation
import biz.belcorp.consultoras.util.anotation.OffersOriginTypeSection
import biz.belcorp.consultoras.util.anotation.SearchOriginType
import biz.belcorp.consultoras.util.broadcast.FestBroadcast
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.util.*
import biz.belcorp.library.util.CountryUtil
import biz.belcorp.library.util.StringUtil
import biz.belcorp.mobile.components.design.counter.Counter
import biz.belcorp.mobile.components.dialogs.bottom.BottomDialog
import biz.belcorp.mobile.components.offers.CatInSearch
import biz.belcorp.mobile.components.offers.MiniVerticalOffer
import biz.belcorp.mobile.components.offers.model.CatInSearchModel
import biz.belcorp.mobile.components.design.carousel.promotion.model.PromotionModel
import biz.belcorp.mobile.components.design.carousel.promotiondual.OfferPromotionModel
import biz.belcorp.mobile.components.offers.model.OfferModel
import biz.belcorp.mobile.components.offers.sello.Sello
import com.uxcam.UXCam
import kotlinx.android.synthetic.main.fragment_search_list.*
import java.text.DecimalFormat
import javax.inject.Inject
import kotlin.collections.ArrayList


/**
 * Created by Leonardo on 13/09/2018.
 */

class SearchListFragment : BaseFragment(), SearchListView,
    SearchListAdapter.SearchListener,
    VoiceRecognitionDialog.VoiceRecognitionListener,
    View.OnFocusChangeListener, CatInSearch.CatInSearchListener, MiniVerticalOffer.Listener, SafeLet {

    @Inject
    lateinit var presenter: SearchListPresenter
    private var listener: Listener? = null
    private var user: User? = null
    private lateinit var searchListAdapter: SearchListAdapter
    private var decimalFormat: DecimalFormat = DecimalFormat()
    private var searchEnabled: Boolean = false // Debe ser true
    var itemsAdded: Boolean = false

    private lateinit var searchLayoutManager: LinearLayoutManager
    private lateinit var searchList: ArrayList<PromotionModel>

    private var softKeyHandler = Handler()
    private var handler = Handler()

    private var recentOffers: ArrayList<OfferModel?> = arrayListOf()

    private var listSellos: ArrayList<Sello?> = arrayListOf()
    private var flagRecentOffers: Boolean = false


    val regex = "-?\\d+(\\.\\d+)?".toRegex()
    private val delay: Long = 700 // milliseconds

    var isVoiceSearch: Boolean = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        UXCam.tagScreenName(UXCamUtils.SearchListFragmentName)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search_list, container, false)
    }

    @Throws(IllegalStateException::class)
    override fun onInjectView(): Boolean {
        getComponent(SearchComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        this.presenter.attachView(this)
        init()

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Listener) {
            listener = context
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            1 -> {
                val matches = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                matches?.let {
                    edt_search.setText(it[0])
                }
            }
            SearchResultsActivity.RESULT -> {
                if (resultCode == Activity.RESULT_OK) itemsAdded = true
            }

            SearchListFragment.REQUEST_CODE_RECENT_OFFER -> {
                if (resultCode == Activity.RESULT_CANCELED)
                    context?.let { presenter.getRecentOffers(it) }

            }
        }
    }

    // BaseFragment Overrides
    override fun context(): Context? {
        val activity = activity
        return activity?.applicationContext
    }

    // SearchListView Overrides
    override fun initScreenTrack(model: LoginModel) {
        Tracker.trackScreen(GlobalConstant.SCREEN_SEARCH_LIST, model)
    }

    override fun trackBackPressed(model: LoginModel) {
        Tracker.trackBackEvent(GlobalConstant.SCREEN_SEARCH_LIST, model)
    }

    override fun setUser(user: User?) {
        this.user = user
        this.decimalFormat = CountryUtil.getDecimalFormatByISO(user?.countryISO!!, true)

        searchListAdapter.setData(user.countryMoneySymbol ?: StringUtil.Empty,
            user.caracteresBuscadorMostrar ?: SearchListAdapter.DEFAULT_CARACTERES_MOSTRAR,
            this.decimalFormat)
    }

    override fun onSearchResult(result: SearchResponse?) {

        val maxList = user?.totalResultadosBuscador ?: DEFAULT_RESULTADOS_MOSTRAR

        result?.let { response ->

            //Promociones
            searchListAdapter.promotion = response.promocion

            //for promotion no product result list
            if (response.productos?.any() != true && (searchListAdapter.promotion?.detalle?.any() == true)) {
                response.productos = mutableListOf(ProductCUV().apply { tipoPersonalizacion = resources.getString(R.string.promotion_val_noresultlist) })
            }

            // Productos
            response.productos?.let {

                if (it.isEmpty() && isAdded) {
                    showEmptyScreen(resources.getString(R.string.search_empty_title),
                        resources.getString(R.string.search_empty_body))

                    if (isVoiceSearch) {
                        presenter.trackPrintEventSearch(AnalyticsCategoryType.BUSCADOR, Search.ACTION_SEARCH_VOICE_NOT_FOUND, edt_search.text.toString(), GlobalConstant.SCREEN_SEARCH_LIST, Search.EVENT_SEARCH_VOICE_NOT_FOUND_RESULT)
                    } else {
                        presenter.trackPrintEventSearch(AnalyticsCategoryType.BUSCADOR, Search.ACTION_SEARCH_NOT_FOUND, edt_search.text.toString(), GlobalConstant.SCREEN_SEARCH_LIST, Search.EVENT_SEARCH_NOT_FOUND)
                    }


                } else {

                    if (user?.isMostrarBotonVerTodosBuscador == true) {
                        if (user?.isAplicarLogicaCantidadBotonVerTodosBuscador == true) {
                            if (response.total ?: 0 > it.size) btnResultados.visibility = View.VISIBLE
                        } else {
                            btnResultados.visibility = View.VISIBLE
                        }
                    } else {
                        btnResultados.visibility = View.GONE
                    }

                    var showedMoreResults = false
                    if(btnResultados.visibility == View.VISIBLE) showedMoreResults = true

                    lnlEmpty.visibility = View.GONE

                    if (it.size <= maxList) searchListAdapter.setList(it.toList(), showedMoreResults)
                    else searchListAdapter.setList(it.take(maxList), showedMoreResults)

                    softKeyHandler.postDelayed({
                        try {
                            hideKeyboard()
                        } catch (e: Exception) {
                            // EMPTY
                        }
                    }, 800)

                    if (isVoiceSearch) {
                        presenter.trackPrintEventSearch(AnalyticsCategoryType.BUSCADOR, Search.ACTION_SEARCH_VOICE, edt_search.text.toString(), GlobalConstant.SCREEN_SEARCH_LIST, Search.EVENT_SEARCH_VOICE_RESULT)
                    } else {
                        presenter.trackPrintEventSearch(AnalyticsCategoryType.BUSCADOR, Search.ACTION_SEARCH_RESULTS, edt_search.text.toString(), GlobalConstant.SCREEN_SEARCH_LIST, Search.EVENT_SEARCH_RESULTS)
                    }
                }
            }
        } ?: run {
            if (isAdded)
                showEmptyScreen(resources.getString(R.string.search_empty_title),
                    resources.getString(R.string.search_empty_body))
        }

    }

    override fun onSearchError(text: String?, exception: Throwable?) {
        showEmptyScreen(resources.getString(R.string.search_error_title), resources.getString(R.string.search_error_body))
        exception?.let { BelcorpLogger.d("SearchListFragment", it) }
    }

    override fun onAddProductError(text: String?, exception: Throwable?) {
        context?.let {
            ToastUtil.show(it, text
                ?: resources.getString(R.string.search_add_product_error), Toast.LENGTH_LONG)
        }
        exception?.let { BelcorpLogger.d("SearchListFragment", it) }
    }

    override fun onPrintProduct(item: ProductCUV, posicion: Int) {
        user?.let {
            presenter.trackPrintProduct(item, it, posicion, OffersOriginTypeLocation.ORIGEN_BUSCADOR, OffersOriginTypeSection.ORIGEN_DESPLEGABLE)
        }
    }

    override fun onPrintClickProduct(item: ProductCUV, posicion: Int) {
        user?.let {
            presenter.trackPrintClickProduct(item, it, posicion, OffersOriginTypeLocation.ORIGEN_BUSCADOR, OffersOriginTypeSection.ORIGEN_DESPLEGABLE)
        }
    }

    override fun onPrintAddProduct(item: ProductCUV, quantity: Int) {
        user?.let {
            presenter.trackPrintAddProduct(item, it, quantity, OffersOriginTypeLocation.ORIGEN_BUSCADOR, OffersOriginTypeSection.ORIGEN_DESPLEGABLE)
        }
    }


    override fun showLoadingSearch() {
        btnResultados.visibility = View.GONE

        img_cancel.visibility = View.GONE
        loader.visibility = View.VISIBLE
        lnlClear.isClickable = false
        img_voice.isClickable = false
    }

    override fun hideLoadingSearch() {
        img_cancel.visibility = View.VISIBLE
        loader.visibility = View.GONE
        lnlClear.isClickable = true
        img_voice.isClickable = true
        llContainerCategorias.visibility = View.GONE
    }

    override fun onProductAdded(productCUV: ProductCUV?, showImage: Boolean, message: String?, type: Int) {
        productCUV?.let { product ->
            activity?.let { FestBroadcast.sendAddUpdateToCart(it) }
            updateCart(product.cantidad ?: 0)
            searchListAdapter.setProductAdded(product.cuv.toString())
            val messagex = message ?: getString(R.string.msj_offer_added)
            val image = ImageUtils.verifiedImageUrl(productCUV)

            context?.let {
                when (type) {
                    TYPE_FESTIVAL_ADDED -> {

                        val url = if (showImage) image else null
                        val colorText = ContextCompat.getColor(it, R.color.leaf_green)
                        showBottomDialog(it, messagex, url, colorText)

                    }
                    TYPE_FESTIVAL_REACHED -> {

                        showBottomDialogAction(it, message) {
                            val intent = Intent(it, FestActivity::class.java)
                            it.startActivity(intent)
                        }
                    }
                }
            }
        }
    }

    private fun updateCart(cantidad: Int) {
        activity?.let {
            val ordersCount = SessionManager.getInstance(it).getOrdersCount() ?: 0
            SessionManager.getInstance(it).saveOffersCount(ordersCount + cantidad)

            val countIntent = Intent(BaseFichaActivity.BROADCAST_COUNT_ACTION)
            it.sendBroadcast(countIntent)
        }
    }

    // SearchListAdapter VinculacionListener Overrides
    override fun onAddProduct(item: ProductCUV) {
        val iden = DeviceUtil.getId(activity)
        item.identifier = iden
        item.origenPedidoWeb = calcularOrigenPedidoWeb(item, SearchOriginType.ORIGEN_DESPLEGABLE)
        presenter.agregar(item, iden)
        itemsAdded = true

    }

    override fun onShowProduct(item: ProductCUV) {

        user?.let {
            Tracker.trackEvent(GlobalConstant.SCREEN_SEARCH_LIST,
                GlobalConstant.EVENT_CAT_SEARCH,
                GlobalConstant.EVENT_ACTION_SEARCH_CHOOSE,
                edt_search.text.toString(),
                GlobalConstant.EVENT_NAME_VIRTUAL_EVENT,
                it)
        }

        goToFicha(item, BaseFichaActivity.ACCESS_FROM_BUSCADOR_DESPLEGABLE)
    }

    override fun onClickPromotion(item: ProductCUV, promotionPosition: Int) {
        // Tracker.Search.trackClickPromotion(item, promotionPosition)

        goToFicha(item, BaseFichaActivity.ACCESS_FROM_BUSCADOR_DESPLAGABLE_PROMOCION_PREMIO)
    }

    override fun onClickCondition(item: OfferPromotionModel) {
        // Tracker.Search.trackClickPromotion(item, promotionPosition)
        goToFicha(ProductCUV().apply {
            cuv = item.cuvCondition
            tipoPersonalizacion = item.typeCondition
        }, BaseFichaActivity.ACCESS_FROM_BUSCADOR_DESPLAGABLE_PROMOCION_CONDICION)
    }

    private fun goToFicha(item: ProductCUV, accessFrom: Int) {
        val productItem = ProductCUVModel.transformList(item)

        activity?.let { mContext ->
            if (!NetworkUtil.isThereInternetConnection(context())) {
                showNetworkError()
            } else {
                val extras = Bundle()
                extras.putString(BaseFichaActivity.EXTRA_KEY_ITEM, item.cuv)
                extras.putString(BaseFichaActivity.EXTRA_TYPE_OFFER, item.tipoPersonalizacion)
                item.marcaId?.let { extras.putInt(BaseFichaActivity.EXTRA_MARCA_ID, it) }
                extras.putString(BaseFichaActivity.EXTRA_MARCA_NAME, item.descripcionMarca)
                extras.putString(BaseFichaActivity.EXTRA_ORIGEN_PEDIDO_WEB_FROM, SearchOriginType.ORIGEN_BUSCADOR_DESPLEGABLE_FICHA)

                extras.putInt(BaseFichaActivity.EXTRA_ACCESS_FROM, accessFrom)
                extras.putString(BaseFichaActivity.EXTRA_TYPE_PERSONALIZATION, item.tipoPersonalizacion)
                extras.putParcelableArrayList(BaseFichaActivity.EXTRAS_ORIGENES_PEDIDO_SEARCH, OrigenModel.transformList(item.origenesPedidoWeb))

                extras.putParcelableArrayList(BaseFichaActivity.EXTRAS_PRODUCTO_ITEM, arrayListOf<ProductCUVModel>().apply { add(productItem) })

                val intentToLaunch = FichaProductoActivity.getCallingIntent(mContext)
                intentToLaunch.putExtras(extras)
                mContext.startActivity(intentToLaunch)

                itemsAdded = true
            }
        }
    }

    private fun getVisibleItemsPromotion(item: Any): Promotion? {
        var visible: Promotion? = null
        when (item) {
            is Promotion -> {
                visible = item
            }
        }

        return visible
    }

    private fun calcularOrigenPedidoWeb(item: ProductCUV, origenPedidoWebFrom: String?): String {
        return item.origenesPedidoWeb?.firstOrNull { it?.codigo == origenPedidoWebFrom }?.valor
            ?: "0"
    }

    // VinculacionListener
    interface Listener {
        fun onFilter()
    }


    // VoiceRecognitionListener
    override fun onRecognitionSuccess(found: String) {
        llContainerCategorias.visibility = View.GONE
        isVoiceSearch = true
        edt_search.setText(found)
    }

    override fun onRecognitionCancel(retryActive: Boolean) {
        showVoiceSearch()
    }

    override fun onRecognitionDismiss() {
        showVoiceSearch()
    }

    override fun onRecognitionRetry() {
        // EMPTY
    }

    // Private Functions
    @SuppressLint("SetTextI18n")
    private fun init() {

        //  setupOffers()

        miniVerticalOffers.listener = this

        presenter.getUser()
        presenter.getCategoriasInSearch()
        context?.let { presenter.getConfigSellos(it) }

        edt_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(e: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(c: CharSequence?, p1: Int, p2: Int, p3: Int) {
                c?.let {
                    if(it.isNotEmpty()){
                        lnlClear.visibility = View.VISIBLE
                        showRecentOffers(false)
                        openKeyboard()
                    } else{
                        lnlClear.visibility = View.GONE
                        closeKeyboard()
                    }
                }
                showCategories(true)
                validateText(c.toString())
            }
        })

        edt_search.setOnTouchListener { p0, p1 ->
            if(!flagRecentOffers){
                if(edt_search.length()==0){
                    showCategories(false)
                    flagRecentOffers = true
                    context?.let { presenter.getRecentOffers(it) }
                }
            }
            openKeyboard()
            true
        }

        edt_search.setOnKeyListener { view, i, keyEvent ->

            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                when (i) {

                    KeyEvent.KEYCODE_ENTER -> {
                        user?.let {
                            Tracker.trackEvent(GlobalConstant.SCREEN_SEARCH_LIST,
                                GlobalConstant.EVENT_CAT_SEARCH,
                                GlobalConstant.EVENT_ACTION_BOTON_RESULT,
                                edt_search.text.toString(),
                                GlobalConstant.EVENT_NAME_VIRTUAL_EVENT, it)
                        }

                        if (edt_search.text.length > 0) {
                            val intent = Intent(context, SearchResultsActivity::class.java)
                            intent.putExtra("searched", edt_search.text.toString())
                            startActivityForResult(intent, SearchResultsActivity.RESULT)
                        }
                    }

                }
            }
            false
        }

        img_voice.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (PermissionUtil.isRecordAudioGranted(context)) {
                    showVoiceRecognitionDialog(true)
                } else {
                    ActivityCompat.requestPermissions(activity!!,
                        arrayOf(Manifest.permission.RECORD_AUDIO),
                        RECORD_REQUEST_CODE)
                }
            } else {
                showVoiceRecognitionDialog(true)
            }
        }

        lnlBack.setOnClickListener { activity?.onBackPressed() }

        lnlClear.setOnClickListener {

            if (searchEnabled) {
                if (PermissionUtil.isRecordAudioGranted(context)) {
                    showVoiceRecognitionDialog(true)
                } else {
                    ActivityCompat.requestPermissions(activity!!,
                        arrayOf(Manifest.permission.RECORD_AUDIO),
                        RECORD_REQUEST_CODE)
                }
            } else {
                btnResultados.visibility = View.GONE
                edt_search.setText("")
                searchListAdapter.clearData()
            }

        }

        btnResultados.setOnClickListener {
            val intent = Intent(context, SearchResultsActivity::class.java)
            intent.putExtra(TEXT_SEARCHED, edt_search.text.toString())
            startActivityForResult(intent, SearchResultsActivity.RESULT)
            presenter.trackPrintEventSearch(AnalyticsCategoryType.BUSCADOR, Search.ACTION_SEARCH_RESULTS_MORE, Search.NOT_AVAILABLE, GlobalConstant.SCREEN_SEARCH_LIST, Search.EVENT_SEARCH_RESULTS_MORE)
        }

        searchLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        searchListAdapter = SearchListAdapter(this@SearchListFragment.context!!, this)
        rvw_search.layoutManager = searchLayoutManager
        rvw_search.adapter = searchListAdapter

        edt_search.onFocusChangeListener = this

        rvw_search.addOnScrollListener(object: RecyclerScroll() {

            override fun show() {
                lnlResultados.animate().translationY(0f).setInterpolator(DecelerateInterpolator(0.3f)).start()
            }

            override fun hide() {
                lnlResultados.animate().translationY(lnlResultados.height.toFloat()).setInterpolator(AccelerateInterpolator(0.3f)).start()
            }

        })

    }

    private fun showRecentOffers(visible: Boolean) {
        if (visible) {
            context?.let { container.setBackgroundColor(ContextCompat.getColor(it, R.color.gray_1)) }
            llContainerRecentOffers.visibility = View.VISIBLE
        } else {
            context?.let { container.setBackgroundColor(ContextCompat.getColor(it, R.color.white)) }
            llContainerRecentOffers.visibility = View.GONE
        }

    }

    private fun showCategories(flag: Boolean) {
        if (flag)
            llContainerCategorias.visibility = View.VISIBLE
        else
            llContainerCategorias.visibility = View.GONE
    }


    override fun setCategoriesInSearch(categories: List<Categoria?>) {

        if (categories.isNotEmpty()) {
            val list = transformCategoriesList(categories)
            catInSearch.setValues(list)
            catInSearch.catInSearchListener = this
        } else
            llContainerCategorias.visibility = View.GONE

    }

    override fun loadRecentOffers(recentOffers: List<SearchRecentOffer?>?) {
        val recentOfferList = transformList(recentOffers)
        this.recentOffers.clear()
        this.recentOffers.addAll(recentOfferList)
        miniVerticalOffers.setProducts(recentOfferList)
        miniVerticalOffers.setStamp(this.listSellos)

        if (recentOfferList.isEmpty())
            showRecentOffers(false)
        else
            showRecentOffers(true)

        flagRecentOffers = false
    }

    override fun setConfigSellos(listSello: ArrayList<Sello?>) {
        this.listSellos.clear()
        this.listSellos.addAll(listSello)

    }

    private fun transformCategoriesList(input: List<Categoria?>): ArrayList<CatInSearchModel> {
        val output = arrayListOf<CatInSearchModel>()
        input.filterNotNull().forEach {
            output.add(CatInSearchModel(it.nombre ?: getString(R.string.categories_default_name),
                it.cantidad.toString(),
                it.urlImagenGrupo.toString(),
                it.codigo.toString()))
        }
        return output
    }

    //Listener catInSearch
    override fun pressedItemCat(item: CatInSearchModel, position: Int) {
        val intent = Intent(context, SearchResultsActivity::class.java)
        intent.putExtra(TEXT_SEARCHED, StringUtil.Empty)
        intent.putExtra(CODE_CAT, item.code)
        intent.putExtra(NAME_CAT, item.nameCat)
        startActivityForResult(intent, SearchResultsActivity.RESULT)
    }

    private fun showVoiceSearch() {
        if (edt_search.text.isEmpty()) {
            if(llContainerRecentOffers.visibility == View.GONE)
                llContainerCategorias.visibility = View.VISIBLE
            else
                llContainerCategorias.visibility = View.GONE
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun showKeyboard() {
        activity?.getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    private fun validateText(text: String) {

        val maxList = user?.totalResultadosBuscador ?: DEFAULT_RESULTADOS_MOSTRAR
        val maxSearch = user?.caracteresBuscador ?: DEFAULT_CARACTERES_BUSCADOR

        val promotionGroupListEnabled = presenter.getPromotionGroupList() ?: false

        softKeyHandler.removeCallbacksAndMessages(null)
        handler.removeCallbacksAndMessages(null)
        handler = Handler()
        handler.postDelayed({

            if (text.length >= maxSearch) {

                presenter.searchText(SearchRequest().apply {
                    campaniaId = user?.campaing?.toInt()
                    codigoZona = user?.zoneCode
                    textoBusqueda = text
                    personalizacionesDummy = user?.personalizacionesDummy ?: StringUtil.Empty
                    fechaInicioFacturacion = user?.billingStartDate ?: StringUtil.Empty
                    configuracion = SearchConfiguracion().apply {
                        rdEsSuscrita = user?.isRDEsSuscrita
                        rdEsActiva = user?.isRDEsActiva
                        lider = user?.lider
                        rdActivoMdo = user?.isRDActivoMdo
                        rdTieneRDC = user?.isRDTieneRDC
                        rdTieneRDI = user?.isRDTieneRDI
                        rdTieneRDCR = user?.isRDTieneRDCR
                        diaFacturacion = user?.diaFacturacion
                        agrupaPromociones = promotionGroupListEnabled
                    }
                    paginacion = SearchPaginacion().apply {
                        numeroPagina = 0
                        cantidad = maxList
                    }
                    orden = SearchOrden().apply {
                        campo = "orden"
                        tipo = "Asc"
                    }
                })
            }

        }, delay)


        btnResultados.visibility = View.GONE

        if (text.isEmpty()) {
            searchListAdapter.clearData()
            lnlEmpty.visibility = View.GONE
            img_cancel.visibility = View.GONE
            llContainerCategorias.visibility = View.VISIBLE
        } else {
            searchEnabled = false
            llContainerCategorias.visibility = View.GONE
            lnlClear.isClickable = true
            img_cancel.visibility = View.VISIBLE
            loader.visibility = View.GONE
        }

    }

    private fun showEmptyScreen(title: String, body: String) {
        lnlEmpty.visibility = View.VISIBLE
        lnl_empty_title.text = title
        lnl_empty_body.text = body
    }

    // Public Functions
    fun trackBackPressed() {
        presenter.trackBackPressed()
    }

    fun openKeyboard() {
        edt_search.requestFocus()
        if (edt_search.requestFocus()) {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(edt_search, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    fun closeKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(edt_search?.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    fun showVoiceRecognitionDialog(status: Boolean) {
        if (status) {
            llContainerCategorias.visibility = View.GONE
            context?.let{
                VoiceRecognitionDialog(it,
                    it.resources?.getString(R.string.voice_recognition_buscar),
                    null,
                    this).show()
            }

        } else {
            ToastUtil.show(context!!, resources.getString(R.string.search_voice_permission), Toast.LENGTH_SHORT)
        }
    }


    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (hasFocus) {
            user?.let {
                presenter.trackPrintEventSearch(AnalyticsCategoryType.BUSCADOR, Search.ACTION_SEARCH_SELECT, Search.NOT_AVAILABLE, GlobalConstant.SCREEN_SEARCH_LIST, Search.EVENT_SEARCH_SELECT)
            }
        }
    }

    override fun didDeletedItem(pos: Int) {
        recentOffers[pos]?.let { offer ->
            offer.key?.let {
                recentOffers.removeAt(pos)
                presenter.deleteByCuv(it)
            }
        }
    }

    override fun didPressedBorrarTodo() {
        context?.let {
            BottomDialog.Builder(it)
                .setIcon(R.drawable.ic_anima_por)
                .setTitle(resources.getString(R.string.recent_search_delete_title))
                .setTitleBold()
                .setNegativeText(resources.getString(R.string.recent_search_delete_negative))
                .setPositiveText(resources.getString(R.string.recent_search_delete_positive))
                .setNegativeTextColor(R.color.black)
                .setNegativeBorderColor(R.color.black)
                .setNegativeBackgroundColor(R.color.white)
                .onNegative(object : BottomDialog.ButtonCallback {
                    override fun onClick(dialog: BottomDialog) {
                        openKeyboard()
                        dialog.dismiss()

                    }
                })
                .onPositive(object : BottomDialog.ButtonCallback {
                    override fun onClick(dialog: BottomDialog, chbxConfirmacion: CheckBox) {
                        deleteAllRecentOffers()
                        showCategories(true)
                        dialog.dismiss()

                    }
                })
                .setPositiveBackgroundColor(R.color.magenta)
                .show()
        }

    }

    private fun deleteAllRecentOffers() {
        this.recentOffers.clear()
        presenter.deleteAllRecentOffers()
        showRecentOffers(false)
    }

    override fun didPressedItem(item: OfferModel, pos: Int, type: Any?) {
        val extras = Bundle()
        extras.putString(BaseFichaActivity.EXTRA_KEY_ITEM, item.key)
        extras.putString(BaseFichaActivity.EXTRA_TYPE_OFFER, item.leverName)

        extras.putInt(BaseFichaActivity.EXTRA_ACCESS_FROM, BaseFichaActivity.ACCESS_FROM_BUSCADOR_DESPLEGABLE)

        activity?.let {
            val intentToLaunch = FichaProductoActivity.getCallingIntent(it)
            intentToLaunch.putExtras(extras)
            it.startActivityForResult(intentToLaunch, REQUEST_CODE_RECENT_OFFER)
        }

    }

    override fun didPressedItemButtonAdd(typeLever: String, keyItem: String, quantity: Int, counterView: Counter, type: Any?) {
        // NOT REQUIRED

    }

    override fun didPressedItemButtonSelection(item: OfferModel, pos: Int, type: Any?) {
        // NOT REQUIRED

    }

    override fun didPressedItemButtonShowOffer(item: OfferModel, pos: Int, type: Any?) {
        // NOT REQUIRED

    }

    override fun impressionItems(typeLever: String, list: ArrayList<OfferModel>, type: Any?) {
        // NOT REQUIRED
    }

    override fun loadPromotions(list: ArrayList<OfferPromotionDual>) {
        var position = 0
        if (this.searchListAdapter.positionCarousel > 0) {
            position = this.searchListAdapter.positionCarousel - 1
        }

            val data = this.searchListAdapter.getList()
        if (data.size > 0 && list.size > 0) {
            data[position]?.let {
                val item = it as? Promotion
                item?.let{itemV ->
                    itemV.detalleCondition = list
                    data[position] = itemV
                    this.searchListAdapter.notifyItemChanged(position)
                }
            }
        }
    }

    private fun transformList(recentOffers: List<SearchRecentOffer?>?): ArrayList<OfferModel> {
        var listOfferModel = arrayListOf<OfferModel>()
        val countryMoneySymbol = this.user?.countryMoneySymbol
        recentOffers?.forEach {
            val offer = OfferModel()
            safeLet(it?.cuv, it?.nombreOferta, it?.precioValorizado, it?.precioCatalogo, it?.tipoOferta) { cuv, nombre, precioValorizado, precioCatalogo, tipo ->
                offer.apply {
                    this.key = cuv
                    this.productName = nombre
                    this.personalAmount = "$countryMoneySymbol ${decimalFormat.format(precioValorizado)}"
                    this.clientAmount = "$countryMoneySymbol ${decimalFormat.format(precioCatalogo)}"
                    this.leverName = tipo
                }
            }
            offer.imageURL = it?.imagenURL ?: StringUtil.Empty
            offer.flagPromotion = it?.flagPromocion ?: false
            offer.flagFestival = it?.flagFestival ?: false
            offer.esCatalogo = it?.flagCatalogo ?: false
            listOfferModel.add(offer)
        }
        return listOfferModel
    }

    override fun onScroll() {
        hideKeyboard()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    companion object {
        const val DEFAULT_CARACTERES_BUSCADOR = 3
        const val DEFAULT_RESULTADOS_MOSTRAR = 20
        const val RECORD_REQUEST_CODE = 100
        const val TEXT_SEARCHED = "searched"
        const val CODE_CAT = "codeCat"
        const val NAME_CAT = "nameCat"

        const val TYPE_FESTIVAL_ADDED = 1
        const val TYPE_FESTIVAL_REACHED = 2
        const val REQUEST_CODE_RECENT_OFFER = 3
    }

}

