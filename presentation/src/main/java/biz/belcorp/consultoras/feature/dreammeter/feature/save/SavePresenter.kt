package biz.belcorp.consultoras.feature.dreammeter.feature.save

import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.base.SafeLet
import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.BasicDto
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.entity.dreammeter.DreamMeter
import biz.belcorp.consultoras.domain.interactor.DreamMeterUseCase
import biz.belcorp.consultoras.domain.interactor.UserUseCase
import biz.belcorp.library.util.CountryUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.text.DecimalFormat
import javax.inject.Inject


@PerActivity
class SavePresenter @Inject
internal constructor(private val dreamMeterUseCase: DreamMeterUseCase,
                     private val userUseCase: UserUseCase) : Presenter<SaveView>, SafeLet {

    private var view: SaveView? = null

    private var user: User? = null
    private var decimalFormat = DecimalFormat()

    private var textDreamName = ""
    private var textDreamAmount = ""
    private var huboAlgunCambio = false
    private val RESPONSE_OK = "0000"
    private val RESPONSE_ERROR = "0001"
    private val RESPONSE_AMOUNT_ERROR = "100"
    private var replaceScreen: Boolean? = null

    override fun attachView(view: SaveView) {
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

    fun saveDreamMeter(extra: DreamMeter?, nameDream: String, amountDream: String) {
        if (huboAlgunCambio) {
            view?.let {
                GlobalScope.launch(Dispatchers.IO) {
                    //si no tiene un sueño, guardar
                    if (extra?.consultantDream?.programDreamId == null) {
                        saveOrUpdateDream(nameDream, amountDream, extra?.id)
                        //si no actualizar
                    } else {
                        saveOrUpdateDream(nameDream, amountDream, extra.id, true)
                    }
                }
            }
        }
    }

    private suspend fun saveOrUpdateDream(nameDream: String, amountDream: String, programDreamId: Int? = null, isUpdate: Boolean = false) {
        GlobalScope.launch(Dispatchers.Main) {
            view?.showLoading()
            view?.setCanBack(false)
        }
        try {
            val response = if (isUpdate) {
                dreamMeterUseCase.updateDreamMeter(programDreamId, nameDream.trim(), amountDream.trim())
            } else {
                dreamMeterUseCase.saveDreamMeter(programDreamId, nameDream.trim(), amountDream.trim())
            }

            GlobalScope.launch(Dispatchers.Main) {

                //si es creacion reemplazar, sino no remplazar
                val isReplace = !isUpdate

                when (response.code) {
                    RESPONSE_OK -> getDreamMeter(null, replaceScreen ?: isReplace)
                    RESPONSE_ERROR -> {
                        val message = getMessageFormat(response.message)

                        view?.hideLoading()
                        view?.showMessage(message)
                        view?.setCanBack(true)
                    }
                    else -> {
                        view?.hideLoading()
                        view?.showError(null)
                        view?.setCanBack(true)
                    }
                }
            }
        } catch (ex: Exception) {
            GlobalScope.launch(Dispatchers.Main) {
                view?.hideLoading()
                view?.showError(ex)
                view?.setCanBack(true)
            }
        }
    }

    private fun getMessageFormat(message: String?): String {
        try {
            message?.let {
                val index1 = it.indexOf("{")
                val index2 = it.indexOf("}")

                return if (index1 != -1 && index2 != -1) {
                    val monto = it.subSequence(index1 + 1, index2).toString()
                    val montoFormateado = formatWithMoneySymbol(monto.toBigDecimal())
                    it.replaceRange(index1, index2 + 1, montoFormateado)
                } else {
                    it
                }
            }
            return ""
        } catch (e: Exception) {
            return message ?: ""
        }
    }

    fun getDreamMeter(extra: DreamMeter?, isReplace: Boolean) {
        view?.let { v ->
            //Sí hay un sueño como extra mostrarlo
            extra?.let {
                GlobalScope.launch(Dispatchers.Main) {
                    textDreamName = it.consultantDream?.description ?: ""
                    textDreamAmount = it.consultantDream?.dreamAmount.toString()

                    v.showDream(it)
                    v.showDreamMeter(it)
                    v.hideLoading()
                    v.setCanBack(true)
                }
            } ?: run {
                v.showLoading()
                v.setCanBack(false)
                //Sí no hay sueño como extra hacer la peticion del sueño
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val response: BasicDto<DreamMeter?> = dreamMeterUseCase.getDreamMeter()
                        val user: User? = userUseCase.getUser()

                        GlobalScope.launch(Dispatchers.Main) {

                            when (response.code) {
                                RESPONSE_AMOUNT_ERROR -> {
                                    replaceScreen = true
                                    v.showDream(response.data)
                                    v.showDreamMeter(response.data)
                                    v.setDreamMeter(response.data)
                                    v.showMessage(getMessageFormat(response.message))
                                    v.hideLoading()
                                    v.setCanBack(true)
                                }
                                else -> {
                                    response.data?.let {
                                        //Si la consultora no tiene campaign, salir
                                        //Si la campaign actual de la consultora no está en el rango del programa, salir
                                        //Si la campaign actual es igual a la ultima del programa y el sueño es nulo, salir
                                        if (
                                            user?.campaing.isNullOrEmpty() ||
                                            it.startProgramCampaignId ?: 0 > user?.campaing?.toIntOrNull() ?: 0 || it.endProgramCampaignId ?: 0 < user?.campaing?.toIntOrNull() ?: 0 ||
                                            (user?.campaing?.toIntOrNull() ?: 0 == it.endProgramCampaignId && it.consultantDream == null)
                                        ) {
                                            v.onErrorLoad()
                                        } else {
                                            //Sí tiene un sueño guardado, ir a la otra pantalla
                                            if (it.consultantDream?.programDreamId != null) {
                                                GlobalScope.launch(Dispatchers.Main) {
                                                    v.goToDetail(it, isReplace)
                                                }
                                                //Si no quedarse en dicha pantalla
                                            } else {
                                                GlobalScope.launch(Dispatchers.Main) {
                                                    v.hideLoading()
                                                    v.setupListener()
                                                    v.showDreamMeter(it)
                                                    v.setDreamMeter(it)
                                                    v.setCanBack(true)
                                                }
                                            }
                                        }
                                    } ?: run {
                                        v.onErrorLoad()
                                    }
                                }
                            }
                        }
                    } catch (e: Exception) {
                        GlobalScope.launch(Dispatchers.Main) {
                            v.hideLoading()
                            v.showError(e)
                            v.setCanBack(true)
                        }
                    }
                }
            }
        }
    }

    fun onEdtDreamNameAndDreamAmountTextChanged(textName: String, textAmount: String) {
        huboAlgunCambio = true
        textDreamName = textName
        textDreamAmount = textAmount
        isBothWithCharacter()
    }

    private fun isBothWithCharacter() {
        if (textDreamName.trim().length >= 2 && textDreamAmount.isNotEmpty() && textDreamAmount.toLongOrNull() != 0L) {
            view?.enableButton()
        } else {
            view?.disableButton()
        }
    }

    fun formatWithMoneySymbol(precio: BigDecimal): String {
        return "${user?.countryMoneySymbol} ${decimalFormat.format(precio)}"
    }

    fun getConfiguration() {
        GlobalScope.launch(Dispatchers.IO) {
            userUseCase.getUser()?.let {
                user = it
                decimalFormat = CountryUtil.getDecimalFormatByISO(user?.countryISO, true)
                GlobalScope.launch(Dispatchers.Main) {
                    view?.onGetConfiguration(it)
                }
            }
        }
    }
}
