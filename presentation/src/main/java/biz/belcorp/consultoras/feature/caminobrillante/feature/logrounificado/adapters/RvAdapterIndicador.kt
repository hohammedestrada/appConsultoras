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
import kotlinx.android.synthetic.main.item_indicador.view.*

class RvAdapterIndicador internal constructor(private var mIndicadores: List<LogroCaminoBrillante.Indicador>?, private val listener: Listener) : RecyclerView.Adapter<RvAdapterIndicador.ViewHolder>(), RvAdapterMedallas.Listener, RvAdapterPeriodo.Listener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //val view = LayoutInflater.from(parent.context).inflate(R.layout.item_indicador_linear, parent, false)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_indicador, parent, false)
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

                holder.fmeUltimo.visibility = View.VISIBLE
                holder.tvwUltimo.visibility = View.VISIBLE
            } ?: run {
                holder.fmeUltimo.visibility = View.GONE
                holder.tvwUltimo.visibility = View.GONE
            }

            holder.tvwTitulo.text = indicador.titulo

            holder.rvwMedallas.layoutManager = object : GridLayoutManager(holder.itemView.context, spanCount) {
                override fun checkLayoutParams(lp: RecyclerView.LayoutParams?): Boolean {
                    val result = super.checkLayoutParams(lp)
                    lp.apply { this!!.width = holder.rvwMedallas.width / spanCount }
                    return result
                }
            }

            holder.rvwMedallas.adapter = RvAdapterMedallas(medallasNoDestacadas, this)
        }
    }

    override fun getItemCount() = mIndicadores?.size ?: 0

    override fun onItemMedallaClick(view: View, position: Int, medalla: LogroCaminoBrillante.Indicador.Medalla) {
        listener.onItemMedallaClick(view, position, medalla)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvwTitulo = itemView.tvwTitulo!!
        val rvwMedallas = itemView.rvwMedallas
        val tvwUltimo = itemView.tvwUltimoObtenido
        val fmeUltimo = itemView.fmeUltimoObtenido
    }

    interface Listener {
        fun onItemMedallaClick(view: View, position: Int, medalla: LogroCaminoBrillante.Indicador.Medalla)
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
            .setBackground(R.drawable.bg_indicador)
            .setSize(88F)
            .build()
    }

    private fun getPedido(context: Context): View {
        return Icon.Builder(context)
            .setSize(88F)
            .setBackground(R.drawable.bg_indicador)
            .setIcon(R.drawable.ic_logro_pedido)
            .build()
    }

}
