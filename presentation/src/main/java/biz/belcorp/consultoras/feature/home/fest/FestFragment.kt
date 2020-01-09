package biz.belcorp.consultoras.feature.home.fest

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.SafeLet
import biz.belcorp.consultoras.common.model.product.ProductCUVModel
import biz.belcorp.consultoras.data.manager.SessionManager
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.feature.ficha.common.BaseFichaActivity
import biz.belcorp.consultoras.feature.ficha.premio.FichaPremioActivity
import biz.belcorp.consultoras.feature.home.BaseHomeFragment
import biz.belcorp.consultoras.feature.home.fest.adapter.FestAdapter
import biz.belcorp.consultoras.feature.home.fest.di.FestComponent
import biz.belcorp.consultoras.feature.home.ganamas.adapters.OffersGridItemListener
import biz.belcorp.consultoras.util.FormatUtil
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.ImageUtils
import biz.belcorp.consultoras.util.UXCamUtils
import biz.belcorp.consultoras.util.anotation.AddedAlertType
import biz.belcorp.consultoras.util.anotation.CategoryFilter
import biz.belcorp.consultoras.util.anotation.SearchOriginType
import biz.belcorp.consultoras.util.anotation.SearchOriginType.*
import biz.belcorp.consultoras.util.decorations.GridCenterSpacingDecoration
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.util.DeviceUtil
import biz.belcorp.library.util.NetworkUtil
import biz.belcorp.library.util.StringUtil
import biz.belcorp.mobile.components.core.extensions.setSafeOnClickListener
import biz.belcorp.mobile.components.core.helpers.ImagesHelper
import biz.belcorp.mobile.components.core.helpers.StylesHelper
import biz.belcorp.mobile.components.design.carousel.fest.CarouselFest
import biz.belcorp.mobile.components.design.carousel.fest.model.Fest
import biz.belcorp.mobile.components.design.counter.Counter
import biz.belcorp.mobile.components.design.filter.FilterPickerDialog
import biz.belcorp.mobile.components.design.filter.model.CategoryFilterModel
import biz.belcorp.mobile.components.design.filter.model.FilterModel
import biz.belcorp.mobile.components.dialogs.bottom.BottomDialog
import biz.belcorp.mobile.components.dialogs.list.ListDialog
import biz.belcorp.mobile.components.dialogs.list.model.ListDialogModel
import biz.belcorp.mobile.components.offers.Offer
import biz.belcorp.mobile.components.offers.model.OfferModel
import biz.belcorp.mobile.components.offers.sello.Sello
import com.uxcam.UXCam
import kotlinx.android.synthetic.main.fragment_fest.*
import kotlinx.android.synthetic.main.view_offers_filters.*
import org.jetbrains.anko.displayMetrics
import javax.inject.Inject
import biz.belcorp.consultoras.util.StringUtil as AppStringUtil

class FestFragment : BaseHomeFragment(), FestView, OffersGridItemListener, SafeLet, CarouselFest.Listener {

    @Inject
    lateinit var presenter: FestPresenter

    lateinit var typeLever: String

    private lateinit var offerFestLayManager: GridLayoutManager
    private lateinit var festAdapter: FestAdapter
    private lateinit var imageHelper: ImagesHelper
    private lateinit var stylesHelper: StylesHelper
    private lateinit var errorFragmentFest: FestErrorFragment
    private var listener: Listener? = null
    private var imageDialog: Boolean = false
    private var user: User? = null
    private var festival: FestivalResponse? = null
    private var configFest: FestivalConfiguracion? = null
    private var stampList: MutableList<Sello> = mutableListOf()
    private var filtros: ArrayList<CategoryFilterModel>? = null
    private var arrayDialogOrder: ArrayList<ListDialogModel>? = null
    private var order: String = DEFAULT_ORDER
    private var orderType: String = DEFAULT_ORDER_TYPE
    private var conditionsQuantity: Int = 0
    private var rowsForPage: Int = 0
    private var isFree = false
    private var hasSearchEngine = false
    private var filterFestivalProductsList = mutableListOf<FestivalProduct>()

