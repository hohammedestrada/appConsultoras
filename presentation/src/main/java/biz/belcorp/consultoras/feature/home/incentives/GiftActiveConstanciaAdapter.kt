package biz.belcorp.consultoras.feature.home.incentives

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.model.incentivos.ConcursoModel
import biz.belcorp.consultoras.common.model.incentivos.NivelModel
import biz.belcorp.consultoras.feature.contest.constancy.PerConstancyActivity
import biz.belcorp.consultoras.util.GlobalConstant
import kotlinx.android.synthetic.main.item_incentive_constancia.view.*
import java.util.*

internal class GiftActiveConstanciaAdapter(private val context: Context,
                                           currentContest: List<ConcursoModel>,
                                           private val countryISO : String?)
    : RecyclerView.Adapter<GiftActiveConstanciaAdapter.GiftConstancyHolder>() {
    private val allItems = currentContest
    private var campaignHeader: String? = null
    private var trackListener: IncentivesContainerAdapter.TrackEventListener? = null

    internal inner class GiftConstancyHolder (v: View) : RecyclerView.ViewHolder(v) {
        fun bind(position: Int) = with(itemView) {
            val currentContest = allItems[position]

            currentContest.niveles?.let {
                if(!TextUtils.isEmpty(currentContest.campaniaId)){
                    rvwContestConstancia.visibility = View.VISIBLE

                    var currentCampaignFinal = ""
                    var startCampaignNumber = ""
                    var endCampaignNumber = ""

                    if (currentContest.campaniaIDInicio != null && currentContest.campaniaIDInicio!!.toString().length > 4)
                        startCampaignNumber = currentContest.campaniaIDInicio!!.toString().substring(4)
                    if (currentContest.campaniaIDFin != null && currentContest.campaniaIDFin!!.toString().length > 4)
                        endCampaignNumber = currentContest.campaniaIDFin!!.toString().substring(4)

                    if (currentContest.campaniaIDInicio == currentContest.campaniaIDFin) {
                        campaignHeader = "C" + currentContest.campaniaIDInicio!!.toString().substring(4)
                        tvwIncentiveConstanciaTitulo.text = String.format(context.getString(R.string.incentives_constancia_title_activity_simple), startCampaignNumber)
                    } else {
                        campaignHeader = "C" + currentContest.campaniaIDInicio!!.toString().substring(4) + "-C" + currentContest.campaniaIDFin!!.toString().substring(4)
                        currentCampaignFinal = currentContest.campaniaIDFin!!.toString().substring(4)
                        tvwIncentiveConstanciaTitulo.text = String.format(context.getString(R.string.incentives_constancia_title_activity), startCampaignNumber, endCampaignNumber)
                    }

                    val listaPremio = ArrayList<NivelModel>()

                    val niveles = it.size

                    it.forEach {
                        if (niveles == currentContest.nivelAlcanzado) {
                            val restaFirst = currentContest.nivelAlcanzado - it.codigoNivel

                            if (restaFirst == 0) {
                                listaPremio.add(it)
                                tvwNivelGift.text = String.format(context.getString(R.string.incentives_level_points),
                                    it.codigoNivel, it.puntosNivel)
                                return@forEach
                            }
                        } else {
                            val restaSecond = it.codigoNivel - currentContest.nivelAlcanzado

                            if (restaSecond == 1) {
                                listaPremio.add(it)
                                tvwNivelGift.text = String.format(context.getString(R.string.incentives_level_points),
                                    it.codigoNivel, it.puntosNivel)
                                return@forEach
                            }
                        }
                    }

                    if(listaPremio.isNotEmpty()){
                        incentivesChoiceText.visibility = if(listaPremio[0].opciones.size > 1) View.VISIBLE else View.GONE
                    }

                    val giftConstancyAdapter = GiftActiveChildConstanciaAdapter(context, listaPremio,
                        currentContest.puntosAcumulados!!, currentContest.nivelAlcanzado!!,
                        currentContest.isIndicadorPremioAcumulativo!!, currentCampaignFinal, campaignHeader!!)
                    rvwContestConstancia.adapter = giftConstancyAdapter
                    rvwContestConstancia.layoutManager = LinearLayoutManager(context,
                        LinearLayoutManager.VERTICAL, false)
                    rvwContestConstancia.setHasFixedSize(true)
                    rvwContestConstancia.isNestedScrollingEnabled = false

                    val divider = DividerItemDecoration(rvwContestConstancia.context, DividerItemDecoration.VERTICAL)
                    divider.setDrawable(ContextCompat.getDrawable(context, R.drawable.recycler_horizontal_divider)!!)
                    rvwContestConstancia.addItemDecoration(divider)

                }
            }

            cvwIncentiveConstancia.setOnClickListener {
                if (trackListener != null) {
                    trackListener!!.track(
                        GlobalConstant.SCREEN_INCENTIVES_GIFT_ORDER,
                        tvwIncentiveConstanciaTitulo.text.toString(),
                        GlobalConstant.EVENT_NAME_INCENTIVES_BONIFICATION)
                }

                val intent = PerConstancyActivity.getCallingIntent(itemView.context)
                intent.putExtra(GlobalConstant.CODE_CONCURSO, allItems[adapterPosition].codigoConcurso)
                intent.putExtra(GlobalConstant.CODE_CAMPAIGN, campaignHeader)
                intent.putExtra(GlobalConstant.TRACK_VAR_COUNTRY, countryISO)
                itemView.context.startActivity(intent)
            }

        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int)
        : GiftActiveConstanciaAdapter.GiftConstancyHolder {
        return GiftConstancyHolder(LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_incentive_constancia, viewGroup, false))
    }

    override fun onBindViewHolder(holder: GiftActiveConstanciaAdapter.GiftConstancyHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return allItems.size
    }

    fun setTrackListener(trackListener: IncentivesContainerAdapter.TrackEventListener) {
        this.trackListener = trackListener
    }
}
