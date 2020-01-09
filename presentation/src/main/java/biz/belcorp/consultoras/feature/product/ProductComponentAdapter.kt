package biz.belcorp.consultoras.feature.product

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.model.product.ComponentItem
import kotlinx.android.synthetic.main.item_product_component.view.*

class ProductComponentAdapter constructor(
    private var componentList : List<ComponentItem>) : RecyclerView.Adapter<ProductComponentAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(item: ComponentItem) = with(itemView) {
            txtDescripcion.text ="x${item.cantidad} ${item.nombreProducto}"
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ProductComponentAdapter.ViewHolder {
        return ProductComponentAdapter.ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_product_component, viewGroup, false))
    }
    override fun onBindViewHolder(holder: ProductComponentAdapter.ViewHolder, position: Int) = holder.bind(componentList[position])
    override fun getItemCount(): Int = componentList.size

}
