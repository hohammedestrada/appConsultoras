package biz.belcorp.consultoras.feature.search.results

import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.feature.home.ganamas.adapters.BaseViewHolder
import biz.belcorp.consultoras.feature.home.ganamas.adapters.MultiOfferGridViewHolder
import biz.belcorp.consultoras.feature.home.ganamas.adapters.OffersGridItemListener
import biz.belcorp.mobile.components.offers.model.OfferModel
import biz.belcorp.mobile.components.offers.sello.Sello

class SearchResultsGridAdapter(
    private val hideViewsForTesting: Boolean
) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    companion object {
        const val TYPE_OFFER = 0
    }

    var listenerOffer: OffersGridItemListener? = null
    var itemPlaceholder: Drawable? = null

    val data: MutableList<Any> = mutableListOf()
    var stampLists: MutableList<Sello> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            TYPE_OFFER -> {
                val normal = LayoutInflater.from(parent.context).inflate(R.layout.item_offers_grid_item, parent, false)
                MultiOfferGridViewHolder(normal, listenerOffer, itemPlaceholder, hideViewsForTesting, stampLists)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = data[position]
        when (holder) {
            is MultiOfferGridViewHolder -> holder.bind(position, element as OfferModel)
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int {
        return TYPE_OFFER
    }

    fun updateData(newData: List<Any?>?) {
        data.clear()
        newData?.filterNotNull()?.let { data.addAll(it) }
        notifyDataSetChanged()
    }

    fun resetData() {
        data.clear()
        notifyDataSetChanged()
    }

}
