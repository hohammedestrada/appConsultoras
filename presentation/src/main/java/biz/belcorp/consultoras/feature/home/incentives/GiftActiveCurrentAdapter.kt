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
import biz.belcorp.consultoras.feature.contest.order.current.PerCurrentOrderActivity
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.library.annotation.Country
import kotlinx.android.synthetic.main.item_incentive_current.view.*
import java.util.*

internal class GiftActiveCurrentAdapter(private val allItems: List<ConcursoModel>,private val countryISO :String?)
    : RecyclerView.Adapter<GiftActiveCurrentAdapter.GiftOrderHolder>() {

    private var campaignHeader: String? = null
    private var trackListener: IncentivesContainerAdapter.TrackEventListener? = null

    internal inner class GiftOrderHolder (v: View) : RecyclerView.ViewHolder(v) {

        fun bind(position: Int) = with(itemView) {
            val currentContest = allItems[position]

            if (currentContest.campaniaIDInicio == currentContest.campaniaIDFin) {
                var formatGift = context.getString(R.string.incentives_pedido_title_activity_simple)
                if(Country.PE.equals(countryISO)){
                    formatGift = context.getString(R.string.bonificaciones_pedido_title_activity_simple)
                }
                campaignHeader = "C" + currentContest.campaniaIDInicio.toString().substring(4)
                tvwIncentiveCurrentTitulo.text = String.format(formatGift,
                    currentContest.campaniaIDInicio!!.toString().substring(4))
            } else {
                var formatGift = context.getString(R.string.incentives_pedido_title_activity)
                if(Country.PE.equals(countryISO)) {
                    formatGift = context.getString(R.string.bonificaciones_pedido_title_activity)
                }
                campaignHeader = "C" + currentContest.campaniaIDInicio.toString().substring(4) + "-C" + currentContest.campaniaIDFin!!.toString().substring(4)
                tvwIncentiveCurrentTitulo.text = String.format(formatGift,
                    currentContest.campaniaIDInicio!!.toString().substring(4),
                    currentContest.campaniaIDFin!!.toString().substring(4))
            }

            currentContest?.niveles?.let {
                val nivelesActuales = ArrayList<NivelModel>()

                val niveles = it.size

                it.forEach {
                    if (niveles == currentContest.nivelAlcanzado) {
                        val restaFirst = currentContest.nivelAlcanzado!! - it.codigoNivel

                        if (restaFirst == 0) {
                            nivelesActuales.add(it)
                            return@forEach
                        }
                    } else {
                        val restaSecond = it.codigoNivel - currentContest.nivelAlcanzado

                        if (restaSecond == 1) {
                            nivelesActuales.add(it)
                            return@forEach
                        }
                    }
                }

                //si hay varias opciones, se esconde los header del recycler de opciones y se mostrará en este adapter
                if (nivelesActuales.size > 0) {
                    nivelesActuales[0].let {
                        if (it.opciones.size > 1) {
                            tvwNivelGift.text = String.format(context.getString(R.string.incentives_level_points),
                                it.codigoNivel, it.puntosNivel)
                            lltNivelOpcionMultiple.visibility = View.VISIBLE
                            tvwNivelGift.visibility = View.VISIBLE
                        } else {
                            lltNivelOpcionMultiple.visibility = View.GONE
                            tvwNivelGift.visibility = View.GONE
                        }
                    }
                }

                val currentListAdapter = GiftActiveChildCurrentAdapter(context, nivelesActuales,
                    currentContest.puntosAcumulados!!, currentContest.nivelAlcanzado!!, campaignHeader!!)

                rvwContestCurrent.adapter = currentListAdapter
                rvwContestCurrent.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                rvwContestCurrent.setHasFixedSize(true)
                rvwContestCurrent.isNestedScrollingEnabled = false

                val divider = DividerItemDecoration(rvwContestCurrent.context, DividerItemDecoration.VERTICAL)
                divider.setDrawable(ContextCompat.getDrawable(context, R.drawable.recycler_horizontal_divider)!!)
                rvwContestCurrent.addItemDecoration(divider)
            }

            cvwIncentiveCurrent.setOnClickListener {
                if (trackListener != null) {
                    trackListener!!.track(
                        GlobalConstant.SCREEN_INCENTIVES_GIFT_ORDER,
                        tvwIncentiveCurrentTitulo.text.toString(),
                        GlobalConstant.EVENT_NAME_INCENTIVES_BONIFICATION)
                }

                val intent = PerCurrentOrderActivity.getCallingIntent(itemView.context)
                intent.putExtra(GlobalConstant.CODE_CONCURSO, allItems[adapterPosition].codigoConcurso)
                intent.putExtra(GlobalConstant.CODE_CAMPAIGN, campaignHeader)
                intent.putExtra(GlobalConstant.TRACK_VAR_COUNTRY, countryISO)
                itemView.context.startActivity(intent)
            }
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): GiftActiveCurrentAdapter.GiftOrderHolder {
        return GiftOrderHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_incentive_current, viewGroup, false))
    }

    override fun onBindViewHolder(holder: GiftActiveCurrentAdapter.GiftOrderHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return allItems.size
    }


    fun setTrackListener(trackListener: IncentivesContainerAdapter.TrackEventListener) {
        this.trackListener = trackListener
    }

}
