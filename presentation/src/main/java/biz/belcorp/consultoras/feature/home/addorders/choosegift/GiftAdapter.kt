package biz.belcorp.consultoras.feature.home.addorders.choosegift

import android.graphics.Bitmap
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.domain.entity.EstrategiaCarrusel
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.library.util.DeviceUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.item_giftlist.view.*

class GiftAdapter(private var giftlist: List<EstrategiaCarrusel?>,
                  private var listener: GiftListenerProduct,
                  private var flagMensjeBotones: Boolean,
                  private var iso : String?,
                  private var regalo: Boolean) : RecyclerView.Adapter<GiftAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder((LayoutInflater.from(parent.context).inflate(R.layout.item_giftlist, parent, false)))

    override fun getItemCount(): Int {
        return giftlist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(giftlist[position])
        if (flagMensjeBotones) {
            holder.putTagButton()
        }
    }

    fun updateStatusGift(cuv: String) {
        giftlist?.filterNotNull().forEach {
            if (it.cuv == cuv) {
                it.flagSeleccionado = 1
            } else
                it.flagSeleccionado = 0
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(producto: EstrategiaCarrusel?) = with(itemView) {

            producto?.let {

                Glide.with(this).asBitmap().load(it.fotoProductoSmall)
                    .apply(RequestOptions.noTransformation().priority(Priority.HIGH))
                    .listener(object : RequestListener<Bitmap> {
                        override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            return false
                        }

                        override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Bitmap>, isFirstResource: Boolean): Boolean {
                            ivGift.setImageResource(R.drawable.ic_product)
                            return true
                        }
                    })
                    .into(ivGift)

                lnrElegirGift.setOnClickListener { _ ->

                    Tracker.EligeTuRegalo.selectGift(producto.cuv,
                        it.descripcionCUV,
                        it.descripcionMarca,
                        GlobalConstant.VARIANT_ESTANDAR,
                        it.precioValorizado.toString(),
                        it.cantidad.toString(),
                        iso)

                    lnrElegirGift.isFocusable = false
                    lnrElegirGift.isClickable = false

                    flagMensjeBotones = true
                    if(!it.flagPremioDefault){
                        listener.agregarRegalo(it, DeviceUtil.getId(context))
                    }
                    listener.changeMessage(true, flagMensjeBotones)



                    lnrElegirGift.isFocusable = true
                    lnrElegirGift.isClickable = true

                }
                addTagElegido(it.flagSeleccionado)
                checkTextNull(producto?.descripcionMarca, tvwGiftMarca)
                checkTextNull(producto?.descripcionCUV, tvwDescriptionGift)


            }
        }

        private fun checkTextNull(text: String?, textview: AppCompatTextView) {
            textview.text = if (text.isNullOrEmpty()) " " else text
        }

        private fun addTagElegido(estatus: Int?) = with(itemView) {

            if (flagMensjeBotones) { // significa que tiene el regalo elegido en la orden de pedido
                when (estatus) {
                    1 -> {
                        lnrElegirGift.visibility = View.GONE
                        tvwRegaloElegido.visibility = View.VISIBLE
                        var message = if (regalo) {
                            context?.getText(R.string.regaloElegido).toString()
                        } else {
                            context?.getText(R.string.productoElegido).toString()
                        }
                        tvwRegaloElegido.text = context.getText(R.string.check).toString().plus(" ".plus(message))
                        listener.changeMessage(true, flagMensjeBotones)
                        giftBorder.background = resources.getDrawable(R.drawable.border_black)
                    }
                    0 -> {
                        lnrElegirGift.visibility = View.VISIBLE
                        tvwRegaloElegido.visibility = View.GONE
                        listener.changeMessage(false, flagMensjeBotones)
                        giftBorder.setBackgroundResource(0)
                    }
                    else -> {
                        lnrElegirGift.visibility = View.GONE
                        tvwRegaloElegido.visibility = View.GONE
                        giftBorder.setBackgroundResource(0)
                        listener.changeMessage(false, flagMensjeBotones)
                    }
                }
            } else {
                lnrElegirGift.visibility = View.VISIBLE
                tvwRegaloElegido.visibility = View.GONE
                listener.changeMessage(false, flagMensjeBotones)
                giftBorder.setBackgroundResource(0)

            }

        }

        fun putTagButton() = with(itemView) {
            giftlist?.filterNotNull().forEach { it1 ->
                if (it1.flagSeleccionado == 1) {
                    giftlist?.filterNotNull().forEach { it2 ->
                        if (it2.flagSeleccionado == 0) {
                            txtBtnGift.text = resources.getText(R.string.cambiar).toString()
                        }
                    }
                }
            }
        }
    }

    interface GiftListenerProduct {
        fun agregarRegalo(regalo: EstrategiaCarrusel, identificador: String)
        fun changeMessage(esEscogido: Boolean, flagPulsaste: Boolean)
    }
}
