package biz.belcorp.consultoras.feature.caminobrillante.feature.ofertasepeciales.demostrador

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.domain.entity.caminobrillante.DemostradorCaminoBrillante
import biz.belcorp.mobile.components.core.adapters.AbsAdapter
import biz.belcorp.mobile.components.design.counter.Counter
import biz.belcorp.mobile.components.offers.Multi

class DemostradorAdapter(data: MutableList<DemostradorCaminoBrillante>?) : AbsAdapter<DemostradorCaminoBrillante>(data) {

    var listener: Listener? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AbsViewHolder<DemostradorCaminoBrillante> {
        return DemostradorViewHolder(LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_oferta_camino_brillante, viewGroup, false), listener)
    }

    class DemostradorViewHolder(itemView: View, val listener: Listener?) : AbsViewHolder<DemostradorCaminoBrillante>(itemView) {
        private var multi: Multi = itemView.findViewById(R.id.oferta)

        override fun bind(itemView: View, item: DemostradorCaminoBrillante, position: Int) {
            multi.setValues(
                item.descripcionMarca ?: "",
                item.descripcionCortaCUV ?: item.descripcionCUV ?: "",
                DemostradorFragment.formatWithMoneySymbol(item.precioValorizado ?: 0.toDouble()),
                DemostradorFragment.formatWithMoneySymbol(item.precioCatalogo ?: 0.toDouble()),
                item.fotoProductoSmall ?: "")

            multi.setTitleLabelYou(itemView.context.getString(R.string.camino_brillante_price))
            multi.setTitleLabelClient(itemView.context.getString(R.string.camino_brillante_price_for_you))
            multi.setVisibilityCounter(View.VISIBLE)
            multi.setTacharLabelYou(true)

            setUpListener(multi, item, position)
        }

        private fun setUpListener(multi: Multi, demostrador: DemostradorCaminoBrillante, position: Int) {
            multi.multiButtonListener = object : Multi.MultiButtonListener {
                override fun onClickAdd(quantity: Int, counterView: Counter) {
                    listener?.onClickAdd(counterView, demostrador, quantity, position)
                }

                override fun onClickSelection() {
                    Log.d("DemostradorAdapter", "onClickSelection")
                }

                override fun onClickShowOffer() {
                    Log.d("DemostradorAdapter", "onClickShowOffer")
                }
            }

            multi.multiContainerListener = object : Multi.ContainerClickListener {
                override fun onClick() {
                    listener?.onClickItem(demostrador, position)
                }
            }
        }
    }

    interface Listener {
        fun onClickAdd(counterView: Counter, demostrador: DemostradorCaminoBrillante, quantity: Int, position: Int)
        fun onClickItem(demostrador: DemostradorCaminoBrillante, position: Int)
    }

}
