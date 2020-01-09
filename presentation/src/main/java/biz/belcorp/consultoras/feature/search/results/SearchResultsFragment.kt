package biz.belcorp.consultoras.feature.search.results

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.GridLayoutManager
import android.util.DisplayMetrics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.base.SafeLet
import biz.belcorp.consultoras.common.component.FilterLayout
import biz.belcorp.consultoras.common.component.OrderByListBottom
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.model.product.ProductCUVModel
import biz.belcorp.consultoras.common.model.search.OrigenModel
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.data.manager.SessionManager
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.util.OfferTypes
import biz.belcorp.consultoras.feature.ficha.common.BaseFichaActivity
import biz.belcorp.consultoras.feature.ficha.producto.FichaProductoActivity
import biz.belcorp.consultoras.feature.home.fest.FestActivity
import biz.belcorp.consultoras.feature.home.ganamas.adapters.OffersGridItemListener
import biz.belcorp.consultoras.feature.search.di.SearchComponent
import biz.belcorp.consultoras.feature.search.list.SearchListFragment
import biz.belcorp.consultoras.util.AppBarStateChangeListener
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.ImageUtils
import biz.belcorp.consultoras.util.analytics.Search
import biz.belcorp.consultoras.util.anotation.AnalyticsCategoryType
import biz.belcorp.consultoras.util.anotation.OffersOriginTypeLocation
import biz.belcorp.consultoras.util.anotation.OffersOriginTypeSection
import biz.belcorp.consultoras.util.anotation.SearchOriginType
import biz.belcorp.consultoras.util.broadcast.FestBroadcast
import biz.belcorp.consultoras.util.decorations.GridCenterSpacingDecoration
import biz.belcorp.consultoras.util.Belcorp
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.util.CountryUtil
import biz.belcorp.library.util.DeviceUtil
import biz.belcorp.library.util.NetworkUtil
import biz.belcorp.library.util.StringUtil
import biz.belcorp.mobile.components.core.helpers.ImagesHelper
import biz.belcorp.mobile.components.design.counter.Counter
import biz.belcorp.mobile.components.design.filter.FilterPickerDialog
import biz.belcorp.mobile.components.design.filter.model.CategoryFilterModel
import biz.belcorp.mobile.components.design.filter.model.FilterModel
import biz.belcorp.mobile.components.offers.Offer
import biz.belcorp.mobile.components.offers.model.OfferModel
import biz.belcorp.mobile.components.offers.sello.Sello
import kotlinx.android.synthetic.main.fragment_search_results.*
import java.text.DecimalFormat
import javax.inject.Inject


/**
 * Created by Leonardo on 13/09/2018.
 */

