package biz.belcorp.consultoras.feature.dreammeter.feature.detail

import android.util.Log
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.entity.dreammeter.DreamMeter
import biz.belcorp.consultoras.domain.interactor.DreamMeterUseCase
import biz.belcorp.consultoras.domain.interactor.UserUseCase
import biz.belcorp.consultoras.util.sumByBigDecimal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class DetailPresenter @Inject
internal constructor(private val userUseCase: UserUseCase, private val dreamMeterUseCase: DreamMeterUseCase) : Presenter<DetailView> {

    private var view: DetailView? = null

    private var dreamMeter: DreamMeter? = null
    private var user: User? = null

    override fun attachView(view: DetailView) {
        this.view = view
    }

    override fun resume() {
    }

    override fun pause() {
    }

    override fun destroy() {
        view?.hideLoading()
        view = null
    }

    fun getDreamMeter(extra: DreamMeter?) {
        view?.let { v ->
            GlobalScope.launch(Dispatchers.Main) {
                extra?.consultantDream?.let {
                    dreamMeter = extra
                    if (it.details.isNullOrEmpty()) {
                        v.onErrorDreamMeter()
                        v.hideLoading()
                        v.setCanBack(true)
                    } else {
                        val totalProgress = getTotalProgress(it.dreamAmount, it.details
                            ?: emptyList())
                        val showAnimation = !(it.status
                            ?: false) //si el status es true no muestra la animación, caso contrario si

                        if (totalProgress >= 100.toBigDecimal()) {
                            v.setMensajeDream(R.string.mensaje_sueno_ya_logrado)
                            v.hideEditar()
                        } else {
                            val firstCampaign = "C${it.details?.firstOrNull()?.campaignId.toString().substring(4)}"
                            val lastCampaign = "C${it.details?.lastOrNull()?.campaignId.toString().substring(4)}"

                            v.setMensajeDream(view?.getStringByIdRes(R.string.mensaje_sueno_aun_no_logrado, firstCampaign, lastCampaign) ?: "")
                            v.showEditar()
                        }

                        v.showDreamMeter(user, it, totalProgress, showAnimation)
                        v.hideLoading()
                        v.setCanBack(true)
                    }
                } ?: run {
                    v.onErrorDreamMeter()
                    v.hideLoading()
                    v.setCanBack(true)
                }
            }
        }
    }

    private fun getTotalProgress(dreamAmount: BigDecimal?, details: List<DreamMeter.ConsultantDream.ConsultantDreamDetail>): BigDecimal {
        val sumaGanancias: BigDecimal = details.sumByBigDecimal {
            it.realGain ?: 0.00.toBigDecimal()
        }
        val division = sumaGanancias.divide(dreamAmount
            ?: 0.00.toBigDecimal(), 5, RoundingMode.DOWN)
        return division.multiply(100.00.toBigDecimal())
    }

    fun getConfiguracion() {
        GlobalScope.launch(Dispatchers.IO) {
            userUseCase.getUser()?.let {
                user = it
                GlobalScope.launch(Dispatchers.Main) {
                    view?.onLoadConfiguracion(it)
                }
            }
        }
    }

    fun updateStatus() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                dreamMeterUseCase.updateStatus(dreamMeter?.id, true)
            } catch (ex: Exception) {
                Log.d("Exception", ex.message)
            }
        }
    }

}
