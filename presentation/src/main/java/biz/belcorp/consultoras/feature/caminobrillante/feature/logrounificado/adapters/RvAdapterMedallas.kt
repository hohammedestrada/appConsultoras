package biz.belcorp.consultoras.feature.caminobrillante.feature.logrounificado.adapters

import android.content.Context
import android.os.Build
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
import kotlinx.android.synthetic.main.item_medalla.view.*

class RvAdapterMedallas internal constructor(private val mMedallas: List<LogroCaminoBrillante.Indicador.Medalla>?, private val mListener: Listener) : RecyclerView.Adapter<RvAdapterMedallas.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_medalla, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mMedallas?.let {
            val medalla = it[position]

            holder.medalla.setupUI(
                Frame.Builder(holder.itemView.context)
                    .setCustomView(getIconMedalla(medalla, holder.itemView.context))
                    .setCustomViewWidth(52F)
                    .setSubtitle(medalla.subtitulo)
                    .setSubtitleUnderline(!medalla.isEstado && !medalla.modalTitulo.isNullOrEmpty() && !medalla.modalDescripcion.isNullOrEmpty())
                    .setSubtitleSize(holder.itemView.resources.getDimension(R.dimen.textsize_title_frame))
                    .setSubtitleColor(R.color.gray_disabled_1)
            )

            if (!medalla.isEstado && !medalla.modalTitulo.isNullOrEmpty() && !medalla.modalDescripcion.isNullOrEmpty()) {
                holder.itemView.setOnClickListener { v -> mListener.onItemMedallaClick(v, position, medalla) }
            } else {
                holder.itemView.setOnClickListener(null)
            }

        }
    }

    override fun getItemCount() = mMedallas?.size ?: 0

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var medalla = itemView.frame!!
    }

    private fun getIconMedalla(medalla: LogroCaminoBrillante.Indicador.Medalla, context: Context): View? {
        var view: View? = null
        when (medalla.tipo) {
            MedallaCaminoBrilanteType.CIRCULAR -> {
                view = getIndicador(medalla, context)
            }
            MedallaCaminoBrilanteType.PEDIDO -> {
                view = getPedido(medalla, context)
            }
        }
        return view
    }

    private fun getIndicador(medalla: LogroCaminoBrillante.Indicador.Medalla, context: Context): View {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            return Indicador.Builder(context)
                .setText(medalla.valor ?: "")
                .setTextSize(context.resources.getDimension(R.dimen._13sp))
                .setTextColor(if (medalla.isEstado) R.color.white else R.color.gray_disabled_1)
                .setBackground(if (medalla.isEstado) R.drawable.bg_medalla_active else R.drawable.ic_sticker)
                .setSize(52F)
                .build()
        } else {
            return Indicador.Builder(context)
                .setText(medalla.valor ?: "")
                .setTextSize(context.resources.getDimension(R.dimen._13sp))
                .setTextColor(if (medalla.isEstado) R.color.white else R.color.gray_disabled_1)
                .setBackground(if (medalla.isEstado) R.drawable.bg_medalla_active else R.drawable.sticker_camino)
                .setSize(52F)
                .build()
        }

    }

    private fun getPedido(medalla: LogroCaminoBrillante.Indicador.Medalla, context: Context): View {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            val backgroundMedalla = when {
                !medalla.isEstado && medalla.modalTitulo.isNullOrEmpty() && medalla.modalDescripcion.isNullOrEmpty() -> R.drawable.bg_medalla_inactive

                !medalla.isEstado -> R.drawable.ic_sticker
                else -> R.drawable.bg_medalla_active
            }
            return Icon.Builder(context)
                .setSize(52F)
                .setBackground(backgroundMedalla)
                .setIcon(if (medalla.isEstado) R.drawable.ic_logro_pedido else R.drawable.ic_logro_pedido_disabled)
                .build()
        }
        else {
            val backgroundMedalla = when {
                !medalla.isEstado && medalla.modalTitulo.isNullOrEmpty() && medalla.modalDescripcion.isNullOrEmpty() -> R.drawable.bg_medalla_inactive
                !medalla.isEstado -> R.drawable.sticker_camino
                else -> R.drawable.bg_medalla_active
            }
            return Icon.Builder(context)
                .setSize(52F)
                .setBackground(backgroundMedalla)
                .setIcon(if (medalla.isEstado) R.drawable.ic_logro_pedido else R.drawable.ic_logro_pedido_disabled)
                .build()
        }


    }


    interface Listener {
        fun onItemMedallaClick(view: View, position: Int, medalla: LogroCaminoBrillante.Indicador.Medalla)
    }

}
