package biz.belcorp.consultoras.feature.caminobrillante.feature.logrounificado.adapters

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.domain.entity.caminobrillante.LogroCaminoBrillante
import biz.belcorp.consultoras.util.anotation.MedallaCaminoBrilanteType
import biz.belcorp.mobile.components.charts.circle.Frame
import biz.belcorp.mobile.components.charts.circle.Icon
import biz.belcorp.mobile.components.charts.circle.Indicador
import kotlinx.android.synthetic.main.item_logro.view.*

class RvAdapterLogro internal constructor(private val listener: Listener) : RecyclerView.Adapter<RvAdapterLogro.ViewHolder>(), RvAdapterMedallas.Listener, RvAdapterPeriodo.Listener {

    private var mLogros: List<LogroCaminoBrillante>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_logro, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mLogros?.let {

            val logro = it[position]

            when (logro.id) {
                TYPE_CRECIMIENTO -> {
                    holder.ctlPeriodo.visibility = View.GONE
                    holder.ctlIndicador.visibility = View.VISIBLE

                    logro.indicadores?.let {
                        if (it.size > 0) {
                            val indicador = it[0]

                            val ultimaMedallaObtenida = indicador.medallas?.filter { it.isDestacado }?.firstOrNull()
                            val medallasNoDestacadas = indicador.medallas?.filter { !it.isDestacado }

                            var spanCount = 4

                            ultimaMedallaObtenida?.let {
                                spanCount = 3

                                holder.fmeUltimoIndicador.setupUI(
                                    Frame.Builder(holder.itemView.context)
                                        .setCustomView(getIconMedalla(it, holder.itemView.context))
                                        .setCustomViewWidth(88F)
                                        .setTitle(it.subtitulo)
                                        .setTitleSize(30F)
                                        .setTitleColor(R.color.gray_disabled_1)
                                        .setTitleMaxLines(1)
                                )

                                holder.grpUltimoObtenido.visibility = View.VISIBLE
                            } ?: run {
                                holder.grpUltimoObtenido.visibility = View.GONE
                            }

                            holder.tvwTituloIndicador.text = indicador.titulo

                            holder.rvwMedallasIndicador.layoutManager = GridLayoutManager(holder.itemView.context, spanCount)
                            holder.rvwMedallasIndicador.adapter = RvAdapterMedallas(medallasNoDestacadas, this)
                        }
                    }
                }
                else -> {
                    holder.ctlIndicador.visibility = View.GONE
                    holder.ctlPeriodo.visibility = View.VISIBLE

                    val size = logro.indicadores?.size ?: 0

                    holder.tvwTituloPeriodo.text = logro.titulo
                    holder.tvwTituloPeriodo.visibility = View.VISIBLE

                    holder.ivwBack.visibility = if (size > 0) View.VISIBLE else View.GONE
                    holder.ivwNext.visibility = View.GONE

                    val pagerSnapHelper = PagerSnapHelper()
                    pagerSnapHelper.attachToRecyclerView(holder.rvwPeriodos)

                    holder.rvwPeriodos.layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
                    holder.rvwPeriodos.adapter = RvAdapterPeriodo(logro.indicadores?.reversed(), this)
                    holder.rvwPeriodos.scrollToPosition(if (size - 1 == -1) 0 else size - 1)

                    holder.rvwPeriodos.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)

                            val cantidadPeriodos = logro.indicadores?.size ?: 0
                            val currentPosition = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                            listener.onScrollPeriodo(cantidadPeriodos, currentPosition, holder.ivwBack, holder.ivwNext)
                        }
                    })

                    holder.ivwNext.setOnClickListener { v ->
                        val currentPosition = (holder.rvwPeriodos.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                        listener.onItemClickNext(v, currentPosition, holder.rvwPeriodos)
                    }

                    holder.ivwBack.setOnClickListener { v ->
                        val currentPosition = (holder.rvwPeriodos.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                        listener.onItemClickBefore(v, currentPosition, holder.rvwPeriodos)
                    }

                }
            }
        }
    }

    override fun getItemCount() = mLogros?.size ?: 0

    fun updateData(logros: List<LogroCaminoBrillante>) {
        this.mLogros = logros
        notifyDataSetChanged()
    }

    override fun onItemMedallaClick(view: View, position: Int, medalla: LogroCaminoBrillante.Indicador.Medalla) {
        listener.onItemMedallaClick(view, position, medalla)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvwTituloPeriodo = itemView.tvwTituloPeriodo!!
        val rvwPeriodos = itemView.rvwPeriodos!!
        val ivwBack = itemView.ivwBack!!
        val ivwNext = itemView.ivwNext!!
        val ctlPeriodo = itemView.ctlPeriodo!!

        val tvwTituloIndicador = itemView.tvwTituloIndicador!!
        val rvwMedallasIndicador = itemView.rvwMedallasIndicador!!
        val grpUltimoObtenido = itemView.grpUltimoObtenidoIndicador!!
        val fmeUltimoIndicador = itemView.fmeUltimoObtenidoIndicador!!
        val ctlIndicador = itemView.ctlIndicador!!
    }

    interface Listener {
        fun onItemMedallaClick(view: View, position: Int, medalla: LogroCaminoBrillante.Indicador.Medalla)

        fun onItemClickBefore(view: View, position: Int, rvwLogro: RecyclerView)

        fun onItemClickNext(view: View, position: Int, rvwLogro: RecyclerView)

        fun onScrollPeriodo(cantidadPeriodos: Int, posicion: Int, ivwBack: ImageView, ivwNext: ImageView)
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

    companion object {

        private const val TYPE_CRECIMIENTO = "CRECIMIENTO"
        private const val TYPE_CONSTANCIA_DETALLADA = "CONSTANCIA_DETALLADA"

    }

}
