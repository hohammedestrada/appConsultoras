package biz.belcorp.consultoras.feature.caminobrillante.feature.home.adapter

import android.graphics.Typeface
import android.support.v7.widget.AppCompatImageView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.domain.entity.caminobrillante.NivelCaminoBrillante
import biz.belcorp.consultoras.util.anotation.BeneficioType
import biz.belcorp.mobile.components.core.adapters.AbsAdapter

class BeneficiosAdapter(private var dataList: MutableList<NivelCaminoBrillante.BeneficioCaminoBrillante>, private var collapseCount: Int) : AbsAdapter<NivelCaminoBrillante.BeneficioCaminoBrillante>(dataList) {

    var explaList: Boolean = false

    override fun getItemCount(): Int {
        if (dataList == null) {
            return 0
        } else if (explaList) {
            return dataList.size
        }
        return Math.min(collapseCount, dataList.size)
    }

    override fun getItemViewType(position: Int): Int {
        if (explaList && position == collapseCount) return 1
        if (dataList.size < position) return 1
        return 2
    }

    override fun onBindViewHolder(holder: AbsViewHolder<NivelCaminoBrillante.BeneficioCaminoBrillante>, position: Int) {
        if (holder is BeneficioViewHolder) {
            holder.bind(holder.itemView, dataList[position], position)
            return
        }

    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BeneficioViewHolder {
        return BeneficioViewHolder(LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_list_beneficios, viewGroup, false))

    }

    class BeneficioViewHolder(itemView: View) : AbsViewHolder<NivelCaminoBrillante.BeneficioCaminoBrillante>(itemView) {

        private var imageBenefit: AppCompatImageView = itemView.findViewById(R.id.imageBenefit)
        private var titleBenefit: TextView = itemView.findViewById(R.id.titleBenefit)
        private var descriptionBenefit: TextView = itemView.findViewById(R.id.descriptionBenefit)

        override fun bind(itemView: View, item: NivelCaminoBrillante.BeneficioCaminoBrillante, position: Int) {

            imageBenefit.setImageResource(
                when (item.urlIcono) {
                    BeneficioType.BENEFICIO_1 -> R.drawable.ic_laptop_beneficio
                    BeneficioType.BENEFICIO_2 -> R.drawable.ic_beneficios_beneficios
                    BeneficioType.BENEFICIO_3 -> R.drawable.ic_brazalete
                    BeneficioType.BENEFICIO_4 -> R.drawable.ic_chat_beneficio
                    BeneficioType.BENEFICIO_5 -> R.drawable.ic_catalogo_beneficio
                    BeneficioType.BENEFICIO_6 -> R.drawable.ic_descuento_beneficio
                    BeneficioType.BENEFICIO_7 -> R.drawable.ic_kit_beneficios
                    BeneficioType.BENEFICIO_8 -> R.drawable.ic_pago_diferido
                    BeneficioType.BENEFICIO_9 -> R.drawable.ic_productos_beneficio
                    BeneficioType.BENEFICIO_10 -> R.drawable.ic_programa_brillante
                    BeneficioType.BENEFICIO_11 -> R.drawable.ic_reconocimiento
                    BeneficioType.BENEFICIO_12 -> R.drawable.ic_regalo_beneficio
                    BeneficioType.BENEFICIO_13 -> R.drawable.ic_talleres
                    BeneficioType.BENEFICIO_14 -> R.drawable.ic_flete_descuento
                    else -> R.drawable.ic_catalogo_beneficio
                })

            titleBenefit.text = item.nombreBeneficio

            if (item.descripcion == null || item.descripcion!!.isEmpty()) {
                descriptionBenefit.visibility = View.GONE
                titleBenefit.setTypeface(null, Typeface.NORMAL)
            } else {
                descriptionBenefit.visibility = View.VISIBLE
                descriptionBenefit.text = item.descripcion
                titleBenefit.setTypeface(null, Typeface.BOLD)
            }
        }
    }

}
