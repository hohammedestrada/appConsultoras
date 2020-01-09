package biz.belcorp.consultoras.feature.search.results

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.domain.entity.ProductCUV
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.item_search_results.view.*
import kotlinx.android.synthetic.main.view_product_opcion.view.*
import kotlinx.android.synthetic.main.view_product_operator.view.*
import kotlinx.android.synthetic.main.view_product_operator_disabled.view.*
import java.text.DecimalFormat


/**
 * Created by Leonardo on 13/09/2018.
 */

class SearchResultsAdapter
constructor(private val listener: SearchListener) : RecyclerView.Adapter<SearchResultsAdapter.ViewHolder>() {

    private var productList = mutableListOf<(ProductCUV)>()
    private var moneySymbol : String = ""
    private var decimalFormat: DecimalFormat = DecimalFormat()
    private var maxNameShow = DEFAULT_CARACTERES_MOSTRAR

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(item: ProductCUV, moneySymbol: String, maxNameShow: Int, decimalFormat: DecimalFormat, listener: SearchListener) = with(itemView) {

            item.fotoProductoSmall?.let {
                Glide.with(this).asBitmap().load(it)
                    .apply(RequestOptions.noTransformation().priority(Priority.HIGH))
                    .listener(object : RequestListener<Bitmap> {
                        override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            return false
                        }

                        override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Bitmap>, isFirstResource: Boolean): Boolean {
                            img_product.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_container_placeholder))
                            return true
                        }
                    })
                    .into(img_product)
            }

            //txtElige.textSize = 12f
            txt_precio.text = "$moneySymbol ${decimalFormat.format(item.precioValorizado)}"
            txt_precio.paintFlags = txt_precio.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            txt_precio_oferta.text = "$moneySymbol ${decimalFormat.format(item.precioCatalogo)}"
            txt_catalogo.text = item.descripcionEstrategia


            item.description?.let {
                txt_descripcion.text = it.trim()
            }

            btnGoItem.visibility = View.GONE

            if (item.stock == true){

                // Oferta digital y de tipo estrategia compuesta variable (Elige tu opcion)
                if(isOfertaDigital(item.codigoTipoEstrategia) && item.codigoEstrategia == COD_EST_COMPUESTA_VARIABLE){


                    lnl_panel.visibility = View.VISIBLE
                    lnl_panel_disabled.visibility = View.GONE
                    lltOperators.visibility = View.INVISIBLE
                    btnAddItem.visibility = View.GONE
                    lnl_panel_opcion.visibility = View.VISIBLE


                }
                // Estrategia individual o compuesta fija (Agregalo)
                else {
                    lnl_panel.visibility = View.VISIBLE
                    lnl_panel_disabled.visibility = View.GONE
                    lltOperators.visibility = View.VISIBLE
                    lnl_panel_opcion.visibility = View.GONE
                    btnAddItem.visibility = View.VISIBLE
                }

            } else{

                lnl_panel_opcion.visibility = View.GONE
                lnl_panel_disabled.visibility = View.VISIBLE
                lnl_panel.visibility = View.GONE
                lltOperatorsDis.isEnable(false)
            }

            if(isOfertaDigital(item.codigoTipoEstrategia)){
                lnlFicha.setOnClickListener {
                    listener.onShowProduct(item)
                }
            }else{
                lnlFicha.setOnClickListener(null)
            }

            if(item.agregado == true){
                txt_agregado.text = context.resources.getString(R.string.search_added)
            } else{
                txt_agregado.text = ""
            }

            btnAddItem.setOnClickListener{
                item.cantidad = lltOperators.quantity
                listener.onAddProduct(item)
            }

            btnOpcion.setOnClickListener {
                item.cantidad = lltOperators.quantity
                listener.onShowProduct(item)
            }

        }

        // Private Functions
        private fun isOfertaDigital(codigoTipoEstrategia: String?): Boolean{
            return (codigoTipoEstrategia in listOf("030","005","001","007","008","009","010","011", "LMG"))
        }

        private fun getQuantity(textView: TextView): Int{
            val quantity = textView.text.toString()
            return if(quantity.isEmpty() || quantity == "") 0
            else Integer.parseInt(quantity)
        }

        private fun restarQuantity(quantity: Int): Int {
            return if ((quantity - 1) <= 0) 1 else { quantity - 1 }
        }

        private fun sumarQuantity(quantity: Int): Int {
            return if (quantity == 99) { quantity } else { quantity + 1 }
        }

        /*private fun setFullSpan(itemView: View, state: Boolean){
            val layoutParams = itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
            layoutParams.isFullSpan = state
        }*/

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): SearchResultsAdapter.ViewHolder {
        return SearchResultsAdapter.ViewHolder(LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_search_results, viewGroup, false))
    }

    override fun onBindViewHolder(holder: SearchResultsAdapter.ViewHolder, position: Int) = holder.bind(productList[position], moneySymbol, maxNameShow, decimalFormat, listener)
    override fun getItemCount(): Int = productList.size

    // Interface
    interface SearchListener {
        fun onAddProduct(item: ProductCUV)
        fun onShowProduct(item: ProductCUV)
    }

    // Public Functions
    fun setList(list: List<ProductCUV?>?) {
        list?.filterNotNull()?.let { productList.addAll(it) }
        notifyDataSetChanged()
    }

    fun setData(moneySymbol: String, maxNameShow: Int, decimalFormat: DecimalFormat) {
        this.moneySymbol = moneySymbol
        this.maxNameShow = maxNameShow
        this.decimalFormat = decimalFormat
    }

    fun clearData(){
        productList.clear()
        notifyDataSetChanged()
    }

    fun setProductAdded(cuv:String){
        productList.first { it.cuv.equals(cuv) }.apply { agregado = true; cantidad = 1 }
        notifyDataSetChanged()
    }

    companion object {
        const val DEFAULT_CARACTERES_MOSTRAR = 25
        const val COD_EST_COMPUESTA_VARIABLE = 2003
    }

}
