package biz.belcorp.consultoras.feature.home.subcampaing

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.SafeLet
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.data.manager.SessionManager
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.util.OfferTypes
import biz.belcorp.consultoras.feature.ficha.common.BaseFichaActivity
import biz.belcorp.consultoras.feature.ficha.producto.FichaProductoActivity
import biz.belcorp.consultoras.feature.home.BaseHomeFragment
import biz.belcorp.consultoras.feature.home.fest.FestFragment
import biz.belcorp.consultoras.feature.home.ganamas.GanaMasFragment.Companion.FILTER_TITLE
import biz.belcorp.consultoras.feature.home.ganamas.adapters.OffersGridAdapter
import biz.belcorp.consultoras.feature.home.ganamas.adapters.OffersGridItemListener
import biz.belcorp.consultoras.feature.home.ganamas.fragments.GanaMasErrorFragment
import biz.belcorp.consultoras.feature.home.subcampaing.di.SubCampaingComponent
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.ImageUtils
import biz.belcorp.consultoras.util.anotation.CategoryFilter
import biz.belcorp.consultoras.util.anotation.FromOpenActivityType
import biz.belcorp.consultoras.util.anotation.OffersOriginType
import biz.belcorp.consultoras.util.decorations.GridCenterSpacingDecoration
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.util.CountryUtil
import biz.belcorp.library.util.DeviceUtil
import biz.belcorp.library.util.NetworkUtil
import biz.belcorp.mobile.components.core.extensions.setSafeOnClickListener
import biz.belcorp.mobile.components.core.helpers.ImagesHelper
import biz.belcorp.mobile.components.design.counter.Counter
import biz.belcorp.mobile.components.design.filter.FilterPickerDialog
import biz.belcorp.mobile.components.design.filter.model.CategoryFilterModel
import biz.belcorp.mobile.components.design.filter.model.FilterModel
import biz.belcorp.mobile.components.dialogs.bottom.BottomDialog
import biz.belcorp.mobile.components.dialogs.list.ListDialog
import biz.belcorp.mobile.components.dialogs.list.model.ListDialogModel
import biz.belcorp.mobile.components.offers.Offer
import biz.belcorp.mobile.components.offers.model.OfferModel
import kotlinx.android.synthetic.main.fragment_fest_subcampaing.*
import kotlinx.android.synthetic.main.view_offers_filters.*
import org.jetbrains.anko.displayMetrics
import java.text.DecimalFormat
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlin.system.measureTimeMillis

class FestSubCampaingFragment : BaseHomeFragment(), SubCampaingView, OffersGridItemListener, SafeLet {

    override fun setOrders(orders: List<Ordenamiento?>) {
        setFilterOrderLabel(true, presenter.LAYOUT_ORDER)
        arrayDialogOrder = transformOrdersList(orders)
        listener?.setOrder(listItemListener, arrayDialogOrder!!)

        listener?.setSelectedItemOrder(0)
    }

    @Inject
    lateinit var presenter: SubCampaingPresenter
    private var isFromODD: Boolean = false
    lateinit var typeLever: String
    private var decimalFormat = DecimalFormat()
    private var moneySymbol: String = ""

    private var ofertaMap = mutableMapOf<String, Oferta>()

    private lateinit var errorFragment: SubCampaingErrorFragment

    private lateinit var ofertas: ArrayList<OfferModel?>
    private var offers = mutableListOf<Oferta>() // Lista original
    private lateinit var carouselLayManager: GridLayoutManager
    private lateinit var carouselAdapter: OffersGridAdapter
    private lateinit var imageHelper: ImagesHelper
    private var user: User? = null

    private var filtros: ArrayList<CategoryFilterModel>? = null
    private var filterOffers = mutableListOf<Oferta>() // Lista filtrada

    private var arrayDialogOrder: ArrayList<ListDialogModel>? = null

    private var order: String = DEFAULT_ORDER
    private var orderType: String = DEFAULT_ORDER_TYPE