class SearchResultsFragment : BaseFragment(), SearchResultsView,
    OrderByListBottom.Listener,
    FilterLayout.OnFilterClick, SafeLet, OffersGridItemListener {


    private var productList = mutableListOf<(ProductCUV?)>()
    private lateinit var searchGridAdapter: SearchResultsGridAdapter
    private var moneySymbol: String = ""
    private lateinit var imageHelper: ImagesHelper
    private lateinit var carouselLayManager: GridLayoutManager
    private lateinit var ofertas: ArrayList<OfferModel?>

    @Inject
    lateinit var presenter: SearchResultsPresenter
    private var orderByListBottom = OrderByListBottom.newInstance()
    private var user: User? = null

    private var decimalFormat: DecimalFormat = DecimalFormat()
    private var searched: String = ""
    private var searchTotal: Int? = null
    private var searchPage: Int = FIRST_PAGE
    private var firstLoad: Boolean = true
    private var canScroll: Boolean = true
    private var order: String = DEFAULT_ORDER
    private var orderType: String = DEFAULT_ORDER_TYPE
    private var selectedFilters = mutableListOf<SearchFilter>()

    private var filtros: ArrayList<CategoryFilterModel>? = null

    var itemsAdded: Boolean = false
    var imageDialog: Boolean = false

    var codeCat: String? = null
    var categoryName: String? = null

    private var counterViewProductAdded: Counter? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search_results, container, false)
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

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    /**
     * BaseFragment Overrides
     */
    override fun context(): Context? {
        val activity = activity
        return activity?.applicationContext
    }

    /**
     * SearchListView Overrides
     */
    override fun initScreenTrack(model: LoginModel) {
        Tracker.trackScreen(GlobalConstant.SCREEN_SEARCH_LIST, model)
    }

    override fun trackBackPressed(model: LoginModel) {
        Tracker.trackBackEvent(GlobalConstant.SCREEN_SEARCH_LIST, model)
    }

    override fun setUser(user: User?) {
        this.user = user
        this.decimalFormat = CountryUtil.getDecimalFormatByISO(user?.countryISO!!, true)
        this.moneySymbol = user.countryMoneySymbol?.let { it } ?: ""

        showLoading()
        getProductList()
    }

    @SuppressLint("SetTextI18n")
    override fun onSearchResult(result: SearchResponse?) {
        result?.let { response ->

            response.productos?.let { products ->
                if (products.isEmpty() && ofertas.isEmpty()) {

                    emptyScreen(true)
                    setFiltersCount(response.total ?: 0, filtros ?: arrayListOf())

                } else {

                    setFiltersCount(response.total ?: 0, filtros ?: arrayListOf())

                    emptyScreen(false)
                    productList.addAll(products)

                    setOfertas(transformListProductCUV(products.toList()))

                    if (firstLoad) {

                        firstLoad = false

                        if (user?.isMostrarFiltrosBuscador == false && user?.isMostrarOpcionesOrdenamientoBuscador == false) {
                            appbar_layout.visibility = View.GONE
                            activity?.let { (it as SearchResultsActivity).setToolbarElevation(true) }
                            txt_orderby.text = "$searchTotal ${resources.getString(R.string.search_result_number)} $searched"
                            lnl_orderby.visibility = View.VISIBLE
                            return
                        }

                        if (user?.isMostrarOpcionesOrdenamientoBuscador == true && user?.isMostrarFiltrosBuscador == true) {
                            btn_orderby.visibility = View.VISIBLE
                            btn_filterby.visibility = View.VISIBLE
                            presenter.getOrderByParameters()
                            return
                        }

                        if (user?.isMostrarOpcionesOrdenamientoBuscador == true) {
                            btn_orderby.visibility = View.VISIBLE
                            presenter.getOrderByParameters()
                            return
                        }

                        if (user?.isMostrarFiltrosBuscador == true) {
                            btn_filterby.visibility = View.VISIBLE
                            appbar_layout?.setExpanded(true, true)
                            return
                        }
                    }
                }
            }

            canScroll = true

        } ?: run {
            Toast.makeText(context, "No se encontraron resultados", Toast.LENGTH_SHORT).show()
        }
    }

    fun setOfertas(ofertas: List<OfferModel>) {
        updateData(ofertas)
    }

    private fun updateData(ofertas: List<OfferModel>) {
        this.ofertas.addAll(ofertas)
        searchGridAdapter.itemPlaceholder = resources.getDrawable(R.drawable.ic_container_placeholder)
        searchGridAdapter.updateData(this.ofertas)
    }

    private fun transformListProductCUV(offers: List<ProductCUV?>): ArrayList<OfferModel> {
        val list = arrayListOf<OfferModel>()

        for (offer in offers) {

            var showAmount = true
            if (offer?.tipoPersonalizacion == OfferTypes.HV) {
                showAmount = false
            }

            safeLet(offer?.cuv, offer?.descripcionEstrategia ?: "", offer?.description,
                offer?.precioValorizado, offer?.precioCatalogo, offer?.fotoProductoSmall
                ?: "", "", "#000000", offer?.codigoTipoEstrategia ?: "",
                offer?.codigoEstrategia ?: 0, offer?.marcaId ?: 0, offer?.stock ?: true, offer?.flagPromocion ?: false)
            { cuv, nombreMarca, nombreOferta, precioValorizado, precioCatalogo, imagenURL,
              imagenFondo, colorTexto, codigoTipoEstrategia, codigoEstrategia, marcaID, stock, flagPromocion ->
                list.add(Offer.transform(cuv, nombreMarca, nombreOferta,
                    formatWithMoneySymbol(precioCatalogo.toString()),
                    formatWithMoneySymbol(precioValorizado.toString()),
                    imagenURL, "", checkSelectionType(codigoTipoEstrategia, codigoEstrategia), marcaID,
                    imageHelper.getResolutionURL(imagenFondo), colorTexto, showAmount, null, offer?.tipoPersonalizacion, !stock, flagPromocion))
            }
        }

        return list
    }


    private fun formatWithMoneySymbol(precio: String): String {
        return "$moneySymbol ${decimalFormat.format(precio.toBigDecimal())}"
    }

    private fun checkSelectionType(codigoTipoEstrategia: String?, codigoEstrategia: Int): Boolean {
        return (isOfertaDigital(codigoTipoEstrategia) && codigoEstrategia == COD_EST_COMPUESTA_VARIABLE)
    }

    private fun isOfertaDigital(codigoTipoEstrategia: String?): Boolean {
        return (codigoTipoEstrategia in listOf("030", "005", "001", "007", "008", "009", "010", "011", "LMG"))
    }

    private fun showItemSelected(keyItem: String, marcaID: Int, marca: String, position: Int) {

        searchProductFromOfferModel(keyItem)?.let { product ->

            val productItem = ProductCUVModel.transformList(product)

            user?.let {
                presenter.trackEvent(GlobalConstant.SCREEN_SEARCH_LIST,
                    GlobalConstant.EVENT_CAT_SEARCH_RESULT,
                    GlobalConstant.EVENT_ACTION_SEARCH_CHOOSE,
                    "${product.description} - $searched",
                    GlobalConstant.EVENT_NAME_VIRTUAL_EVENT,
                    it)
            }

            activity?.let { mContext ->
                if (!NetworkUtil.isThereInternetConnection(context())) {
                    showNetworkError()
                } else {

                    val extras = Bundle()
                    extras.putString(BaseFichaActivity.EXTRA_KEY_ITEM, product.cuv)
                    extras.putString(BaseFichaActivity.EXTRA_TYPE_OFFER, product.tipoPersonalizacion)
                    marcaID.let { extras.putInt(BaseFichaActivity.EXTRA_MARCA_ID, it) }
                    extras.putString(BaseFichaActivity.EXTRA_MARCA_NAME, product.descripcionMarca)
                    extras.putInt(BaseFichaActivity.EXTRA_ACCESS_FROM, BaseFichaActivity.ACCESS_FROM_BUSCADOR_LANDING)
                    extras.putString(BaseFichaActivity.EXTRA_ORIGEN_PEDIDO_WEB_FROM, SearchOriginType.ORIGEN_BUSCADOR_LANDING_FICHA)
                    extras.putString(BaseFichaActivity.EXTRA_TYPE_PERSONALIZATION, product.tipoPersonalizacion)
                    extras.putParcelableArrayList(BaseFichaActivity.EXTRAS_ORIGENES_PEDIDO_SEARCH, OrigenModel.transformList(product.origenesPedidoWeb))

                    extras.putParcelableArrayList(BaseFichaActivity.EXTRAS_PRODUCTO_ITEM, arrayListOf<ProductCUVModel>().apply { add(productItem) })

                    val intentToLaunch = FichaProductoActivity.getCallingIntent(mContext)
                    intentToLaunch.putExtras(extras)
                    mContext.startActivity(intentToLaunch)
                    itemsAdded = true

                }
            }
        }
    }

    override fun onSearchError(text: String?, exception: Throwable?) {
        canScroll = true
        exception?.let { BelcorpLogger.d("SearchListFragment", it) }
    }

    override fun onAddProductError(text: String?, exception: Throwable?) {
        Toast.makeText(context, text
            ?: resources.getString(R.string.search_add_product_error), Toast.LENGTH_LONG).show()
        /*
        text?.let {
            context?.let { context ->
                BottomDialog.Builder(context)
                    .setIcon(R.drawable.ic_mano_error)
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
        */
        exception?.let { BelcorpLogger.d("SearchListFragment", it) }
    }

    override fun onProductAdded(productCUV: ProductCUV, message: String?, resultCode: String?) {
        productCUV?.cuv?.let {
            activity?.let { FestBroadcast.sendAddUpdateToCart(it) }
            updateCart(productCUV.cantidad ?: 0)
            val image = ImageUtils.verifiedImageUrl(productCUV)

            if (resultCode == GlobalConstant.CODIGO_PREMIO_FESTIVAL_ALCANZADO){
                context?.let{
                    showBottomDialogAction(it, message?: StringUtil.Empty, ::goToFestival)
                }
            } else {
                val messagex = message ?: getString(R.string.msj_offer_added)
                context?.let {
                    val url = if (imageDialog) image else null  // Verifica que el flag este activo para mostrar la imagen
                    val colorText = ContextCompat.getColor(it, R.color.leaf_green)
                    showBottomDialog(it, messagex, url, colorText)
                }
            }
        }
        this.counterViewProductAdded?.resetQuantity()
    }

    override fun onPrintProduct(item: OfferModel, position: Int) {
        searchProductFromOfferModel(item.key)?.let {
            val product = it
            user?.let {
                presenter.trackPrintProduct(product, it, position, OffersOriginTypeLocation.ORIGEN_BUSCADOR, OffersOriginTypeSection.ORIGEN_RESULTADOS)
            }
        }
    }

    override fun onPrintClickProduct(item: OfferModel, position: Int) {
        searchProductFromOfferModel(item.key)?.let {
            val product = it
            user?.let {
                presenter.trackPrintClickProduct(product, it, position, OffersOriginTypeLocation.ORIGEN_BUSCADOR, OffersOriginTypeSection.ORIGEN_RESULTADOS)
            }
        }
    }

    override fun onPrintAddProduct(item: OfferModel, quantity: Int) {
        searchProductFromOfferModel(item.key)?.let {
            val product = it
            user?.let {
                presenter.trackPrintAddProduct(product, it, quantity, OffersOriginTypeLocation.ORIGEN_BUSCADOR, OffersOriginTypeSection.ORIGEN_RESULTADOS)
            }
        }
    }


    fun searchProductFromOfferModel(key: String): ProductCUV? {
        return productList.firstOrNull { it!!.cuv == key }
    }

    private fun goToFestival(){
        context?.let {
            val intent = Intent(it, FestActivity::class.java)
            ContextCompat.startActivity(it, intent, null)
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

    override fun setImageEnabled(imageDialog: Boolean) {
        this.imageDialog = imageDialog
    }

    fun getFilterTextetSelect(): String {
        var filters = StringUtil.Empty
        filtros?.forEach { filter ->
            val optionsSelected = filter.list.filter { it.checked }
            optionsSelected.let { option ->
                option.forEach {op ->
                    op.name.let { filter ->
                        filters = if(filters.isEmpty())
                            filter
                        else
                            filters + Search.MIDDLE_DASH + filter
                    }
                }
            }
        }
        return filters
    }

    // Filter Listener Overrides
    /*override fun onApply() {

        productList.clear()
        ofertas.clear()
        searchGridAdapter.resetData()

        refresh()
        hideFilters()
    }

    override fun onClose() {
        hideFilters()
    }

    // Chips Listener Overrides
    override fun onChipDeleted(chip: SearchFilterChild) {

        val lista = mutableListOf<SearchFilter>()
        selectedFilters.forEach { filter ->
            filter.opciones?.filter { it?.idFiltro != chip.idFiltro }?.let {
                lista.add(SearchFilter().apply { nombreGrupo = filter.nombreGrupo; opciones = it })
            }
        }
        filter_layout.setFilters(lista)
        refresh()
        presenter.trackEvent(
            GlobalConstant.SCREEN_SEARCH_LIST,
            GlobalConstant.EVENT_CAT_SEARCH_RESULT,
            GlobalConstant.EVENT_ACTION_DELETE_FILTER,
            chip.nombreFiltro ?: GlobalConstant.NOT_AVAILABLE,
            GlobalConstant.EVENT_NAME_VIRTUAL_EVENT
        )
    }*/


    private fun calcularOrigenPedidoWeb(item: ProductCUV, origenPedidoWebFrom: String?): String {
        return item.origenesPedidoWeb?.firstOrNull { it?.codigo == origenPedidoWebFrom }?.valor
            ?: "0"
    }

    override fun onOrderByParametersResult(parameters: Collection<SearchOrderByResponse?>) {
        orderByListBottom.setData(parameters.filterNotNull())

        appbar_layout.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout, state: AppBarStateChangeListener.State) {
                if (state == AppBarStateChangeListener.State.COLLAPSED) {
                    activity?.let { (it as SearchResultsActivity).setToolbarElevation(true) }
                } else {
                    activity?.let { (it as SearchResultsActivity).setToolbarElevation(false) }
                }
            }
        })

        btn_orderby.setOnClickListener {
            activity?.let {
                if (!orderByListBottom.isVisible) {
                    orderByListBottom.show(it.supportFragmentManager, "total_fragment")
                }
            }
        }

        Handler().postDelayed({
            orderByListBottom.setDefaultChecked()
            appbar_layout?.setExpanded(true, true)
            parameters.toList()[0]?.descripcion?.let {
                txt_orderby?.text = "${resources.getString(R.string.search_result_order_by)} $it"
                lnl_orderby?.visibility = View.VISIBLE
            }
        }, 300)

    }

    /**
     * OrderByListBottom Overrides
     */
    override fun setSortMethod(orderByItem: SearchOrderByResponse, position: Int) {

        productList.clear()
        ofertas.clear()
        searchGridAdapter.resetData()

        progressBar.visibility = View.GONE

        val value = orderByItem.valor?.split("-")
        order = value?.get(0) ?: DEFAULT_ORDER
        orderType = value?.get(1) ?: DEFAULT_ORDER_TYPE

        txt_orderby.text = "${resources.getString(R.string.search_result_order_by)} ${orderByItem.descripcion}"

        user?.let {
            Tracker.trackEvent(GlobalConstant.SCREEN_SEARCH_LIST,
                GlobalConstant.EVENT_CAT_SEARCH_RESULT,
                GlobalConstant.EVENT_ACTION_RESULT_ORDER_BY,
                orderByItem.descripcion,
                GlobalConstant.EVENT_NAME_VIRTUAL_EVENT,
                it)
        }

        Handler().postDelayed({
            refresh()
            orderByListBottom.dismiss()
        }, 200)

        orderByItem.descripcion?.let { text ->
            presenter.trackPrintEventSearch(AnalyticsCategoryType.BUSCADOR, Search.ACTION_ORDER_BY, text, GlobalConstant.SCREEN_SEARCH_LIST, Search.EVENT_SEARCH_RESULT_ORDER_BY)
        }
    }

    override fun onFilterSelected(filterName: String?, filterAction: String?) {
        presenter.trackEventFilterSelected(filterAction ?: GlobalConstant.NOT_AVAILABLE,
            GlobalConstant.SCREEN_SEARCH_LIST,
            GlobalConstant.EVENT_CAT_SEARCH_RESULT,
            GlobalConstant.EVENT_ACTION_FILTER_FROM,
            filterName ?: GlobalConstant.NOT_AVAILABLE,
            GlobalConstant.EVENT_NAME_VIRTUAL_EVENT)
    }

    override fun onFilterClear(buttonName: String) {
        presenter.trackEvent(
            GlobalConstant.SCREEN_SEARCH_LIST,
            GlobalConstant.EVENT_CAT_SEARCH_RESULT,
            GlobalConstant.EVENT_ACTION_CLICK_BUTTON,
            buttonName,
            GlobalConstant.EVENT_NAME_VIRTUAL_EVENT
        )
    }

    /**
     * Private Functions
     */
    @SuppressLint("SetTextI18n")
    private fun init() {

        val session = context()?.let { SessionManager.getInstance(it) }

        orderByListBottom.listener = this

        searched = arguments?.getString(SearchListFragment.TEXT_SEARCHED) ?: ""
        codeCat = arguments?.getString(SearchListFragment.CODE_CAT)
        categoryName = arguments?.getString(SearchListFragment.NAME_CAT)
        txt_searched.text = searched

        ViewCompat.setNestedScrollingEnabled(rvw_search, false)

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            btn_filterby.setBackgroundResource(R.drawable.selector_button_gris)
            btn_orderby.setBackgroundResource(R.drawable.selector_button_gris)
        }

        if (checkScreenDensity()) {
            rvw_search.layoutManager = GridLayoutManager(context, 2)
        } else {
            rvw_search.layoutManager = GridLayoutManager(context, 1)
        }


        carouselLayManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        rvw_search.layoutManager = carouselLayManager
        rvw_search.setHasFixedSize(true)

        ofertas = arrayListOf()
        val hideViewABTest = session?.getHideViewsGridGanaMas() ?: false
        searchGridAdapter =  SearchResultsGridAdapter(hideViewABTest)

        searchGridAdapter.stampLists.add(getPromotionStamp())

        searchGridAdapter.listenerOffer = this
        rvw_search.adapter = searchGridAdapter

        val spacingGrid = resources.getDimensionPixelSize(R.dimen.offer_grid_spacing)
        rvw_search.addItemDecoration(GridCenterSpacingDecoration(2, spacingGrid, 0))

        presenter.getUser()
        presenter.getImageEnabled()

        presenter.getFiltros(true)

        nsv_content.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, _, _, _ ->
            if (!nsv_content.canScrollVertically(1)) {
                if (canScroll) {
                    if (getNextPage()) {
                        canScroll = false
                        progressBar.visibility = View.VISIBLE
                        searchPage += 1
                        getProductList()
                    } else {
                        progressBar.visibility = View.GONE
                    }
                }
            }
        })

        setupFilter()

        activity?.let {
            imageHelper = ImagesHelper(it)
        }

    }

    fun getPromotionStamp(): Sello{
        var sello = Sello(requireContext())
        sello.setText(getString(R.string.ficha_promotion))
        sello.setTextSize(requireContext().resources.getDimension(R.dimen.promotion_text))
        sello.setType(Sello.PROMOTION)
        sello.setStartColor(ContextCompat.getColor(requireContext(), R.color.gana_mas_promotion_start))
        sello.setEndColor(ContextCompat.getColor(requireContext(), R.color.gana_mas_promotion_end))
        sello.setOrientation(3)
        sello.setWidthLayout(requireContext().resources.getDimension(R.dimen.promotion_widht).toInt())
        sello.setHeightLayout(requireContext().resources.getDimension(R.dimen.promotion_height).toInt())

        return sello
    }

    override fun setFilters(filters: List<GroupFilter?>) {
        if (filters.isEmpty())
            hideFilters()
        else
            filtros = transformCategoriesFiltersList(filters)
    }

    private fun transformCategoriesFiltersList(input: List<GroupFilter?>): ArrayList<CategoryFilterModel> {
        val output = arrayListOf<CategoryFilterModel>()
        input.forEach {group ->
            codeCat?.let {
                if(group?.nombre != TITLE_CATEGORIES){
                    output.add(CategoryFilterModel(
                        key = group?.nombre!!,
                        name = group?.nombre!!,
                        excluyente = group?.excluyente!!,
                        list = transformFilterList(group.filtros)
                    ))
                }
            }?:run {
                output.add(CategoryFilterModel(
                    key = group?.nombre!!,
                    name = group?.nombre!!,
                    excluyente = group?.excluyente!!,
                    list = transformFilterList(group.filtros)
                ))
            }



        }
        return output
    }

    private fun transformFilterList(input: List<Filtro?>?): ArrayList<FilterModel> {
        val output = arrayListOf<FilterModel>()
        input?.filterNotNull()?.forEach {
            safeLet(it.nombre, it.descripcion, it.valorMinimo, it.valorMaximo, it.idFiltro, it.idPadre, it.codigo, it.idSeccion) { name, description, minValue, maxValue, idFiltro, idPadre, codigo, idSeccion ->
                output.add(FilterModel(idFiltro.toString(), name, false, minValue, maxValue, 0, false, idPadre.toString(), codigo, idSeccion))
            }
        }
        return output
    }

    private fun setupFilter(){
        btn_filterby.setOnClickListener {
            context?.let {
                filtros?.let { filters ->
                    FilterPickerDialog.Builder(it)
                        .setCategories(filters)
                        .setOnFilterPickerListener(object: FilterPickerDialog.FilterPickerListener{
                            override fun onApply(dialog: Dialog, filters: ArrayList<CategoryFilterModel>) {
                                filtros = filters
                                productList.clear()
                                ofertas.clear()

                                refresh()
                                appbar_layout?.setExpanded(true, true)
                                presenter.trackPrintEventSearch(AnalyticsCategoryType.BUSCADOR, Search.ACTION_APPLY_FILTER, getFilterTextetSelect(), GlobalConstant.SCREEN_SEARCH_LIST, Search.EVENT_SEARCH_RESULT_APPLY_FILTER)

                                dialog.dismiss()
                            }

                            override fun onClose(dialog: Dialog) {
                                dialog.dismiss()
                            }

                            override fun onButtonClicked(buttonName: String?) {
                                presenter.trackPrintEventSearch(AnalyticsCategoryType.BUSCADOR, Search.ACTION_CLEAR_FILTER, Search.NOT_AVAILABLE, GlobalConstant.SCREEN_SEARCH_LIST, Search.EVENT_SEARCH_RESULT_CLEAR_FILTER)
                            }

                            override fun onItemRemoved(filterName: String?) {
                                //Not required
                            }

                            override fun onItemSelected(filterName: String?, filterGroup: String?) {
                                filterName?.let {
                                    presenter.trackPrintEventSearch(AnalyticsCategoryType.BUSCADOR, Search.ACTION_ADD_FILTER_BY, filterName, GlobalConstant.SCREEN_SEARCH_LIST, Search.EVENT_SEARCH_RESULT_ADD_FILTER)
                                }
                            }
                        }).show()

                }

            }

            presenter.trackEvent(
                GlobalConstant.SCREEN_SEARCH_LIST,
                GlobalConstant.EVENT_CAT_SEARCH_RESULT,
                GlobalConstant.EVENT_ACTION_CLICK_BUTTON,
                GlobalConstant.EVENT_LABEL_FILTER_BUTTON,
                GlobalConstant.EVENT_NAME_VIRTUAL_EVENT
            )
        }
    }

    private fun checkScreenDensity(): Boolean {
        val metrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(metrics)
        return metrics.density >= 2.0
    }

    private fun getProductList() {

        val maxList = user?.totalResultadosBuscador ?: SearchListFragment.DEFAULT_RESULTADOS_MOSTRAR

        val selectedFilters = arrayListOf<CategoryFilterModel>()
        selectedFilters.addAll(filtros?.toList() ?: arrayListOf())
        val requestFilters = getSelectedFilters(selectedFilters)
        val requestFiltersMutable = requestFilters?.toMutableList() ?: mutableListOf()

        codeCat?.let {code ->
            requestFiltersMutable.add(SearchFilter().apply {
                nombreGrupo = "Categorías"
                opciones = mutableListOf(SearchFilterChild().apply {
                    idFiltro = code
                    nombreFiltro = categoryName
                })
            })

            txt_searched.text = categoryName
        }

        presenter.searchText(SearchRequest().apply {
            campaniaId = user?.campaing?.toInt()
            codigoZona = user?.zoneCode
            textoBusqueda = searched
            personalizacionesDummy = user?.personalizacionesDummy ?: ""
            fechaInicioFacturacion = user?.billingStartDate ?: ""
            configuracion = SearchConfiguracion().apply {
                rdEsSuscrita = user?.isRDEsSuscrita
                rdEsActiva = user?.isRDEsActiva
                lider = user?.lider
                rdActivoMdo = user?.isRDActivoMdo
                rdTieneRDC = user?.isRDTieneRDC
                rdTieneRDI = user?.isRDTieneRDI
                rdTieneRDCR = user?.isRDTieneRDCR
                diaFacturacion = user?.diaFacturacion
                agrupaPromociones = false
            }
            paginacion = SearchPaginacion().apply {
                numeroPagina = searchPage
                cantidad = maxList
            }
            orden = SearchOrden().apply {
                campo = order
                tipo = orderType
            }
            filtros = requestFiltersMutable.toList()
            presenter.trackEventFilterOnApply(filtros,
                GlobalConstant.SCREEN_SEARCH_LIST,
                GlobalConstant.EVENT_CAT_SEARCH_RESULT,
                GlobalConstant.EVENT_ACTION_APPLY_FILTER,
                GlobalConstant.EVENT_NAME_VIRTUAL_EVENT)
        })

    }


    private fun getSelectedFilters(groups: ArrayList<CategoryFilterModel>?): List<SearchFilter?>?{
        val filterList = arrayListOf<SearchFilter>()

        groups?.filter { it.list.any { it
            it.checked } }?.forEach {
            filterList.add(SearchFilter().apply {
                nombreGrupo = it.key
                opciones = (it.list as List<FilterModel>).filter { it.checked }.map {
                    SearchFilterChild().apply {
                        idFiltro=it.codigo
                        min = it.minValue.toInt()
                        max = it.maxValue.toInt()
                        nombreFiltro=it.name
                        idSeccion = it.idSeccion
                    }
                }
            })
        }

        return filterList

    }

    private fun emptyScreen(show: Boolean) {

        if (show) {
            lnlEmpty.visibility = View.VISIBLE
            rvw_search.visibility = View.GONE
            progressBar.visibility = View.GONE
        } else {
            lnlEmpty.visibility = View.GONE
            rvw_search.visibility = View.VISIBLE
        }

    }

    private fun setFiltersCount(total: Int, filters: ArrayList<CategoryFilterModel>){
        searchTotal = total
        var cantidad = 0
        filters.forEach { category ->
            category.list.forEach { filter ->
                if(filter.checked)
                    cantidad++
            }
        }

        if(cantidad!=0)  txtFiltrar.text = "${resources.getText(R.string.search_filter_text)} ($cantidad)" else txtFiltrar.text = "${resources.getText(R.string.search_filter_text)}"

        if (total == 0) {
            txt_searched.visibility = View.GONE
            txt_cantidad?.text = "$searchTotal resultados"
        } else {
            txt_searched.visibility = View.VISIBLE
            txt_cantidad?.text = "$searchTotal ${resources.getString(R.string.search_result_number)}"
        }
    }


    private fun selectedFilters(filters: List<SearchFilter?>?): List<SearchFilter?>? {
        selectedFilters.clear()
        filters?.forEach {
            val options = it?.opciones?.filter { it?.marcado == true }
            options?.let { op ->
                if (!op.isEmpty()) {
                    if (it.nombreGrupo == "Precios") {
                        try {
                            op.forEach { option ->
                                val values = option?.idFiltro?.split("-")
                                values.let {
                                    option?.max = values!![2].toInt()
                                    option.min = values[1].toInt()
                                }
                            }
                        } catch (e: Exception) {
                            BelcorpLogger.d("SearchResultFragment : ${e.message}")
                        }
                    }

                    selectedFilters.add(SearchFilter().apply { nombreGrupo = it.nombreGrupo; opciones = options })
                }
            }
        }
        return selectedFilters
    }

    private fun getNextPage(): Boolean {
        searchTotal?.let {
            return it != searchGridAdapter.itemCount
        }
        return false
    }

    private fun refresh() {
        searchPage = 0
        searchGridAdapter.resetData()
        progressBar.visibility = View.GONE
        showLoading()
        getProductList()
    }

    override fun hideFilters() {
        btn_filterby.visibility = View.GONE
    }

    /**
     * Public Functions
     */
    fun trackBackPressed() {
        presenter.trackBackPressed()
    }


    override fun pressedItem(keyItem: String, marcaID: Int, marca: String, position: Int) {
        showItemSelected(keyItem, marcaID, marca, position)
    }

    override fun pressedItemButtonAdd(keyItem: String, quantity: Int, counterView: Counter) {

        val iden = DeviceUtil.getId(activity)
        val prod: List<ProductCUV?>

        prod = productList.filter {
            it!!.cuv.equals(keyItem)
        }

        val product: ProductCUV? = productList.firstOrNull { it?.cuv == keyItem }
        product?.apply {
            cantidad = quantity
            identifier = iden
            origenPedidoWeb = calcularOrigenPedidoWeb(prod[0]!!, SearchOriginType.ORIGEN_LANDING)
        }

        user?.let {
            var searchByCuv = false
            context?.let {
                if (Belcorp.isValidCuv(it, searched)) searchByCuv = true
            }
            presenter.trackAddItem(prod[0]!!, searchByCuv, it, "Resultados", searched)
        }

        product?.let {
            this.counterViewProductAdded = counterView
            presenter.agregar(it, iden)
        }

        itemsAdded = true
    }

    override fun pressedItemButtonSelection(keyItem: String, marcaID: Int, marca: String, position: Int) {
        showItemSelected(keyItem, marcaID, marca, position)
    }

    /** Listeners */
    companion object {
        const val COD_EST_COMPUESTA_VARIABLE = 2003
        const val FIRST_PAGE = 0
        const val DEFAULT_ORDER = "ORDEN"
        const val DEFAULT_ORDER_TYPE = "ASC"
        const val TITLE_CATEGORIES = "Categorías"
    }

}
