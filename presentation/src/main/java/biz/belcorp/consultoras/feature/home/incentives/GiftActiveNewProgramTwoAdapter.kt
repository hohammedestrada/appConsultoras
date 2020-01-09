package biz.belcorp.consultoras.feature.home.incentives

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.model.incentivos.ConcursoModel
import biz.belcorp.consultoras.common.model.incentivos.NivelProgramaNuevaModel
import biz.belcorp.consultoras.feature.contest.news.NewProgramActivity
import biz.belcorp.consultoras.feature.home.incentives.GiftActiveNewProgramTwoAdapter.GiftHolder
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.library.util.StringUtil
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_incentive_child_new_program_2.view.*
import kotlinx.android.synthetic.main.item_incentive_new_program.view.*
import java.math.BigDecimal
import java.text.DecimalFormat

class GiftActiveNewProgramTwoAdapter internal constructor(private val context: Context,
                                                          currentContest: List<ConcursoModel>,
                                                          private val currencySymbol: String,
                                                          private val decimalFormat: DecimalFormat)
    : RecyclerView.Adapter<GiftHolder>() {
    private val allItems = currentContest
    private var trackListener: IncentivesContainerAdapter.TrackEventListener? = null

    /** */

    inner class GiftHolder (v: View) : RecyclerView.ViewHolder(v) {

        fun bind(position: Int) = with(itemView) {
            val contest = allItems[position]

            if (null != contest.codigoNivelProgramaNuevas && !contest.codigoNivelProgramaNuevas.isEmpty()) {
                val level = Integer.parseInt(contest.codigoNivelProgramaNuevas)
                tvwIncentiveTitle.text = String.format(context.getString(R.string.incentives_new_program_title), level)
            }

            val importe = currencySymbol + " " + decimalFormat.format(contest.importePedido)

            if (null != contest.urlBannerCuponesProgramaNuevas
                && !contest.urlBannerCuponesProgramaNuevas.isEmpty()
                && null != contest.nivelProgramaNuevas) {
                lltCupon.visibility = View.VISIBLE

                val level = getLevel(contest.codigoNivelProgramaNuevas, contest.nivelProgramaNuevas)

                if (null != level && null != level.cupones && !level.cupones.isEmpty()) {

                    val resta = level.montoExigidoCupon.subtract(contest.importePedido)

                    if (resta.compareTo(BigDecimal.ZERO) <= 0) {
                        tvwCuponMensaje.text = String.format(context.getString(R.string.incentives_nuevas_ganaste),
                            StringUtil.getEmojiByUnicode(0x1F603))
                        tvwCuponMensaje.setTextColor(ContextCompat.getColor(context, R.color.lograste_puntaje))
                        ivwCheckCupon.visibility = View.VISIBLE
                    } else {
                        val faltante = currencySymbol + " " + decimalFormat.format(resta)
                        tvwCuponMensaje.text = String.format(context.getString(R.string.incentives_nuevas_progress_message_new), importe, faltante)
                        ivwCheckCupon.visibility = View.GONE
                    }

                }

                Glide.with(context).load(contest.urlBannerCuponesProgramaNuevas).into(ivwBannerCupon)
            } else {
                lltCupon.visibility = View.GONE
            }

            if (null != contest.urlBannerPremiosProgramaNuevas
                && !contest.urlBannerPremiosProgramaNuevas.isEmpty()
                && null != contest.nivelProgramaNuevas) {
                rltPremio.visibility = View.VISIBLE

                Glide.with(context).load(contest.urlBannerPremiosProgramaNuevas).into(ivwBanner)

                val level = getLevel(contest.codigoNivelProgramaNuevas, contest.nivelProgramaNuevas)

                if (null != level && null != level.premiosNuevas && !level.premiosNuevas.isEmpty()) {

                    val resta = level.montoExigidoPremio.subtract(contest.importePedido)

                    if (resta.compareTo(BigDecimal.ZERO) <= 0) {
                        tvwPremioMensaje.text = String.format(context.getString(R.string.incentives_nuevas_ganaste),
                            StringUtil.getEmojiByUnicode(0x1F603))
                        tvwPremioMensaje.setTextColor(ContextCompat.getColor(context, R.color.lograste_puntaje))
                        ivwCheckCupon.visibility = View.VISIBLE
                    } else {
                        val faltante = currencySymbol + " " + decimalFormat.format(resta)
                        tvwPremioMensaje.text = String.format(context.getString(R.string.incentives_nuevas_progress_message_new), importe, faltante)
                    }

                    lltCuponMessage.visibility = View.GONE

                    val premioNuevaModel = level.premiosNuevas[0]

                    if (premioNuevaModel.precioUnitario.compareTo(BigDecimal.ZERO) > 0) {
                        tvwPremio.text = String.format(context.getString(R.string.incentives_new_program_title_gift_1),
                            premioNuevaModel.descripcionProducto, currencySymbol, premioNuevaModel.precioUnitario)
                    } else {
                        tvwPremio.text = String.format(context.getString(R.string.incentives_new_program_title_gift_2),
                            premioNuevaModel.descripcionProducto)
                    }

                }

            } else {
                rltPremio.visibility = View.GONE
                viewSeparatorCupon.visibility = View.GONE
            }

            cvwIncentive.setOnClickListener {
                event()
            }

            lltContainer.setOnClickListener {
                event()
            }

        }

        private fun View.event() {
            if (null != trackListener) {
                trackListener!!.track(
                    GlobalConstant.SCREEN_INCENTIVES_GIFT_ORDER,
                    tvwIncentiveTitle!!.text.toString(),
                    GlobalConstant.EVENT_NAME_INCENTIVES_BONIFICATION)
            }

            val intent = NewProgramActivity.getCallingIntent(itemView.context)
            intent.putExtra(GlobalConstant.CODE_CONCURSO, allItems[adapterPosition].codigoConcurso)
            itemView.context.startActivity(intent)
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): GiftHolder {
        return GiftHolder(LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_incentive_new_program, viewGroup, false))
    }

    override fun onBindViewHolder(holder: GiftHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return allItems.size
    }

    internal fun getLevel(code: String, list: List<NivelProgramaNuevaModel>): NivelProgramaNuevaModel? {
        var level: NivelProgramaNuevaModel? = null

        for (model in list)
            if (model.codigoNivel == code)
                level = model

        return level
    }

    internal fun setTrackListener(trackListener: IncentivesContainerAdapter.TrackEventListener) {
        this.trackListener = trackListener
    }

}
