package biz.belcorp.consultoras.feature.caminobrillante.feature.ofertasepeciales.kit

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.domain.entity.caminobrillante.KitCaminoBrillante
import biz.belcorp.consultoras.util.anotation.ConsultoraNivelCode.*
import biz.belcorp.mobile.components.core.adapters.AbsAdapter
import biz.belcorp.mobile.components.design.counter.Counter
import biz.belcorp.mobile.components.offers.Multi

class KitAdapter(data: MutableList<KitCaminoBrillante>?) : AbsAdapter<KitCaminoBrillante>(data) {

    var listener: Listener? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AbsViewHolder<KitCaminoBrillante> {
        return KitViewHolder(LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_oferta_camino_brillante, viewGroup, false), listener)
    }

    class KitViewHolder(itemView: View, var listener: Listener?) : AbsViewHolder<KitCaminoBrillante>(itemView) {
        private var multi: Multi = itemView.findViewById(R.id.oferta)

        override fun bind(itemView: View, item: KitCaminoBrillante, position: Int) {
            multi.setValues(
                item.descripcionMarca ?: "",
                item.descripcionCortaCUV ?: item.descripcionCUV ?: "",
                KitFragment.formatWithMoneySymbol(item.precioCatalogo ?: 0.toDouble()),
                KitFragment.formatWithMoneySymbol(item.ganancia ?: 0.toDouble()),
                item.fotoProductoSmall ?: "")

            multi.setTitleLabelYou(itemView.context.getString(R.string.camino_brillante_price_for_you))
            multi.setTitleLabelClient(itemView.context.getString(R.string.camino_brillante_earn_up))
            multi.setVisibilityCounter(View.GONE)
            multi.setTacharLabelYou(false)
            multi.setEnabledMulti(item.flagHabilitado ?: false, true)
            multi.setImageTag(getImageNivel(item.codigoNivel, item.flagHabilitado ?: false))

            setUpListener(multi, item, position)
        }

        private fun getImageNivel(codigoNivel: String?, isHabilitado: Boolean): Int =
            when (codigoNivel) {
                NIVEL_2 -> if (isHabilitado) R.drawable.tag_coral_enabled else R.drawable.tag_coral_disabled
                NIVEL_3 -> if (isHabilitado) R.drawable.tag_ambar_enabled else R.drawable.tag_ambar_disabled
                NIVEL_4 -> if (isHabilitado) R.drawable.tag_perla_enabled else R.drawable.tag_perla_disabled
                NIVEL_5 -> if (isHabilitado) R.drawable.tag_topacio_enabled else R.drawable.tag_topacio_disabled
                else -> if (isHabilitado) R.drawable.tag_brillante_enabled else R.drawable.tag_brillante_disabled
            }

        private fun setUpListener(multi: Multi, kit: KitCaminoBrillante, position: Int) {
            multi.multiButtonListener = object : Multi.MultiButtonListener {
                override fun onClickAdd(quantity: Int, counterView: Counter) {
                    listener?.onClickAdd(kit, quantity)
                }
                override fun onClickSelection() {
                    Log.d("KitAdapter", "onClickSelection")
                }
                override fun onClickShowOffer() {
                    Log.d("KitAdapter", "onClickShowOffer")
                }
            }

            multi.multiContainerListener = object : Multi.ContainerClickListener {
                override fun onClick() {
                    listener?.onClickItem(kit, position)
                }
            }
        }
    }

    interface Listener {
        fun onClickAdd(item: KitCaminoBrillante, quantity: Int)
        fun onClickItem(kit: KitCaminoBrillante, position: Int)
    }

}