    var listener: Listener? = null
    var imageDialog: Boolean = false

    private var configuracionList: List<ConfiguracionPorPalanca>? = null
    private var configuracionListOriginal = mutableListOf<ConfiguracionPorPalanca>()
    private var sectionSelected: Int = -1


    private fun init() {

        ofertas = arrayListOf()
        carouselLayManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        carouselOffers.layoutManager = carouselLayManager
        carouselOffers.isNestedScrollingEnabled = false
        carouselOffers.setHasFixedSize(true)

        val hideViewABTest = arguments?.getBoolean("flag_hide_views_for_testing") ?: false
        carouselAdapter = OffersGridAdapter(hideViewABTest)

        carouselAdapter.listenerOffer = this
        carouselAdapter.itemPlaceholder = resources.getDrawable(R.drawable.ic_container_placeholder)
        carouselOffers.adapter = carouselAdapter

        val spacingGrid = resources.getDimensionPixelSize(R.dimen.offer_grid_spacing)
        carouselOffers.addItemDecoration(GridCenterSpacingDecoration(2, spacingGrid, 1))

        carouselLayManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when {
                    carouselAdapter.isHeader(position) -> carouselLayManager.spanCount
                    else -> 1
                }
            }
        }

        lltOrderContainer.setOnClickListener {
            listener?.showOrder()
        }
        activity?.let {
            imageHelper = ImagesHelper(it)
        }

        presenter.getUser()

        setupErrorOffer()

        if (arguments?.getString(GlobalConstant.FROM_OPEN_ACTIVITY) == FromOpenActivityType.NOTIFICATION)
            presenter.getTitleSubcampaign()

    }

    override fun setTitleSubcampaign(title: String) {
        listener?.updateSubcampaignTitle(title)
    }

    override fun onOffersByLeverResponse(offers: List<Oferta?>, typeLever: String) {

        offers[0]?.let { oferta ->
            val orderedOffers = mutableListOf<Oferta>()

            if (oferta.tipoOferta == OfferTypes.SR) {
                //orderedOffers.addAll(offers.asSequence().filterNotNull().filter { it.esSubCampania == false }.toList())
                orderedOffers.addAll(offers.asSequence().filterNotNull().filter { it.esSubCampania == true }.toList())
            } else {
                orderedOffers.addAll(offers.filterNotNull())
            }
            Tracker.trackView(GlobalConstant.SCREEN_GANA_MAS_SECTION + typeLever, user)

            showFilters(true)

            try {
                    if (orderedOffers.isNotEmpty()) {
                        this.offers = orderedOffers
                        setOfertas( transformListOferta("",orderedOffers) , typeLever == OfferTypes.ODD)


                        setCantidadOferta(orderedOffers.size)

                        offers.forEach {
                            it?.cuv?.let { cuv -> this.ofertaMap.put(cuv, it) }
                        }

                    } else {
                        presenter.showErroFragment()
                    }

            } catch (e: Exception) {
                BelcorpLogger.w(GlobalConstant.GANA_MAS_SET_OFFERS, e.message)
            }
        }
    }

    private fun showFilters(show: Boolean) {
        abLayout?.visibility = View.VISIBLE
        abLayout.setExpanded(true, false)
        if (show) {
            lnlFilters?.visibility = View.VISIBLE
        } else {
            lnlFilters?.visibility = View.GONE
        }
    }

    private fun setupFilter() {
        lnlFiltro.setSafeOnClickListener {

            context?.let {
                filtros?.let { filters ->
                    FilterPickerDialog.Builder(it)
                        .setCategories(filters)
                        .setOnFilterPickerListener(object : FilterPickerDialog.FilterPickerListener {
                            override fun onApply(dialog: Dialog, filters: ArrayList<CategoryFilterModel>) {
                                filtros = filters
                                setFiltersCount(filters)
                                filterOffers(filtros)
                                dialog.dismiss()
                            }

                            override fun onClose(dialog: Dialog) {
                                dialog.dismiss()
                            }

                            override fun onButtonClicked(buttonName: String?) {
                                // Empty
                            }

                            override fun onItemRemoved(filterName: String?) {
                                // Empty
                            }

                            override fun onItemSelected(filterName: String?, filterGroup: String?) {
                                // Empty
                            }

                        })
                        .show()
                }
            }
        }
    }

    private fun setFiltersCount(filters: ArrayList<CategoryFilterModel>) {
        var cantidad = 0
        filters.forEach { category ->
            category.list.forEach { filter ->
                if (filter.checked)
                    cantidad++
            }
        }

        if (cantidad != 0) txtFiltrar.text = "$FILTER_TITLE ($cantidad)" else txtFiltrar.text = FILTER_TITLE
    }

    override fun filterOffers(filters: ArrayList<CategoryFilterModel>?) {

        filterByStrategy(filters)

    }

    override fun setFilterOrderLabel(isVisible: Boolean, layoutView: Int) {
        when (layoutView) {
            presenter.LAYOUT_ORDER -> {
                var pxLeft = 0f
                if (isVisible) {
                    lltOrderContainer.visibility = View.VISIBLE
                    pxLeft = 23f
                } else {
                    lltOrderContainer.visibility = View.GONE
                }
                val param = txtOfferSize.layoutParams as LinearLayout.LayoutParams
                val dpTop: Int = (3 * (context as Activity).displayMetrics.density).roundToInt()
                val dpLeft: Int = (pxLeft * (context as Activity).displayMetrics.density).roundToInt()
                param.setMargins(dpLeft, dpTop, 0, 0)
                txtOfferSize.layoutParams = param
            }
            presenter.LAYOUT_FILTER -> {
                if (isVisible) {
                    lnlFiltro.visibility = View.VISIBLE
                } else {
                    lnlFiltro.visibility = View.GONE
                }
            }
        }
    }



    private val listItemListener = object : ListDialog.ListItemDialogListener {
        override fun clickItem(position: Int) {

            val keyval: String? = arrayDialogOrder?.get(position)?.key
            val value = keyval?.split("-")
            order = value?.get(0) ?: DEFAULT_ORDER
            orderType = value?.get(1) ?: DEFAULT_ORDER_TYPE

            sortedByStrategy(arrayDialogOrder?.get(position)?.name)
        }
    }


    private fun transformOrdersList(input: List<Ordenamiento?>): ArrayList<ListDialogModel> {
        val output = arrayListOf<ListDialogModel>()

        input.filterNotNull().forEach {
            output.add(ListDialogModel(it.ordenValor,
                it.ordenDescripcion,
                false,
                true))
        }
        return output
    }

    private fun sortedByStrategy(sortSelection: String?) {
        try {
                if (offers.isNotEmpty()) {
                    val sortedOffers = if (filterOffers.size != 0) {
                        presenter.setOrderOffer(filterOffers, order, orderType) as MutableList<Oferta>
                    } else {
                        presenter.setOrderOffer(offers, order, orderType) as MutableList<Oferta>
                    }
                    setOfertas(transformListOferta("", sortedOffers))
                    abLayout.setExpanded(true)
                }

        } catch (e: Exception) {
            BelcorpLogger.w(GlobalConstant.GANA_MAS_SET_OFFERS, e.message)
        }
    }

    private fun filterByStrategy(filters: ArrayList<CategoryFilterModel>?){

        filterOffers.clear()

        var filterMarcas: MutableList<Oferta> = mutableListOf()
        var filterPrecios: MutableList<Oferta> = mutableListOf()

        try {


                if (offers.isNotEmpty()) {
                    filters?.let { parent ->
                        if(parent.size == 1){
                            if(parent[0].name!=CategoryFilter.MARCAS)
                                filterMarcas.addAll(offers)
                            if(parent[0].name!=CategoryFilter.PRECIOS)
                                filterPrecios.addAll(offers)
                        }
                    }

                    filters?.forEach { parent ->
                        var setFilterSelected = mutableSetOf<Boolean>()
                        parent.list.forEach {
                            setFilterSelected.add(it.checked)
                        }
                        when (parent.name) {
                            CategoryFilter.MARCAS -> if (setFilterSelected.size == 1) filterMarcas.addAll(offers)
                            CategoryFilter.PRECIOS -> if (setFilterSelected.size == 1) filterPrecios.addAll(offers)
                        }


                        setFilterSelected.clear()
                    }

                    filters?.forEach { parent ->
                        when (parent.name) {
                            CategoryFilter.MARCAS -> if (filterMarcas.size == 0) filterMarcas = offers.filter { o -> (parent.list as List<FilterModel>).filter { it.checked }.any { it.name == o.nombreMarca } }.toMutableList()
                            CategoryFilter.PRECIOS -> if (filterPrecios.size == 0) {

                                filterPrecios = offers.filter { o ->
                                    (parent.list as List<FilterModel>).filter { it.checked }.any {
                                        if (o.precioCatalogo != null) {
                                            if (it.maxValue != 0f)
                                                o.precioCatalogo!! >= it.minValue && o.precioCatalogo!! < it.maxValue
                                            else
                                                o.precioCatalogo!! >= it.minValue
                                        } else {
                                            false
                                        }
                                    }
                                }.toMutableList()
                            }
                        }
                    }

                    filterOffers = filterMarcas?.filter { o -> filterPrecios?.any { it.cuv == o.cuv }!! }!!.toMutableList()
                    setCantidadOferta(filterOffers.size)

                    if (filterOffers.size != 0) {

                        showErrorMessage(false)

                        val sortedOffers = presenter.setOrderOffer(filterOffers, order, orderType) as MutableList<Oferta>
                        setOfertas( transformListOferta("",sortedOffers) )


                        abLayout.setExpanded(true)
                    } else {

                        showErrorMessage(true)

                    }
                } else {
                    showErrorScreenMessage(SubCampaingErrorFragment.ERROR_MESSAGE_EMPTY_FEST)
                }
        } catch (e: Exception) {
            BelcorpLogger.w(GlobalConstant.GANA_MAS_SET_OFFERS, e.message)
        }

    }

    fun setOfertas(ofertas: List<OfferModel>, isOriginODD: Boolean = false) {
        this.isFromODD = isOriginODD
        carouselOffers.visibility = View.VISIBLE
        updateData(ofertas, this.isFromODD)
    }

    fun showErrorMessage(visible:Boolean){

        if(visible){
            carouselOffers.visibility = View.GONE
            errorContainer.visibility=View.VISIBLE
        }else{

            errorContainer.visibility=View.GONE
            carouselOffers.visibility = View.VISIBLE

        }

    }

    private fun setupErrorOffer() {

        val args = Bundle()
        args.putInt(SubCampaingErrorFragment.FRAGMENT_ERROR_TYPE_KEY, SubCampaingErrorFragment.ERROR_MESSAGE_EMPTY_FILTER)

        errorFragment = SubCampaingErrorFragment.newInstance()
        errorFragment.arguments = args

        childFragmentManager.beginTransaction()
            .add(R.id.errorContainer, errorFragment, SubCampaingErrorFragment.TAG)
            .commit()

    }

    fun refreshSchedule() {
        presenter.refreshSchedule()
    }

    override fun goToOffers() {
        listener?.goToOffers()
    }

    private fun updateData(ofertas: List<OfferModel>, isFromODD: Boolean) {
        this.ofertas.clear()
        this.ofertas.addAll(ofertas)

        carouselAdapter.isODD = isFromODD
        carouselAdapter.updateData(ofertas)
        carouselLayManager.scrollToPositionWithOffset(0, 0)
    }

    private fun setCantidadOferta(cantidad: Int) {
        txtOfferSize.text = if (cantidad > 1 || cantidad == 0) "$cantidad ${resources.getString(R.string.ganamas_plural_offer)}" else "$cantidad ${resources.getString(R.string.ganamas_singular_offer)}"
        txtOfferSize.visibility = View.VISIBLE
    }

    private fun transformListOferta(type: String, offers: List<Oferta?>): ArrayList<OfferModel> {
        val list = arrayListOf<OfferModel>()

        for (offer in offers) {

            var showAmount = true
            if (offer?.tipoOferta == OfferTypes.HV) {
                showAmount = false
            }

            safeLet(offer?.cuv, offer?.nombreMarca
                ?: "", offer?.nombreOferta, offer?.precioValorizado,
                offer?.precioCatalogo, offer?.imagenURL, offer?.configuracionOferta?.imgFondoApp
                ?: "", offer?.configuracionOferta?.colorTextoApp ?: "", offer?.marcaID ?: 0) {
                cuv, nombreMarca, nombreOferta, precioValorizado, precioCatalogo, imagenURL,
                imagenFondo, colorTexto, marcaID ->

                list.add(Offer.transform(cuv, nombreMarca, nombreOferta,
                    formatWithMoneySymbol(precioCatalogo.toString()),
                    formatWithMoneySymbol(precioValorizado.toString()),
                    imagenURL, type, offer?.flagEligeOpcion ?: false, marcaID,
                    imageHelper.getResolutionURL(imagenFondo), colorTexto, showAmount, null, null, offer?.agotado, flagFestival = offer?.flagFestival))
            }
        }

        return list
    }

    private fun formatWithMoneySymbol(precio: String): String {
        return "$moneySymbol ${decimalFormat.format(precio.toBigDecimal())}"
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Listener) this.listener = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_fest_subcampaing, container, false)

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun context(): Context = context?.let { it }
        ?: throw NullPointerException("No hay contexto")

    override fun onInjectView(): Boolean {

        getComponent(SubCampaingComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {

        super.onViewInjected(savedInstanceState)
        this.presenter.attachView(this)
        init()

    }

    override fun showLoading() {
        listener?.showLoading()
    }

    override fun hideLoading() {
        listener?.hideLoading()
    }

    override fun showErrorScreenMessage(type: Int) {
        listener?.showErrorScreen(type)
    }

    override fun setUser(user: User?) {
        user?.let { us ->

            this.user = us
            this.decimalFormat = CountryUtil.getDecimalFormatByISO(us.countryISO, true)
            this.moneySymbol = us.countryMoneySymbol?.let { it } ?: ""

            presenter.getOrders()
            presenter.getFilters()
            presenter.getConfiguracion(NetworkUtil.isThereInternetConnection(context))

            setupFilter()
        }
    }


    override fun setConfig(config: List<ConfiguracionPorPalanca?>, flagATP: Boolean?) {
        val time = measureTimeMillis {
            configuracionListOriginal.addAll(config.filterNotNull())
            configuracionList = filterConfigList(config)
            configuracionList?.firstOrNull { it.tipoOferta == OfferTypes.SR }?.let {
                presenter.getOffersByLever(it, OfferTypes.SR)
            }?: run {
                hideLoading()
                showErrorScreenMessage(SubCampaingErrorFragment.ERROR_MESSAGE_EMPTY_SUBCAMPAIGN)
            }
        }
        BelcorpLogger.i("Time x ofertas => $time")
    }

    private fun filterConfigList(input: List<ConfiguracionPorPalanca?>): List<ConfiguracionPorPalanca> {
        val output = mutableListOf<ConfiguracionPorPalanca>()
        input.filterNotNull().forEach {
            when (it.tipoOferta) {
                OfferTypes.SR -> if (it.tieneEvento == true && it.cantidadMostrarCarrusel ?: 0 > 0) output.add(it)
                OfferTypes.PN, OfferTypes.DP, OfferTypes.ATP -> output.add(it)
                OfferTypes.LAN -> output.add(it)
                else -> if (it.cantidadMostrarCarrusel ?: 0 > 0) output.add(it)
            }
        }
        return output
    }

    override fun setOrdenamientos(orders: List<Ordenamiento?>) {
        setFilterOrderLabel(true, presenter.LAYOUT_ORDER)
        arrayDialogOrder = transformOrdersList(orders)
        listener?.setOrder(listItemListener, arrayDialogOrder!!)
        listener?.setSelectedItemOrder(0)
    }



    override fun setFilters(filters: List<GroupFilter?>) {
        if (filters.isEmpty())
            hideFilters()
        else
            filtros = transformCategoriesFiltersList(filters)
    }

    private fun transformCategoriesFiltersList(input: List<GroupFilter?>): ArrayList<CategoryFilterModel> {
        val output = arrayListOf<CategoryFilterModel>()
        input.forEach {
            if(it?.nombre == "CATEG"){
                if(sectionSelected==-1){
                    output.add(CategoryFilterModel(
                        key = it?.nombre!!,
                        name = it?.nombre!!,
                        excluyente = it?.excluyente!!,
                        list = transformFilterList(it.filtros)
                    ))
                }
            }else{
                output.add(CategoryFilterModel(
                    key = it?.nombre!!,
                    name = it?.nombre!!,
                    excluyente = it?.excluyente!!,
                    list = transformFilterList(it.filtros)
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

    override fun hideFilters() {
        lnlFiltro.visibility = View.GONE
    }


    override fun onProductAdded(productCUV: ProductCUV, message: String?) {

        productCUV?.cuv?.let {

            updateCart(productCUV.cantidad ?: 0)

            val messagex =   message ?: getString(R.string.msj_offer_added)
            val image = ImageUtils.verifiedImageUrl(productCUV)
            context?.let {
                val url = if (imageDialog) image else null  // Verifica que el flag este activo para mostrar la imagen
                val colorText = ContextCompat.getColor(it, R.color.leaf_green)
                showBottomDialog(it, messagex, url, colorText)
            }
        }
    }


    private fun showItemSelected(keyItem: String, marcaID: Int, marca: String, position: Int) {

        activity?.let { mContext ->

            val session = SessionManager.getInstance(context())
            if (!NetworkUtil.isThereInternetConnection(context()) && session.getApiCacheEnabled() == false) {
                showNetworkError()
            } else {
                val extras = Bundle()
                extras.putString(BaseFichaActivity.EXTRA_KEY_ITEM, keyItem)
                extras.putInt(BaseFichaActivity.EXTRA_MARCA_ID, marcaID)
                extras.putString(BaseFichaActivity.EXTRA_MARCA_NAME, marca)
                extras.putString(BaseFichaActivity.EXTRA_ORIGEN_PEDIDO_WEB_FROM, OffersOriginType.ORIGEN_SUBCAMPANIA_FICHA)

                ofertaMap[keyItem]?.let {
                    extras.putString(BaseFichaActivity.EXTRA_TYPE_OFFER, it.tipoOferta)
                }

                extras.putInt(BaseFichaActivity.EXTRA_ACCESS_FROM, BaseFichaActivity.ACCESS_FROM_GANAMAS)

                val intentToLaunch = FichaProductoActivity.getCallingIntent(mContext, extras)
                mContext.startActivity(intentToLaunch)

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

    override fun onAddProductError(text: String?, exception: Throwable?) {
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
    }

    override fun pressedItem(keyItem: String, marcaID: Int, marca: String, position: Int) {
        showItemSelected(keyItem, marcaID, marca, position)
    }

    override fun pressedItemButtonAdd(keyItem: String, quantity: Int, counterView: Counter) {


        if (quantity == 0) {
            context?.let {
                BottomDialog.Builder(it)
                    .setTitle(R.string.error_title)
                    .setContent(getString(R.string.error_zero_quantity))
                    .setNeutralText(getString(R.string.msj_entendido))
                    .onNeutral(object : BottomDialog.ButtonCallback {
                        override fun onClick(dialog: BottomDialog) {
                            dialog.dismiss()
                        }
                    })
                    .setNeutralBackgroundColor(R.color.magenta)
                    .show()
            }
        } else {
            if (NetworkUtil.isThereInternetConnection(context())) {

                ofertaMap[keyItem]?.let {
                    //listener?.addFromLandingPalanca(it, quantity, counterView)

                    addToCart(it, quantity, counterView)
                }

            } else {
                showNetworkError()
            }
        }


    }

    fun addToCart(oferta: Oferta, quantity: Int, counterView: Counter) {
        val iden = DeviceUtil.getId(activity)
        val productCUV = getAddRequest(oferta, "-", quantity, iden)
        productCUV?.let {
            presenter.agregarSubCampania(productCUV, iden)
        }

    }

    private fun getAddRequest(oferta: Any, origenPedidoWebCarrusel: String?, quantity: Int, id: String) : ProductCUV? {
        if (oferta is Oferta)
            return ProductCUV().apply {
                cuv = oferta.cuv
                description = oferta.nombreOferta
                descripcionMarca = oferta.nombreMarca
                marcaId = oferta.marcaID
                precioCatalogo = oferta.precioCatalogo
                precioValorizado = oferta.precioValorizado
                fotoProducto = oferta.imagenURL
                origenPedidoWeb = origenPedidoWebCarrusel
                tipoEstrategiaId = oferta.tipoEstrategiaID
                codigoEstrategia = oferta.codigoEstrategia?.toInt()
                estrategiaId = oferta.estrategiaID
                indicadorMontoMinimo = oferta.indicadorMontoMinimo
                limiteVenta = oferta.limiteVenta
                cantidad = quantity
                identifier = id
                isSugerido = false
                clienteId = 0
            }
        else if (oferta is ProductCUV)
            return ProductCUV().apply {
                cuv = oferta.cuv
                description = oferta.description
                descripcionMarca = oferta.descripcionMarca
                marcaId = oferta.marcaId
                precioCatalogo = oferta.precioCatalogo
                precioValorizado = oferta.precioValorizado
                fotoProducto = oferta.fotoProductoSmall
                tipoEstrategiaId = oferta.tipoEstrategiaId
                codigoEstrategia = oferta.codigoEstrategia
                estrategiaId = oferta.estrategiaId
                indicadorMontoMinimo = oferta.indicadorMontoMinimo
                limiteVenta = oferta.limiteVenta
                cantidad = quantity
                identifier = id
                isSugerido = false
                clienteId = 0
            }
        return null
    }

    override fun pressedItemButtonSelection(keyItem: String, marcaID: Int, marca: String, position: Int) {
        showItemSelected(keyItem, marcaID, marca, position)
    }

    fun refreshTodo() {
        /* Empty */
    }

    override fun onPrintProduct(item: OfferModel, position: Int) {
        /* Empty */
    }

    override fun onPrintClickProduct(item: OfferModel, position: Int) {
        /* Empty */
    }

    override fun onPrintAddProduct(item: OfferModel, quantity: Int) {
        /* Empty */
    }

    interface Listener {

        fun setScreenTitle(title: String)
        fun showLoading()
        fun hideLoading()
        fun showErrorScreen(type: Int)
        fun updateOffersCount(count: Int)
        fun goToOffers()
        fun isToolbarText(s: String)
        fun showOrder()
        fun dismissOrder()
        fun setOrder(itemListener: ListDialog.ListItemDialogListener, arrayDialog:ArrayList<ListDialogModel>)
        fun setSelectedItemOrder(index:Int)
        fun updateSubcampaignTitle(title: String)

    }
    companion object {
        const val DEFAULT_ORDER = "ORDEN"
        const val DEFAULT_ORDER_TYPE = "ASC"
        val TAG: String = FestFragment::class.java.simpleName
    }
}

