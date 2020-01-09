package biz.belcorp.consultoras.feature.home.incentives

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.model.incentivos.NivelModel
import biz.belcorp.consultoras.common.model.incentivos.OpcionModel
import biz.belcorp.consultoras.feature.contest.order.current.PerCurrentOrderActivity
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.library.util.StringUtil
import kotlinx.android.synthetic.main.item_incentive_child_generic.view.*

internal class GiftActiveChildCurrentAdapter(private val context: Context,
                                             private val allItems: List<NivelModel>,
                                             private val puntosAcumulados: Int,
                                             private val nivelAlcanzado: Int,
                                             private val campaignHeader: String)
    : RecyclerView.Adapter<GiftActiveChildCurrentAdapter.GiftOrderHolder>() {

    internal inner class GiftOrderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int) = with(itemView) {
            val model = allItems[position]

            tvwPuntajeInicialBar.text = String.format(context.getString(R.string.incentives_ptos), puntosAcumulados)
            tvwPuntajeFinalProgressBar.text = String.format(context.getString(R.string.incentives_ptos), model.puntosNivel)

            seekbarPuntosBar.isEnabled = false

            //Niveles Inferiores e Igual al Alcanzado

            if (nivelAlcanzado >= model.codigoNivel) {
                rltGift.visibility = View.VISIBLE
                lltBar.visibility = View.GONE

                tvwMessageGift.text = String.format(context.getString(R.string.incentives_winner_label),
                    StringUtil.getEmojiByUnicode(0x1F603))
                tvwMessageGift.setTextColor(ContextCompat.getColor(context, R.color.lograste_puntaje))

                setupRecyclerView(rvwOpcionesGift, model.opciones)

            } else {
                //Niveles Superiores al Alcanzado
                rltGift.visibility = View.GONE
                lltBar.visibility = View.VISIBLE

                setupRecyclerView(rvwOpcionesBar, model.opciones)

                val resta = model.codigoNivel - nivelAlcanzado

                if (resta == 1) {
                    //Nivel Superior En Progreso

                    if (model.puntosFaltantes as Int == model.puntosNivel) {
                        seekbarPuntosBar.progress = 0
                        tvwPuntosBar.text = String.format(context.getString(R.string.incentives_progress_bar_message), model.puntosFaltantes.toString())
                        tvwPuntosBar.setTextColor(ContextCompat.getColor(context, R.color.default_magenta))

                    } else {
                        seekbarPuntosBar.progress = (puntosAcumulados.toFloat() / model.puntosNivel!! * 100).toInt()
                        tvwPuntosBar.text = String.format(context.getString(R.string.incentives_progress_bar_message), model.puntosFaltantes.toString())
                        tvwPuntosBar.setTextColor(ContextCompat.getColor(context, R.color.default_magenta))
                    }
                } else {
                    //Niveles Superiores
                    seekbarPuntosBar.progress = 0
                    tvwPuntosBar.text = String.format(context.getString(R.string.incentives_progress_bar_message), model.puntosFaltantes.toString())
                    tvwPuntosBar.setTextColor(ContextCompat.getColor(context, R.color.default_magenta))
                }
            }
            lltItemIncentiveChildGeneric.setOnClickListener {
                arrow()
            }
        }

        private fun setupRecyclerView(rv: RecyclerView, opciones: List<OpcionModel>) {
            val currentListAdapter = GiftActiveOptionsCurrentAdapter(context, opciones, allItems,
                puntosAcumulados, nivelAlcanzado, campaignHeader, false)

            rv.adapter = currentListAdapter
            rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rv.setHasFixedSize(true)
            rv.isNestedScrollingEnabled = false
        }


        fun arrow() {
            val intent = PerCurrentOrderActivity.getCallingIntent(itemView.context)
            intent.putExtra(GlobalConstant.CODE_CONCURSO, allItems[adapterPosition].codigoConcurso)
            intent.putExtra(GlobalConstant.CODE_CAMPAIGN, campaignHeader)
            itemView.context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): GiftOrderHolder {
        val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_incentive_child_generic, viewGroup, false)
        return GiftOrderHolder(itemView)
    }

    override fun onBindViewHolder(holder: GiftOrderHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return allItems.size
    }

}
