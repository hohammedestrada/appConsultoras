package biz.belcorp.consultoras.feature.home.survey

import android.text.TextUtils
import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.common.model.survey.OptionSurveyModel
import biz.belcorp.consultoras.common.model.survey.SurveyAnswerModel
import biz.belcorp.consultoras.common.model.survey.SurveyModelDataMapper
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.interactor.AccountUseCase
import biz.belcorp.consultoras.domain.interactor.SurveyUseCase
import biz.belcorp.consultoras.domain.interactor.UserUseCase
import biz.belcorp.consultoras.util.anotation.SurveyValidationType
import biz.belcorp.consultoras.util.anotation.UserConfigAccountCode
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.util.NetworkUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class SurveyPresenter
@Inject
internal constructor(
    private val userUseCase: UserUseCase,
    private val surveyUseCase: SurveyUseCase,
    private val accountUseCase: AccountUseCase,
    private val surveyModelDataMapper: SurveyModelDataMapper) : Presenter<SurveyView> {

    var view: SurveyView? = null
    var validationType = 0
    var campaign: String? = null
    var encuestaID : Int = 0
    var codigoCampania : Int = 0
    var anteriorCalificacionSeleccionada = ""
    var user : User? = null
    var motivosString = ""

    fun getSurvey() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val userconfig = accountUseCase.getConfig(UserConfigAccountCode.FUNCIONALIDAD_HOME)

                var isActiveSurvey = false
                userconfig.forEach {
                    if (it.code == UserConfigAccountCode.ENCUESTA){
                        isActiveSurvey = true
                        return@forEach
                    }
                }
                if (isActiveSurvey){
                    val survey = surveyUseCase.getSurvey(validationType)
                    GlobalScope.launch(Dispatchers.Main) {
                        if (survey.isEmpty()){
                            view?.onSurveyIsResolved()
                        } else {
                            var title = if(validationType == SurveyValidationType.VALIDATION_SURVEY_HOME) "${survey[0]?.campaniaID}" else campaign
                            if (!TextUtils.isEmpty(title) && title?.length == 6) {
                                title = title.substring(4)
                            }
                            this@SurveyPresenter.encuestaID = survey[0]?.encuestaID ?: 0
                            this@SurveyPresenter.codigoCampania = survey[0]?.campaniaID ?: 0
                            view?.showSurvey(surveyModelDataMapper.transform(survey), title)
                            val user = userUseCase.getUser()
                            Tracker.Survey.trackScreenEncuesta(user)
                        }
                    }
                } else {
                    launch(Dispatchers.Main) {
                        view?.onSurveyIsResolved()
                    }
                }
            } catch (e: Exception) {
                BelcorpLogger.e(e)
            }
        }
    }

    fun fillUser(){
        GlobalScope.launch(Dispatchers.IO) {
            user = userUseCase.getUser()
        }
    }

    fun sendAnswer(optionSurveyModel: OptionSurveyModel?, isShowSuccessful: Boolean = true) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val surveyAnswer = SurveyAnswerModel()
                surveyAnswer.encuestaCalificacionId = optionSurveyModel?.calificacionID
                surveyAnswer.encuestaId = optionSurveyModel?.encuestaID
                surveyAnswer.createdBy = user?.primerNombre
                surveyAnswer.createHost = NetworkUtil.getIPAddress(true)
                surveyAnswer.codigoCampania = if(validationType == SurveyValidationType.VALIDATION_SURVEY_HOME) optionSurveyModel?.campaniaID.toString() else campaign
                val filterListSelected = optionSurveyModel?.motivoEncuesta?.filter { it.isSelect }?.toMutableList()
                    ?: mutableListOf()
                surveyAnswer.respuestas = filterListSelected

                val user = userUseCase.getUser()
                optionSurveyModel?.let {
                    filterListSelected?.let { list->
                        motivosString = ""
                        list.forEachIndexed{index , item ->
                            if(index == (list.size - 1)){
                                motivosString += item.motivo
                            }else{
                                motivosString += item.motivo + "-"
                            }
                        }
                    }


                }

                surveyUseCase.sendAnswerSurvey(surveyModelDataMapper.transformSurveyAnswer(surveyAnswer))

                GlobalScope.launch(Dispatchers.Main) {
                    if (isShowSuccessful) {
                        view?.hideLoading()
                        view?.onSendAnswerSurveySuccessful()

                        Tracker.Survey.trackScreenGracias(user)
                    }
                }
            } catch (e: Exception) {
                view?.hideLoading()
                BelcorpLogger.e(e)
            }
        }
    }

    override fun attachView(view: SurveyView) {
        this.view = view
        GlobalScope.launch(Dispatchers.IO) {
            user = userUseCase.getUser()
        }
    }

    override fun resume() {
        // Empty
    }

    override fun pause() {
        // Empty
    }

    override fun destroy() {
        userUseCase.dispose()
        surveyUseCase.dispose()
    }
}
