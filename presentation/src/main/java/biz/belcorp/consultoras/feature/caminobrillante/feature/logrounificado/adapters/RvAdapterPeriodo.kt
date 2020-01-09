package biz.belcorp.consultoras.feature.caminobrillante.feature.logrounificado.adapters

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.domain.entity.caminobrillante.LogroCaminoBrillante
import biz.belcorp.consultoras.util.anotation.MedallaCaminoBrilanteType
import biz.belcorp.mobile.components.charts.circle.Frame
import biz.belcorp.mobile.components.charts.circle.Icon
import biz.belcorp.mobile.components.charts.circle.Indicador

import kotlinx.android.synthetic.main.item_periodo.view.*

class RvAdapterPeriodo internal constructor(private val mIndicadores: List<LogroCaminoBrillante.Indicador>?, private val mListener: Listener) : RecyclerView.Adapter<RvAdapterPeriodo.ViewHolder>(), RvAdapterMedallas.Listener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_periodo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mIndicadores?.let {
            val indicador = it[position]

            val ultimaMedallaObtenida = indicador.medallas?.filter { it.isDestacado }?.firstOrNull()
            val medallasNoDestacadas = indicador.medallas?.filter { !it.isDestacado }

            var spanCount = 4

            ultimaMedallaObtenida?.let {
                spanCount = 3

                holder.fmeUltimo.setupUI(
                    Frame.Builder(holder.itemView.context)
                        .setCustomView(getIconMedalla(it, holder.itemView.context))
                        .setCustomViewWidth(88F)
                        .setTitle(it.subtitulo)
                        .setTitleSize(30F)
                        .setTitleColor(R.color.gray_disabled_1)
                        .setTitleMaxLines(1)
                )

                holder.grpUltimaMedalla.visibility = View.VISIBLE
            } ?: run {
                holder.grpUltimaMedalla.visibility = View.GONE
            }

            holder.tvwPeriodo.text = indicador.titulo

            holder.rvwMedallas.layoutManager = GridLayoutManager(holder.itemView.context, spanCount)
            holder.rvwMedallas.adapter = RvAdapterMedallas(medallasNoDestacadas, this)
        }
    }

    override fun getItemCount() = mIndicadores?.size ?: 0

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var rvwMedallas = itemView.rvwMedallas!!
        var grpUltimaMedalla = itemView.grpUltimoObtenido
        var tvwPeriodo = itemView.tvwPeriodo
        var fmeUltimo = itemView.fmeUltimoObtenido
    }

    interface Listener {
        fun onItemMedallaClick(view: View, position: Int, medalla: LogroCaminoBrillante.Indicador.Medalla)
    }

    override fun onItemMedallaClick(view: View, position: Int, medalla: LogroCaminoBrillante.Indicador.Medalla) {
        mListener.onItemMedallaClick(view, position, medalla)
    }

    private fun getIconMedalla(medalla: LogroCaminoBrillante.Indicador.Medalla, context: Context): View? {
        var view: View? = null
        when (medalla.tipo) {
            MedallaCaminoBrilanteType.CIRCULAR -> {
                view = getIndicador(medalla, context)
            }
            MedallaCaminoBrilanteType.PEDIDO -> {
                view = getPedido(context)
            }
        }
        return view
    }

    private fun getIndicador(medalla: LogroCaminoBrillante.Indicador.Medalla, context: Context): View {
        return Indicador.Builder(context)
            .setText(medalla.valor ?: "")
            .setTextSize(50F)
            .setTextColor(R.color.white)
            .setBackground(R.drawable.bg_medalla_active)
            .setSize(88F)
            .build()
    }

    private fun getPedido(context: Context): View {
        return Icon.Builder(context)
            .setSize(88F)
            .setBackground(R.drawable.bg_medalla_active)
            .setIcon(R.drawable.ic_logro_pedido)
            .build()
    }


}
