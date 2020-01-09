package biz.belcorp.consultoras.feature.payment.online.metodopago

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.model.pagoonline.BancosConfig
import biz.belcorp.consultoras.common.model.pagoonline.PagoOnlineConfigModel
import biz.belcorp.consultoras.util.GlobalConstant
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.item_metodo_pago.view.*
import kotlinx.android.synthetic.main.item_pago_online.view.*

class OptionsPayListAdapter(private var opcionesPago: List<BancosConfig.OpcionPago>,
                            private var listaBancoOnline: List<PagoOnlineConfigModel.Banco>?,
                            private var listener: OnCardItemSelected)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun getItemViewType(position: Int): Int {
        return if (!opcionesPago[position].codigo.equals(GlobalConstant.CODIGO_MEDIO_PAGO_INTENRNET)) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0){
            OptionsPayViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_metodo_pago, parent, false))
        }
        else {
            BancosOnlineViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_pago_online, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return if (opcionesPago.isEmpty()) 0 else opcionesPago.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == 0) {
            var hold: OptionsPayViewHolder = holder as OptionsPayViewHolder
            hold.bind(opcionesPago[position],listener)

        } else {
            var hold: BancosOnlineViewHolder = holder as BancosOnlineViewHolder
            hold.bind(opcionesPago[position], listaBancoOnline!!/*,listener*/)
        }
    }

    open class OptionsPayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        open fun bind(opcion: BancosConfig.OpcionPago, list: OnCardItemSelected) = with(itemView) {
            opcion.rutaIcono.let {
                opcion.description.let { des ->
                    var drawItem = DrawItem(it!!, des!!)
                    drawItem.bind(iconmediopago, tvwmedio, this)
                }
            }
            lnr_itm_pago.setOnClickListener {
                list.cardSelected(opcion.rutaIcono!!, opcion.medioPagoDetalleId, opcion.description)
            }
        }
    }

    inner class BancosOnlineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), BancosAdapter.TrackBankSelectedListener {
        fun bind(opciones: BancosConfig.OpcionPago, bancoOnline: List<PagoOnlineConfigModel.Banco>/*,list: OnCardItemSelected*/)/* = with(itemView)*/ {
            var adapeterBanco = BancosAdapter(bancoOnline,this)
            opciones.rutaIcono.let {
                opciones.description.let { des ->
                    var drawItem = DrawItem(it!!, des!!)
                    drawItem.bind(itemView.iconmediopagopbo, itemView.tvwmediopbo, itemView)
                }
            }
            itemView.rvwgbanco.adapter = adapeterBanco
            itemView.lnritmpagopbo.setOnClickListener {
                if(itemView.lnrbancos.visibility == View.VISIBLE){
                    itemView.imgarrow.animate().rotation(0f)
                    itemView.lnrbancos.visibility= View.GONE
                }
                else{
                    itemView.imgarrow.animate().rotation(90f)
                    itemView.lnrbancos.visibility= View.VISIBLE
                }
            }
        }

        override fun onBankTrack(nombreBanco: String) {
            listener.bankSelectedTrack(nombreBanco)
        }
    }

    class DrawItem(private var url: String, private var description: String) {
        fun bind(imagen: ImageView, text: TextView, itemViewHolder: View) {
            url.let {
                Glide.with(itemViewHolder).asBitmap().load(it)
                    .apply(RequestOptions.noTransformation().priority(Priority.HIGH))
                    .listener(object : RequestListener<Bitmap> {
                        override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            return false
                        }
                        override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Bitmap>, isFirstResource: Boolean): Boolean {
                            imagen.setImageResource(R.drawable.ic_bancainternet)
                            return true
                        }
                    })
                    .into(imagen)
            }
            description.let {
                text.text = it
            }
        }
    }

    interface OnCardItemSelected{
        fun cardSelected(rutaIcono: String, medioPagoDetalleId: Int?, description: String?)
        fun bankSelectedTrack(nombreBanco: String)
    }
}
