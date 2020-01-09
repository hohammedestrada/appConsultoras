package biz.belcorp.consultoras.feature.home.ganamas.fragments

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.BuildConfig
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.data.manager.SessionManager
import biz.belcorp.consultoras.domain.entity.Oferta
import biz.belcorp.consultoras.domain.entity.ProductCUV
import biz.belcorp.consultoras.feature.ficha.common.BaseFichaActivity
import biz.belcorp.consultoras.feature.home.BaseHomeFragment
import biz.belcorp.consultoras.feature.home.di.HomeComponent
import biz.belcorp.consultoras.feature.home.ganamas.adapters.OfferBannerListener
import biz.belcorp.consultoras.feature.home.ganamas.adapters.OffersGridAdapter
import biz.belcorp.consultoras.feature.home.ganamas.adapters.OffersGridItemListener
import biz.belcorp.consultoras.feature.home.ganamas.adapters.OffersTimerItemListener
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.TimeUtil
import biz.belcorp.consultoras.util.anotation.FromActionOfferType
import biz.belcorp.consultoras.util.anotation.OffersOriginType
import biz.belcorp.consultoras.util.decorations.GridCenterSpacingDecoration
import biz.belcorp.library.util.NetworkUtil
import biz.belcorp.mobile.components.design.counter.Counter
import biz.belcorp.mobile.components.dialogs.bottom.BottomDialog
import biz.belcorp.mobile.components.offers.model.OfferModel
import biz.belcorp.mobile.components.offers.sello.Sello
import kotlinx.android.synthetic.main.fragment_ganamas_landing.*

class GanaMasLandingFragment : BaseHomeFragment(), OffersGridItemListener, OffersTimerItemListener, OfferBannerListener {

    private lateinit var ofertas: ArrayList<OfferModel?>
    private lateinit var carouselAdapter: OffersGridAdapter
    private lateinit var carouselLayManager: GridLayoutManager

    var listener: Listener? = null
    var gridListener: GridTimerListener? = null
    private var productCUVMap = mutableMapOf<String, ProductCUV>()
    private var ofertaMap = mutableMapOf<String, Oferta>()
    private var stampLists: MutableList<Sello> = mutableListOf()
    private var fromActionOffer = 0

    private var isFromODD: Boolean = false

    // Analytics List Visible
    var listOffers = arrayListOf<Oferta>()
    var listProducts = arrayListOf<ProductCUV>()


