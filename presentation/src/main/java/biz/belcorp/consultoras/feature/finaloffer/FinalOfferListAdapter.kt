package biz.belcorp.consultoras.feature.finaloffer

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
import biz.belcorp.consultoras.common.model.orders.OfertaFinalModel
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.item_final_offer_list.view.*
import kotlinx.android.synthetic.main.view_product_operator.view.*
import java.text.DecimalFormat
import java.util.ArrayList


class FinalOfferListAdapter
internal constructor(
    private val context: Context,
    private val listener: Listener
) : RecyclerView.Adapter<FinalOfferListAdapter.ViewHolder>() {

    private val mList = ArrayList<OfertaFinalModel>()
    private var moneySymbol: String = ""
    private var decimalFormat: DecimalFormat = DecimalFormat()
    private lateinit var animBounce: Animation


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(item: OfertaFinalModel, position: Int, listener: Listener,
                 decimalFormat: DecimalFormat, moneySymbol: String, animBounce: Animation) = with(itemView) {

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


            tvwBrand.text = item.nombreMarca
            tvwDescription.text = item.nombreComercialCorto
            tvwPrice.text = "$moneySymbol ${decimalFormat.format(item.precioValorizado)}"
            tvwPrice.paintFlags = tvwPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            tvwPricePromo.text = "$moneySymbol ${decimalFormat.format(item.precioCatalogo)}"

            btnAddItem.setOnClickListener {

                if(lltOperators.quantity==0){
                    Toast.makeText(context, context.getString(R.string.add_order_product_quantity_empty), Toast.LENGTH_SHORT).show()
                }else{
                    item.cantidad = lltOperators.quantity
                    listener.onAddItem(item, position)
                }

            }


            if (item.added) {
                tvwAddResponse.visibility = View.VISIBLE
                animBounce?.let { tvwAddResponse.startAnimation(it) }
            }
            else
                tvwAddResponse.visibility = View.INVISIBLE

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
          .inflate(R.layout.item_final_offer_list, viewGroup, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(mList[position], position, listener, decimalFormat, moneySymbol, animBounce)

    override fun getItemCount(): Int = mList.size

    /** */

    fun setList(list: List<OfertaFinalModel?>?) {
        mList.clear()
        list?.filterNotNull()?.let {
            mList.addAll(it)
        }
        notifyDataSetChanged()
    }

    fun updateItemList(item: OfertaFinalModel?, position: Int) {

        item?.let {
            item.cantidad = 1
            item.added = true
            mList.set(position, item)
        }
        notifyItemChanged(position)
    }

    fun setFormat(decimalFormat: DecimalFormat, moneySymbol: String) {
        this.decimalFormat = decimalFormat
        this.moneySymbol = moneySymbol
        notifyDataSetChanged()
    }

    /** */

    interface Listener {
        fun onAddItem(item: OfertaFinalModel, position: Int)
    }
}
