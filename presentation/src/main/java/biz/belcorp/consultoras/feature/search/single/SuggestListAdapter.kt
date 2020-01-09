package biz.belcorp.consultoras.feature.search.single

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.domain.entity.ProductCUV
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.item_suggest_list.view.*
import java.text.DecimalFormat
import java.util.ArrayList


class SuggestListAdapter
internal constructor(
    private val context: Context,
    private val listener: Listener
) : RecyclerView.Adapter<SuggestListAdapter.ViewHolder>() {

    private var type = SearchProductFragment.TYPE_SUGGEST_PRODUCT
    private val mList = ArrayList<ProductCUV>()
    private var moneySymbol: String = ""
    private var decimalFormat: DecimalFormat = DecimalFormat()
    private lateinit var animBounce: Animation


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(item: ProductCUV, position: Int, listener: Listener, decimalFormat: DecimalFormat, moneySymbol: String, animBounce: Animation, type: Int) = with(itemView) {

            if (item.fotoProductoSmall != null && !item.fotoProductoSmall!!.isEmpty()) {
                Glide.with(this).asBitmap().load(item.fotoProductoSmall)
                    .apply(RequestOptions.noTransformation().priority(Priority.HIGH))
                    .listener(object : RequestListener<Bitmap> {
                        override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            return false
                        }

                        override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Bitmap>, isFirstResource: Boolean): Boolean {
                            ivwProduct.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_product))
                            return true
                        }
                    })
                    .into(ivwProduct)
            }

            item.cantidad?.let {
                edtQuantity.setText(item.cantidad.toString())
            } ?: kotlin.run {
                edtQuantity.setText("1")
            }

            tvwBrand.text = item.descripcionMarca
            tvwDescription.text = item.description
            tvwPrice.text = "$moneySymbol ${decimalFormat.format(item.precioCatalogo)}"

            item.precioValorizado?.let { valorizado ->
                item.precioCatalogo?.let {precio ->
                    if(precio < valorizado){
                        tvwPriceValorizado.text = "$moneySymbol ${decimalFormat.format(valorizado)}"
                        tvwPriceValorizado.paintFlags = tvwPriceValorizado.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        tvwPriceValorizado.visibility = View.VISIBLE
                    }
                }
            }

            item.descripcionEstrategia?.let {
                tvWEstrategia.text = it
                tvWEstrategia.visibility = View.VISIBLE
            }

            if(item.isAdded == true){
                lnlAdded.visibility = View.VISIBLE
            }


            btnAddItem.setOnClickListener {
                if (edtQuantity.text.toString().isEmpty() || edtQuantity.text.toString() == "0") {
                    Toast.makeText(context, context.getString(R.string.add_order_product_quantity_empty), Toast.LENGTH_SHORT).show()
                } else {
                    item.cantidad = Integer.parseInt(edtQuantity.text.toString())
                    listener.onAddItem(type, item)
                }
            }

            btnRemoveOne.setOnClickListener {

                if (edtQuantity.text.isEmpty()) edtQuantity.setText("1")

                val quantity = restarQuantity(Integer.parseInt(edtQuantity.text.toString()))
                edtQuantity.setText(quantity.toString())
                item.cantidad = quantity
            }

            btnAddOne.setOnClickListener {

                if (edtQuantity.text.isEmpty()) edtQuantity.setText("1")

                val quantity = sumarQuantity(Integer.parseInt(edtQuantity.text.toString()))
                edtQuantity.setText(quantity.toString())
                item.cantidad = quantity
            }

        }

        private fun restarQuantity(quantity: Int): Int {
            return if ((quantity - 1) <= 0) 1
            else quantity - 1
        }

        private fun sumarQuantity(quantity: Int): Int {
            return if (quantity >= 99) {
                quantity
            } else {
                quantity + 1
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        animBounce = AnimationUtils.loadAnimation(context, R.anim.bounce_in)
        return ViewHolder(LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_suggest_list, viewGroup, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(mList[position], position, listener, decimalFormat, moneySymbol, animBounce, type)

    override fun getItemCount(): Int = mList.size

    fun setList(type:Int, list: Collection<ProductCUV?>?) {
        this.type = type
        mList.clear()
        list?.filterNotNull()?.let {
            mList.addAll(it)
        }
        notifyDataSetChanged()
    }

    fun updateItemList(item: ProductCUV?, position: Int) {

        item?.let {
            item.cantidad = 1
            mList.set(position, item)
        }
        notifyItemChanged(position)
    }

    fun setFormat(decimalFormat: DecimalFormat, moneySymbol: String) {
        this.decimalFormat = decimalFormat
        this.moneySymbol = moneySymbol
        notifyDataSetChanged()
    }

    fun setAdded(cuv:String){
        mList.find { it.cuv == cuv }?.isAdded = true
        notifyDataSetChanged()
    }

    interface Listener {
        fun onAddItem(type: Int, productCUV: ProductCUV)
    }
}
