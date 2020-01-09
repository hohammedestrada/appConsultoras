package biz.belcorp.consultoras.feature.finaloffer

import android.content.Context
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.model.orders.OfertaFinalModel
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.util.analytics.AddOrder as AnalyticsAddOrder
import kotlinx.android.synthetic.main.view_offers_list.view.*
import java.text.DecimalFormat

class FinalOfferList :
    LinearLayout,
    FinalOfferListAdapter.Listener {

    var listener: Listener? = null
    var lm: LinearLayoutManager? = null

    var user: User? = null

    var offersList: List<OfertaFinalModel?>? = null
    var offersListAnalytics: ArrayList<OfertaFinalModel> = ArrayList()

    private var nameListTracker: String? = null

    private lateinit var listAdapter: FinalOfferListAdapter

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int)
        : super(context, attrs, defStyle) {
        init()
    }

    /** Functions */

    fun init() {
        View.inflate(context, R.layout.view_offers_list, this)
        carouselOffers.isNestedScrollingEnabled = false

        listAdapter = FinalOfferListAdapter(context, this)
        lm = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        carouselOffers.layoutManager = lm
        carouselOffers.adapter = listAdapter
        carouselOffers.addOnScrollListener(CustomScrollListener())
    }

    fun setData(list: List<OfertaFinalModel?>?) {
        offersList = list
        listAdapter.setList(list)

        Handler().postDelayed({ onProductImpression() }, 100)
    }

    fun setNameListTracker(nameList: String?){
        this.nameListTracker = nameList
    }

    fun setDataUser(user: User?) {
        this.user = user
    }

    fun setDecimalFormat(decimalFormat: DecimalFormat, moneySymbol: String) {
        listAdapter.setFormat(decimalFormat, moneySymbol)
    }

    fun onProductAdded(item: OfertaFinalModel, position: Int) {
        listAdapter.updateItemList(item, position)
    }

    fun onProductImpression() {

        val posInit = lm?.findFirstCompletelyVisibleItemPosition() ?: 0
        val posEnd = lm?.findLastCompletelyVisibleItemPosition() ?: 0

        val offersListVisible: ArrayList<OfertaFinalModel> = ArrayList()

        if (posInit == -1 || posEnd == -1) return

        offersList?.isNotEmpty().apply {

            if (posInit == posEnd) {
                val item = offersList!![posInit]
                item?.index = posInit
                if (!offersListAnalytics.contains(item)) {
                    offersListVisible.add(item!!)
                    offersListAnalytics.add(item!!)
                }
            } else
                for (pos in posInit until posEnd) {
                    val item = offersList!![pos]
                    item?.index = posInit
                    if (!offersListAnalytics.contains(item)) {
                        offersListVisible.add(item!!)
                        offersListAnalytics.add(item!!)
                    }
                }
        }

        if (offersListVisible.size > 0)
            AnalyticsAddOrder.impressionFinalOffers(user?.countryISO, offersListVisible, nameListTracker)
    }

    /** FinalOfferListAdapter.Listener  */

    override fun onAddItem(item: OfertaFinalModel, position: Int) {
        listener?.addFinalOffer(item, position)
    }


    /** Listeners */

    interface Listener {
        fun addFinalOffer(item: OfertaFinalModel, position: Int)
    }

    /** Class */

    inner class CustomScrollListener : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            when (newState) {
                RecyclerView.SCROLL_STATE_IDLE ->
                {
                    println("The RecyclerView is not scrolling")
                    onProductImpression()
                }
                RecyclerView.SCROLL_STATE_DRAGGING -> println("Scrolling now")
                RecyclerView.SCROLL_STATE_SETTLING -> println("Scroll Settling")
            }

        }
    }

}
