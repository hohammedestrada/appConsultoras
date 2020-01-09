package biz.belcorp.consultoras.feature.home.ganamas.adapters

import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import biz.belcorp.consultoras.R

import biz.belcorp.consultoras.feature.home.ganamas.fragments.GanaMasLandingFragment
import biz.belcorp.mobile.components.design.timer.Timer
import biz.belcorp.mobile.components.offers.model.OfferModel
import biz.belcorp.mobile.components.offers.sello.Sello
import kotlinx.android.synthetic.main.item_offers_grid_banner.view.*
import kotlinx.android.synthetic.main.item_offers_grid_header.view.*

class OffersGridAdapter(
    private val hideViewsForTesting: Boolean
) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    companion object {
        const val TYPE_TIMER = 0
        const val TYPE_BANNER = 1
        const val TYPE_OFFER = 2
    }

    var listenerTimer: OffersTimerItemListener? = null
    var listenerOffer: OffersGridItemListener? = null
    var listenerBanner: OfferBannerListener? = null
    var itemPlaceholder: Drawable? = null
    var stampLists: MutableList<Sello> = mutableListOf()

    var isODD: Boolean = false
    val data: ArrayList<Any> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {

        return when (viewType) {
            TYPE_TIMER -> {
                val header = LayoutInflater.from(parent.context).inflate(R.layout.item_offers_grid_header, parent, false)
                OfferTimerViewHolder(header, listenerTimer)
            }
            TYPE_BANNER -> {
                val header = LayoutInflater.from(parent.context).inflate(R.layout.item_offers_grid_banner, parent, false)
                OfferBannerViewHolder(header, listenerBanner)
            }
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
            is OfferTimerViewHolder -> holder.bind(position, element as GanaLandingSection.Timer)
            is OfferBannerViewHolder -> holder.bind(position, element as GanaLandingSection.Banner)
            is MultiOfferGridViewHolder -> holder.bind(position, element as OfferModel)
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is GanaLandingSection.Timer -> TYPE_TIMER
            is GanaLandingSection.Banner -> TYPE_BANNER
            else -> TYPE_OFFER
        }
    }

    fun updateData(newData: List<Any>) {
        data.clear()
        data.add(GanaLandingSection.Timer(isODD))
        //data.add(GanaLandingSection.Banner("ViewBanner"))
        data.addAll(newData)
        notifyDataSetChanged()
    }


    fun resetData() {
        data.clear()
        notifyDataSetChanged()
    }

    fun isHeader(position: Int): Boolean {
        return position == 0
    }


    fun isBanner(position: Int): Boolean {
        return position == 1
    }

    fun updateKitNuevaFlag(value: Boolean) {

        var kitIndex = -1

        for (i in data.indices) {
            val item = data[i]
            if ((item is OfferModel) && (item.flagKitNueva != null)) {
                kitIndex = i
                break
            }
        }

        if (kitIndex != -1) {

            (data[kitIndex] as OfferModel).flagKitNueva = value
            notifyItemChanged(kitIndex)

        }

    }

    /**
     * ViewHolder - Timer
     */


    inner class OfferTimerViewHolder(
        val view: View,
        private val listenerTimer: OffersTimerItemListener?
    ) : BaseViewHolder<GanaLandingSection.Timer>(view), GanaMasLandingFragment.GridTimerListener {

        private val timerView = view.oddTimer as Timer

        override fun bind(position: Int, item: GanaLandingSection.Timer) {
            listenerTimer?.updateGridListener(this)

            if (item.show) {
                showTimer(listenerTimer?.getTime() ?: 0L)
            } else {
                hideTimer()
            }
        }

        override fun startTimer(timeLeftInMillis: Long) {
            timerView.setTime(timeLeftInMillis)
        }

        override fun stopTimer() {
            timerView.stopTimer()
        }

        private fun showTimer(time: Long) {
            timerView.visibility = View.VISIBLE
            startTimer(time)
        }

        private fun hideTimer() {
            stopTimer()
            timerView.visibility = View.GONE
        }

    }

    /**
     * ViewHolder - Banner Fest
     */

    inner class OfferBannerViewHolder(
        val view: View,
        private val listenerBanner: OfferBannerListener?
    ) : BaseViewHolder<GanaLandingSection.Banner>(view) {

        private val viewBanner = view.fltBannerFest as FrameLayout

        override fun bind(position: Int, item: GanaLandingSection.Banner) {
            viewBanner.setOnClickListener {
                listenerBanner?.onClickBannerFest()
            }
        }
    }

}


sealed class GanaLandingSection {
    class Timer(val show: Boolean) : GanaLandingSection()
    class Banner(val obj: String) : GanaLandingSection()
}

interface OffersTimerItemListener {
    fun getTime(): Long
    fun updateGridListener(gridListener: GanaMasLandingFragment.GridTimerListener)
}

interface OfferBannerListener {
    fun onClickBannerFest()
}
