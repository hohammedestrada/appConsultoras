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
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.util.DateUtil
import biz.belcorp.library.util.StringUtil
import kotlinx.android.synthetic.main.item_incentive_detail_generic.view.*
import java.math.BigDecimal
import java.text.ParseException
import java.util.*

internal class GiftActiveDetailPreviousAdapter(private val context: Context,
                                               private val allItems: List<NivelModel>,
                                      private val puntosAcumulados: Int,
                                      private val nivelAlcanzado: Int,
                                      private val indicadorPremioAcumulativo: Boolean,
                                      private val previousCampania: String,
                                      private val currentCampania: String,
                                      private val premiacionCampania: String,
                                      private val moneySymbol: String)
    : RecyclerView.Adapter<GiftActiveDetailPreviousAdapter.GiftOrderHolder>() {


    internal inner class GiftOrderHolder (v: View) : RecyclerView.ViewHolder(v) {

        val listMessages = ArrayList<String>()
        val currentValue = ArrayList<Int>()

        fun bind(position: Int) = with(itemView) {
            val model = allItems[position]

            tvwNivel.text = String.format(context.getString(R.string.incentives_level), model.codigoNivel)

            tvwPuntajeInicial.text = String.format(context.getString(R.string.incentives_ptos), puntosAcumulados)
            tvwPuntajeFinal.text = String.format(context.getString(R.string.incentives_ptos), model.puntosNivel)
            tvwPuntajeFinalProgress.text = String.format(context.getString(R.string.incentives_ptos), model.puntosNivel)

            seekbarPuntos.isEnabled = false

            //Validaciones Indicador Nivel Electivo
            if (model.indicadorNivelElectivo) {
                tvwElectivo.visibility = View.VISIBLE
                tvwElectivo.text = context.getString(R.string.incentives_points_elective_message)
            } else {
                tvwElectivo.visibility = View.GONE
            }

            setupRecyclerView(rvwOpciones, model.opciones)

            //Validaciones Indicador Premio Acumulativo

            var lograste = ""

            if (indicadorPremioAcumulativo) {
                if (nivelAlcanzado == allItems.size) {
                    tvwPremio.visibility = View.GONE
                } else {
                    //Validaciones Mensaje Acumulativo
                    if (model.codigoNivel > 1) {
                        tvwPremio.visibility = View.VISIBLE
                        tvwPremio.text = context.getString(R.string.incentives_points_acumulativo_message)
                    } else {
                        tvwPremio.visibility = View.GONE
                    }
                }

                //Niveles Inferiores e Igual al Alcanzado
                if (nivelAlcanzado >= model.codigoNivel) {

                    lograste = context.getString(R.string.incentives_ganaste_previous)

                    lltSeekbar.visibility = View.GONE
                    seekbarPuntos.progress = 1
                    tvwRecord.text = String.format(context.getString(R.string.incentives_winner_label),
                        StringUtil.getEmojiByUnicode(0x1F603))
                    tvwRecord.setTextColor(ContextCompat.getColor(context, R.color.lograste_puntaje))
                    ivwCheck.setImageResource(R.drawable.ic_check_selector_green)
                } else {
                    //Nivel Superior al Alcanzado
                    val resta = model.codigoNivel - nivelAlcanzado

                    if (resta == 1) {
                        //Nivel Superior En Progreso
                        if (model.puntosFaltantes as Int == model.puntosNivel) {
                            seekbarPuntos.progress = 0
                        } else {
                            seekbarPuntos.progress = (puntosAcumulados.toFloat() / model.puntosNivel * 100).toInt()
                        }

                        tvwRecord.setTextColor(ContextCompat.getColor(context, R.color.default_magenta))
                        tvwRecord.text = generateRecordMessage(previousCampania, model.puntosFaltantes.toString())

                        //ivwCheck.setImageResource(R.drawable.ic_arrow_active);
                        ivwCheck.visibility = View.GONE
                    } else {
                        //Niveles Superiores
                        seekbarPuntos.progress = 0
                        lltSeekbar.visibility = View.GONE

                        tvwRecord.setTextColor(ContextCompat.getColor(context, R.color.default_magenta))
                        tvwRecord.text = generateRecordMessage(previousCampania, model.puntosFaltantes.toString())

                        viewBlocked.visibility = View.VISIBLE
                        lltSeekbar.visibility = View.GONE
                        ivwCheck.setImageResource(R.drawable.ic_locked)
                    }
                }
            } else {

                if (nivelAlcanzado == allItems.size) {
                    tvwPremio.visibility = View.GONE
                } else {
                    //Validaciones Mensaje No Acumulativo
                    if (model.codigoNivel > 1) {
                        tvwPremio.visibility = View.VISIBLE
                        tvwPremio.text = context.getString(R.string.incentives_points_no_acumulativo_message)
                    } else {
                        tvwPremio.visibility = View.GONE
                    }
                }

                if (nivelAlcanzado >= model.codigoNivel) {

                    lograste = String.format(context.getString(R.string.incentives_winner_label),
                        StringUtil.getEmojiByUnicode(0x1F603))

                    if (nivelAlcanzado == model.codigoNivel) {
                        //Nivel Igual al Alcanzado
                        seekbarPuntos.progress = 1
                        tvwRecord.text = String.format(context.getString(R.string.incentives_winner_label),
                            StringUtil.getEmojiByUnicode(0x1F603))
                        tvwRecord.setTextColor(ContextCompat.getColor(context, R.color.lograste_puntaje))
                        ivwCheck.setImageResource(R.drawable.ic_check_selector_green)
                    } else {
                        //Niveles Inferiores al Alcanzado
                        seekbarPuntos.progress = 0
                        viewBlocked.visibility = View.VISIBLE
                        ivwCheck.visibility = View.GONE
                        tvwRecord.visibility = View.GONE
                        //ivwCheck.setImageResource(R.drawable.ic_check_down);
                        ivwCheck.visibility = View.GONE
                    }

                    lltSeekbar.visibility = View.GONE
                } else if (nivelAlcanzado < model.codigoNivel) {

                    val resta = model.codigoNivel - nivelAlcanzado

                    if (resta == 1) {
                        //Nivel Superior En Progreso
                        if (model.puntosFaltantes as Int == model.puntosNivel) {
                            seekbarPuntos.progress = 0
                        } else {
                            seekbarPuntos.progress = (puntosAcumulados.toFloat() / model.puntosNivel * 100).toInt()
                        }

                        tvwRecord.setTextColor(ContextCompat.getColor(context, R.color.default_magenta))
                        tvwRecord.text = generateRecordMessage(previousCampania, model.puntosFaltantes.toString())
                        //ivwCheck.setImageResource(R.drawable.ic_arrow_active);
                        ivwCheck.visibility = View.GONE
                    } else {
                        //Niveles Superiores
                        viewBlocked.visibility = View.VISIBLE
                        ivwCheck.setImageResource(R.drawable.ic_locked)
                        lltSeekbar.visibility = View.GONE
                        seekbarPuntos.progress = 0

                        tvwRecord.setTextColor(ContextCompat.getColor(context, R.color.default_magenta))
                        tvwRecord.text = generateRecordMessage(previousCampania, model.puntosFaltantes.toString())

                    }
                }
            }

            //Premiación Campaña

            var premiacion = ""

            if (premiacionCampania == currentCampania) {
                if (model.indicadorPremiacionPedido && model.montoPremiacionPedido == BigDecimal.valueOf(0.0)) {
                    premiacion = context.getString(R.string.incentives_message_1)
                } else if (model.indicadorPremiacionPedido && model.montoPremiacionPedido > BigDecimal.ZERO) {
                    premiacion = String.format(context.getString(R.string.incentives_message_2), moneySymbol + " " + model.montoPremiacionPedido.toString())
                } else {
                    premiacion = ""
                }
            } else if (model.indicadorPremiacionPedido && model.montoPremiacionPedido == BigDecimal.valueOf(0.0)) {
                premiacion = String.format(context.getString(R.string.incentives_message_campania_1), "C" + premiacionCampania.substring(4))
            } else if (model.indicadorPremiacionPedido && model.montoPremiacionPedido > BigDecimal.ZERO) {
                premiacion = String.format(context.getString(R.string.incentives_message_campania_2), moneySymbol + " " + model.montoPremiacionPedido.toString(), "C" + premiacionCampania.substring(4))
            } else {
                premiacion = ""
            }

            //BellCenter

            var bellcenter = ""

            if (model.indicadorBelCenter && !TextUtils.isEmpty(model.fechaVentaRetail)) {
                try {
                    bellcenter = String.format(context.getString(R.string.incentives_message_3), StringUtil.capitalize(DateUtil.convertFechaToString(DateUtil.convertirISODatetoDate(model.fechaVentaRetail), "dd MMM")))
                } catch (e: ParseException) {
                    BelcorpLogger.w("ParseException", e)
                }

            } else {
                bellcenter = ""
            }

            //Animación

            if (!lograste.isEmpty()) listMessages.add(lograste)
            if (!premiacion.isEmpty()) listMessages.add(premiacion)
            if (!bellcenter.isEmpty()) listMessages.add(bellcenter)

            if (listMessages.size > 1 && tvwRecord.visibility == View.VISIBLE) {
                var content = ""
                listMessages.forEach { content += "$it \n" }
                tvwRecord.text = content

                /*

                val fadeOut = ObjectAnimator.ofFloat(tvwRecord, "alpha", 1f, .75f)
                fadeOut.duration = 250
                val fadeIn = ObjectAnimator.ofFloat(tvwRecord, "alpha", .75f, 1f)
                fadeIn.duration = 5000

                val animatorSet = AnimatorSet()
                animatorSet.play(fadeIn).after(fadeOut)

                animatorSet.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        animatorSet.start()

                        currentValue.addFromHome(0)

                        if (currentValue.size == listMessages.size) {
                            currentValue.clear()
                        }

                        tvwRecord.text = listMessages[currentValue.size]
                    }
                })

                animatorSet.start()

                */
            }
        }

        private fun setupRecyclerView(rv: RecyclerView, opciones: List<OpcionModel>) {
            val currentListAdapter = GiftActiveOptionsCurrentAdapter(context, opciones, allItems,
                puntosAcumulados, nivelAlcanzado, premiacionCampania, true)
            rv.adapter = currentListAdapter
            rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rv.setHasFixedSize(true)
            rv.isNestedScrollingEnabled = false
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int)
        : GiftActiveDetailPreviousAdapter.GiftOrderHolder {
        val itemView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_incentive_detail_generic, viewGroup, false)
        return GiftOrderHolder(itemView)
    }

    override fun onBindViewHolder(holder: GiftOrderHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return allItems.size
    }

    /** */

    private fun generateRecordMessage(campaing: String, requiredPoints: String): String {

        return if (TextUtils.isEmpty(campaing)) {
            String.format(context.getString(R.string.incentives_progress_message_previous), requiredPoints)
        } else {
            String.format(context.getString(R.string.incentives_progress_message_previous_hasta), requiredPoints, previousCampania)
        }
    }

}