    var itemsAdded: Boolean = false

    /**
     * Lifecycle override functions
     */

    override fun onAttach(context: Context?) {

        super.onAttach(context)
        if (context is Listener) {
            this.listener = context
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_fest, container, false)

    override fun onResume() {
        super.onResume()
        UXCam.tagScreenName(UXCamUtils.FestFragmentName)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    /**
     * Custom override functions
     */

    override fun context(): Context = context?.let { it }
        ?: throw NullPointerException("No hay contexto")

    override fun onInjectView(): Boolean {
        getComponent(FestComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        this.presenter.attachView(this)
        init()
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {

            FichaPremioActivity.REQUEST_CODE_FICHA_PREMIO -> {
                if (resultCode == Activity.RESULT_OK) {
                    itemsAdded = true
                    presenter.getFestivalProgress()
                }
            }
        }
    }

    // FestView

    override fun onProductAdded(productCUV: ProductCUV, message: String?, code: Int) {
        productCUV.cuv?.let {

            val res: Boolean = carouselFest?.adapter?.list?.filter { it?.status == CarouselFest.FEST_SELECTED }?.any()
                ?: false
            if (!res) {
                updateCart(productCUV.cantidad ?: 0)
            }

            if (code == AddedAlertType.DEFAULT) {
                val finalMessage = message ?: getString(R.string.msj_offer_added)
                val image = ImageUtils.verifiedImageUrl(productCUV)
                context?.let {
                    val url = if (imageDialog) image else null
                    val colorText = ContextCompat.getColor(it, R.color.leaf_green)
                    showBottomDialog(it, finalMessage, url, colorText)
                }
            }

            if (code == AddedAlertType.FESTIVAL) {
                context?.let {
                    showBottomDialogAction(it, message) {
                        // move to up fragment
                        appbarLayout1.setExpanded(true)
                        rvwOffersFest.scrollToPosition(0)
                    }
                }
            }

            itemsAdded = true
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

    override fun showLoading() {
        listener?.showLoading()
    }

    override fun hideLoading() {
        listener?.hideLoading()
    }

    override fun trackBackPressed() {
        //Not required
    }

    override fun initScreenTrack() {
        //Not required
    }

    override fun setUser(user: User?) {
        user?.let {
            this.user = it
            hasSearchEngine = it.isMostrarBuscador ?: false
        }
        configureBySearchEngine()
    }

    override fun setImageEnabled(imageDialog: Boolean) {
        this.imageDialog = imageDialog
    }

    override fun showErrorScreenMessage(type: Int) {
        listener?.showErrorScreen(type)
    }

    override fun setOffersFestival(festival: FestivalResponse) {
        listener?.resetFestPendingUpdate()
        this.festival = festival
        this.conditionsQuantity = festival?.listConditions?.count() ?: 0
        carouselFest.loadFest(transformFestivalAwardToFestModel(festival?.listAwards))
        setConditions(festival?.listConditions)
    }

    override fun setFilters(filters: List<GroupFilter?>) {
        if (filters.isEmpty())
            hideFilters()
        else
            filtros = transformCategoriesFiltersList(filters)
    }

    override fun hideFilters() {
        lnlFiltro.visibility = View.GONE
    }

    override fun setOrders(orders: List<Ordenamiento?>) {
        setFilterOrderLabel(true, FestPresenter.LAYOUT_ORDER)
        arrayDialogOrder = transformOrdersList(orders)
        arrayDialogOrder?.let {
            listener?.setOrder(listItemListener, it)
            listener?.setSelectedItemOrder(0)
        }
    }

    override fun setFilterOrderLabel(isVisible: Boolean, layoutView: Int) {
        when (layoutView) {
            FestPresenter.LAYOUT_ORDER -> {
                var pxLeft = 0f
                if (isVisible) {
                    lltOrderContainer.visibility = View.VISIBLE
                    pxLeft = 23f
                } else {
                    lltOrderContainer.visibility = View.GONE
                }
                val param = txtOfferSize.layoutParams as LinearLayout.LayoutParams
                val dpTop: Int = Math.round(3 * (context as Activity).displayMetrics.density)
                val dpLeft: Int = Math.round(pxLeft * (context as Activity).displayMetrics.density)
                param.setMargins(dpLeft, dpTop, 0, 0)
                txtOfferSize.layoutParams = param
            }
            FestPresenter.LAYOUT_FILTER -> {
                if (isVisible) {
                    lnlFiltro.visibility = View.VISIBLE
                } else {
                    lnlFiltro.visibility = View.GONE
                }
            }
        }
    }

    override fun filterOffers(filters: ArrayList<CategoryFilterModel>?) {
        presenter.getConditionsOffersByFilter(FEST_CATEGORY_CODE, FEST_CATEGORY_NAME, order, orderType, filters)
    }

    override fun setConditionsOffersByFilter(offers: List<FestivalProduct?>) {
        this.conditionsQuantity = offers.count()
        setConditions(offers, true)
    }

    override fun updateRewards(progressList: List<FestivalProgressResponse?>) {
        progressList.forEach { p ->
            listener?.resetFestPendingUpdate()
            val award = this.festival?.listAwards?.firstOrNull { it?.product?.cuv == p?.cuvPremio }
            award?.let {
                it.progressLevel = p?.porcentajeProgreso
                it.remainingAmount = p?.montoRestante
                it.flagPremioAgregado = p?.flagPremioAgregado

                val status = if (p?.porcentajeProgreso ?: 0 >= 100 && p?.flagPremioAgregado == true) {
                    CarouselFest.FEST_SELECTED
                } else if (p?.porcentajeProgreso ?: 0 >= 100) {
                    CarouselFest.FEST_ENABLED
                } else {
                    CarouselFest.FEST_DISABLED
                }

                safeLet(it.product?.cuv, p?.porcentajeProgreso, p?.montoRestante) { cuv, porcentaje, montoRestante ->
                    carouselFest.changeItemProgress(cuv, porcentaje, FormatUtil.formatWithMoneySymbol(user?.countryISO, user?.countryMoneySymbol, montoRestante.toString()), status)
                }
            }
        }
    }

    /**
     * Recycler override functions
     */

    override fun pressedItem(keyItem: String, marcaID: Int, marca: String, position: Int) {
        showItemSelected(keyItem, marcaID, marca)
    }

    override fun pressedItemButtonAdd(keyItem: String, quantity: Int, counterView: Counter) {
        addToCart(keyItem, quantity, ORIGEN_LANDING_FEST_CARRUSEL_CONDICION, counterView, true)
    }

    override fun pressedItemButtonSelection(keyItem: String, marcaID: Int, marca: String, position: Int) {
        showItemSelected(keyItem, marcaID, marca)
    }

    /**
     * Fragment functions
     */

    private fun init() {
        imageHelper = ImagesHelper(context())
        stylesHelper = StylesHelper(context())

        setupRecycler()
        setupListener()
        setupErrorOfferFest()

        presenter.getUser()
        presenter.getImageEnabled()
        presenter.getOffersFestival(FEST_CATEGORY_CODE, FEST_CATEGORY_NAME, order, orderType)

        carouselFest.listener = this
    }

    private fun setupListener() {
        lltOrderContainer.setOnClickListener {
            listener?.showOrder()
        }
    }

    private fun setupRecycler() {
        offerFestLayManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        rvwOffersFest.layoutManager = offerFestLayManager

        val hideViewABTest = arguments?.getBoolean(GlobalConstant.TESTING_KEY_FLAG_HIDE_VIEW)
            ?: false
        festAdapter = FestAdapter(hideViewABTest)
        festAdapter.listenerOffer = this
        festAdapter.itemPlaceholder = resources.getDrawable(R.drawable.ic_container_placeholder)

        rvwOffersFest.adapter = festAdapter
        val spacingGrid = resources.getDimensionPixelSize(R.dimen.offer_grid_spacing)
        rvwOffersFest.addItemDecoration(GridCenterSpacingDecoration(2, spacingGrid, 0))

        offerFestLayManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when {
                    festAdapter.isHeader(position) -> offerFestLayManager.spanCount
                    else -> 1
                }
            }
        }
    }

    private fun setFiltersCount(filters: ArrayList<CategoryFilterModel>){
        var cantidad = 0
        filters.forEach { category ->
            category.list.forEach { filter ->
                if(filter.checked)
                    cantidad++
            }
        }

        if(cantidad!=0)  txtFiltrar.text = "$FILTER_TITLE ($cantidad)" else txtFiltrar.text = FILTER_TITLE
    }

    private fun configureBySearchEngine(){
        user?.let {
            presenter.getOrders()
            presenter.getFilters()
            setupFilter()
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

        configFest?.Sello?.let { config ->
            config.selloTexto.let { if (!it.isNullOrBlank()) sello.setText(it) }
            config.selloColorTexto.let {
                if (biz.belcorp.consultoras.util.StringUtil.isHexColor(it)) {
                    sello.setTextColor(Color.parseColor(it))
                }
            }
            safeLet(config.selloColorInicio, config.selloColorFin, config.selloColorDireccion) { startColor, endColor, orientation ->
                sello.setBackgroundSello(startColor, endColor, orientation)
            }
        }

        stampList.add(sello)
    }

    private fun setupErrorOfferFest() {

        val args = Bundle()
        args.putInt(FestErrorFragment.FRAGMENT_ERROR_TYPE_KEY, FestErrorFragment.ERROR_MESSAGE_EMPTY_FEST)

        errorFragmentFest = FestErrorFragment.newInstance()
        errorFragmentFest.arguments = args

        childFragmentManager.beginTransaction()
            .add(R.id.containerErrorOfferFest, errorFragmentFest, FestErrorFragment.TAG)
            .commit()

    }

    private fun setConditions(list: List<FestivalProduct?>?, withFilter: Boolean = false) {
        if (list.isNullOrEmpty()) {
            lnlError.visibility = View.VISIBLE
            rvwOffersFest.visibility = View.GONE
            this.conditionsQuantity = 0
            setQuantityOffer()
            if (withFilter) {
                errorFragmentFest.showMessage(FestErrorFragment.ERROR_MESSAGE_EMPTY_FILTER)
            } else {
                errorFragmentFest.showMessage(FestErrorFragment.ERROR_MESSAGE_EMPTY_FEST)
            }
        } else {
            this.conditionsQuantity = list.count()
            setQuantityOffer()
            festAdapter.stampLists = stampList
            festAdapter.setList(transformListFestivalProductToOfferModel(list), this.getRowForPage())
            lnlError.visibility = View.GONE
            rvwOffersFest.visibility = View.VISIBLE
        }
    }

    private fun transformFestivalAwardToFestModel(awards: List<FestivalAward?>?): ArrayList<Fest> {
        val list = arrayListOf<Fest>()

        awards?.let {
            for (award in awards) {
                safeLet(award?.product?.cuv, award?.product?.description ?: StringUtil.Empty,
                    FormatUtil.formatWithMoneySymbol(user?.countryISO, user?.countryMoneySymbol, award?.remainingAmount.toString()),
                    FormatUtil.formatWithMoneySymbol(user?.countryISO, user?.countryMoneySymbol, award?.product?.catalogPrice.toString()),
                    award?.product?.imageUrl ?: StringUtil.Empty, award?.progressLevel
                    ?: 0, award?.flagPremioAgregado ?: false)
                { cuv, description, firstAmount, secondAmount, image, progress, flagPremioAgregado ->

                    val status = if (progress >= 100 && flagPremioAgregado) {
                        CarouselFest.FEST_SELECTED
                    } else if (progress >= 100) {
                        CarouselFest.FEST_ENABLED
                    } else {
                        CarouselFest.FEST_DISABLED
                    }

                    list.add(Fest().apply {
                        key = cuv; this.description = description; this.firstAmount = firstAmount; this.secondAmount = secondAmount
                        this.image = image; this.status = status; this.progress = progress; animateProgress = true; this.free = isFree
                    })
                }
            }
        }

        return list
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

    private fun transformListFestivalProductToOfferModel(products: List<FestivalProduct?>?): ArrayList<OfferModel> {
        val list = arrayListOf<OfferModel>()

        products?.let {
            for (product in products) {
                var itemOffer: OfferModel? = null

                safeLet(product?.cuv ?: StringUtil.Empty, product?.brandName ?: StringUtil.Empty,
                    product?.description ?: StringUtil.Empty, product?.valuedPrice ?: 0L,
                    product?.catalogPrice ?: 0L, product?.imageUrl ?: StringUtil.Empty,
                    StringUtil.Empty, StringUtil.Empty, product?.flagChoiceOption ?: false,
                    product?.brandId ?: 0, product?.offerType ?: StringUtil.Empty)
                { cuv, brandName, description, valuedPrice, catalogPrice, imageUrl,
                  imageBackground, textColor, flagChoiceOption, brandId, offerType ->

                    itemOffer = Offer.transform(cuv, brandName, description,
                        FormatUtil.formatWithMoneySymbol(user?.countryISO, user?.countryMoneySymbol, catalogPrice.toString()),
                        FormatUtil.formatWithMoneySymbol(user?.countryISO, user?.countryMoneySymbol, valuedPrice.toString()),
                        imageUrl, StringUtil.Empty, flagChoiceOption, brandId,
                        imageHelper.getResolutionURL(imageBackground), textColor,
                        true, null, offerType, null, null)

                }

                itemOffer?.let{
                    // always in "true" because it belongs to the festival landing
                    it.flagFestival = true

                    list.add(it)
                }
            }
        }

        return list
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

                                if (hasSearchEngine)
                                    filterOffers(filtros)
                                else
                                    filterByStrategy(filtros)

                                dialog.dismiss()
                            }

                            override fun onClose(dialog: Dialog) {
                                dialog.dismiss()
                            }

                            override fun onButtonClicked(buttonName: String?) = Unit

                            override fun onItemRemoved(filterName: String?) = Unit

                            override fun onItemSelected(filterName: String?, filterGroup: String?) = Unit

                        })
                        .show()
                }
            }
        }
    }

    private fun setQuantityOffer() {
        txt_count.text = if (conditionsQuantity > 1 || conditionsQuantity == 0) "$conditionsQuantity ${resources.getString(R.string.ganamas_plural_offer)}" else "$conditionsQuantity ${resources.getString(R.string.ganamas_singular_offer)}"
    }

    private fun filterByStrategy(filters: ArrayList<CategoryFilterModel>?){
        filterFestivalProductsList.clear()

        val productList = (festival?.listConditions?.filterNotNull() ?: arrayListOf()).toList()
        var filterBrand = mutableListOf<FestivalProduct>()
        var filterPrice = mutableListOf<FestivalProduct>()

        try {
            if (productList.isNotEmpty()) {

                filters?.let { parent ->
                    if(parent.size == 1){
                        if(parent[0].name!=CategoryFilter.MARCAS)
                            filterBrand.addAll(productList)
                        if(parent[0].name!=CategoryFilter.PRECIOS)
                            filterPrice.addAll(productList)
                    }
                }


                filters?.forEach { parent ->
                    var setFilterSelected = mutableSetOf<Boolean>()
                    parent.list.forEach {
                        setFilterSelected.add(it.checked)
                    }
                    when (parent.name) {
                        CategoryFilter.MARCAS -> if (setFilterSelected.size == 1) filterBrand.addAll(productList)
                        CategoryFilter.PRECIOS -> if (setFilterSelected.size == 1) filterPrice.addAll(productList)
                    }
                    setFilterSelected.clear()
                }

                filters?.forEach { parent ->
                    when (parent.name) {
                        CategoryFilter.MARCAS ->
                            if (filterBrand.size == 0)
                                filterBrand = productList.filter { o -> (parent.list as List<FilterModel>).filter { it.checked }.any {  AppStringUtil.unAccent(it.name).toLowerCase() == AppStringUtil.unAccent(o.brandName).toLowerCase() } }.toMutableList()
                        CategoryFilter.PRECIOS ->
                            if (filterPrice.size == 0) {

                                filterPrice = productList.filter { o ->
                                    (parent.list as List<FilterModel>).filter { it.checked }.any {
                                        if (o.catalogPrice != null) {
                                            if (it.maxValue != 0f)
                                                o.catalogPrice!! >= it.minValue && o.catalogPrice!! < it.maxValue
                                            else
                                                o.catalogPrice!! >= it.minValue
                                        } else {
                                            false
                                        }
                                    }
                                }.toMutableList()
                            }
                    }
                }

                filterFestivalProductsList  = if (!filterPrice.isNullOrEmpty()) {
                    filterBrand.filter { o -> filterPrice.any { it.cuv == o.cuv } }.toMutableList()
                } else {
                    filterBrand
                }

                if (filterFestivalProductsList.size != 0) {
                    val sortedOffers = presenter.setOrderFestivalProduct(filterFestivalProductsList, order, orderType)
                    setConditions(sortedOffers)
                } else {
                    setConditions(filterFestivalProductsList)
                }
            }
        } catch (e: Exception) {
            BelcorpLogger.w(FEST_LANDING_SET_OFFERS, e.message)
        }

    }

    private val listItemListener = object : ListDialog.ListItemDialogListener {
        override fun clickItem(position: Int) {

            val keyVal: String? = arrayDialogOrder?.get(position)?.key
            val value = keyVal?.split("-")
            order = value?.get(0) ?: DEFAULT_ORDER
            orderType = value?.get(1) ?: DEFAULT_ORDER_TYPE

            if (hasSearchEngine)
                sortedByStrategy()
            else
                sortedByStrategyLocal(arrayDialogOrder?.get(position)?.name)
        }
    }

    private fun sortedByStrategy() {
        try {
            presenter.getConditionsOffersByFilter(FEST_CATEGORY_CODE, FEST_CATEGORY_NAME, order, orderType, this.filtros)
        } catch (e: Exception) {
            BelcorpLogger.w(FEST_LANDING_SET_OFFERS, e.message)
        }
    }

    private fun sortedByStrategyLocal(sortSelection: String?) {
        try {
            val sortedFestivalProduct = presenter.setOrderFestivalProduct(festival?.listConditions, order, orderType)
            setConditions(sortedFestivalProduct)

            festival?.listConditions?.let {
                if (it.isNotEmpty()) {
                    val sortedFestivalProduct = if (filterFestivalProductsList.size != 0) {
                        presenter.setOrderFestivalProduct(filterFestivalProductsList, order, orderType)
                    } else {
                        presenter.setOrderFestivalProduct(festival?.listConditions, order, orderType)
                    }
                    setConditions(sortedFestivalProduct as List<FestivalProduct?>)
                }
            }
        } catch (e: Exception) {
            BelcorpLogger.w(FEST_LANDING_SET_OFFERS, e.message)
        }
    }


    private fun transformCategoriesFiltersList(input: List<GroupFilter?>): ArrayList<CategoryFilterModel> {
        val output = arrayListOf<CategoryFilterModel>()
        input.forEach {
            output.add(CategoryFilterModel(
                key = it?.nombre!!,
                name = it.nombre!!,
                excluyente = it.excluyente!!,
                list = transformFilterList(it.filtros)
            ))
        }
        return output
    }

    private fun transformFilterList(input: List<Filtro?>?): ArrayList<FilterModel> {
        val output = arrayListOf<FilterModel>()
        input?.filterNotNull()?.forEach {
            safeLet(it.nombre, it.descripcion, it.valorMinimo, it.valorMaximo) { name, description, minValue, maxValue ->
                output.add(FilterModel(description, name, false, minValue, maxValue, 0, false, "0", "", ""))
            }
        }
        return output
    }

    private fun showItemSelected(keyItem: String, marcaID: Int, marca: String) {
        val itemFestival = this.festival?.listConditions?.firstOrNull { it?.cuv == keyItem }

        itemFestival?.let { item ->

            val productItem = ProductCUVModel.transformList(FestivalProduct.transformFestivalProductToProductCUV(item))

            activity?.let { mContext ->
                if (!NetworkUtil.isThereInternetConnection(context())) {
                    showNetworkError()
                } else {

                    val extras = Bundle()
                    extras.putString(BaseFichaActivity.EXTRA_KEY_ITEM, item.cuv)
                    extras.putString(BaseFichaActivity.EXTRA_TYPE_OFFER, item.offerType)
                    marcaID.let { extras.putInt(BaseFichaActivity.EXTRA_MARCA_ID, marcaID) }
                    extras.putString(BaseFichaActivity.EXTRA_MARCA_NAME, marca)
                    extras.putInt(BaseFichaActivity.EXTRA_ACCESS_FROM, BaseFichaActivity.ACCESS_FROM_LANDING_FEST)
                    extras.putString(BaseFichaActivity.EXTRA_ORIGEN_PEDIDO_WEB_FROM, ORIGEN_LANDING_FESTIVAL)
                    extras.putString(BaseFichaActivity.EXTRA_TYPE_PERSONALIZATION, item.offerType)

                    extras.putParcelableArrayList(BaseFichaActivity.EXTRAS_PRODUCTO_ITEM, arrayListOf<ProductCUVModel>().apply { add(productItem) })

                    listener?.goToFichaProduct(extras)
                }
            }

        }
    }

    private fun updateCart(quantity: Int) {
        activity?.let {
            val ordersCount = SessionManager.getInstance(it).getOrdersCount() ?: 0
            SessionManager.getInstance(it).saveOffersCount(ordersCount + quantity)

            val countIntent = Intent(BaseFichaActivity.BROADCAST_COUNT_ACTION)
            it.sendBroadcast(countIntent)
        }
    }

    /*private fun calcularOrigenPedidoWeb(item: ProductCUV, origenPedidoWebFrom: String?): String {
        return item.origenesPedidoWeb?.firstOrNull { it?.codigo == origenPedidoWebFrom }?.valor ?: "0"
    }*/

    private fun addToCart(keyItem: String, quantity: Int, codigoOrigenPedidoWeb: String, counterView: Counter?, isCondition: Boolean) {
        val iden = DeviceUtil.getId(activity)
        var festivalProduct: ProductCUV? = null

        if (isCondition) {
            festivalProduct =  this.festival?.listConditions?.firstOrNull { it?.cuv == keyItem }?.let { FestivalProduct.transformFestivalProductToProductCUV(it) }
        } else {
            val productAward = festival?.listAwards?.firstOrNull() { it?.product?.cuv == keyItem }
            val agregado = festival?.listAwards?.firstOrNull() { it?.flagPremioAgregado == true }?.flagPremioAgregado
            val producto = productAward?.product

            producto?.let {
                festivalProduct = FestivalProduct.transformFestivalProductToProductCUV(it)
            }
            festivalProduct?.reemplazarFestival = agregado
        }

        festivalProduct?.let {

            it.apply {
                cantidad = quantity
                identifier = iden
            }

            it.let {
                presenter.addToCart(it, codigoOrigenPedidoWeb, counterView, iden)
            }
        }
    }

    override fun setConfiguracion(config: FestivalConfiguracion?) {
        configFest = config
        val serator = System.getProperty("line.separator")

        config?.let {

            isFree = config.PremioGratis ?: false // Despacho Automatico

            if(config.Activo == true){
                tvwDescriptionFest.setShortText(config.DescripcionCorta)
                tvwDescriptionFest.setBigText("${config.DescripcionCorta?.trim()}$serator${config.DescripcionLarga?.trim()}")
                tvwDescriptionFest.useTrimMode(false)
                config.Titulo?.let {
                    carouselFest.setTitle(it)
                    listener?.setScreenTitle(it)
                }

                generateStampFest()
            } else {
                activity?.finish()
            }

        } ?: run { activity?.finish() }
    }

    override fun goToOffers() {
        listener?.goToOffers()
    }

    /**
     * OffersGridItemListener
     */

    override fun onPrintClickProduct(item: OfferModel, position: Int) {
        //Not required
    }

    override fun onPrintProduct(item: OfferModel, position: Int) {
        //Not required
    }

    override fun onPrintAddProduct(item: OfferModel, quantity: Int) {
        //Not required
    }

    /**
     * Fragment public functions
     */

    fun refreshFestPremio() {
        itemsAdded = true
        presenter.getFestivalProgress()
    }

    fun refreshTodo() = Unit

    fun refreshSchedule() {
        presenter.refreshSchedule()
    }


    /**
     * CarouselFest
     */

    override fun onButtonClick(item: Fest) {

        val agregado = festival?.listAwards?.firstOrNull { it?.flagPremioAgregado == true }?.flagPremioAgregado

        if (agregado == true) {
            context?.let { context ->
                BottomDialog.Builder(context)
                    .setIcon(R.drawable.ic_mano_error)
                    .setTitle(context.getString(R.string.fest_alert_title))
                    .setTitleBold()
                    .setContent(context.getString(R.string.fest_alert_message))
                    .setNegativeText(context.getString(R.string.fest_alert_cancel))
                    .setPositiveText(context.getString(R.string.fest_alert_ok))
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
                            item.key?.let {
                                addToCart(it, 1, ORIGEN_LANDING_FEST_CARRUSEL_PREMIO,null, false)
                            }
                        }
                    })
                    .setPositiveBackgroundColor(R.color.magenta)
                    .show()
            }
        } else {
            item.key?.let {
                addToCart(it, 1, ORIGEN_LANDING_FEST_CARRUSEL_PREMIO, null, false)
            }
        }
    }

    override fun onCardClick(item: Fest) {

        if(!isFree){
            val extras = Bundle()
            val festivalAward = getFestivalAward(item.key)

            extras.putString(BaseFichaActivity.EXTRA_KEY_ITEM, item.key)
            extras.putString(BaseFichaActivity.EXTRA_TYPE_OFFER, GlobalConstant.TYPE_OFFER_FESTIVAL)
            extras.putDouble(BaseFichaActivity.EXTRA_REMAINING_AMOUNT, festivalAward?.remainingAmount?:0.0)

            (activity as FestActivity).goToFichaPremio(extras)
        }
    }

    private fun getFestivalAward(keyItem: String?): FestivalAward? {

        festival?.listAwards?.forEach {
            if (it?.product?.cuv== keyItem)
                return it
        }
        return null
    }

    override fun setRowForPage(value: Int) {
        rowsForPage = value
    }

    override fun getRowForPage(): Int {
        return rowsForPage
    }

    /**
     * Listener
     */
    interface Listener {
        fun setScreenTitle(title: String)
        fun showLoading()
        fun hideLoading()
        fun showErrorScreen(type: Int)
        fun updateOffersCount(count: Int)
        fun goToOffers()
        fun goToFichaProduct(extras: Bundle)
        fun showOrder()
        fun setOrder(itemListener: ListDialog.ListItemDialogListener, arrayDialog: ArrayList<ListDialogModel>)
        fun setSelectedItemOrder(index: Int)
        fun resetFestPendingUpdate()
    }

    /**
     * Static constants/functions
     */

    companion object {
        val TAG: String = FestFragment::class.java.simpleName
        const val DEFAULT_ORDER = "ORDEN"
        const val DEFAULT_ORDER_TYPE = "ASC"
        const val FEST_LANDING_SET_OFFERS = "FestLandingSetOffers"
        const val FEST_CATEGORY_CODE = "cat-festival"
        const val FEST_CATEGORY_NAME = "Festival"
        const val FILTER_TITLE = "Filtros"
        fun newInstance(): FestFragment = FestFragment()
    }
}
