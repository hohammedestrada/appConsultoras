package biz.belcorp.consultoras.feature.home.fest.adapter

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.feature.home.ganamas.adapters.BaseViewHolder
import biz.belcorp.consultoras.feature.home.ganamas.adapters.MultiOfferGridViewHolder
import biz.belcorp.consultoras.feature.home.ganamas.adapters.OffersGridItemListener
import biz.belcorp.mobile.components.offers.model.OfferModel
import biz.belcorp.mobile.components.offers.sello.Sello
import kotlinx.android.synthetic.main.item_loader.view.*
import kotlinx.android.synthetic.main.item_offers_fest_header.view.*

class FestAdapter(private val hideViewsForTesting: Boolean) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_OFFER = 1
        const val TYPE_LOADER = 2
    }

    var listenerOffer: OffersGridItemListener? = null
    var listenerFest: OfferFestBannerListener? = null
    var itemPlaceholder: Drawable? = null
    var stampLists: MutableList<Sello> = mutableListOf()

    private var offers: ArrayList<Any?> = arrayListOf()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            TYPE_HEADER -> {
                val header = LayoutInflater.from(parent.context).inflate(R.layout.item_offers_fest_header, parent, false)
                OfferFestHeaderViewHolder(header, listenerFest)
            }
            TYPE_OFFER -> {
                val normal = LayoutInflater.from(parent.context).inflate(R.layout.item_offers_grid_item, parent, false)
                MultiOfferGridViewHolder(normal, listenerOffer, itemPlaceholder, hideViewsForTesting, stampLists)
            }
            TYPE_LOADER -> {
                val loader = LayoutInflater.from(parent.context).inflate(R.layout.item_loader, parent, false)
                LoaderViewHolder(loader)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = offers[position] // data.currentList[position]
        element?.let {
            when (holder) {
                //is OfferFestHeaderViewHolder -> holder.bind(position, element as FestHeaderItem)
                is MultiOfferGridViewHolder -> holder.bind(position, element as OfferModel)
                is LoaderViewHolder -> holder.bind(position, element as LoaderModel)
                else -> throw IllegalArgumentException()
            }
        }
    }

    override fun getItemCount(): Int = offers.size //data.currentList.size

    override fun getItemViewType(position: Int): Int {
        return when (offers[position]) {
            //is FestHeaderItem -> TYPE_HEADER
            is OfferModel -> TYPE_OFFER
            is LoaderModel -> TYPE_LOADER
            else -> throw IllegalStateException("Unknown view type at position $position")
        }
    }

    /**
     * ViewHolder - Offer Fest Banner
     */

    inner class OfferFestHeaderViewHolder(
        val view: View,
        private val listenerFest: OfferFestBannerListener?
    ) : BaseViewHolder<FestHeaderItem>(view) {

        private val viewBanner = view.fltFestHeader as FrameLayout

        override fun bind(position: Int, item: FestHeaderItem) {
            viewBanner.setOnClickListener {
                listenerFest?.onClickOfferFest()
            }
        }
    }

    inner class LoaderViewHolder(itemView: View) :
        BaseViewHolder<LoaderModel>(itemView) {

        override fun bind(position: Int, item: LoaderModel) {
            itemView.loadmore_progress1.visibility = View.GONE
        }

    }

    /**
     * Public methods
     */

    fun isHeader(position: Int): Boolean {
        return position == -1
    }

    fun setList(list: ArrayList<OfferModel>?, rowForPage:Int) {
        list?.let {
            offers.clear()
            offers.addAll(list)
        }
        notifyDataSetChanged()
    }

    fun addData(listItems: ArrayList<Any>) {
        val size = this.offers.size
        this.offers.addAll(listItems)
        val sizeNew = this.offers.size
        notifyItemRangeChanged(size, sizeNew)
    }

    fun removeLoading() {
        val position = this.offers.size - 1
        val item = this.offers[position]
    }
}

object FestHeaderItem
class LoaderModel {
    var status: Boolean = false
}

/**
 * Diff items presented by this adapter.
 */


interface OfferFestBannerListener {
    fun onClickOfferFest()
}
