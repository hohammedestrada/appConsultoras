package biz.belcorp.consultoras.feature.payment.online.metodopago

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.model.pagoonline.PagoOnlineConfigModel
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.item_banco.view.*

class BancosAdapter(private var listaBancoOnline: List<PagoOnlineConfigModel.Banco>,
                    private var listener: TrackBankSelectedListener) : RecyclerView.Adapter<BancosAdapter.BancosViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BancosViewHolder = BancosViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_banco, parent, false))

    override fun getItemCount(): Int {
        return listaBancoOnline.size
    }

    override fun onBindViewHolder(holder: BancosViewHolder, position: Int) {
        holder.bind(listaBancoOnline[position])
    }

    inner class BancosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: PagoOnlineConfigModel.Banco) = with(itemView) {
            item.let {
                Glide.with(this).asBitmap().load(it.urlIcono)
                    .apply(RequestOptions.noTransformation().priority(Priority.HIGH))
                    .listener(object : RequestListener<Bitmap> {
                        override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            return false
                        }

                        override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Bitmap>, isFirstResource: Boolean): Boolean {
                            imgBanco.setImageResource(R.drawable.ic_bancainternet)
                            return true
                        }
                    })
                    .into(imgBanco)
            }

            itmbanconline.setOnClickListener {
                listener.onBankTrack(item.banco!!)
                if (item.packageApp.isNullOrBlank()) {
                    openUrl(itmbanconline.context, item.urlWeb)
                } else {
                    if (isAppInstalled(item.packageApp!!, itemView.context)) {
                        item.packageApp?.let{
                            val intent = itmbanconline.context.packageManager.getLaunchIntentForPackage(it)
                            intent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            itmbanconline.context.startActivity(intent)
                        }
                    } else {
                        openUrl(itmbanconline.context, item.urlWeb)
                    }
                }
            }
        }

        private fun isAppInstalled(app: String, context: Context): Boolean {
            val pm = context.packageManager
            return try {
                pm.getPackageInfo(app, PackageManager.GET_ACTIVITIES)
                true
            } catch (e: PackageManager.NameNotFoundException) {
                false
            }
        }

        private fun openUrl(context: Context, url: String?) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

    }

    interface TrackBankSelectedListener {
        fun onBankTrack(nombreBanco: String)
    }

}
