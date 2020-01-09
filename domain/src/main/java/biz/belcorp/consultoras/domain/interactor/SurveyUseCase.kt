package biz.belcorp.consultoras.domain.interactor

import biz.belcorp.consultoras.domain.entity.BasicDto
import biz.belcorp.consultoras.domain.entity.survey.OptionSurvey
import biz.belcorp.consultoras.domain.entity.survey.SurveyAnswer
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.repository.SurveyRepository
import javax.inject.Inject

class SurveyUseCase @Inject
constructor(
    threadExecutor: ThreadExecutor
    , postExecutionThread: PostExecutionThread,
    private val surveyDataRepository: SurveyRepository) : UseCase(threadExecutor, postExecutionThread) {

    suspend fun getSurvey(validSurvey: Int?): List<OptionSurvey?> {
        return surveyDataRepository.getSurvey(validSurvey)
    }

    suspend fun sendAnswerSurvey(surveyAnswerEntity : SurveyAnswer?) : String {
        return surveyDataRepository.sendAnswerSurvey(surveyAnswerEntity)
    }
}
