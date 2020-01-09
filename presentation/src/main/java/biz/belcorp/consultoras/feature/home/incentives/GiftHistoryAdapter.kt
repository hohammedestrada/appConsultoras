package biz.belcorp.consultoras.feature.home.incentives

import android.graphics.Color
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.model.incentivos.ConcursoModel
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.library.annotation.Country
import biz.belcorp.library.util.StringUtil

import kotlinx.android.synthetic.main.item_incentive_history.view.*


internal class GiftHistoryAdapter (private val allItems: List<ConcursoModel>,private val countryISO : String?)
    : RecyclerView.Adapter<GiftHistoryAdapter.Holder>() {

    private var trackListener: IncentivesContainerAdapter.TrackEventListener? = null

    internal inner class Holder (v: View) : RecyclerView.ViewHolder(v) {

        fun bind(position: Int) = with(itemView) {

            val currentContest = allItems[position]

            var lograste = false

            if (currentContest != null && currentContest.niveles != null &&
                !currentContest.niveles.isEmpty() && !TextUtils.isEmpty(currentContest.campaniaId)) {
                rvwContest!!.visibility = View.VISIBLE

                var startCampaignNumber = ""
                var endCampaignNumber = ""

                if (currentContest.campaniaIDInicio != null && currentContest.campaniaIDInicio!!.toString().length > 4)
                    startCampaignNumber = currentContest.campaniaIDInicio!!.toString().substring(4)
                if (currentContest.campaniaIDFin != null && currentContest.campaniaIDFin!!.toString().length > 4)
                    endCampaignNumber = currentContest.campaniaIDFin!!.toString().substring(4)

                if (currentContest.campaniaIDInicio == currentContest.campaniaIDFin) {
                    if (currentContest.tipoConcurso == "X") {
                        var concursoFormat = context.getString(R.string.incentives_pedido_title_activity_simple_history)
                        if(Country.PE.equals(countryISO)){
                            concursoFormat = context.getString(R.string.bonificaciones_pedido_title_activity_simple_history)
                        }
                        tvwTitle?.text = String.format(concursoFormat, startCampaignNumber)
                    } else {
                        var concursoFormat = context.getString(R.string.incentives_constancia_title_activity_simple_history)
                        if(Country.PE.equals(countryISO)){
                            concursoFormat = context.getString(R.string.bonificaciones_constancia_title_activity_simple_history)
                        }
                        tvwTitle?.text = String.format(concursoFormat, startCampaignNumber)
                    }
                } else {

                    if (currentContest.tipoConcurso == "X") {
                        var concursoFormat = context.getString(R.string.incentives_pedido_title_activity_history)
                        if(Country.PE.equals(countryISO)){
                            concursoFormat = context.getString(R.string.bonificaciones_pedido_title_activity_history)
                        }
                        tvwTitle?.text = String.format(concursoFormat, startCampaignNumber, endCampaignNumber)
                    } else {
                        var concursoFormat = context.getString(R.string.incentives_constancia_title_activity_history)
                        if(Country.PE.equals(countryISO)){
                            concursoFormat = context.getString(R.string.bonificaciones_constancia_title_activity_history)
                        }

                        tvwTitle?.text = String.format(concursoFormat, startCampaignNumber, endCampaignNumber)
                    }
                }

                tvwSubtitle?.text = String.format(context.getString(R.string.incentives_points_previous),
                    currentContest.puntosAcumulados.toString())

                if (currentContest.niveles != null && !currentContest.niveles.isEmpty()
                    && currentContest.nivelAlcanzado > 0) {
                    ivwArrowData?.visibility = View.VISIBLE
                    tvwWinnerMessage.visibility = View.VISIBLE
                    if (currentContest.nivelAlcanzado != null && currentContest.nivelAlcanzado != 0) {
                        tvwWinnerMessage.text = String.format(context.getString(R.string.incentives_winner_label),
                            StringUtil.getEmojiByUnicode(0x1F603))
                        lograste = true
                    } else {
                        tvwWinnerMessage.text = String.format(context.getString(R.string.incentives_loser_label),
                            StringUtil.getEmojiByUnicode(0x1F61E))
                        tvwWinnerMessage.setTextColor(Color.GRAY)
                    }

                    val giftHistoryAdapter = GiftHistoryChildAdapter(
                        currentContest.niveles, currentContest.puntosAcumulados,
                        currentContest.nivelAlcanzado, currentContest.isIndicadorPremioAcumulativo)
                    rvwContest.adapter = giftHistoryAdapter
                    rvwContest.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    rvwContest.setHasFixedSize(true)
                    rvwContest.isNestedScrollingEnabled = false

                } else {
                    tvwWinnerMessage.visibility = View.GONE
                    ivwArrowData.visibility = View.INVISIBLE
                }
            }

            lltHeader.setOnClickListener {
                trackListener?.let {
                    trackListener?.track(
                        GlobalConstant.SCREEN_INCENTIVES_GIFT_HISTORIC,
                        tvwTitle!!.text.toString(),
                        GlobalConstant.EVENT_NAME_INCENTIVE_HISTORIC)
                }

                if (lltData.visibility == View.GONE) {
                    lltData.visibility = View.VISIBLE
                    tvwWinnerMessage.visibility = View.GONE
                    tvwSubtitle.visibility = View.GONE
                    ivwArrowData.setImageDrawable(VectorDrawableCompat.create(context.resources, R.drawable.ic_arrow_up_black, null))
                } else {
                    lltData.visibility = View.GONE
                    tvwSubtitle.visibility = View.VISIBLE
                    if (lograste) tvwWinnerMessage.visibility = View.VISIBLE
                    ivwArrowData.setImageDrawable(VectorDrawableCompat.create(context.resources, R.drawable.ic_arrow_down_black, null))
                }
            }

        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_incentive_history, viewGroup, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return allItems.size
    }

    internal fun setTrackListener(trackListener: IncentivesContainerAdapter.TrackEventListener) {
        this.trackListener = trackListener
    }
}
