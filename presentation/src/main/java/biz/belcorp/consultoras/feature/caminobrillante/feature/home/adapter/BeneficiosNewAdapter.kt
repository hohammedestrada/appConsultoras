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

class BeneficiosNewAdapter(data: MutableList<NivelCaminoBrillante.BeneficioCaminoBrillante>) : AbsAdapter<NivelCaminoBrillante.BeneficioCaminoBrillante>(data) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AbsViewHolder<NivelCaminoBrillante.BeneficioCaminoBrillante> {
        return BeneficiosNewViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_list_beneficios, viewGroup, false))
    }

    class BeneficiosNewViewHolder(itemView: View) : AbsViewHolder<NivelCaminoBrillante.BeneficioCaminoBrillante>(itemView) {

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

