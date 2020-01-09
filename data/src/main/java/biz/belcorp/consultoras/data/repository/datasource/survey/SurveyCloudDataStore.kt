package biz.belcorp.consultoras.data.repository.datasource.survey

import biz.belcorp.consultoras.data.entity.survey.OptionSurveyEntity
import biz.belcorp.consultoras.data.entity.survey.SurveyAnswerEntity
import biz.belcorp.consultoras.data.net.service.ISurveyService
import kotlinx.coroutines.Deferred

class SurveyCloudDataStore internal constructor(private val service: ISurveyService) : SurveyDataStore {

    override fun getSurvey(validSurvey: Int?): Deferred<List<OptionSurveyEntity?>?> {
        return service.getSurvey(validSurvey)
    }

    override fun sendAnswerSurvey(surveyAnswerEntity: SurveyAnswerEntity?): Deferred<String> {
        return service.sendAnswerSurvey(surveyAnswerEntity)
    }
}
