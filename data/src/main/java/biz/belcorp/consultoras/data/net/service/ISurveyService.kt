package biz.belcorp.consultoras.data.net.service

import biz.belcorp.consultoras.data.entity.survey.OptionSurveyEntity
import biz.belcorp.consultoras.data.entity.survey.SurveyAnswerEntity
import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.library.net.dto.ServiceDto
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface ISurveyService {

    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.3/Consultora/Encuesta")
    fun getSurvey(@Query("validaEncuesta") validSurvey: Int?): Deferred<List<OptionSurveyEntity?>?>

    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "api/v1.3/Consultora/Encuesta/Respuesta")
    fun sendAnswerSurvey(@Body surveyAnswerEntity : SurveyAnswerEntity?) : Deferred<String>

}


