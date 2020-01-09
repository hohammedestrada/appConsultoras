package biz.belcorp.consultoras.feature.home.incentives

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.model.incentivos.NivelModel
import biz.belcorp.consultoras.common.model.incentivos.OpcionModel
import biz.belcorp.library.util.StringUtil
import kotlinx.android.synthetic.main.item_incentive_detail_generic.view.*
import java.util.*

class GiftActiveDetailCurrentAdapter(private val context: Context,
                                     niveles: List<NivelModel>,
                                     private val puntosAcumulados: Int,
                                     private val nivelAlcanzado: Int,
                                     private val indicadorPremioAcumulativo: Boolean,
                                     private var currentCampaniaFinal: String)
    : RecyclerView.Adapter<GiftActiveDetailCurrentAdapter.GiftOrderHolder>() {

    private var allItems = niveles as ArrayList<NivelModel>

    inner class GiftOrderHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int) = with(itemView) {
            val model = allItems[position]

            tvwNivel.text = String.format(context.getString(R.string.incentives_level), model.codigoNivel)

            tvwPuntajeInicial.text = String.format(context.getString(R.string.incentives_ptos), puntosAcumulados)
            tvwPuntajeFinal.text = String.format(context.getString(R.string.incentives_ptos), model.puntosNivel)
            tvwPuntajeFinalProgress.text = String.format(context.getString(R.string.incentives_ptos), model.puntosNivel)
            seekbarPuntos.isEnabled = false

            setupRecyclerView(rvwOpciones, model.opciones)

            //Validaciones Indicador Premio Acumulativo
            if (indicadorPremioAcumulativo) {

                //Niveles Inferiores e Igual al Alcanzado
                if (nivelAlcanzado >= model.codigoNivel) {
                    lltSeekbar.visibility = View.GONE
                    seekbarPuntos.progress = 1

                    tvwRecord.text = String.format(context.getString(R.string.incentives_winner_label),
                        StringUtil.getEmojiByUnicode(0x1F603))
                    tvwRecord.setTextColor(ContextCompat.getColor(context, R.color.lograste_puntaje))
                    ivwCheck.setImageResource(R.drawable.ic_check_selector_green)
                } else {
                    //Niveles Superiores al Alcanzado

                    val resta = model.codigoNivel - nivelAlcanzado

                    if (resta == 1) {
                        //Nivel Superior En Progreso
                        if (model.puntosFaltantes as Int == model.puntosNivel) {
                            seekbarPuntos.progress = 0
                        } else {
                            seekbarPuntos.progress = (puntosAcumulados.toFloat() / model.puntosNivel!! * 100).toInt()
                        }

                        tvwRecord.setTextColor(ContextCompat.getColor(context, R.color.default_magenta))
                        tvwRecord.text = generateRecordMessage(currentCampaniaFinal, model.puntosFaltantes.toString())

                        //ivwCheck.setImageResource(R.drawable.ic_arrow_active);
                        ivwCheck.visibility = View.GONE
                    } else {
                        //Niveles Superiores
                        seekbarPuntos.progress = 0

                        tvwRecord.setTextColor(ContextCompat.getColor(context, R.color.default_magenta))
                        tvwRecord.text = generateRecordMessage(currentCampaniaFinal, model.puntosFaltantes.toString())

                        viewBlocked.visibility = View.VISIBLE
                        lltSeekbar.visibility = View.GONE
                        ivwCheck.setImageResource(R.drawable.ic_locked)

                    }
                }
            } else {

                if (nivelAlcanzado >= model.codigoNivel) {
                    if (nivelAlcanzado == model.codigoNivel) {
                        //Nivel Igual al Alcanzado
                        seekbarPuntos.progress = 1

                        tvwRecord.setTextColor(ContextCompat.getColor(context, R.color.lograste_puntaje))
                        tvwRecord.text = String.format(context.getString(R.string.incentives_winner_label),
                            StringUtil.getEmojiByUnicode(0x1F603))
                        ivwCheck.setImageResource(R.drawable.ic_check_selector_green)
                    } else {
                        //Niveles Inferiores al Alcanzado
                        seekbarPuntos.progress = 0
                        viewBlocked.visibility = View.VISIBLE
                        tvwRecord.visibility = View.GONE
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
                            seekbarPuntos.progress = (puntosAcumulados.toFloat() / model.puntosNivel!! * 100).toInt()
                        }

                        tvwRecord.setTextColor(ContextCompat.getColor(context, R.color.default_magenta))
                        tvwRecord.text = generateRecordMessage(currentCampaniaFinal, model.puntosFaltantes.toString())
                        ivwCheck.visibility = View.GONE
                    } else {
                        //Niveles Superiores
                        viewBlocked.visibility = View.VISIBLE
                        lltSeekbar.visibility = View.GONE
                        seekbarPuntos.progress = 0

                        tvwRecord.setTextColor(ContextCompat.getColor(context, R.color.default_magenta))
                        tvwRecord.text = generateRecordMessage(currentCampaniaFinal, model.puntosFaltantes.toString())
                        ivwCheck.setImageResource(R.drawable.ic_locked)

                    }
                }
            }

            lltCondicionesData.background = lltContestData.background

            if (model.puntosExigidos > 0) {
                lltCondiciones.visibility = View.VISIBLE
            } else {
                lltCondiciones.visibility = View.GONE
            }

            //Condiciones
            lltContestData.post {
                val params = lltCard.layoutParams as RelativeLayout.LayoutParams
                params.height = lltCard.measuredHeight
                lltCard.requestLayout()
            }

            lltCondiciones.setOnClickListener {
                val shrinkSet = AnimatorInflater.loadAnimator(context, R.animator.grow_from_middle) as AnimatorSet
                shrinkSet.setTarget(lltContestData)
                shrinkSet.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {
                        // EMPTY
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        lltContestData.visibility = View.GONE
                        lltCondicionesData.visibility = View.VISIBLE
                        tvwCondicionesMessage1.text = String.format(context.getString(R.string.incentives_condiciones_message_1), model.puntosExigidos.toString())
                        tvwCondicionesMessage2.text = String.format(context.getString(R.string.incentives_condiciones_message_2), model.puntosExigidosFaltantes.toString())
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        // EMPTY
                    }

                    override fun onAnimationRepeat(animation: Animator) {
                        // EMPTY
                    }
                })
                shrinkSet.start()
            }

            tvwCondicionesBack.setOnClickListener {
                val growSet = AnimatorInflater.loadAnimator(context, R.animator.grow_from_middle) as AnimatorSet
                growSet.setTarget(lltCondicionesData)
                growSet.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {
                        // EMPTY
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        lltCondicionesData.visibility = View.GONE
                        lltContestData.visibility = View.VISIBLE
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        // EMPTY
                    }

                    override fun onAnimationRepeat(animation: Animator) {
                        // EMPTY
                    }
                })
                growSet.start()
            }

            if(model.opciones.size == 1){
                tvwPremio.visibility = View.GONE
                tvwElectivo.visibility = View.GONE
            }


        }

        private fun setupRecyclerView(rv: RecyclerView, opciones: List<OpcionModel>) {
            val currentListAdapter = GiftActiveOptionsCurrentAdapter(context, opciones, allItems,
                puntosAcumulados, nivelAlcanzado, currentCampaniaFinal, true)
            rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rv.setHasFixedSize(true)
            rv.isNestedScrollingEnabled = false
            rv.adapter = currentListAdapter
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): GiftOrderHolder {
        val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_incentive_detail_generic, viewGroup, false)
        return GiftOrderHolder(itemView)
    }

    override fun onBindViewHolder(holder: GiftOrderHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return allItems.size
    }

    private fun generateRecordMessage(currentCampaniaFinal: String, requiredPoints: String): String {

        return if (TextUtils.isEmpty(currentCampaniaFinal)) {
            String.format(context.getString(R.string.incentives_progress_message), requiredPoints)
        } else {
            String.format(context.getString(R.string.incentives_progress_message_hasta), requiredPoints, currentCampaniaFinal)
        }
    }

}
