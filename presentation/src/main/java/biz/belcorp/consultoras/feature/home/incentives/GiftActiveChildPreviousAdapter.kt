package biz.belcorp.consultoras.feature.home.incentives

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.model.incentivos.NivelModel
import biz.belcorp.consultoras.common.model.incentivos.OpcionModel
import biz.belcorp.consultoras.feature.contest.order.previous.PerPreviousOrderActivity
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.util.DateUtil
import biz.belcorp.library.util.StringUtil
import kotlinx.android.synthetic.main.item_incentive_child_generic.view.*
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.ParseException
import java.util.*

internal class GiftActiveChildPreviousAdapter(
    private val context: Context,
    private var allItems : ArrayList<NivelModel>,
    private val puntosAcumulados: Int,
    private val nivelAlcanzado: Int,
    private val currentCampania: String,
    private val premiacionCampania: String,
    private val moneySymbol: String,
    private val decimalFormat: DecimalFormat,
    private val campaignHeader: String)
    : RecyclerView.Adapter<GiftActiveChildPreviousAdapter.GiftOrderHolder>() {

    override fun getItemCount(): Int {
        return allItems.size
    }

    inner class GiftOrderHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int) = with(itemView) {
            val model = allItems[position]

            tvwPuntajeInicialBar.text = String.format(context.getString(R.string.incentives_ptos), puntosAcumulados)
            tvwPuntajeFinalProgressBar.text = String.format(context.getString(R.string.incentives_ptos), model.puntosNivel)

            seekbarPuntosBar.isEnabled = false

            //Niveles Inferiores e Igual al Alcanzado

            var lograste = ""

            if (nivelAlcanzado >= model.codigoNivel) {
                rltGift.visibility = View.VISIBLE
                lltBar.visibility = View.GONE

                lograste = String.format(context.getString(R.string.incentives_winner_label),
                    StringUtil.getEmojiByUnicode(0x1F603))
                tvwMessageGift.text = lograste
                tvwMessageGift.setTextColor(ContextCompat.getColor(context, R.color.lograste_puntaje))

                setupRecyclerView(rvwOpcionesGift, model.opciones)

//                ivwImageGift.background = ContextCompat.getDrawable(context, R.drawable.ic_circle_brand)
//                ivwImageGift.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_gift_white))
            } else {
                //Niveles Superiores al Alcanzado
                rltGift.visibility = View.GONE
                lltBar.visibility = View.VISIBLE

                setupRecyclerView(rvwOpcionesBar, model.opciones)

                lograste = ""

                val resta = model.codigoNivel - nivelAlcanzado

                if (resta == 1) {
                    //Nivel Superior En Progreso

                    if (model.puntosFaltantes as Int == model.puntosNivel) {
                        seekbarPuntosBar.progress = 0
                        tvwPuntosBar.text = String.format(context.getString(R.string.incentives_progress_bar_message), model.puntosFaltantes.toString())
                        tvwPuntosBar.setTextColor(ContextCompat.getColor(context, R.color.default_magenta))

                    } else {
                        seekbarPuntosBar.progress = (puntosAcumulados.toFloat() / model.puntosNivel!! * 100).toInt()
                        tvwPuntosBar.text = String.format(context.getString(R.string.incentives_progress_bar_message), model.puntosFaltantes.toString())
                        tvwPuntosBar.setTextColor(ContextCompat.getColor(context, R.color.default_magenta))
                    }
                } else {
                    //Niveles Superiores

                    seekbarPuntosBar.progress = 0
                    tvwPuntosBar.text = String.format(context.getString(R.string.incentives_progress_bar_message), model.puntosFaltantes.toString())
                    tvwPuntosBar.setTextColor(ContextCompat.getColor(context, R.color.default_magenta))
                }
            }

            //Premiación Campaña

            var premiacion: String

            if (premiacionCampania == currentCampania) {
                if (model.indicadorPremiacionPedido && model.montoPremiacionPedido == BigDecimal.valueOf(0.0)) {
                    premiacion = context.getString(R.string.incentives_message_1)
                } else if (model.indicadorPremiacionPedido && model.montoPremiacionPedido > BigDecimal.ZERO) {
                    premiacion = String.format(context.getString(R.string.incentives_message_2),
                        moneySymbol + " " + decimalFormat.format(model.montoPremiacionPedido))
                } else {
                    premiacion = ""
                }
            } else if (model.indicadorPremiacionPedido && model.montoPremiacionPedido == BigDecimal.valueOf(0.0)) {
                premiacion = String.format(context.getString(R.string.incentives_message_campania_1),
                    "C ${premiacionCampania.substring(4)}")
            } else if (model.indicadorPremiacionPedido && model.montoPremiacionPedido > BigDecimal.ZERO) {
                premiacion = String.format(context.getString(R.string.incentives_message_campania_2), moneySymbol + " " + decimalFormat.format(model.montoPremiacionPedido), "C" + premiacionCampania.substring(4))
            } else {
                premiacion = ""
            }

            //BellCenter

            var bellcenter = ""

            if (model.indicadorBelCenter!! && !TextUtils.isEmpty(model.fechaVentaRetail)) {
                try {
                    bellcenter = String.format(context.getString(R.string.incentives_message_3), StringUtil.capitalize(DateUtil.convertFechaToString(DateUtil.convertirISODatetoDate(model.fechaVentaRetail), "dd MMM")))
                } catch (e: ParseException) {
                    BelcorpLogger.w("parseException", e)
                }

            } else {
                bellcenter = ""
            }

            //Animación

            val listMessages = ArrayList<String>()
            val currentValue = ArrayList<Int>()

            if (!lograste.isEmpty()) listMessages.add(lograste)
            if (!premiacion.isEmpty()) listMessages.add(premiacion)
            if (!bellcenter.isEmpty()) listMessages.add(bellcenter)

            if (listMessages.size > 1) {
                val fadeOut = ObjectAnimator.ofFloat(tvwMessageGift, "alpha", 1f, .75f)
                fadeOut.duration = 250
                val fadeIn = ObjectAnimator.ofFloat(tvwMessageGift, "alpha", .75f, 1f)
                fadeIn.duration = 5000

                val animatorSet = AnimatorSet()
                animatorSet.play(fadeIn).after(fadeOut)

                animatorSet.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        animatorSet.start()
                        currentValue.add(0)
                        if (currentValue.size == listMessages.size) {
                            currentValue.clear()
                        }
                        tvwMessageGift.text = listMessages[currentValue.size]
                    }
                })
                animatorSet.start()
            }

        }

        private fun setupRecyclerView(rv: RecyclerView, opciones: List<OpcionModel>) {
            val currentListAdapter = GiftActiveOptionsCurrentAdapter(context, opciones, allItems, puntosAcumulados, nivelAlcanzado, campaignHeader, false)

            rv.adapter = currentListAdapter
            rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rv.setHasFixedSize(true)
            rv.isNestedScrollingEnabled = false
        }



        fun arrow() {
            val intent = PerPreviousOrderActivity.getCallingIntent(itemView.context)
            intent.putExtra(GlobalConstant.CODE_CONCURSO, allItems[adapterPosition].codigoConcurso)
            intent.putExtra(GlobalConstant.CURRENT_CAMPAIGN, currentCampania)
            intent.putExtra(GlobalConstant.CODE_CAMPAIGN, campaignHeader)
            itemView.context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): GiftActiveChildPreviousAdapter.GiftOrderHolder {

        val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_incentive_child_generic, viewGroup, false)
        return GiftOrderHolder(itemView)
    }

    override fun onBindViewHolder(holder: GiftOrderHolder, position: Int) {
        holder.bind(position)
    }

}
