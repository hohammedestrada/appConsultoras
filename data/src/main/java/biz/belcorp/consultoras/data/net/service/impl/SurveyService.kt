package biz.belcorp.consultoras.data.net.service.impl

import android.content.Context
import biz.belcorp.consultoras.data.entity.survey.OptionSurveyEntity
import biz.belcorp.consultoras.data.entity.survey.SurveyAnswerEntity
import biz.belcorp.consultoras.data.net.RestApi
import biz.belcorp.consultoras.data.net.service.ISurveyService
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.library.net.AccessToken
import kotlinx.coroutines.Deferred

class SurveyService(context: Context, accessToken: AccessToken?, appName: String?,
                    appCountry: String?) : BaseService(context), ISurveyService {

    private val service: ISurveyService = RestApi.create(ISurveyService::class.java, accessToken, appName, appCountry)

    override fun getSurvey(validSurvey: Int?): Deferred<List<OptionSurveyEntity?>?> {
        if (!isThereInternetConnection)
            throw NetworkErrorException()
        else
            return service.getSurvey(validSurvey)
    }

    override fun sendAnswerSurvey(surveyAnswerEntity: SurveyAnswerEntity?): Deferred<String> {
        if (!isThereInternetConnection)
            throw NetworkErrorException()
        else
            return service.sendAnswerSurvey(surveyAnswerEntity)
    }
}
