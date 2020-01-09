package biz.belcorp.consultoras.feature.makeup

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.data.entity.ProductoMasivo
import biz.belcorp.consultoras.util.anotation.ResponseResultCode
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.item_summary.view.*
import java.text.DecimalFormat
import java.util.*
import javax.sql.DataSource


/**
 * Created by Leonardo on 13/09/2018.
 */

class SummaryAdapter: RecyclerView.Adapter<SummaryAdapter.ViewHolder>() {

    private val summaryList = ArrayList<ProductoMasivo>()
    private var moneySymbol : String? = null
    private var decimalFormat: DecimalFormat? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(item: ProductoMasivo, moneySymbol: String?, decimalFormat: DecimalFormat?) = with(itemView) {

            item.urlImagen?.let {
                Glide.with(this).asBitmap().load(it)
                    .apply(RequestOptions.noTransformation().priority(Priority.HIGH))
                    .listener(object : RequestListener<Bitmap> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                            img_product.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_product))
                            return false
                        }

                        override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: com.bumptech.glide.load.DataSource?, isFirstResource: Boolean): Boolean {
                            img_product.setImageBitmap(resource)
                            return true
                        }

                    })
                    .into(img_product)
            }

            txt_descripcion.text = item.descripcion
            txt_cuv.text = item.cuv
            txt_catalogo.text = item.marcaDescripcion

            item.tono?.let {
                if(it.isNotEmpty()){
                    txt_tono.visibility = View.VISIBLE
                    txt_tono.text = context.getString(R.string.tono_label) + it
                }else{
                    txt_tono.visibility= View.GONE
                }
            }

            txt_price_before.text = moneySymbol + "  " + decimalFormat?.format(item.precioCatalogo)
            if (item.codigoRespuesta == ResponseResultCode.OK) {
                txt_error.visibility = View.GONE
            } else {
                txt_error.text = item.mensajeRespuesta
            }


        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): SummaryAdapter.ViewHolder {
        return SummaryAdapter.ViewHolder(LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_summary, viewGroup, false))
    }

    override fun onBindViewHolder(holder: SummaryAdapter.ViewHolder, position: Int) = holder.bind(summaryList[position], moneySymbol, decimalFormat)
    override fun getItemCount(): Int = summaryList.size

    // Public Functions
    fun setList(list: List<ProductoMasivo?>?) {
        list?.filterNotNull()?.let { summaryList.addAll(it) }
        notifyDataSetChanged()
    }

    fun setData(moneySymbol: String?, decimalFormat: DecimalFormat?) {
        this.moneySymbol = moneySymbol
        this.decimalFormat = decimalFormat
    }

    fun clearData(){
        summaryList.clear()
        notifyDataSetChanged()
    }
}
