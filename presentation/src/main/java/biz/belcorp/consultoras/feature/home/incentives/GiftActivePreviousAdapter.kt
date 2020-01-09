package biz.belcorp.consultoras.feature.home.incentives

import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.model.incentivos.ConcursoModel
import biz.belcorp.consultoras.common.model.incentivos.NivelModel
import biz.belcorp.consultoras.feature.contest.order.previous.PerPreviousOrderActivity
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.library.annotation.Country
import kotlinx.android.synthetic.main.item_incentive_previous.view.*
import java.text.DecimalFormat
import java.util.*

internal class GiftActivePreviousAdapter(private val allItems: List<ConcursoModel>,
                                         private val currentCampania: String,
                                         private val countryMoneySymbol: String,
                                         private val decimalFormat: DecimalFormat,
                                         private val countryISO : String?)
    : RecyclerView.Adapter<GiftActivePreviousAdapter.GiftOrderHolder>() {

    private var campaignHeader: String? = null
    private var trackListener: IncentivesContainerAdapter.TrackEventListener? = null

    internal inner class GiftOrderHolder (v: View) : RecyclerView.ViewHolder(v) {

        fun bind(position: Int) = with(itemView) {
            val contest = allItems[position]

            var previousCampaniaFinal = ""

            if (contest.campaniaIDInicio == contest.campaniaIDFin) {

                var giftFormat = context.getString(R.string.incentives_pedido_title_activity_simple)
                if(Country.PE.equals(countryISO)){
                    giftFormat = context.getString(R.string.bonificaciones_pedido_title_activity_simple)
                }

                campaignHeader = "C" + contest.campaniaIDInicio.toString().substring(4)
                tvwIncentivePreviousTitulo.text = String.format(giftFormat,
                    contest.campaniaIDInicio.toString().substring(4))
            } else {
                var giftFormat = context.getString(R.string.incentives_pedido_title_activity)
                if(Country.PE.equals(countryISO)){
                    giftFormat = context.getString(R.string.bonificaciones_pedido_title_activity)
                }
                campaignHeader = "C" + contest.campaniaIDInicio.toString().substring(4) +
                    "-C" + contest.campaniaIDFin!!.toString().substring(4)
                previousCampaniaFinal = contest.campaniaIDFin.toString().substring(4)
                tvwIncentivePreviousTitulo.text = String.format(giftFormat,
                    contest.campaniaIDInicio.toString().substring(4),
                    contest.campaniaIDFin.toString().substring(4))
            }

            contest?.niveles?.let {
                val nivelesList = ArrayList<NivelModel>()
                it.forEach { ni ->
                    if (ni.codigoNivel == contest.nivelAlcanzado) {
                        nivelesList.add(ni)
                        return@forEach
                    } else {
                        val restaSecond = ni.codigoNivel - contest.nivelAlcanzado

                        if (restaSecond == 1) {
                            nivelesList.add(ni)
                            return@forEach
                        }
                    }

                }

                //si hay varias opciones, se esconde los header del recycler de opciones y se mostrará en este adapter
                if (nivelesList.size > 0) {
                    nivelesList[0].let {
                        if (it.opciones.size > 1) {
                            tvwPuntajeFinalGift.text = String.format(context.getString(R.string.incentives_level_points), it.codigoNivel, it.puntosNivel)
                            lltNivelOpcionMultiple.visibility = View.VISIBLE
                            tvwPuntajeFinalGift.visibility = View.VISIBLE
                        } else {
                            lltNivelOpcionMultiple.visibility = View.GONE
                            tvwPuntajeFinalGift.visibility = View.GONE
                        }
                    }
                }

                val adapter = GiftActiveChildPreviousAdapter(context, nivelesList,
                    contest.puntosAcumulados, contest.nivelAlcanzado,
                    currentCampania, contest.campaniaIDPremiacion, countryMoneySymbol,
                    decimalFormat, campaignHeader!!)
                rvwContestPrevious.adapter = adapter
                rvwContestPrevious.layoutManager = LinearLayoutManager(context,
                    LinearLayoutManager.VERTICAL, false)
                rvwContestPrevious.setHasFixedSize(true)
                rvwContestPrevious.isNestedScrollingEnabled = false

                val divider = DividerItemDecoration(rvwContestPrevious.context,
                    DividerItemDecoration.VERTICAL)
                divider.setDrawable(ContextCompat.getDrawable(context,
                    R.drawable.recycler_horizontal_divider)!!)
                rvwContestPrevious.addItemDecoration(divider)
            }

            cvwIncentivePrevious.setOnClickListener {
                if (null != trackListener) {
                    trackListener!!.track(
                        GlobalConstant.SCREEN_INCENTIVES_GIFT_ORDER,
                        tvwIncentivePreviousTitulo.text.toString(),
                        GlobalConstant.EVENT_NAME_INCENTIVES_BONIFICATION)
                }

                val intent = PerPreviousOrderActivity.getCallingIntent(itemView.context)
                intent.putExtra(GlobalConstant.CODE_CONCURSO,
                    allItems[adapterPosition].codigoConcurso)
                intent.putExtra(GlobalConstant.CURRENT_CAMPAIGN, currentCampania)
                intent.putExtra(GlobalConstant.CODE_CAMPAIGN, campaignHeader)
                intent.putExtra(GlobalConstant.TRACK_VAR_COUNTRY, countryISO)
                itemView.context.startActivity(intent)
            }

        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int)
        : GiftActivePreviousAdapter.GiftOrderHolder {
        return GiftOrderHolder(LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_incentive_previous, viewGroup, false))
    }

    override fun onBindViewHolder(holder: GiftOrderHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return allItems.size
    }

    fun setTrackListener(trackListener: IncentivesContainerAdapter.TrackEventListener) {
        this.trackListener = trackListener
    }

}
