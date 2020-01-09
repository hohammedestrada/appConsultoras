package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.survey.OptionSurveyEntity
import biz.belcorp.consultoras.data.entity.survey.ReasonEntity
import biz.belcorp.consultoras.data.entity.survey.SurveyAnswerEntity
import biz.belcorp.consultoras.domain.entity.survey.OptionSurvey
import biz.belcorp.consultoras.domain.entity.survey.Reason
import biz.belcorp.consultoras.domain.entity.survey.SurveyAnswer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SurveyEntityDataMapper @Inject
internal constructor(){

    fun transform(list : List<OptionSurveyEntity?>?) : List<OptionSurvey> {
        val listOption: MutableList<OptionSurvey> = mutableListOf()
        list?.map {
            it?.let { it2 ->
                listOption.add(transformOptionSurvey(it2))
            }
        }
        return listOption
    }

    fun transformSurveyAnswer(surveyAnswer : SurveyAnswer?) : SurveyAnswerEntity {
        return SurveyAnswerEntity().apply {
            surveyAnswer?.let {
                encuestaCalificacionId = it.encuestaCalificacionId
                encuestaId = it.encuestaId
                createdBy = it.createdBy
                createHost = it.createHost
                codigoCampania = it.codigoCampania
                respuestas = transformReasonList(it.respuestas)
            }
        }
    }

    private fun transformOptionSurvey(optionSurveyEntity: OptionSurveyEntity) : OptionSurvey{
        return OptionSurvey().apply {
            optionSurveyEntity.let {
                encuestaID = it.encuestaID
                calificacionID = it.calificacionID
                calificacion = it.calificacion
                campaniaID = it.campaniaID
                preguntaDescripcion = it.preguntaDescripcion
                motivoEncuesta = transformReasonEntityList(it.motivoEncuesta)
            }
        }
    }

    private fun transformReasonEntityList(list : MutableList<ReasonEntity>?) : MutableList<Reason>{
        val listReason = mutableListOf<Reason>()
        list?.map {
            listReason.add(Reason().apply {
                motivoID = it.motivoID
                motivo = it.motivo
                tipoMotivo = it.tipoMotivo
            })
        }
        return listReason
    }

    private fun transformReasonList(list : MutableList<Reason>?) : MutableList<ReasonEntity>{
        val listReason = mutableListOf<ReasonEntity>()
        list?.map {
            listReason.add(ReasonEntity().apply {
                motivoID = it.motivoID
                motivo = it.motivo
                tipoMotivo = it.tipoMotivo
            })
        }
        return listReason
    }
}
