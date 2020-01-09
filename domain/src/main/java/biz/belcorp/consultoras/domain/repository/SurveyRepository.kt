package biz.belcorp.consultoras.domain.repository

import biz.belcorp.consultoras.domain.entity.survey.OptionSurvey
import biz.belcorp.consultoras.domain.entity.survey.SurveyAnswer

interface SurveyRepository {
    suspend fun getSurvey(validSurvey: Int?) : List<OptionSurvey?>

    suspend fun sendAnswerSurvey(surveyAnswerEntity : SurveyAnswer?) : String
}
