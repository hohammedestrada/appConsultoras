package biz.belcorp.consultoras.feature.home.incentives

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.dialog.IncentiveGiftDialog
import biz.belcorp.consultoras.common.model.incentivos.NivelModel
import biz.belcorp.consultoras.common.model.incentivos.OpcionModel
import biz.belcorp.consultoras.feature.contest.rewards.GiftsActivity
import biz.belcorp.consultoras.util.IncentivesUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.item_incentive_child_opcion.view.*
import java.util.*

internal class GiftActiveOptionsCurrentAdapter(private val context: Context,
                                               private val opciones: List<OpcionModel>,
                                               private val allItems: List<NivelModel>,
                                               private val puntosAcumulados: Int,
                                               private val nivelAlcanzado: Int,
                                               private val campaignHeader: String,
                                               private val fromDetail: Boolean)
    : RecyclerView.Adapter<GiftActiveOptionsCurrentAdapter.GiftOrderHolder>() {

    internal inner class GiftOrderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int) = with(itemView) {
            val model = opciones[position]

            model.let {
                if(it.premios.size > 1){
                    tvwPremio.text = context.getString(R.string.incentives_pack)
                    it.premios[0]?.let {

                        Glide.with(context).load(R.drawable.ic_multiple_option).into(ivwImage)

                        if(opciones.size == 1){
                            tvwNivelGift.text = String.format(context.getString(R.string.incentives_level_points),
                                allItems[0].codigoNivel, allItems[0].puntosNivel)
                            tvwOpcion.visibility = View.GONE
                        } else {
                            tvwOpcion.text = String.format(context.getString(R.string.incentives_option_cod), model.opcion, it.codigoPremio)
                            tvwNivelGift.visibility = View.GONE
                        }
                    }
                } else {
                    tvwVerContenidoPack.visibility = View.GONE
                    it.premios[0]?.let {
                        val premios = IncentivesUtil.getTextDescriptionNew(context, it.codigoPremio, it.descripcionPremio, it.numeroPremio.toString())
                        tvwPremio.text = premios

                        loadImage(it.imagenPremio,ivwImage, premios.toString())

                        if(opciones.size == 1){
                            tvwNivelGift.text = String.format(context.getString(R.string.incentives_level_points),
                                allItems[0].codigoNivel, allItems[0].puntosNivel)
                            tvwOpcion.visibility = View.GONE
                        } else {
                            tvwOpcion.text = String.format(context.getString(R.string.incentives_option_cod), model.opcion, it.codigoPremio)
                            tvwNivelGift.visibility = View.GONE
                        }
                    }

                }
            }

            tvwVerContenidoPack.setOnClickListener {
                val intent = Intent(context, GiftsActivity::class.java)
                val bundle = Bundle()
                bundle.putParcelable("opcion", model)
                bundle.putParcelableArrayList("premios", ArrayList(model.premios))
                intent.putExtras(bundle)
                context.startActivity(intent)
            }

            if(fromDetail){
                tvwNivelGift.visibility = View.GONE
            }

        }

        private fun loadImage(url: String?, ivw: ImageView, premio: String) {

            url?.let {
                Glide.with(ivw).asBitmap().load(it)
                    .listener(object : RequestListener<Bitmap> {
                        override fun onResourceReady(resource: Bitmap?, model: Any?,
                                                     target: Target<Bitmap>?,
                                                     dataSource: DataSource?,
                                                     isFirstResource: Boolean): Boolean {
                            setOnClickIncentiveImage(url , premio, ivw)
                            ivw.setPadding(0,0,0,0)
                            return false
                        }

                        override fun onLoadFailed(e: GlideException?, model: Any,
                                                  target: Target<Bitmap>,
                                                  isFirstResource: Boolean): Boolean {
                            ivw.setImageDrawable(ContextCompat.getDrawable(ivw.context, R.drawable.ic_gift_brand))
                            return true
                        }
                    })
                    .into(ivw)
            } ?: kotlin.run {
                ivw.setImageDrawable(ContextCompat.getDrawable(ivw.context, R.drawable.ic_gift_brand))
            }
        }

        private fun setOnClickIncentiveImage(url: String?, name: String, ivw: ImageView){
            url?.let {
                ivw.setOnClickListener {
                    IncentiveGiftDialog.Builder(context).withImage(url).withName(name).show()
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): GiftOrderHolder {
        val itemView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_incentive_child_opcion, viewGroup, false)
        return GiftOrderHolder(itemView)
    }

    override fun onBindViewHolder(holder: GiftOrderHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return opciones.size
    }

}
