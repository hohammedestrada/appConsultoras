package biz.belcorp.consultoras.feature.home.ganamas

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.support.annotation.NonNull
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.SafeLet
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.model.ganamas.ConfigFlagAbTesting
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.data.net.RestApi
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.util.OfferTypes
import biz.belcorp.consultoras.domain.util.OfferTypes.getOfferTypeForAnalytics
import biz.belcorp.consultoras.feature.ficha.common.BaseFichaActivity
import biz.belcorp.consultoras.feature.home.BaseHomeFragment
import biz.belcorp.consultoras.feature.home.HomeActivity
import biz.belcorp.consultoras.feature.home.HomeActivity.Companion.KIT_NUEVA_CUV
import biz.belcorp.consultoras.feature.home.di.HomeComponent
import biz.belcorp.consultoras.feature.home.ganamas.GanaMasPagerAdapter.Companion.LANDING_FRAGMENT
import biz.belcorp.consultoras.feature.home.ganamas.GanaMasPagerAdapter.Companion.OFFERS_FRAGMENT
import biz.belcorp.consultoras.feature.home.ganamas.armatupack.ArmaTuPackFragment
import biz.belcorp.consultoras.feature.home.ganamas.fragments.GanaMasErrorFragment
import biz.belcorp.consultoras.feature.home.ganamas.fragments.GanaMasLandingFragment
import biz.belcorp.consultoras.feature.home.ganamas.fragments.GanaMasOffersFragment
import biz.belcorp.consultoras.feature.home.subcampaing.FestSubCampaingActivity
import biz.belcorp.consultoras.feature.home.subcampaing.FestSubCampaingActivity.Companion.TITLE_KEY
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.ImageUtils
import biz.belcorp.consultoras.util.StringUtil
import biz.belcorp.consultoras.util.analytics.Ofertas
import biz.belcorp.consultoras.util.anotation.*
import biz.belcorp.consultoras.util.anotation.OfferTypeCode.KIT_NUEVA_CODE
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
import biz.belcorp.mobile.components.design.sections.Sections
import biz.belcorp.mobile.components.design.sections.model.SectionModel
import biz.belcorp.mobile.components.dialogs.bottom.BottomDialog
import biz.belcorp.mobile.components.dialogs.list.ListDialog
import biz.belcorp.mobile.components.dialogs.list.model.ListDialogModel
import biz.belcorp.mobile.components.offers.Bonificacion
import biz.belcorp.mobile.components.offers.Offer
import biz.belcorp.mobile.components.offers.model.OfferModel
import biz.belcorp.mobile.components.offers.sello.Sello
import biz.belcorp.mobile.components.offers.subcomponents.OfferBanner
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_ganamas.*
import kotlinx.android.synthetic.main.view_offers_filters.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.displayMetrics
import java.text.DecimalFormat
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.system.measureTimeMillis


