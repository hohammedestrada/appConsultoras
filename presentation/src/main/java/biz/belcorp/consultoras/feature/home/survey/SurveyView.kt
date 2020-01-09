package biz.belcorp.consultoras.feature.home.survey

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.model.survey.OptionSurveyModel
import biz.belcorp.consultoras.common.view.LoadingView

interface SurveyView : View {
    fun showSurvey(surveyModel: List<OptionSurveyModel>, campaign: String?)
    fun onSurveyIsResolved()
    fun onSendAnswerSurveySuccessful()
    fun showLoading()
    fun hideLoading()
}