    // Overrides Fragment
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Listener) {
            this.listener = context
        }
    }

    override fun onInjectView(): Boolean {
        getComponent(HomeComponent::class.java).inject(this)
        return true
    }

    override fun context(): Context {
        return context?.let { it } ?: throw NullPointerException("No hay contexto")
    }

    // Overrides Fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ganamas_landing, container, false)
    }


    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        init()
    }

    private fun init() {
        ofertas = arrayListOf()
        carouselLayManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        carouselOffers.layoutManager = carouselLayManager
        carouselOffers.setHasFixedSize(true)

        val spacingGrid = resources.getDimensionPixelSize(R.dimen.offer_grid_spacing)
        carouselOffers.addItemDecoration(GridCenterSpacingDecoration(2, spacingGrid, 1))

        // agregue un parametro mas al adapter que es un flag para saber si ocultar ciertas vistas o no
        val hideViewABTest = arguments?.getBoolean(GlobalConstant.TESTING_KEY_FLAG_HIDE_VIEW) ?: false
        carouselAdapter = OffersGridAdapter(hideViewABTest)
        carouselAdapter.listenerTimer = this
        carouselAdapter.listenerBanner = this
        carouselAdapter.listenerOffer = this
        carouselAdapter.itemPlaceholder = resources.getDrawable(R.drawable.ic_container_placeholder)
        carouselOffers.adapter = carouselAdapter

        carouselLayManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when {
                    carouselAdapter.isHeader(position) -> carouselLayManager.spanCount
                    //carouselAdapter.isBanner(position) -> carouselLayManager.spanCount
                    else -> 1
                }
            }
        }
    }


    fun setProductCUVMap(offers: List<ProductCUV?>) {
        fromActionOffer = FromActionOfferType.CATEGORY
        offers.forEach {
            it?.cuv?.let { cuv -> this.productCUVMap.put(cuv, it) }
        }
    }

    fun setStampList(stamps: MutableList<Sello>) {
        stampLists = stamps
    }

    fun setOfertaMap(offers: List<Oferta?>) {
        fromActionOffer = FromActionOfferType.SHOW_MORE
        offers.forEach {
            it?.cuv?.let { cuv -> this.ofertaMap.put(cuv, it) }
        }
    }

    fun setOfertas(ofertas: List<OfferModel>, isOriginODD: Boolean = false) {
        this.isFromODD = isOriginODD
        carouselOffers.visibility = View.VISIBLE

        updateData(ofertas, this.isFromODD)
    }

    private fun updateData(ofertas: List<OfferModel>, isFromODD: Boolean) {
        this.ofertas.clear()
        this.ofertas.addAll(ofertas)

        carouselAdapter.stampLists = stampLists
        carouselAdapter.isODD = isFromODD
        carouselAdapter.updateData(ofertas)

        carouselOffers.addOnScrollListener(GridScrollListener())

        carouselLayManager.scrollToPositionWithOffset(0, 0)
        listener?.enableCaterogyScroll(true)

        carouselOffers.postDelayed({ onItemImpression() }, 200)
    }

    override fun onPause() {
        super.onPause()
        if (listener?.isLandingVisible() == true)
            stopTimer()
    }

    override fun onResume() {
        super.onResume()
        if (listener?.isLandingVisible() == true)
            startTimer()
    }

    fun stopTimer() {
        if (isFromODD)
            gridListener?.stopTimer()
    }

    fun startTimer() {
        if (isFromODD)
            gridListener?.startTimer(getTime())
    }


    fun getOfferType(): Int {
        return fromActionOffer
    }

    fun updateKitNueva(value: Boolean) {
        carouselAdapter.updateKitNuevaFlag(value)
    }

    private fun onItemImpression() {
        var posInit = carouselLayManager.findFirstVisibleItemPosition()
        var posEnd = carouselLayManager.findLastVisibleItemPosition()

        if (posInit == -1 || posEnd == -1) return
        if (posInit == 0) posInit = 1
        if (posEnd == 0) posEnd = 1

        if (fromActionOffer == FromActionOfferType.SHOW_MORE) {
            getVisibleOffers(posInit, posEnd)
            return
        } else if (fromActionOffer == FromActionOfferType.CATEGORY) {
            getVisibleProducts(posInit, posEnd)
            return
        }
    }

    private fun getVisibleOffers(posInit: Int, posEnd: Int) {
        val list = arrayListOf<Oferta>()

        this.ofertas?.isNotEmpty().apply {

            if (posInit == posEnd) {
                val item = ofertaMap[ofertas[posInit - 1]?.key]
                item?.let {
                    it.index = posInit
                    list.add(it)
                }
            } else
                for (pos in posInit - 1 until posEnd) {
                    val item = ofertaMap[ofertas[pos]?.key]
                    item?.let {
                        it.index = pos
                        list.add(it)
                    }
                }
        }
        if (list.size > 0) {
            if (listOffers.size == 0) {
                listOffers = arrayListOf()
                listOffers.addAll(list)
                listener?.impressionItems(list)
            } else {
                val filterList = list.minus(listOffers) as ArrayList
                listOffers = arrayListOf()
                listOffers.addAll(filterList)
                if (filterList.size > 0) listener?.impressionItems(filterList)
            }
        }
    }

    private fun getVisibleProducts(posInit: Int, posEnd: Int) {
        val list = arrayListOf<ProductCUV>()

        this.ofertas?.isNotEmpty().apply {
            if (posInit == posEnd) {
                val item = productCUVMap[ofertas[posInit - 1]?.key]
                item?.let {
                    it.index = posInit
                    list.add(it)
                }
            } else
                for (pos in posInit - 1 until posEnd) {
                    val item = productCUVMap[ofertas[pos]?.key]
                    item?.let {
                        it.index = pos
                        list.add(it)
                    }
                }
        }
        if (list.size > 0) {
            if (listProducts.size == 0) {
                listProducts = arrayListOf()
                listProducts.addAll(list)
                listener?.impressionItemsCategories(list)
            } else {
                val filterList = list.minus(listProducts) as ArrayList
                listProducts = arrayListOf()
                listProducts.addAll(filterList)
                if (filterList.size > 0) listener?.impressionItemsCategories(filterList)
            }
        }
    }

    //listeners

    override fun getTime(): Long {
        // Modificar esto para que utilice el time correcto (getdevicetime es solo para pruebas)
        return TimeUtil.setServerTime(BuildConfig.TIME_SERVER)
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
                when (fromActionOffer) {
                    FromActionOfferType.CATEGORY -> {
                        productCUVMap[keyItem]?.let {
                            listener?.addFromLandingCategoria(it, quantity, counterView)
                        }
                    }
                    FromActionOfferType.SHOW_MORE -> {
                        ofertaMap[keyItem]?.let {
                            listener?.addFromLandingPalanca(it, quantity, counterView)
                        }
                    }
                }
            } else {
                showNetworkError()
            }
        }
    }

    override fun pressedItemButtonSelection(keyItem: String, marcaID: Int, marca: String, position: Int) {
        showItemSelected(keyItem, marcaID, marca, position)
    }

    private fun showItemSelected(keyItem: String, marcaID: Int, marca: String, position: Int) {
        val session = SessionManager.getInstance(context())
        if (!NetworkUtil.isThereInternetConnection(context()) && session.getApiCacheEnabled() == false) {
            showNetworkError()
        } else {
            val extras = Bundle()
            extras.putString(BaseFichaActivity.EXTRA_KEY_ITEM, keyItem)
            extras.putInt(BaseFichaActivity.EXTRA_MARCA_ID, marcaID)
            extras.putString(BaseFichaActivity.EXTRA_MARCA_NAME, marca)

            if (fromActionOffer == FromActionOfferType.CATEGORY) {
                extras.putString(BaseFichaActivity.EXTRA_ORIGEN_PEDIDO_WEB_FROM,
                    OffersOriginType.ORIGEN_LANDING_CATEGORIA)
                productCUVMap[keyItem]?.let {
                    listener?.clicItemCategories(it, position)
                    extras.putString(BaseFichaActivity.EXTRA_TYPE_OFFER, it.tipoPersonalizacion)
                    it.isMaterialGanancia?.let { it1 ->
                        extras.putBoolean(BaseFichaActivity.EXTRA_MATERIAL_GANANCIA, it1) }
                }
            } else if (fromActionOffer == FromActionOfferType.SHOW_MORE) {
                extras.putString(BaseFichaActivity.EXTRA_ORIGEN_PEDIDO_WEB_FROM,
                    OffersOriginType.ORIGEN_LANDING)
                ofertaMap[keyItem]?.let {
                    listener?.clicItemCategories(it, position)
                    extras.putString(BaseFichaActivity.EXTRA_TYPE_OFFER, it.tipoOferta)
                }
            }

            extras.putInt(BaseFichaActivity.EXTRA_ACCESS_FROM, BaseFichaActivity.ACCESS_FROM_GANAMAS)

            listener?.goToFicha(extras)
        }
    }

    override fun updateGridListener(gridListener: GridTimerListener) {
        this.gridListener = gridListener
    }

    override fun onClickBannerFest() {
        val extras = Bundle()
        listener?.goToFest(extras)
    }

    companion object {

        @JvmStatic
        fun newInstance(flagHideViewsForTesting: Boolean) = GanaMasLandingFragment().apply {
            arguments = Bundle().apply {
                putBoolean("flag_hide_views_for_testing", flagHideViewsForTesting)
            }
        }
    }

    override fun onPrintClickProduct(item: OfferModel, position: Int) {
        //Not required
    }

    override fun onPrintProduct(item: OfferModel, position: Int) {
        //Not required
    }

    override fun onPrintAddProduct(item: OfferModel, quantity: Int) {
        //Not required
    }

    /** Listeners */

    interface Listener {
        fun setBannerScrollVisible(scrollOffer:Int?)
        fun addFromLandingCategoria(oferta: ProductCUV, quantity: Int, counterView: Counter)
        fun goToFicha(extras: Bundle)
        fun goToFest(extras: Bundle)
        fun enableCaterogyScroll(enabled: Boolean)
        fun addFromLandingPalanca(oferta: Oferta, quantity: Int, counterView: Counter): Any
        fun isLandingVisible(): Boolean
        fun setSelectedItemOrder(index: Int)
        fun clicItemCategories(item: Oferta, pos: Int)
        fun clicItemCategories(item: ProductCUV, pos: Int)
        fun impressionItems(list: ArrayList<Oferta>)
        fun impressionItemsCategories(list: ArrayList<ProductCUV>)
    }

    interface GridTimerListener {
        fun startTimer(timeLeftInMillis: Long)
        fun stopTimer()
    }

    /** Class  */

    inner class GridScrollListener : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            when (newState) {
                RecyclerView.SCROLL_STATE_IDLE -> {
                    println("The RecyclerView is not scrolling")
                    onItemImpression()
                }
                RecyclerView.SCROLL_INDICATOR_TOP -> {
                    println("The RecyclerView is on top")
                }
                RecyclerView.SCROLL_INDICATOR_BOTTOM -> {
                    println("The RecyclerView is on bottom")
                }
                RecyclerView.SCROLL_STATE_DRAGGING -> println("Scrolling now")
                RecyclerView.SCROLL_STATE_SETTLING -> println("Scroll Settling")
            }

        }

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            listener?.setBannerScrollVisible(recyclerView?.getChildAt(0)?.top)

        }

    }
}