class GanaMasFragment : BaseHomeFragment(), GanaMasView, Sections.SectionsListener,
    GanaMasOffersFragment.Listener, GanaMasLandingFragment.Listener, SafeLet,
    GanaMasErrorFragment.Listener, OfferBanner.OnBannerListener {

    @Inject
    lateinit var presenter: GanaMasPresenter

    private var listener: Listener? = null
    private var trackedCategories: MutableMap<Int, SectionModel> = mutableMapOf()
    private var viewPagerAdapter: GanaMasPagerAdapter? = null
    private var categorias: ArrayList<SectionModel>? = null
    private var filtros: ArrayList<CategoryFilterModel>? = null
    private var user: User? = null
    private var decimalFormat = DecimalFormat()
    private var moneySymbol: String = ""
    private var configuracionList: List<ConfiguracionPorPalanca>? = null
    private var configuracionListOriginal = mutableListOf<ConfiguracionPorPalanca>()
    private lateinit var imageHelper: ImagesHelper
    private var sectionSelected: Int = -1
    private var fromSearchProduct = FromActionSearchProduct.CATEGORY

    /**
     * 1 = Contenedor, 2 = Landing Palancas, 3 = Landing Categorías
     */
    private var addOrigin: Int? = null

    private var offers = mutableListOf<Oferta>() // Lista original
    private var filterOffers = mutableListOf<Oferta>() // Lista filtrada

    var networkMessageVisible: Boolean = false

    private var order: String = DEFAULT_ORDER
    private var orderType: String = DEFAULT_ORDER_TYPE
    private var currentFragment: Int = -1

    private var categoryName: String = ""
    private var categoryCode: String = ""
    private var strategyName: String = ""

    var tipoPalanca: String = ""

    private var arrayDialogOrder: ArrayList<ListDialogModel>? = null
    private var configuracionFestival: FestivalConfiguracion? = null
    private var configuracionSubcampania: SubCampaniaConfiguracion? = null
    private var stampList: MutableList<Sello> = mutableListOf()

    var leverConfig: ConfiguracionPorPalanca? = null
    var leverType: String? = null
    var imageDialog: Boolean = false

    var bannerVisible = false

    var flagHideViewsForTesting: Boolean = false
    var flagExpandedSearchviewForTesting: Boolean = false

    var flagConfigurableLever: String? = null
    var flagFlagABTestingBonificaciones: String? = null

    var flagFilterApply: Boolean = false
    private var flagCountryGanaMas = false

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun onInjectView(): Boolean {
        getComponent(HomeComponent::class.java).inject(this)
        return true
    }

    // Overrides Fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ganamas, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    // Override BaseFragment
    override fun context(): Context? {
        val activity = activity
        return activity?.applicationContext
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

    override fun initScreenTrack(model: LoginModel) {
        Tracker.trackView(GlobalConstant.SCREEN_GANA_MAS, model)
    }

    override fun trackBackPressed(model: LoginModel) {
        Tracker.trackBackEvent(GlobalConstant.SCREEN_GANA_MAS, model)
    }

    fun init() {
        presenter.getFlagHideViewForTesting()
        presenter.getFlagExpandedSearchviewForTesting()
        presenter.getFlagOrderConfigurableLever()
        presenter.getABTestingBonificaciones()
        presenter.getImageEnabled()
        activity?.let {
            imageHelper = ImagesHelper(it)
            (it as HomeActivity).lockBackButtons()
        }

        lltOrderContainer.setOnClickListener {
            listener?.showOrder()
        }
    }

    override fun setUser(user: User?) {
        // Not necessary to set user
        user?.let { us ->

            Tracker.trackView(GlobalConstant.SCREEN_GANA_MAS, us)

            this.user = us
            this.decimalFormat = CountryUtil.getDecimalFormatByISO(us.countryISO, true)
            this.moneySymbol = us.countryMoneySymbol?.let { it } ?: ""

            setupViewPager()

            // LLAMARLO EN PARALELO
            user?.isMostrarBuscador?.let { if(it) presenter.getCategories() }
            presenter.getConfiguracion(NetworkUtil.isThereInternetConnection(context))
            presenter.getOrders()
            presenter.getFestivalConfiguracion()

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

    fun checkFilter(): Boolean {

        filtros?.forEach { it1 ->
            it1.list.forEach {
                if (it.checked)
                    return false
            }
        }
        return true
    }

    private fun setupFilter() {
        lnlFiltro.setSafeOnClickListener {
            context?.let {
                filtros?.let { filters ->
                    FilterPickerDialog.Builder(it)
                        .setCategories(filters)
                        .setOnFilterPickerListener(object : FilterPickerDialog.FilterPickerListener {
                            override fun onApply(dialog: Dialog, filters: ArrayList<CategoryFilterModel>) {
                                flagFilterApply = true
                                filtros = filters
                                setFiltersCount(filters)
                                filterOffers(filtros)
                                dialog.dismiss()
                            }

                            override fun onClose(dialog: Dialog) {
                                dialog.dismiss()
                            }

                            override fun onButtonClicked(buttonName: String?) {
                                trackClickFilterButton(buttonName)
                            }

                            override fun onItemRemoved(filterName: String?) {
                                trackClickFilterRemove(filterName)
                            }

                            override fun onItemSelected(filterName: String?, filterGroup: String?) {
                                trackClickFilterItem(filterName, filterGroup)
                            }

                        })
                        .show()
                }
            }
        }
    }

    private fun getLandingOfferType(): Int {
        val landingFragment = viewPagerAdapter?.instantiateItem(vpGanaMas, GanaMasPagerAdapter.LANDING_FRAGMENT)
        return if (landingFragment is GanaMasLandingFragment) landingFragment.getOfferType()
        else -1
    }

    private fun trackClickFilterButton(buttonName: String?) {

        val currentLocation = calcularOfferOriginTypeLocation(OffersOriginType.ORIGEN_LANDING)

        when (getLandingOfferType()) {

            FromActionOfferType.CATEGORY -> {

                val currentSection = calcularOffersOriginTypeSection(categoryCode)

                val onAnalyticsLoaded: (analytics: Analytics) -> Unit = { analytics ->
                    Ofertas.clickFilterButton(user, categoryName, buttonName, analytics)
                }

                presenter.getAnalytics(currentLocation, currentSection, null, null, null, null, onAnalyticsLoaded)

            }

            FromActionOfferType.SHOW_MORE -> {

                val currentSection = calcularOffersOriginTypeSection(leverType)

                val onAnalyticsLoaded: (analytics: Analytics) -> Unit = { analytics ->
                    Ofertas.clickFilterButton(user, strategyName, buttonName, analytics)
                }

                presenter.getAnalytics(currentLocation, currentSection, null, null, null, null, onAnalyticsLoaded)

            }

        }
    }

    private fun trackClickFilterItem(filterName: String?, filterGroup: String?) {

        val currentLocation = calcularOfferOriginTypeLocation(OffersOriginType.ORIGEN_LANDING)

        when (getLandingOfferType()) {

            FromActionOfferType.CATEGORY -> {

                val currentSection = calcularOffersOriginTypeSection(categoryCode)

                val onAnalyticsLoaded: (analytics: Analytics) -> Unit = { analytics ->
                    Ofertas.clickFilterItem(filterGroup, user, filterName, analytics, categoryName)
                }

                presenter.getAnalytics(currentLocation, currentSection, null, null, null, null, onAnalyticsLoaded)

            }

            FromActionOfferType.SHOW_MORE -> {

                val currentSection = calcularOffersOriginTypeSection(leverType)

                val onAnalyticsLoaded: (analytics: Analytics) -> Unit = { analytics ->
                    Ofertas.clickFilterItem(filterGroup, user, filterName, analytics, strategyName)
                }

                presenter.getAnalytics(currentLocation, currentSection, null, null, null, null, onAnalyticsLoaded)

            }

        }
    }

    private fun trackClickFilterRemove(filterName: String?) {
        when (getLandingOfferType()) {
            FromActionOfferType.CATEGORY -> {
                Ofertas.removeFilterItem(user, categoryName, filterName)
            }
            FromActionOfferType.SHOW_MORE -> {
                Ofertas.removeFilterItem(user, strategyName, filterName)
            }
        }
    }

    fun setupViewPager() {
        viewPagerAdapter = GanaMasPagerAdapter(childFragmentManager, this, this, this, flagHideViewsForTesting)
        vpGanaMas.setPagingEnabled(false)
        vpGanaMas.offscreenPageLimit = 2
        vpGanaMas.adapter = viewPagerAdapter

        vpGanaMas.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(p0: Int) {
                // EMPTY
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
                // EMPTY
            }

            override fun onPageSelected(p0: Int) {
                val pageOffers = childFragmentManager.findFragmentByTag("android:switcher:" + R.id.vpGanaMas + ":0") as GanaMasOffersFragment
                val pageLanding = childFragmentManager.findFragmentByTag("android:switcher:" + R.id.vpGanaMas + ":1") as GanaMasLandingFragment
                when (p0) {
                    0 -> {
                        pageLanding.stopTimer()
                        pageOffers?.startTimer()
                    }
                    1 -> {
                        pageOffers?.stopTimer()
                    }

                }
            }

        })
    }

    private fun filterByCategory(filters: ArrayList<CategoryFilterModel>?) {
        val code = categorias?.get(sectionSelected)?.code
        val name = categorias?.get(sectionSelected)?.name
        if (code != null && name != null) {

            val filtros = getFiltrosTrackFormat(filters)
            val currentLocation = calcularOfferOriginTypeLocation(OffersOriginType.ORIGEN_LANDING)
            val currentSection = calcularOffersOriginTypeSection(code)

            val onAnalyticsLoaded: (analytics: Analytics) -> Unit = { analytics ->
                Ofertas.applyFilter(user, categoryName, filtros, analytics)
            }

            presenter.getAnalytics(currentLocation, currentSection, null, null, null, null, onAnalyticsLoaded)

            presenter.getOfferByFilter(code, name, order, orderType, filters)
        }
    }

    private fun getFiltrosTrackFormat(filters: ArrayList<CategoryFilterModel>?): String {
        var format = ""

        filters?.let {

            for (filter in it) {

                val filtrosActivos = arrayListOf<FilterModel>()
                filtrosActivos.addAll(filter.list.filter { valuesList -> valuesList.checked })

                if (filtrosActivos.isNotEmpty()) {

                    if (format.isNotEmpty()) format += "| "

                    var subFormat = ""

                    for (filtroActivo in filtrosActivos) {

                        if (subFormat.isNotEmpty()) subFormat += "- "

                        subFormat += "${filtroActivo.name} "
                    }

                    format += subFormat
                }

            }

        }

        return format
    }

    private fun filterByStrategy(filters: ArrayList<CategoryFilterModel>?) {
        filterOffers.clear()

        var filterMarcas: MutableList<Oferta> = mutableListOf()
        var filterPrecios: MutableList<Oferta> = mutableListOf()

        showFragment(GanaMasPagerAdapter.LANDING_FRAGMENT)
        try {
            val landingFragment = viewPagerAdapter?.instantiateItem(vpGanaMas, GanaMasPagerAdapter.LANDING_FRAGMENT)

            if (landingFragment is GanaMasLandingFragment) {
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
                        val sortedOffers = presenter.setOrderOffer(filterOffers, order, orderType) as MutableList<Oferta>
                        landingFragment.setStampList(stampList)
                        landingFragment.setOfertaMap(sortedOffers)
                        landingFragment.setOfertas(transformListOferta("", sortedOffers))
                        abLayout.setExpanded(true)
                    } else {
                        showErrorScreenMessage(GanaMasErrorFragment.ERROR_MESSAGE_EMPTY_FILTER)
                    }
                } else {
                    showErrorScreenMessage(GanaMasErrorFragment.ERROR_MESSAGE_EMPTY_CONTAINER)
                }
            }
            val filtros = getFiltrosTrackFormat(filters)
            val currentLocation = calcularOfferOriginTypeLocation(OffersOriginType.ORIGEN_LANDING)
            val currentSection = calcularOffersOriginTypeSection(leverType)

            val onAnalyticsLoaded: (analytics: Analytics) -> Unit = { analytics ->
                Ofertas.applyFilter(user, strategyName, filtros, analytics)
            }

            presenter.getAnalytics(currentLocation, currentSection, null, null, null, null, onAnalyticsLoaded)

        } catch (e: Exception) {
            BelcorpLogger.w(GlobalConstant.GANA_MAS_SET_OFFERS, e.message)
        }

    }

    private fun sortedByStrategy(sortSelection: String?) {
        try {
            val landingFragment = viewPagerAdapter?.instantiateItem(vpGanaMas, GanaMasPagerAdapter.LANDING_FRAGMENT)

            if (landingFragment is GanaMasLandingFragment) {
                if (offers.isNotEmpty()) {
                    val sortedOffers = if (filterOffers.size != 0) {
                        presenter.setOrderOffer(filterOffers, order, orderType) as MutableList<Oferta>
                    } else {
                        presenter.setOrderOffer(offers, order, orderType) as MutableList<Oferta>
                    }
                    landingFragment.setStampList(stampList)
                    landingFragment.setOfertaMap(sortedOffers)
                    landingFragment.setOfertas(transformListOferta("", sortedOffers))

                    val currentLocation = calcularOfferOriginTypeLocation(OffersOriginType.ORIGEN_LANDING)
                    val currentSection = calcularOffersOriginTypeSection(leverType)

                    val onAnalyticsLoaded: (analytics: Analytics) -> Unit = { analytics ->
                        Ofertas.applySort(user, strategyName, sortSelection, analytics)
                    }

                    presenter.getAnalytics(currentLocation, currentSection, null, null, null, null, onAnalyticsLoaded)

                    abLayout.setExpanded(true)
                }
            } else {
                showErrorScreenMessage(GanaMasErrorFragment.ERROR_MESSAGE_EMPTY_CONTAINER)
            }
        } catch (e: Exception) {
            BelcorpLogger.w(GlobalConstant.GANA_MAS_SET_OFFERS, e.message)
        }
    }

    override fun filterOffers(filters: ArrayList<CategoryFilterModel>?) {
        fromSearchProduct = FromActionSearchProduct.FILTER
        val landingFragment = viewPagerAdapter?.instantiateItem(vpGanaMas, GanaMasPagerAdapter.LANDING_FRAGMENT)
        if (landingFragment is GanaMasLandingFragment) {
            when (landingFragment.getOfferType()) {
                FromActionOfferType.CATEGORY -> {
                    filterByCategory(filters)
                }
                FromActionOfferType.SHOW_MORE -> {
                    filterByStrategy(filters)
                    filters?.let {
                        setBannerVisiblePalanca(checkFilter())
                    }
                }
            }
        }
    }

    override fun setCategories(categories: List<Categoria?>, isCountryGanaMas: Boolean) {
        this.flagCountryGanaMas = isCountryGanaMas
        showCategories(false)
        categorias = transformCategoriesList(categories.sortedWith(compareBy { it?.ordenApp }))
        categorias?.let {
            it.add(SectionModel(KEY_SABER_MAS, CODE_SABER_MAS, TITLE_SABER_MAS))
            laySections?.updateList(it)
            laySections?.sectionsListener = this

            //  Envia los elementos visibles en categories cuando la pantalla termina de cargar
            Handler().postDelayed({
                laySections?.getVisibleCategorys()?.let { showed ->
                    if (showed.isNotEmpty()) {
                        trackedCategories = showed
                        Ofertas.categories(user, showed, true)
                    }
                }
            }, 1000)

        }
    }

    fun clearSaberMas(){
        resetScrollSection()
    }

    override fun setFilters(filters: List<GroupFilter?>) {
        if (filters.isEmpty())
            hideFilters()
        else
            filtros = transformCategoriesFiltersList(filters)
    }


    override fun setOrdenamientos(orders: List<Ordenamiento?>) {
        setFilterOrderLabel(true, presenter.LAYOUT_ORDER)
        arrayDialogOrder = transformOrdersList(orders)
        listener?.setOrder(listItemListener, arrayDialogOrder!!)
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

    override fun openLandingVerMas(typeLever: String, fromBanner: Boolean) {

        setSelectedItemOrder(0)
        clearFilters()
        filterOffers.clear()

        configuracionList?.firstOrNull { it.tipoOferta == typeLever }?.let {

            val currentLocation = null
            val currentSection = null
            val sectionDesc = getOfferTypeForAnalytics(it.tipoOferta)
            val subsection = calcularCarouselTypeSubSection(GlobalConstant.SUBSECCION_NULL)
            val originLocation = calcularOfferOriginTypeLocation(OffersOriginType.ORIGEN_CONTENEDOR)
            val originSection = calcularOffersOriginTypeSection(it.tipoOferta)

            val onAnalyticsLoaded: (analytics: Analytics) -> Unit = { analytics ->
                if (!fromBanner) Ofertas.clickVerMas(analytics)
            }

            presenter.getAnalytics(currentLocation, currentSection, sectionDesc, subsection,
                originLocation, originSection, onAnalyticsLoaded)

            leverConfig = it
            leverType = if (typeLever == OfferTypes.RD) OfferTypes.OPT else typeLever

            presenter.getOffersByLever(it, typeLever)
            listener?.isToolbarText("" + it.titulo, 1)
        }

        offerbanner.bannerListener = object : OfferBanner.OnBannerListener {

            override fun onButtonClick(view: View) {

                val intent = Intent(context, FestSubCampaingActivity::class.java)
                intent.action = System.currentTimeMillis().toString()
                intent.putExtra(TITLE_KEY, offerbanner.getTitle())
                context?.startActivity(intent)

            }
        }
    }


    private fun transformCategoriesList(input: List<Categoria?>): ArrayList<SectionModel> {
        val output = arrayListOf<SectionModel>()
        input.filterNotNull().forEach {
            output.add(SectionModel(it.codigo ?: "0",
                it.descripcion ?: getString(R.string.categories_default_code),
                it.nombre ?: getString(R.string.categories_default_name),
                getCategoryColor(it.colorTextoApp), getCategoryColor(it.colorFondoApp)))
        }
        return output
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

    private fun transformCategoriesFiltersList(input: List<GroupFilter?>): ArrayList<CategoryFilterModel> {
        val output = arrayListOf<CategoryFilterModel>()
        input.forEach {
            if (it?.nombre == CATEGORIES_TITLE) {
                if (sectionSelected == -1) {
                    output.add(CategoryFilterModel(
                        key = it?.nombre!!,
                        name = it?.nombre!!,
                        excluyente = it?.excluyente!!,
                        list = transformFilterList(it.filtros)
                    ))
                }
            } else {
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

    private fun getCategoryColor(colorText: String?): Int? {
        return if (!colorText.isNullOrEmpty())
            Color.parseColor(colorText)
        else
            null
    }


    override fun sectionPressed(sectionModel: SectionModel, position: Int) {
        val list = mutableMapOf<Int, SectionModel>()
        list[position] = sectionModel

        Ofertas.categories(user, list, false)

        categorias?.let {
            if(position == it.size-1){
                listener?.goToEmbebedSuscription(PageUrlType.REVISTA_DIGITAL_INFO)
            } else {
                setSelectedItemOrder(0)
                sectionSelected = position
                clearFilters()
                fromSearchProduct = FromActionSearchProduct.CATEGORY
                presenter.addFilterCat = position != 0
                presenter.getOfferByCategory(sectionModel.code, sectionModel.name, DEFAULT_ORDER, DEFAULT_ORDER_TYPE)

                categoryCode = sectionModel.code
                categoryName = sectionModel.name
            }
        }


        offerbanner.bannerListener = object : OfferBanner.OnBannerListener {
            override fun onButtonClick(view: View) {
                val extras = Bundle()
                listener?.goToFest(extras)
            }
        }
    }

    override fun deselect() {
        sectionSelected = -1
        when {
            configuracionList == null -> showErrorScreenMessage(GanaMasErrorFragment.ERROR_MESSAGE_NETWORK)
            configuracionList?.isEmpty() == true -> showErrorScreenMessage(GanaMasErrorFragment.ERROR_MESSAGE_EMPTY_CONTAINER)
            else -> {
                setBannerVisibility(false)
                showFragment(GanaMasPagerAdapter.OFFERS_FRAGMENT)
            }
        }
    }

    override fun sectionScrolled(items: MutableMap<Int, SectionModel>) {
        val list = mutableMapOf<Int, SectionModel>()
        for (key in items.keys) {
            items[key]?.let {
                if (!trackedCategories.containsKey(key)) {
                    list[key] = it
                }
            }
        }
        if (list.isNotEmpty()) Ofertas.categories(user, list, true)

        trackedCategories = items
    }

    override fun setBannerSubcampaniaPalanca(type: String) {

        setDefaultBanner(false)
        bannerVisible = false


        setBannerVisiblePalanca(false)
        configuracionSubcampania?.let {
            it.tieneSubCampania?.let {tienesubcampania->
                if(tienesubcampania){
                    setBannerConfiguration(configuracionSubcampania)
                    tipoPalanca = type
                    setBannerVisiblePalanca(true)
                }
            }
        }

    }

    private fun setBannerVisiblePalanca(isVisible: Boolean) {

        if (tipoPalanca == OfferTypes.SR) {
            bannerVisible = true
        }

        if (isVisible && bannerVisible) {
            setBannerVisibility(true)
        } else {
            setBannerVisibility(false)
        }

    }

    override fun onOffersByLeverResponse(offers: List<Oferta?>, typeLever: String) {
        offers[0]?.let { oferta ->
            val orderedOffers = mutableListOf<Oferta>()

            if (oferta.tipoOferta == OfferTypes.SR) {
                orderedOffers.addAll(offers.asSequence().filterNotNull().filter { it.esSubCampania == false }.toList())
                orderedOffers.addAll(offers.asSequence().filterNotNull().filter { it.esSubCampania == true }.toList())
            } else {
                orderedOffers.addAll(offers.filterNotNull())
            }

            Tracker.trackView(GlobalConstant.SCREEN_GANA_MAS_SECTION + typeLever, user)

            showFragment(LANDING_FRAGMENT)
            showFilters(true)
            strategyName = configuracionListOriginal.firstOrNull { it.tipoOferta == typeLever }?.titulo
                ?: ""
            try {
                val landingFragment = viewPagerAdapter?.instantiateItem(vpGanaMas, LANDING_FRAGMENT)

                if (landingFragment is GanaMasLandingFragment) {

                    if (orderedOffers.isNotEmpty()) {

                        this.offers = orderedOffers
                        val time = measureTimeMillis {
                            setCantidadOferta(orderedOffers.size)
                            landingFragment.setStampList(stampList)
                            landingFragment.setOfertaMap(orderedOffers)
                            landingFragment.setOfertas(transformListOferta("", orderedOffers), typeLever == OfferTypes.ODD)
                            abLayout.setExpanded(true, false)
                            setBannerSubcampaniaPalanca(typeLever)
                        }
                        BelcorpLogger.i("Time x ofertas by accordion => $time")
                    } else {
                        showErrorScreenMessage(GanaMasErrorFragment.ERROR_MESSAGE_EMPTY_CONTAINER)
                    }
                }
            } catch (e: Exception) {
                BelcorpLogger.w(GlobalConstant.GANA_MAS_SET_OFFERS, e.message)
            }
        }
    }

    override fun goToLeverPack(typeLever: String) {
        if (NetworkUtil.isThereInternetConnection(context)) {
            val titleLever = configuracionList?.firstOrNull { it.tipoOferta == typeLever }?.titulo
                ?: ""
            when (typeLever) {
                OfferTypes.PN, OfferTypes.DP -> {
                    context?.let { listener?.goToLeverPack(it, typeLever, titleLever) }
                }
                OfferTypes.ATP -> listener?.goToArmaTuPack()
            }
        } else {
            showNetworkError()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {

            ArmaTuPackFragment.ATP_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.extras?.let {
                        val quantity = it.getInt(ArmaTuPackFragment.ATP_QUANTITY_KEY, 0)
                        val productCUV = it.getSerializable(ArmaTuPackFragment.ATP_PRODUCT_KEY) as ProductCUV

                        if (it.getBoolean(ArmaTuPackFragment.ATP_IS_UPDATE_KEY, false))
                            onOfferUpdated(productCUV)
                        else {

                            addOrigin = null
                            onOfferAdded(quantity, productCUV, data.getStringExtra(ATP_MESSAGE_ADDED), AddedAlertType.DEFAULT)

                        }

                        updateBannerAtp(OfferTypes.ATP, true)
                    }
                }
            }

        }
    }

    override fun showDialogDeleteArmaTuPack(typeLever: String, titleLever: String) {
        context?.let {
            BottomDialog.Builder(it)
                .setTitle(R.string.arma_tu_pack_dialog_title)
                .setContent(getString(R.string.arma_tu_pack_dialog_message))
                .setIcon(R.drawable.ic_mano_error)
                .setPositiveText(R.string.button_aceptar)
                .setPositiveBackgroundColor(R.color.magenta)
                .onPositive(object : BottomDialog.ButtonCallback {
                    override fun onClick(dialog: BottomDialog) {
                        dialog.dismiss()
                    }
                })
                .setNegativeText(R.string.button_cancelar)
                .setNegativeTextColor(R.color.black)
                .setNegativeBorderColor(R.color.black)
                .setNegativeBackgroundColor(R.color.white)
                .onNegative(object : BottomDialog.ButtonCallback {
                    override fun onClick(dialog: BottomDialog) {
                        dialog.dismiss()
                    }
                })
                .show()
        }

    }

    override fun setOffers(type: String, offers: List<Oferta?>) {
        val time = measureTimeMillis {
            try {
                val offersFragment = viewPagerAdapter?.instantiateItem(vpGanaMas, GanaMasPagerAdapter.OFFERS_FRAGMENT)

                if (offersFragment is GanaMasOffersFragment) {
                    val filterOffers = filterOfferList(type, offers)
                    if (filterOffers.isNotEmpty()) {

                        arguments?.let {
                            offersFragment.arguments = it
                        }

                        offersFragment.setOffersByCuv(filterOffers)
                        offersFragment.setOffers(type, transformListOferta(type, filterOffers), checkoutSizeListOffers(type, offers))
                    } else {
                        removeOffer(type)
                    }
                }
            } catch (e: Exception) {
                BelcorpLogger.w(GlobalConstant.GANA_MAS_SET_OFFERS, e.message)
            }
        }
        BelcorpLogger.i("Time x oferta $type => $time")
    }

    override fun setOffersBySearch(products: List<ProductCUV?>) {

        setBannerVisibility(false)
        Tracker.trackView(GlobalConstant.SCREEN_GANA_MAS_SECTION + categoryName, user)
        showFragment(GanaMasPagerAdapter.LANDING_FRAGMENT)
        showCategories(true)
        val time = measureTimeMillis {
            try {
                val landingFragment = viewPagerAdapter?.instantiateItem(vpGanaMas, GanaMasPagerAdapter.LANDING_FRAGMENT)
                if (landingFragment is GanaMasLandingFragment) {

                    bannerVisible = false

                    categoryCode.let {
                        configuracionFestival?.Categoria?.forEach { festcategoria ->
                            if (categoryCode == festcategoria?.CodigoCategoria) {
                                bannerVisible = checkFilter()
                            }
                        }
                    }

                    setDefaultBanner(true)
                    setBannerConfiguration(configuracionFestival)
                    setBannerVisibility(bannerVisible)

                    setCantidadOferta(products.size)

                    landingFragment.setStampList(stampList)
                    landingFragment.setProductCUVMap(products)
                    landingFragment.setOfertas(transformListProductCUV(products), false)
                    abLayout.setExpanded(true, false)
                }
            } catch (e: Exception) {
                BelcorpLogger.w(GlobalConstant.GANA_MAS_SET_OFFERS, e.message)
            }
        }

        BelcorpLogger.i("Time x oferta by Search => $time")

    }

    fun setBannerConfiguration(conf: SubCampaniaConfiguracion?) {

        conf?.let { config ->

            offerbanner.setTitle(config.bannerTextoTitulo)
            offerbanner.setDescription(config.bannerTextoSubtitulo)

            config.bannerColorTextoTitulo?.let {
                if (!it.isEmpty()) {
                    try {
                        offerbanner.setTitleColor(Color.parseColor(it))
                    } catch (iae: IllegalArgumentException) {
                    }
                }
            }

            config.bannerColorTextoSubTitulo?.let {
                if (!it.isEmpty()) {
                    try {
                        offerbanner.setDescriptionColor(Color.parseColor(it))
                    } catch (iae: IllegalArgumentException) {
                    }
                }
            }

            config.bannerColorFondoTitulo?.let {
                offerbanner.setBackgroundGradient(
                    config.bannerColorFondoTitulo.toString(),
                    config.bannerColorFondoTitulo.toString(),
                    DEFAULT_ORIENTATION)
            } ?: run {

            }

            config.bannerImagenFondo?.let {
                offerbanner.updateBannerImage(it)
            }

        }
    }

    fun setBannerConfiguration(festivalconfiguracion: FestivalConfiguracion?){

        festivalconfiguracion?.let{config->
            config.Banner?.let { banner->
                offerbanner.setTitle(config.Titulo)
                offerbanner.setDescription(banner.bannerDescripcion)


                banner.bannerColorTexto?.let {
                    if(!it.isEmpty() ){
                        try {
                            offerbanner.setTitleColor(Color.parseColor(it))
                            offerbanner.setDescriptionColor(Color.parseColor(it))
                        } catch (iae: IllegalArgumentException) {
                        }
                    }
                }

                banner.bannerImgProducto?.let{
                    offerbanner.updatePicImage(it)
                }


                offerbanner.setBackgroundGradient(
                    banner.bannerFondoColorInicio.toString(),
                    banner.bannerFondoColorFin.toString(),
                    banner.bannerFondoColorDireccion?.let {
                        it
                    }?: run {
                        DEFAULT_ORIENTATION
                    }
                )


                banner.bannerImgMobile?.let{
                    offerbanner.updateBannerImage(it)
                }

            }
        }
    }

    override fun setDefaultBanner(imageBackground: Boolean) {

        offerbanner.setTitleColor( Color.WHITE)
        offerbanner.setDescriptionColor(Color.WHITE)

        if(imageBackground){
            offerbanner.setImageVisible(true)
            offerbanner.updatePicImage("")
        }else{
            offerbanner.setImageVisible(false)
            offerbanner.setBackgroundGradient(Color.BLACK, Color.BLACK,DEFAULT_ORIENTATION)
            offerbanner.setPicImage(null)
        }

    }


    override fun setConfig(config: List<ConfiguracionPorPalanca?>, flagATP: Boolean?) {
        val time = measureTimeMillis {
            configuracionListOriginal.addAll(config.filterNotNull())
            configuracionList = filterConfigList(config)
            configuracionList?.let { conf ->

                if (conf.isEmpty()) {
                    hideLoading()
                    showErrorScreenMessage(GanaMasErrorFragment.ERROR_MESSAGE_EMPTY_CONTAINER)
                } else {
                        showFragment(GanaMasPagerAdapter.OFFERS_FRAGMENT)
                        val fragment = viewPagerAdapter?.instantiateItem(vpGanaMas, GanaMasPagerAdapter.OFFERS_FRAGMENT)
                        if (fragment is GanaMasOffersFragment) {
                            if(!flagConfigurableLever.isNullOrEmpty()){
                                configuracionPalancasABForTesting(conf)
                            }

                            fragment.setStampList(stampList)
                            fragment.setConfig(conf.sortedWith(compareBy { it.orden }), flagATP)
                            //aqui es donde se ordenan las palancas segun el campo "orden" de los objetos ConfiguracionPorPalanca que tenga la lista config
                            presenter.getOffers(configuracionList)
                        }

                }
            }
        }
        BelcorpLogger.i("Time x set config $time")
    }

    private fun configuracionPalancasABForTesting(listConfig: List<ConfiguracionPorPalanca>) {

        val gson = Gson()
        val itemType = object : TypeToken<List<ConfigFlagAbTesting>>() {}.type
        val itemListRemote = gson.fromJson<List<ConfigFlagAbTesting>>(flagConfigurableLever, itemType)

        for (lista in listConfig) {
            for (listaRemota in itemListRemote) {
                if (listaRemota.tipoOferta.equals(lista.tipoOferta)) {
                    lista.orden = listaRemota.orden
                }
            }
        }
    }

    override fun restartFragment() {
        context?.let { RestApi.clearCache(it) }
        EventBus.getDefault().post(RestartGanaMasEvent())
    }

    override fun removeOffer(type: String) {
        val fragment = viewPagerAdapter?.instantiateItem(vpGanaMas, GanaMasPagerAdapter.OFFERS_FRAGMENT)
        if (fragment is GanaMasOffersFragment) {
            fragment.removeOffer(type)
        }
    }

    override fun restartNow() {
        presenter.refreshData()
    }

    override fun addFromHome(oferta: Oferta, quantity: Int, counterView: Counter, originType: String) {

        val origenPedidoWeb = getOrigenPedidoWeb(oferta.tipoOferta, originType, null)
        presenter.agregar(oferta, quantity, counterView, DeviceUtil.getId(activity), origenPedidoWeb)
        addOrigin = 1

    }

    override fun addFromLandingCategoria(oferta: ProductCUV, quantity: Int, counterView: Counter) {

        oferta.cantidad = counterView.quantity
        presenter.agregar(oferta, counterView, DeviceUtil.getId(activity), OffersOriginType.ORIGEN_LANDING_CATEGORIA)
        addOrigin = 3

    }

    override fun addFromLandingPalanca(oferta: Oferta, quantity: Int, counterView: Counter) {

        val opw = getOrigenPedidoWeb(oferta.tipoOferta, OffersOriginType.ORIGEN_LANDING, null)
        presenter.agregar(oferta, quantity, counterView, DeviceUtil.getId(activity), opw)
        addOrigin = 2

    }

    override fun onOfferAdded(quantity: Int, productCUV: ProductCUV, message: String?, code: Int, isFromKitNueva: Boolean) {
        listener?.updateOffersCountFromGanaMas(quantity)
        when (addOrigin) {

            1 -> {

                val sectionDesc = getOfferTypeForAnalytics(productCUV.tipoPersonalizacion)
                val subsection = calcularCarouselTypeSubSection(GlobalConstant.SUBSECCION_NULL)

                val onAnalyticsLoaded: (analytics: Analytics) -> Unit = { analytics ->
                    Ofertas.addOfferFromHome(user, analytics, quantity, productCUV)
                }

                presenter.getAnalytics(null, null, sectionDesc, subsection, null, null, onAnalyticsLoaded)

            }

            2 -> {

                val currentLocation = calcularOfferOriginTypeLocation(OffersOriginType.ORIGEN_LANDING)
                val currentSection = calcularOffersOriginTypeSection(productCUV.tipoPersonalizacion)
                val sectionDesc = getOfferTypeForAnalytics(productCUV.tipoPersonalizacion)

                val onAnalyticsLoaded: (analytics: Analytics) -> Unit = { analytics ->
                    Ofertas.addOfferForLanding(user, productCUV, analytics, null)
                }

                presenter.getAnalytics(currentLocation, currentSection, sectionDesc, null, null, null, onAnalyticsLoaded)

            }

            3 -> {

                val currentLocation = calcularOfferOriginTypeLocation(OffersOriginType.ORIGEN_LANDING)
                val currentSection = calcularOffersOriginTypeSection(categorias?.get(sectionSelected)?.code)
                val sectionDesc = getOfferTypeForAnalytics(productCUV.tipoPersonalizacion)

                val category = categorias?.get(sectionSelected)?.name

                val onAnalyticsLoaded: (analytics: Analytics) -> Unit = { analytics ->
                    Ofertas.addOfferForLanding(user, productCUV, analytics, category)
                }

                presenter.getAnalytics(currentLocation, currentSection, sectionDesc, null, null, null, onAnalyticsLoaded)

            }

        }

        if (code == AddedAlertType.DEFAULT) {
            val messageDialog = message ?: getString(R.string.msj_offer_added_default)
            val image = ImageUtils.verifiedImageUrl(productCUV)
            context?.let {
                val colorText = ContextCompat.getColor(it, R.color.lograste_puntaje)
                val url = if (imageDialog) image else null  // Verifica que el flag este activo para mostrar la imagen
                showBottomDialog(it, messageDialog, url, colorText)
            }
        }

        if (code == AddedAlertType.FESTIVAL) {
            context?.let {
                showBottomDialogAction(it, message) {
                    val extras = Bundle()
                    goToFest(extras)
                }
            }
        }

        if (isFromKitNueva) {

            KIT_NUEVA_CUV = productCUV.cuv
            updateKitNueva(true)

        }

    }

    fun updateKitNueva(value: Boolean) {

        when (vpGanaMas.currentItem) {

            OFFERS_FRAGMENT -> {

                val fragment = viewPagerAdapter?.instantiateItem(vpGanaMas, OFFERS_FRAGMENT)
                if (fragment is GanaMasOffersFragment) {
                    fragment.updateKitNueva(value)
                }

            }

            LANDING_FRAGMENT -> {

                val fragment = viewPagerAdapter?.instantiateItem(vpGanaMas, LANDING_FRAGMENT)
                if (fragment is GanaMasLandingFragment) {
                    fragment.updateKitNueva(value)
                }

                val fragmentOffers = viewPagerAdapter?.instantiateItem(vpGanaMas, OFFERS_FRAGMENT)
                if (fragmentOffers is GanaMasOffersFragment) {
                    fragmentOffers.updateKitNueva(value)
                }

            }

        }

    }

    override fun setImageEnabled(imageDialog: Boolean) {
        this.imageDialog = imageDialog
    }

    override fun setFlatHideViewForABTesting(hideView: Boolean) {
        this.flagHideViewsForTesting = hideView
    }

    override fun setExpandedSearchviewForABTesting(expandedSearchview: Boolean) {
        listener?.getFlagExpandedSearchview(expandedSearchview)
    }

    override fun setFlagOrderConfigurableLever(jsonConfig: String) {
        this.flagConfigurableLever = jsonConfig
    }

    private fun onOfferUpdated(productCUV: ProductCUV?) {
        val message = getString(R.string.msj_offer_updated)
        val image = productCUV?.let { ImageUtils.verifiedImageUrl(it) }
        context?.let {
            val url = if (imageDialog) image else null  // Verifica que el flag este activo para mostrar la imagen
            val colorText = ContextCompat.getColor(it, R.color.leaf_green)
            showBottomDialog(it, message, url, colorText)
        }
    }

    override fun onOfferNotAdded(message: String?) {
        message?.let {
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

    override fun setSelectedItemOrder(index: Int) {
        listener?.setSelectedItemOrder(index)
    }

    private fun setCantidadOferta(cantidad: Int) {
        txtOfferSize.text = if (cantidad > 1 || cantidad == 0) "$cantidad ${resources.getString(R.string.ganamas_plural_offer)}" else "$cantidad ${resources.getString(R.string.ganamas_singular_offer)}"
        txtOfferSize.visibility = View.VISIBLE
    }

    /**
     * Metodo que filtrara las configuraciones de las palancas segun la logica de negocio
     */
    private fun filterConfigList(input: List<ConfiguracionPorPalanca?>): List<ConfiguracionPorPalanca> {
        val output = mutableListOf<ConfiguracionPorPalanca>()
        input.filterNotNull().forEach {
            when (it.tipoOferta) {
                OfferTypes.SR -> if (it.tieneEvento == true && it.cantidadMostrarCarrusel ?: 0 > 0) output.add(it)
                OfferTypes.PN, OfferTypes.DP, OfferTypes.ATP -> output.add(it)
                else -> if (it.cantidadMostrarCarrusel ?: 0 > 0) output.add(it)
            }
        }
        return output
    }

    private fun filterOfferList(type: String, input: List<Oferta?>): List<Oferta> {
        val output = mutableListOf<Oferta>()
        input.filterNotNull().forEach {
            when (type) {
                OfferTypes.SR -> if (it.esSubCampania == false) output.add(it)
                OfferTypes.LAN -> if (it.flagIndividual == true) output.add(it)
                else -> output.add(it)
            }
        }
        return output
    }

    /**
     * Métodos compara la cantidad a mostrar de la configuacion con la cantidad de las ofertas sin filtros para forzar el "VerMas" en el carousel
     */
    private fun checkoutSizeListOffers(type: String, input: List<Oferta?>) : Boolean {
        return when(type) {
            OfferTypes.LAN -> {
                configuracionList?.firstOrNull { it.tipoOferta == type }?.cantidadMostrarCarrusel ?: 0 < input.filterNotNull().size
            }
            else -> false
        }
    }

    /**
     *  Métodos para tranformar Oferta y ProductCUV en LeverModel
     */
    private fun transformListOferta(type: String, offers: List<Oferta?>): ArrayList<OfferModel> {
        val list = arrayListOf<OfferModel>()

        for (offer in offers) {

            offer?.let { ganaMasOffer ->

                var showAmount = true
                if (ganaMasOffer.tipoOferta == OfferTypes.HV) {
                    showAmount = false
                }

                val isKitNueva: Boolean? = if (ganaMasOffer.codigoTipoOferta == KIT_NUEVA_CODE) {

                    ganaMasOffer.pedido != null

                } else null

                if (isKitNueva == true)
                    KIT_NUEVA_CUV = ganaMasOffer.cuv

                safeLet(ganaMasOffer.cuv, ganaMasOffer.nombreMarca
                    ?: "", ganaMasOffer.nombreOferta, ganaMasOffer.precioValorizado,
                    ganaMasOffer.precioCatalogo, ganaMasOffer.imagenURL, ganaMasOffer.configuracionOferta?.imgFondoApp
                    ?: "", ganaMasOffer.configuracionOferta?.colorTextoApp ?: "", ganaMasOffer.marcaID
                    ?: 0, ganaMasOffer.flagBonificacion) { cuv, nombreMarca, nombreOferta, precioValorizado, precioCatalogo, imagenURL,
                            imagenFondo, colorTexto, marcaID, flagBonificacion ->

                    var item = Offer.transform(cuv, nombreMarca, nombreOferta,
                        formatWithMoneySymbol(precioCatalogo.toString()),
                        formatWithMoneySymbol(precioValorizado.toString()),
                        imagenURL, type, ganaMasOffer.flagEligeOpcion ?: false, marcaID,
                        imageHelper.getResolutionURL(imagenFondo), colorTexto, showAmount, null,
                        null, ganaMasOffer.agotado, flagFestival = ganaMasOffer.flagFestival, isKitNueva = isKitNueva)

                    //region ABT GAB-11
                    item.bonificacion = Bonificacion(flagBonificacion,ganaMasOffer.varianteA, ganaMasOffer.varianteB, ganaMasOffer.varianteC)
                    //endregion ABT GAB-11

                    list.add(item)

                }

            }
        }

        return list
    }

    private fun transformListProductCUV(offers: List<ProductCUV?>): ArrayList<OfferModel> {
        val list = arrayListOf<OfferModel>()

        for (offer in offers) {

            var showAmount = true
            if (offer?.tipoPersonalizacion == OfferTypes.HV) {
                showAmount = false
            }

            safeLet(offer?.cuv, offer?.descripcionMarca ?: "", offer?.description,
                offer?.precioValorizado, offer?.precioCatalogo, offer?.fotoProductoSmall
                ?: "", "", "#000000", offer?.codigoTipoEstrategia ?: "",
                offer?.codigoEstrategia ?: 0, offer?.marcaId ?: 0, offer?.tipoPersonalizacion ?: "")
            { cuv, nombreMarca, nombreOferta, precioValorizado, precioCatalogo, imagenURL,
              imagenFondo, colorTexto, codigoTipoEstrategia, codigoEstrategia, marcaID, tipo ->
                list.add(Offer.transform(cuv, nombreMarca, nombreOferta,
                    formatWithMoneySymbol(precioCatalogo.toString()),
                    formatWithMoneySymbol(precioValorizado.toString()),
                    imagenURL, "", checkSelectionType(codigoTipoEstrategia, codigoEstrategia), marcaID,
                    imageHelper.getResolutionURL(imagenFondo), colorTexto, showAmount, null, null, !(offer?.stock
                    ?: true), flagFestival = offer?.flagFestival))
            }

        }

        return list
    }

    private fun checkSelectionType(codigoTipoEstrategia: String?, codigoEstrategia: Int): Boolean {
        return (isOfertaDigital(codigoTipoEstrategia) && codigoEstrategia == COD_EST_COMPUESTA_VARIABLE)
    }

    private fun isOfertaDigital(codigoTipoEstrategia: String?): Boolean {
        return (codigoTipoEstrategia in listOf("030", "005", "001", "007", "008", "009", "010", "011"))
    }

    private fun formatWithMoneySymbol(precio: String): String {
        return "$moneySymbol ${decimalFormat.format(precio.toBigDecimal())}"
    }

    private fun getOrigenPedidoWeb(palancaType: String?, originType: String?, materialGanancia: Boolean?): String {

        var palanca = palancaType
        if (materialGanancia != null) {
            if (palancaType == OfferTypes.OPM) {
                palanca = when (materialGanancia) {
                    false -> {
                        OfferTypes.RD
                    }
                    true -> {
                        OfferTypes.MG
                    }
                }
            }
        }
        val opw = configuracionListOriginal.firstOrNull { it.tipoOferta == palanca }?.listaOrigenes?.filterNotNull()?.firstOrNull { it.codigo == originType }?.valor
            ?: "0"
        return opw
    }

    /** GanaMasOffersFragment.Listener */

    override fun goToFicha(extras: Bundle) {

        configuracionList?.forEach {
            if (it.tipoOferta.equals(extras.get(BaseFichaActivity.EXTRA_TYPE_OFFER).toString())) {
                extras.putBoolean(BaseFichaActivity.EXTRA_ENABLE_SHARE, it.tieneCompartir!!)
            }
        }
        listener?.goToFicha(extras)
    }

    override fun goToFest(extras: Bundle) {
        listener?.goToFest(extras)
    }

    override fun isOffersvisible(): Boolean {
        return vpGanaMas.currentItem == 0
    }

    override fun viewBanner(typeLever: String, pos: Int) {
        val currentLocation = null
        val currentSection = null

        val sectionDesc = getOfferTypeForAnalytics(typeLever)
        val subsection = calcularCarouselTypeSubSection(GlobalConstant.SUBSECCION_NULL)
        val originLocation = calcularOfferOriginTypeLocation(OffersOriginType.ORIGEN_CONTENEDOR)
        val originSection = calcularOffersOriginTypeSection(typeLever)

        val onAnalyticsLoaded: (analytics: Analytics) -> Unit = { analytics ->
            Ofertas.impressionBanner(user?.campaing, typeLever, pos, analytics)
        }

        presenter.getAnalytics(currentLocation, currentSection, sectionDesc, subsection,
            originLocation, originSection, onAnalyticsLoaded)

    }

    override fun clicBanner(typeLever: String, pos: Int) {

        val currentLocation = null
        val currentSection = null

        val sectionDesc = getOfferTypeForAnalytics(typeLever)
        val subsection = calcularCarouselTypeSubSection(GlobalConstant.SUBSECCION_NULL)
        val originLocation = calcularOfferOriginTypeLocation(OffersOriginType.ORIGEN_CONTENEDOR)
        val originSection = calcularOffersOriginTypeSection(typeLever)

        val onAnalyticsLoaded: (analytics: Analytics) -> Unit = { analytics ->
            Ofertas.clickBanner(user?.campaing, typeLever, pos, analytics)
        }

        presenter.getAnalytics(currentLocation, currentSection, sectionDesc, subsection,
            originLocation, originSection, onAnalyticsLoaded)

    }

    override fun impressionItems(typeLever: String, list: ArrayList<Oferta>) {

        val currentLocation = calcularOfferOriginTypeLocation(OffersOriginType.ORIGEN_CONTENEDOR)
        val currentSection = calcularOffersOriginTypeSection(typeLever)
        val sectionDesc = getOfferTypeForAnalytics(typeLever)
        val subsection = null
        val originLocation = null
        val originSection = null

        val onAnalyticsLoaded: (analytics: Analytics) -> Unit = { analytics ->
            Ofertas.impressionItems(user, list, analytics)
        }

        presenter.getAnalytics(currentLocation, currentSection, sectionDesc, subsection,
            originLocation, originSection, onAnalyticsLoaded)

    }

    override fun impressionItems(list: ArrayList<Oferta>) {

        val currentLocation = calcularOfferOriginTypeLocation(OffersOriginType.ORIGEN_LANDING)
        val currentSection = calcularOffersOriginTypeSection(leverType)
        val sectionDesc = getOfferTypeForAnalytics(leverType)
        val subsection = null
        val originLocation = null
        val originSection = null

        val onAnalyticsLoaded: (analytics: Analytics) -> Unit = { analytics ->
            Ofertas.impressionItems(user, list, analytics)
        }

        presenter.getAnalytics(currentLocation, currentSection, sectionDesc, subsection,
            originLocation, originSection, onAnalyticsLoaded)

    }

    override fun impressionItemsCategories(list: ArrayList<ProductCUV>) {

        val currentLocation = calcularOfferOriginTypeLocation(OffersOriginType.ORIGEN_LANDING)
        val currentSection = calcularOffersOriginTypeSection(categoryCode)
        val sectionDesc = getOfferTypeForAnalytics(leverType)
        val subsection = null
        val originLocation = null
        val originSection = null

        val onAnalyticsLoaded: (analytics: Analytics) -> Unit = { analytics ->
            Ofertas.impressionItemsCategories(user, list, analytics, categoryName)
        }

        presenter.getAnalytics(currentLocation, currentSection, sectionDesc, subsection,
            originLocation, originSection, onAnalyticsLoaded)

    }

    override fun clicItem(typeLever: String, item: Oferta, pos: Int) {

        val currentLocation = calcularOfferOriginTypeLocation(OffersOriginType.ORIGEN_CONTENEDOR)
        val currentSection = calcularOffersOriginTypeSection(typeLever)
        val sectionDesc = getOfferTypeForAnalytics(typeLever)

        val onAnalyticsLoaded: (analytics: Analytics) -> Unit = { analytics ->
            Ofertas.clickItem(user, item, pos, analytics)
        }

        presenter.getAnalytics(currentLocation, currentSection, sectionDesc, null, null, null, onAnalyticsLoaded)

    }

    override fun clicItemCategories(item: Oferta, pos: Int) {

        val currentLocation = calcularOfferOriginTypeLocation(OffersOriginType.ORIGEN_LANDING)
        val currentSection = calcularOffersOriginTypeSection(leverType)
        val sectionDesc = getOfferTypeForAnalytics(leverType)

        val onAnalyticsLoaded: (analytics: Analytics) -> Unit = { analytics ->
            Ofertas.clickItem(user, item, pos, analytics)
        }

        presenter.getAnalytics(currentLocation, currentSection, sectionDesc, null, null, null, onAnalyticsLoaded)

    }

    override fun clicItemCategories(item: ProductCUV, pos: Int) {

        val currentLocation = calcularOfferOriginTypeLocation(OffersOriginType.ORIGEN_LANDING)
        val currentSection = calcularOffersOriginTypeSection(categoryCode)
        val sectionDesc = getOfferTypeForAnalytics(item.tipoPersonalizacion)

        val onAnalyticsLoaded: (analytics: Analytics) -> Unit = { analytics ->
            Ofertas.clickItem(user, item, pos, analytics)
        }

        presenter.getAnalytics(currentLocation, currentSection, sectionDesc, null, null, null, onAnalyticsLoaded)

    }

    /** GanaMasLandingFragment.Listener */
    override fun enableCaterogyScroll(enabled: Boolean) {
        val params = abLayout.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior as AppBarLayout.Behavior?
        behavior?.setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
            override fun canDrag(@NonNull appBarLayout: AppBarLayout): Boolean {
                return enabled
            }
        })
    }

    override fun isLandingVisible(): Boolean {
        return vpGanaMas.currentItem == 1
    }

    private fun showCategories(withFilters: Boolean) {
        abLayout?.visibility = View.VISIBLE
        abLayout.setExpanded(true, false)
        laySections?.visibility = View.VISIBLE
        if (withFilters) {
            lnlFilters?.visibility = View.VISIBLE
        } else {
            lnlFilters?.visibility = View.GONE
        }
    }

    private fun showFilters(show: Boolean) {
        abLayout?.visibility = View.VISIBLE
        abLayout.setExpanded(true, false)
        if (show) {
            lnlFilters?.visibility = View.VISIBLE
            laySections?.visibility = View.GONE
        } else {
            laySections?.visibility = View.VISIBLE
            lnlFilters?.visibility = View.GONE
        }
    }

    private fun hideCategoryFilter() {
        laySections?.visibility = View.GONE
        lnlFilters?.visibility = View.GONE
    }

    private fun hideCategories(){
        abLayout?.visibility = View.GONE
        laySections?.visibility = View.GONE
        lnlFilters?.visibility = View.GONE

    }

    private fun clearFilters() {
        filtros?.forEach { f -> f.list.forEach { it.checked = false } }
    }

    fun onBackPress(): Boolean {
        return when (vpGanaMas.currentItem) {
            GanaMasPagerAdapter.OFFERS_FRAGMENT -> false
            GanaMasPagerAdapter.ERROR_FRAGMENT -> {
                if (sectionSelected != -1) {
                    showDefaultOffers()
                    true
                } else false
            }
            GanaMasPagerAdapter.LANDING_FRAGMENT -> {
                showDefaultOffers()
                true
            }
            else -> false
        }
    }

    private fun showDefaultOffers() {
        when {
            configuracionList == null -> showErrorScreenMessage(GanaMasErrorFragment.ERROR_MESSAGE_NETWORK)
            configuracionList?.isEmpty() == true -> showErrorScreenMessage(GanaMasErrorFragment.ERROR_MESSAGE_EMPTY_CONTAINER)
            else -> {
                setBannerVisibility(false)
                showFragment(GanaMasPagerAdapter.OFFERS_FRAGMENT)
            }
        }
        resetScrollSection()
    }

    fun resetFragmentView() {
        setBannerVisibility(false)
        val configNull = configuracionList == null
        val configEmpty = configuracionList?.isEmpty() == true

        if (configNull || configEmpty) {
            if (configNull)
                showErrorScreenMessage(GanaMasErrorFragment.ERROR_MESSAGE_NETWORK)
            else if (configEmpty)
                showErrorScreenMessage(GanaMasErrorFragment.ERROR_MESSAGE_EMPTY_CONTAINER)
        } else {
            showFragment(GanaMasPagerAdapter.OFFERS_FRAGMENT)
            if (vpGanaMas.currentItem != GanaMasPagerAdapter.OFFERS_FRAGMENT)
                showFragment(GanaMasPagerAdapter.OFFERS_FRAGMENT)
            else
                abLayout.setExpanded(true, false)
            val fragment = viewPagerAdapter?.instantiateItem(vpGanaMas, GanaMasPagerAdapter.OFFERS_FRAGMENT)
            if (fragment is GanaMasOffersFragment)
                fragment.resetScroll()
        }
        resetScrollSection()
    }

    override fun showErrorScreenMessage(type: Int) {
        setBannerVisibility(false)
        setCantidadOferta(0)
        when (fromSearchProduct) {
            FromActionSearchProduct.CATEGORY -> showCategories(withFilters = false)
            FromActionSearchProduct.FILTER -> {
                val landingFragment = viewPagerAdapter?.instantiateItem(vpGanaMas, GanaMasPagerAdapter.LANDING_FRAGMENT)
                if (landingFragment is GanaMasLandingFragment) {
                    when (landingFragment.getOfferType()) {
                        FromActionOfferType.CATEGORY -> showCategories(withFilters = true)
                        FromActionOfferType.SHOW_MORE -> showFilters(true)
                    }
                }
            }
        }
        showFragment(GanaMasPagerAdapter.ERROR_FRAGMENT)
        val errorFragment = viewPagerAdapter?.instantiateItem(vpGanaMas, GanaMasPagerAdapter.ERROR_FRAGMENT)
        if (errorFragment is GanaMasErrorFragment) {
            errorFragment.showMessage(type)
        }
    }

    private fun showFragment(fragment: Int) {

        when (fragment) {
            GanaMasPagerAdapter.OFFERS_FRAGMENT -> {
                flagFilterApply = false
                setFiltersCount(arrayListOf())
                enableCaterogyScroll(true)
                showCategories(false)
                vpGanaMas.setCurrentItem(0, false)
            }
            GanaMasPagerAdapter.LANDING_FRAGMENT -> {

                val landingFragment = viewPagerAdapter?.instantiateItem(vpGanaMas, GanaMasPagerAdapter.LANDING_FRAGMENT)

                enableCaterogyScroll(true)
                vpGanaMas.setCurrentItem(1, false)

                Handler().postDelayed({

                    if (landingFragment is GanaMasLandingFragment) {
                        when (landingFragment.getOfferType()) {
                            FromActionOfferType.CATEGORY -> {
                                if (!flagFilterApply)
                                    presenter.getFiltros(true)
                                setupFilter()
                            }
                            FromActionOfferType.SHOW_MORE -> {
                                if (!flagFilterApply)
                                    presenter.getFiltros(false)
                                setupFilter()
                            }
                        }
                    }
                    currentFragment = fragment
                }, 50)
            }
            GanaMasPagerAdapter.ERROR_FRAGMENT -> {
                enableCaterogyScroll(false)
                //hideOptionBar()
                vpGanaMas.setCurrentItem(2, false)
            }
        }
    }

    fun resetScrollSection() {
        laySections?.reset()
    }

    private val listItemListener = object : ListDialog.ListItemDialogListener {
        override fun clickItem(position: Int) {

            val keyval: String? = arrayDialogOrder?.get(position)?.key
            val value = keyval?.split("-")
            order = value?.get(0) ?: DEFAULT_ORDER
            orderType = value?.get(1) ?: DEFAULT_ORDER_TYPE

            val landingFragment = viewPagerAdapter?.instantiateItem(vpGanaMas, GanaMasPagerAdapter.LANDING_FRAGMENT)
            if (landingFragment is GanaMasLandingFragment) {

                when (landingFragment.getOfferType()) {
                    FromActionOfferType.CATEGORY -> {

                        val currentLocation = calcularOfferOriginTypeLocation(OffersOriginType.ORIGEN_LANDING)
                        val currentSection = calcularOffersOriginTypeSection(categoryCode)

                        val onAnalyticsLoaded: (analytics: Analytics) -> Unit = { analytics ->
                            Ofertas.applySort(user, categoryName, arrayDialogOrder?.get(position)?.name, analytics)
                        }

                        presenter.getAnalytics(currentLocation, currentSection, null, null, null, null, onAnalyticsLoaded)

                        when (fromSearchProduct) {
                            FromActionSearchProduct.CATEGORY -> {
                                presenter.getOfferByCategory(categoryCode, categoryName, order, orderType)
                            }
                            FromActionSearchProduct.FILTER -> {
                                presenter.getOfferByFilter(categoryCode, categoryName, order, orderType, filtros)
                            }
                        }
                    }
                    FromActionOfferType.SHOW_MORE -> {
                        sortedByStrategy(arrayDialogOrder?.get(position)?.name)
                    }
                }
            }
        }
    }

    private fun generateStampFest() {
        val sello = Sello(requireContext())
        sello.setText(getString(R.string.tag_festival))
        sello.setTextSize(requireContext().resources.getDimension(R.dimen.fest_text))
        sello.setType(Sello.FESTIVAL)
        sello.setStartColor(ContextCompat.getColor(requireContext(), R.color.gana_mas_festival_start))
        sello.setEndColor(ContextCompat.getColor(requireContext(), R.color.gana_mas_festival_end))
        sello.setOrientation(3)
        sello.setWidthLayout(requireContext().resources.getDimension(R.dimen.fest_widht).toInt())
        sello.setHeightLayout(requireContext().resources.getDimension(R.dimen.fest_height).toInt())
        configuracionFestival?.Sello?.let { config ->
            config.selloTexto.let { if (!it.isNullOrBlank()) sello.setText(it) }
            config.selloColorTexto.let {
                if (StringUtil.isHexColor(it)) {
                    sello.setTextColor(Color.parseColor(it))
                }
            }
            safeLet(config.selloColorInicio, config.selloColorFin, config.selloColorDireccion) { startColor, endColor, orientation ->
                sello.setBackgroundSello(startColor, endColor, orientation)
            }
        }

        stampList.add(sello)

        val selloPromo = Sello(requireContext())
        selloPromo.setText(getString(R.string.ficha_promotion))
        selloPromo.setTextSize(requireContext().resources.getDimension(R.dimen.promotion_text))
        selloPromo.setType(Sello.PROMOTION)
        selloPromo.setStartColor(ContextCompat.getColor(requireContext(), R.color.gana_mas_promotion_start))
        selloPromo.setEndColor(ContextCompat.getColor(requireContext(), R.color.gana_mas_promotion_end))
        selloPromo.setOrientation(3)
        selloPromo.setWidthLayout(requireContext().resources.getDimension(R.dimen.promotion_widht).toInt())
        selloPromo.setHeightLayout(requireContext().resources.getDimension(R.dimen.promotion_height).toInt())
        stampList.add(selloPromo)

    }

    override fun refresh() {
        if (sectionSelected == -1) {
            presenter.getCategories()
            presenter.getConfiguracion(NetworkUtil.isThereInternetConnection(context))
        } else {
            val code = categorias?.get(sectionSelected)?.code
            val name = categorias?.get(sectionSelected)?.name
            if (code != null && name != null) {
                when (fromSearchProduct) {
                    FromActionSearchProduct.CATEGORY -> {
                        presenter.getOfferByCategory(code, name, order, orderType)
                    }
                    FromActionSearchProduct.FILTER -> {
                        presenter.getOfferByFilter(code, name, order, orderType, filtros)
                    }
                }
            }
        }
    }

    override fun hideFilters() {
        lnlFiltro.visibility = View.GONE
    }

    fun updateBannerAtp(typeLever: String, hasATP: Boolean) {
        val configAtp = configuracionList?.firstOrNull { it.tipoOferta == typeLever }
        val fragment = viewPagerAdapter?.instantiateItem(vpGanaMas, GanaMasPagerAdapter.OFFERS_FRAGMENT)
        if (fragment is GanaMasOffersFragment) {
            fragment.updateOfferView(typeLever, configAtp, hasATP)
        }
    }

    open fun calcularOfferOriginTypeLocation(offerOriginTypeLocation: String?): String {
        var originTypeLocation = ""


        when (offerOriginTypeLocation) {
            OffersOriginType.ORIGEN_CONTENEDOR -> {
                originTypeLocation = OffersOriginTypeLocation.ORIGEN_CONTENEDOR
            }

            OffersOriginType.ORIGEN_LANDING -> {
                originTypeLocation = OffersOriginTypeLocation.ORIGEN_LANDING
            }

            OffersOriginType.ORIGEN_CONTENEDOR_FICHA_CARRUSEL -> {
                originTypeLocation = OffersOriginTypeLocation.ORIGEN_FICHA
            }

            OffersOriginType.ORIGEN_LANDING_FICHA -> {
                originTypeLocation = OffersOriginTypeLocation.ORIGEN_FICHA
            }
        }

        return originTypeLocation
    }

    open fun calcularOffersOriginTypeSection(origenSectionDescription: String?): String {
        var originType = ""
        when (origenSectionDescription) {
            OfferTypes.ODD -> {
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
            OfferOriginTypeCategory.ORIGIN_CAT_TODAS -> {
                originType = OffersOriginTypeSection.ORIGEN_TODAS_OFERTAS
            }
            OfferOriginTypeCategory.ORIGIN_CAT_FRAGANCIAS -> {
                originType = OffersOriginTypeSection.ORIGEN_FRAGANCIAS
            }
            OfferOriginTypeCategory.ORIGIN_CAT_MAQUILLAJE -> {
                originType = OffersOriginTypeSection.ORIGEN_MAQUILLAJE
            }
            OfferOriginTypeCategory.ORIGIN_CAT_CUIDADO -> {
                originType = OffersOriginTypeSection.ORIGEN_CUIDADO_PERSONAL
            }
            OfferOriginTypeCategory.ORIGIN_CAT_TRATAMIENTO -> {
                originType = OffersOriginTypeSection.ORIGEN_TRATAMIENTO_FACIAL
            }
            OfferOriginTypeCategory.ORIGIN_CAT_BIJOUTERIE -> {
                originType = OffersOriginTypeSection.ORIGEN_BIJOUTERIE
            }
            OfferOriginTypeCategory.ORIGIN_CAT_MODA -> {
                originType = OffersOriginTypeSection.ORIGEN_MODA
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

    override fun setFestivalConfiguracion(conf: FestivalConfiguracion) {
        configuracionFestival = conf

        generateStampFest()
    }

    override fun setSubCampaniaConfiguracion(conf: SubCampaniaConfiguracion) {
        configuracionSubcampania = conf
    }

    override fun onButtonClick(view: View) {
        val extras = Bundle()
        goToFest(extras)
    }


    override fun setBannerScrollVisible(scrollOffer: Int?) {
        val scrollBounds2 = Rect()
        offerContain.getGlobalVisibleRect(scrollBounds2)

        if ((abs(scrollBounds2.top) == offerContain.height)) {
            setBannerVisibility(false)
        } else {
            if (scrollOffer!! >= 0) {
                setBannerVisibility(bannerVisible)
            }
        }
    }

    fun setBannerVisibility(isVisible: Boolean) {
        if (isVisible) {
            if (bannerVisible) {

                if (offerbanner.visibility != View.VISIBLE) {
                    lnlContenedor.visibility = View.INVISIBLE
                    offerbanner.visibility = View.VISIBLE

                    Handler().postDelayed({

                        lnlContenedor.visibility = View.VISIBLE
                    }, 25)

                }

            }
        } else {

            offerbanner.visibility = View.GONE
        }
    }

    // END LISTENER LEVER
    class RestartGanaMasEvent

    /** Listeners */

    internal interface Listener {
        fun goToFicha(extras: Bundle)
        fun goToArmaTuPack()
        fun goToFest(extras: Bundle)
        fun updateOffersCountFromGanaMas(count: Int)
        fun isToolbarText(s: String, i: Int)
        fun showOrder()
        fun dismissOrder()
        fun setOrder(itemListener: ListDialog.ListItemDialogListener, arrayDialog: ArrayList<ListDialogModel>)
        fun setSelectedItemOrder(index: Int)
        fun goToLeverPack(context: Context, typeLever: String, titleLever: String)
        fun onBackFromFragmentGanaMas()
        fun getFlagExpandedSearchview(flagExpandedSearchView: Boolean)
        fun goToEmbebedSuscription(page: String)
    }

    companion object {
        const val DEFAULT_ORIENTATION=0
        const val COD_EST_COMPUESTA_VARIABLE = 2003
        const val DEFAULT_ORDER = "ORDEN"
        const val DEFAULT_ORDER_TYPE = "ASC"
        const val ATP_REQUEST_CODE = 191
        const val ATP_QUANTITY_KEY = "ATP_QUANTITY_KEY"
        const val ATP_IS_UPDATE_KEY = "ATP_IS_UPDATE_KEY"
        const val ATP_PRODUCT_KEY = "ATP_PRODUCT_KEY"
        const val ATP_MESSAGE_ADDED = "ATP_MESSAGE_ADDED"
        const val FILTER_TITLE = "Filtros"
        const val CATEGORIES_TITLE = "Categorías"
        const val KEY_SABER_MAS = "KEY_SABER_MAS"
        const val CODE_SABER_MAS = "CODE_SABER_MAS"
        const val TITLE_SABER_MAS = "Saber más"
    }

}
