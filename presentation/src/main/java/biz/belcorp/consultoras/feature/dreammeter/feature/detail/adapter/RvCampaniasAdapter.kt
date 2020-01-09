package biz.belcorp.consultoras.feature.dreammeter.feature.detail.adapter

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.entity.dreammeter.DreamMeter
import biz.belcorp.consultoras.feature.dreammeter.feature.detail.DetailFragment
import kotlinx.android.synthetic.main.item_campania_dream_meter.view.*
import java.math.BigDecimal
import android.databinding.adapters.TextViewBindingAdapter.setTextSize
import android.graphics.Paint
import android.graphics.Rect
import android.os.Handler
import android.view.ViewTreeObserver


class RvCampaniasAdapter : RecyclerView.Adapter<RvCampaniasAdapter.CampaniaViewHolder>() {

    private var mDetails: List<DreamMeter.ConsultantDream.ConsultantDreamDetail> = mutableListOf()
    private var mUser: User? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CampaniaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_campania_dream_meter, parent, false)
        return CampaniaViewHolder(view)
    }

    override fun getItemCount(): Int = mDetails.size

    override fun onBindViewHolder(holder: CampaniaViewHolder, position: Int) {
        val mDetail = mDetails[position]

        val campaniaActual = mUser?.campaing?.toInt()
        val isCampaniActual = (campaniaActual == mDetail.campaignId)

        when {
            //campania actual o campania futura
            (isCampaniActual || campaniaActual ?: 0 < mDetail.campaignId ?: 0) -> {

                val textColorCampaign: Int
                val textColor: Int

                if (isCampaniActual) {
                    textColorCampaign = Color.WHITE
                    textColor = Color.BLACK
                } else {
                    textColorCampaign = ContextCompat.getColor(holder.itemView.context, R.color.text_disabled_dream_meter)
                    textColor = ContextCompat.getColor(holder.itemView.context, R.color.text_disabled_dream_meter)
                }

                buildItem(
                    holder = holder,
                    campania = mDetail.campaignId,
                    amountVende = mDetail.sale ?: 0.toBigDecimal(),
                    amountGana = mDetail.gain ?: 0.toBigDecimal(),
                    textColorCampaign = textColorCampaign,
                    textColor = textColor,
                    withBgRed = isCampaniActual,
                    withCheck = false,
                    textGanaResId = R.string.gana,
                    textVendeResId = R.string.vende)
            }

            //campania facturada
            else -> {
                val isWithCheck = (mDetail.realSale != 0.toBigDecimal())

                buildItem(
                    holder = holder,
                    campania = mDetail.campaignId,
                    amountVende = mDetail.realSale ?: 0.toBigDecimal(),
                    amountGana = mDetail.realGain ?: 0.toBigDecimal(),
                    textColorCampaign = Color.BLACK,
                    textColor = Color.BLACK,
                    withBgRed = false,
                    withCheck = isWithCheck,
                    textGanaResId = R.string.ganaste,
                    textVendeResId = R.string.vendiste)
            }
        }

    }

    private fun buildItem(
        holder: CampaniaViewHolder,
        campania: Int?,
        amountVende: BigDecimal,
        amountGana: BigDecimal,
        textColorCampaign: Int,
        textColor: Int,
        withBgRed: Boolean,
        withCheck: Boolean,
        textVendeResId: Int,
        textGanaResId: Int
    ) {
        holder.tvwCampania.text = holder.itemView.context.getString(R.string.campania_dream_meter, campania.toString().substring(4))
        holder.tvwCampania.setTextColor(textColorCampaign)
        holder.tvwCampania.background = if (withBgRed) ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_campania_dream_meter) else null
        holder.ivwCheck.visibility = if (withCheck) View.VISIBLE else View.GONE

        holder.tvwLabelVende.text = holder.itemView.context.getString(textVendeResId)
        holder.tvwLabelVende.setTextColor(textColor)
        holder.tvwAmountVende.setTextColor(textColor)

        holder.tvwLabelGana.text = holder.itemView.context.getString(textGanaResId)
        holder.tvwLabelGana.setTextColor(textColor)
        holder.tvwAmountGana.setTextColor(textColor)

        holder.ivwMoney.setColorFilter(textColor)

        montosFormateados(
            holder.tvwAmountVende, DetailFragment.formatWithMoneySymbol(amountVende),
            holder.tvwAmountGana, DetailFragment.formatWithMoneySymbol(amountGana)
        )
    }

    fun setCampaigns(user: User?, campaigns: List<DreamMeter.ConsultantDream.ConsultantDreamDetail>) {
        mUser = user
        mDetails = campaigns
        notifyDataSetChanged()
    }

    private fun montosFormateados(tvwVende: TextView, montoVende: String, tvwGana: TextView, montoGana: String) {
        var isFirstTime = true

        tvwVende.viewTreeObserver.addOnGlobalLayoutListener {
            val lineCount = tvwVende.lineCount
            Log.d("LineCount", lineCount.toString())

            when (lineCount) {
                2 -> {
                    if (isFirstTime) {
                        isFirstTime = false
                        tvwVende.text = montoVende.replace(" ", "\n")
                        tvwGana.text = montoGana.replace(" ", "\n")
                    }
                }
                3 -> {
                    tvwVende.text = montoVende
                    tvwGana.text = montoGana
                }
            }
        }
        tvwVende.text = montoVende
        tvwGana.text = montoGana
    }

    class CampaniaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            setIsRecyclable(false)
        }

        val tvwCampania: TextView = itemView.tvwCampania
        val ivwCheck: ImageView = itemView.ivwCheck

        val tvwLabelVende: TextView = itemView.tvwLabelAmountVende
        val tvwAmountVende: TextView = itemView.tvwAmountVende

        val tvwLabelGana: TextView = itemView.tvwLabelAmountGana
        val tvwAmountGana: TextView = itemView.tvwAmountGana

        val ivwMoney: ImageView = itemView.ivwMoney
    }
}
