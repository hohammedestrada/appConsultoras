package biz.belcorp.consultoras.data.repository.datasource.survey

import biz.belcorp.consultoras.data.entity.survey.OptionSurveyEntity
import biz.belcorp.consultoras.data.entity.survey.SurveyAnswerEntity
import biz.belcorp.library.net.dto.ServiceDto
import kotlinx.coroutines.Deferred

interface SurveyDataStore {
    fun getSurvey(validSurvey: Int?): Deferred<List<OptionSurveyEntity?>?>

    fun sendAnswerSurvey(surveyAnswerEntity : SurveyAnswerEntity?) : Deferred<String>
}
