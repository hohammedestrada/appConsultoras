package biz.belcorp.consultoras.data.repository

import biz.belcorp.consultoras.data.mapper.BasicDtoDataMapper
import biz.belcorp.consultoras.data.mapper.SurveyEntityDataMapper
import biz.belcorp.consultoras.data.repository.datasource.survey.SurveyDataStoreFactory
import biz.belcorp.consultoras.domain.entity.BasicDto
import biz.belcorp.consultoras.domain.entity.survey.OptionSurvey
import biz.belcorp.consultoras.domain.entity.survey.SurveyAnswer
import biz.belcorp.consultoras.domain.repository.SurveyRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SurveyDataRepository @Inject
internal constructor(
    private var surveyDataStoreFactory: SurveyDataStoreFactory,
    private var surveyEntityMapper: SurveyEntityDataMapper,
    private val basicDtoDataMapper: BasicDtoDataMapper
) : SurveyRepository {

    override suspend fun getSurvey(validSurvey: Int?): List<OptionSurvey?> {
        val storieDataStore = surveyDataStoreFactory.createCloudDataStore()
        return surveyEntityMapper.transform(storieDataStore.getSurvey(validSurvey).await())
    }

    override suspend fun sendAnswerSurvey(surveyAnswer: SurveyAnswer?): String {
        val storieDataStore = surveyDataStoreFactory.createCloudDataStore()
        val suveyAnswerEntity = surveyEntityMapper.transformSurveyAnswer(surveyAnswer)
        return storieDataStore.sendAnswerSurvey(suveyAnswerEntity).await()
    }
}
