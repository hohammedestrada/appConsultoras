package biz.belcorp.consultoras.feature.home.incentives

import android.graphics.Bitmap
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.model.incentivos.NivelModel
import biz.belcorp.consultoras.common.model.incentivos.PremioModel
import biz.belcorp.library.util.StringUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

import kotlinx.android.synthetic.main.item_incentive_child_history.view.*

internal class GiftHistoryChildAdapter (private val allItems: List<NivelModel>,
                                        private val puntosAcumulados: Int,
                                        private val nivelAlcanzado: Int,
                                        private val indicadorPremioAcumulativo: Boolean)
    : RecyclerView.Adapter<GiftHistoryChildAdapter.Holder>() {

    internal inner class Holder (v: View) : RecyclerView.ViewHolder(v) {

        fun bind(position: Int) = with(itemView) {
            val model = allItems[position]

            tvwLevel.text = "Nivel ${model.codigoNivel}"
            tvwPuntaje.text = "[ $puntosAcumulados*/${model.puntosNivel} PUNTOS ]"

            var premios: ArrayList<PremioModel>? = null

            if (model.opciones != null && !model.opciones.isEmpty()) {
                if (model.opciones.firstOrNull() != null) {
                    premios = model.opciones.first().premios as ArrayList<PremioModel>
                }
            }

            //Validaciones Indicador Premio Acumulativo

            if (indicadorPremioAcumulativo) {
                //Niveles Inferiores e Igual al Alcanzado

                if (nivelAlcanzado >= model.codigoNivel) {

                    tvwRecord.text = String.format(context.getString(R.string.incentives_winner_label),
                        StringUtil.getEmojiByUnicode(0x1F603))
                    tvwRecord.setTextColor(ContextCompat.getColor(context, R.color.lograste_puntaje))
                    tvwLevel.setTextColor(ContextCompat.getColor(context, R.color.brand_general))

                    setReward(tvwTitle, ivwImage, premios, R.drawable.ic_gift_brand)

                } else {
                    //Niveles Superiores al Alcanzado

                    val resta = model.codigoNivel - nivelAlcanzado

                    if (resta == 1) {
                        //Nivel Superior En Progreso
                        tvwRecord.text = String.format(context.getString(R.string.incentives_loser_label),
                            StringUtil.getEmojiByUnicode(0x1F61E))

                        tvwLevel.setTextColor(Color.GRAY)
                        setReward(tvwTitle, ivwImage, premios, R.drawable.ic_gift_brand)

                    } else {
                        //Niveles Superiores

                        tvwRecord.text = context.getString(R.string.incentives_no_ganaste)
                        tvwRecord.setTextColor(Color.GRAY)
                        tvwLevel.setTextColor(Color.GRAY)
                        setReward(tvwTitle, ivwImage, premios, R.drawable.ic_gift_brand)
                    }
                }
            } else {
                //Niveles Inferiores e Igual al Alcanzado

                if (nivelAlcanzado >= model.codigoNivel) {
                    if (nivelAlcanzado == model.codigoNivel) {
                        //Nivel Igual al Alcanzado

                        tvwRecord.text = String.format(context.getString(R.string.incentives_winner_label),
                            StringUtil.getEmojiByUnicode(0x1F603))
                        tvwRecord.setTextColor(ContextCompat.getColor(context, R.color.lograste_puntaje))

                        setReward(tvwTitle, ivwImage, premios, R.drawable.ic_gift_brand)

                    } else {
                        //Niveles Inferiores al Alcanzado
                        tvwRecord.text = String.format(context.getString(R.string.incentives_winner_label),
                            StringUtil.getEmojiByUnicode(0x1F603))
                        tvwRecord.setTextColor(Color.GRAY)
                        tvwLevel.setTextColor(Color.GRAY)
                        setReward(tvwTitle, ivwImage, premios, R.drawable.ic_gift_brand)
                    }
                } else {
                    //Niveles Superiores al Alcanzado

                    val resta = model.codigoNivel - nivelAlcanzado

                    if (resta == 1) {
                        //Nivel Superior En Progreso
                        tvwRecord.text = String.format(context.getString(R.string.incentives_loser_label),
                            StringUtil.getEmojiByUnicode(0x1F61E))

                        tvwLevel.setTextColor(Color.GRAY)
                        setReward(tvwTitle, ivwImage, premios, R.drawable.ic_gift_brand)
                    } else {
                        //Niveles Superiores

                        tvwRecord.text = String.format(context.getString(R.string.incentives_loser_label),
                            StringUtil.getEmojiByUnicode(0x1F61E))
                        tvwRecord.setTextColor(Color.GRAY)
                        tvwLevel.setTextColor(Color.GRAY)
                        setReward(tvwTitle, ivwImage, premios, R.drawable.ic_gift_brand)
                    }
                }
            }
        }

        private fun setReward(tvw: TextView, ivw: ImageView, premios: ArrayList<PremioModel>?, icon: Int) {

            var descriptionPremio = ""
            val urlImage: String

            if (premios != null && !premios.isEmpty()) {

                premios.forEach { descriptionPremio += "${it.descripcionPremio}  \n" }

                val size = premios.size

                if (size > 1) {
                    urlImage = premios[0].imagenPremio ?: ""
                    loadImage(urlImage, ivw, icon)
                } else if (size == 1) {
                    urlImage = premios[0].imagenPremio ?: ""
                    loadImage(urlImage, ivw, icon)
                } else {
                    loadImage(null, ivw, icon)
                }

                tvw.text = descriptionPremio

            }

        }

        private fun loadImage(url: String?, ivw: ImageView, icon: Int) {

            url?.let {
                Glide.with(ivw).asBitmap().load(it)
                    .listener(object : RequestListener<Bitmap> {
                        override fun onResourceReady(resource: Bitmap?, model: Any?,
                                                     target: Target<Bitmap>?,
                                                     dataSource: DataSource?,
                                                     isFirstResource: Boolean): Boolean {
                            return false
                        }

                        override fun onLoadFailed(e: GlideException?, model: Any,
                                                  target: Target<Bitmap>,
                                                  isFirstResource: Boolean): Boolean {
                            ivw.setImageDrawable(ContextCompat.getDrawable(ivw.context, icon))
                            return true
                        }
                    })
                    .into(ivw)
            } ?: kotlin.run {
                ivw.setImageDrawable(ContextCompat.getDrawable(ivw.context, icon))
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): Holder {
        val itemView = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_incentive_child_history, viewGroup, false)
        return Holder(itemView)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return allItems.size
    }
}
