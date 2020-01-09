package biz.belcorp.consultoras.feature.home.incentives

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.SafeLet
import biz.belcorp.consultoras.common.model.incentivos.ConcursoModel
import biz.belcorp.consultoras.common.model.incentivos.NivelModel
import biz.belcorp.consultoras.feature.detalleprogramabrillante.DetalleProgramaBrillanteActivity
import biz.belcorp.consultoras.feature.home.BaseHomeFragment
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.library.util.CountryUtil
import kotlinx.android.synthetic.main.fragment_incentivos_regalo_active.*
import kotlinx.android.synthetic.main.view_bright_path_contest.*
import java.text.DecimalFormat
import java.util.ArrayList


class GiftActiveFragment : BaseHomeFragment(), SafeLet{


    private lateinit var newProgramAdapter: GiftActiveNewProgramTwoAdapter
    private lateinit var currentListAdapter: GiftActiveCurrentAdapter
    private lateinit var previousListAdapter: GiftActivePreviousAdapter
    private lateinit var constanciaListAdapter: GiftActiveConstanciaAdapter

    private var newProgramContest: List<ConcursoModel>? = null
    private var constanciaContest: List<ConcursoModel>? = null
    private var currentContest: List<ConcursoModel>? = null
    private var previousContest: List<ConcursoModel>? = null
    private var currentCampania: String? = null
    private var moneySymbol: String? = null
    private var brightPathContest: ConcursoModel? = null
    private var decimalFormat: DecimalFormat? = null
    private var countryConsultISO : String? = null
    private var trackListener: IncentivesContainerAdapter.TrackEventListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?
                              , savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_incentivos_regalo_active,
            container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) init()
    }

    fun setTrackListener(trackListener: IncentivesContainerAdapter.TrackEventListener) {
        this.trackListener = trackListener
    }

    private fun init() {
        val arguments = arguments ?: return

        this.newProgramContest = arguments.getParcelableArrayList(GlobalConstant.CONTEST_NEW_PROGRAM_KEY)
        this.constanciaContest = arguments.getParcelableArrayList(GlobalConstant.CURRENT_CONSTANCIA_KEY)
        this.currentContest = arguments.getParcelableArrayList(GlobalConstant.CURRENT_CONTEST_KEY)
        this.previousContest = arguments.getParcelableArrayList(GlobalConstant.PREVIOUS_CONTEST_KEY)
        this.currentCampania = arguments.getString(GlobalConstant.CAMPAIGN_KEY)
        this.moneySymbol = arguments.getString(GlobalConstant.CURRENCY_SYMBOL_KEY)
        this.countryConsultISO = arguments.getString(GlobalConstant.TRACK_VAR_COUNTRY)
        this.decimalFormat = CountryUtil.getDecimalFormatByISO(countryConsultISO, true)
        this.brightPathContest = arguments.getParcelable(GlobalConstant.CONTEST_BRIGHT_PATH_KEY)
        this.decimalFormat = CountryUtil.getDecimalFormatByISO(countryConsultISO, true)

        showTitle()
        showPedidos()
        showConstancia()
        showBrightPathContest()

        btnMasInfo.setOnClickListener {
            goDetalleBrightProgram()
        }
    }

    private fun showTitle() {

        var count = 0

        newProgramContest?.let {
            if(it.isNotEmpty()){
                if ((null != it[0].urlBannerPremiosProgramaNuevas || null != it[0].urlBannerCuponesProgramaNuevas))
                    count += it.size
            }

        }
        currentContest?.let { count += it.size }
        previousContest?.let { count += it.size }
        constanciaContest?.let { count += it.size }
        brightPathContest?.let { count++ }

        if (count > 0) {
            val title = resources.getQuantityString(R.plurals.incentives_activas_title_plu, count, count)
            tvwCurrentTitle.text = title
            tvwImageDisclaimer.visibility = View.VISIBLE
        } else {
            tvwCurrentTitle.setText(R.string.incentives_activas_sin_concursos)
            tvwImageDisclaimer.visibility = View.GONE
        }
    }

    private fun showPedidos() {

        newProgramContest?.let {
            var show = false

            for (concursoModel in it) {
                if (concursoModel.urlBannerPremiosProgramaNuevas != null
                    && !concursoModel.urlBannerPremiosProgramaNuevas.isEmpty()
                    || concursoModel.urlBannerCuponesProgramaNuevas != null
                    && !concursoModel.urlBannerCuponesProgramaNuevas.isEmpty()) {
                    show = true
                    break
                }
            }

            if (show) {
                rvwContestNewProgram.visibility = View.VISIBLE
                showNewProgramContest()
            }
        }

        //Previous

        previousContest?.let {
            var count = 0

            for (concursoModel in it) {
                if (concursoModel.niveles != null && !concursoModel.niveles.isEmpty()) {
                    count++
                }
            }

            if (count != 0) {
                rvwContestPrevious.visibility = View.VISIBLE
                showPreviousContest(it, currentCampania, moneySymbol)
            }
        }

        //Current
        currentContest?.let {
            var count = 0

            for (concursoModel in it) {
                if (concursoModel.niveles != null && !concursoModel.niveles.isEmpty()) {
                    count++
                }
            }

            if (count != 0) {
                rvwContestCurrent.visibility = View.VISIBLE
                showCurrentContest(it)
            }
        }
    }

    private fun showConstancia() {
        //Current
        constanciaContest?.let {
            var count = 0

            for (concursoModel in it) {
                if (concursoModel.niveles != null && !concursoModel.niveles.isEmpty()) {
                    count++
                }
            }

            if (count != 0) {
                rvwContestConstancia.visibility = View.VISIBLE
                showConstanciaContest(it)
            }
        }

    }

    private fun showNewProgramContest() {
        safeLet(context, newProgramContest, moneySymbol, decimalFormat, trackListener) {context, newProgramContest, moneySymbol, decimalFormat, trackListener ->
            newProgramAdapter = GiftActiveNewProgramTwoAdapter(context, newProgramContest,
                moneySymbol, decimalFormat)
            newProgramAdapter.setTrackListener(trackListener)
            rvwContestNewProgram.adapter = newProgramAdapter
            rvwContestNewProgram.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            rvwContestNewProgram.setHasFixedSize(true)
            rvwContestNewProgram.isNestedScrollingEnabled = false
        }

    }

    private fun showPreviousContest(previousContest: List<ConcursoModel>, currentCampania: String?,
                                    countryMoneySymbol: String?) {
        safeLet(context, previousContest, currentCampania, decimalFormat, countryMoneySymbol,
            trackListener) {context, previousContest, currentCampania, decimalFormat, countryMoneySymbol, trackListener->
            previousListAdapter = GiftActivePreviousAdapter(previousContest, currentCampania, countryMoneySymbol, decimalFormat, countryConsultISO)
            previousListAdapter.setTrackListener(trackListener)
            rvwContestPrevious.adapter = previousListAdapter
            rvwContestPrevious.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            rvwContestPrevious.setHasFixedSize(true)
            rvwContestPrevious.isNestedScrollingEnabled = false
        }
    }

    private fun showCurrentContest(currentContest: List<ConcursoModel>) {
        safeLet(context,trackListener) { _, trackListener ->
            currentListAdapter = GiftActiveCurrentAdapter(currentContest,countryConsultISO)
            currentListAdapter.setTrackListener(trackListener)
            rvwContestCurrent.adapter = currentListAdapter
            rvwContestCurrent.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            rvwContestCurrent.setHasFixedSize(true)
            rvwContestCurrent.isNestedScrollingEnabled = false
        }


    }

    private fun showConstanciaContest(currentContest: List<ConcursoModel>) {
        safeLet(context,trackListener) { context, trackListener ->
            constanciaListAdapter = GiftActiveConstanciaAdapter(context, currentContest,countryConsultISO)
            constanciaListAdapter.setTrackListener(trackListener)
            rvwContestConstancia.adapter = constanciaListAdapter
            rvwContestConstancia.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            rvwContestConstancia.setHasFixedSize(true)
            rvwContestConstancia.isNestedScrollingEnabled = false
        }
    }

    private fun showBrightPathContest(){
        brightPathContest?.let {concursoModel ->
            vvBrigthPathContest.visibility = View.VISIBLE
            seekbar_premios.isEnabled = false

            tvewPuntajeObtenido.visibility = View.VISIBLE
            tvewPuntajeObtenido.text = "${resources.getString(R.string.tienes)}: ${concursoModel.puntosAcumulados} ${resources.getString(R.string.puntos)}."

            val idNivelAlcanzado : Int = concursoModel.nivelAlcanzado ?: -1
            val idNivelSigueinte : Int = concursoModel.nivelSiguiente ?: -1

            concursoModel.niveles?.let{ niveles ->

                var nivelAlcanzado : NivelModel? = null
                var nivelSiguiente : NivelModel? = null

                for(nivel in niveles){
                    if(nivel.codigoNivel == idNivelAlcanzado) {nivelAlcanzado = nivel}
                    if(nivel.codigoNivel == idNivelSigueinte) {nivelSiguiente = nivel}
                }

                nivelSiguiente?.let { nvSiguiente->
                    nvSiguiente.opciones?.let { opciones ->
                        if(concursoModel.nivelAlcanzado < nvSiguiente.codigoNivel) {
                            if (opciones.size >= 1) {
                                opciones[0].premios?.let { premios ->
                                    if (premios.size >= 1) {
                                        tvewPuntajeFaltante.visibility = View.VISIBLE
                                        tvewPuntajeFaltante.text = "${resources.getString(R.string.te_faltan)}: ${nvSiguiente.puntosFaltantes} ${resources.getString(R.string.para_ser)} ${premios[0].descripcionPremio}"
                                    }
                                }
                            }
                        }

                        if(concursoModel.puntosAcumulados != 0 && nvSiguiente.puntosNivel != 0){
                            if(concursoModel.puntosAcumulados <= nvSiguiente.puntosNivel) {
                                seekbar_premios.visibility = View.VISIBLE
                                seekbar_premios.progress = ((concursoModel.puntosAcumulados.toFloat() / nvSiguiente.puntosNivel.toFloat()) * 100).toInt()
                            }else{
                                seekbar_premios.visibility = View.VISIBLE
                                seekbar_premios.progress = 100
                            }
                        }
                    }
                }
            }
        }
    }

    private fun goDetalleBrightProgram() {

        arguments?.let{args ->
            val intent = Intent(activity, DetalleProgramaBrillanteActivity::class.java)

            brightPathContest?.let { model ->
                val extras = Bundle()
                extras.putParcelableArrayList(GlobalConstant.CONTEST_BRIGHT_PATH_LEVELS_KEY, (model.niveles as ArrayList))
                intent.putExtras(extras)
            }
            activity!!.startActivity(intent)
        }
    }

    companion object {

        fun newInstance(): GiftActiveFragment {
            return GiftActiveFragment()
        }
    }
}
