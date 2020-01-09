package biz.belcorp.consultoras.common.model.survey

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.survey.OptionSurvey
import biz.belcorp.consultoras.domain.entity.survey.Reason
import biz.belcorp.consultoras.domain.entity.survey.SurveyAnswer
import javax.inject.Inject

@PerActivity
class SurveyModelDataMapper @Inject internal constructor() {

    fun transform(list: List<OptionSurvey?>): List<OptionSurveyModel> {
        val listOption: MutableList<OptionSurveyModel> = mutableListOf()
        list.map {
            it?.let { it2 ->
                listOption.add(transformOptionSurvey(it2))
            }
        }
        return listOption
    }

    private fun transformOptionSurvey(optionSurveyEntity: OptionSurvey): OptionSurveyModel {
        return OptionSurveyModel().apply {
            optionSurveyEntity.let {
                encuestaID = it.encuestaID
                calificacionID = it.calificacionID
                calificacion = it.calificacion
                campaniaID = it.campaniaID
                preguntaDescripcion = it.preguntaDescripcion
                motivoEncuesta = transformReasonList(it.motivoEncuesta)
            }
        }
    }

    private fun transformReasonList(list: MutableList<Reason>?): MutableList<ReasonModel> {
        val listReason = mutableListOf<ReasonModel>()
        list?.map {
            listReason.add(ReasonModel().apply {
                motivoID = it.motivoID
                motivo = it.motivo
                tipoMotivo = it.tipoMotivo
            })
        }
        return listReason
    }

    private fun transformReasonModelList(list: MutableList<ReasonModel>?): MutableList<Reason> {
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

    fun transformSurveyAnswer(surveyAnswerModel : SurveyAnswerModel?) : SurveyAnswer {
        return SurveyAnswer().apply {
            surveyAnswerModel?.let {
                encuestaCalificacionId = it.encuestaCalificacionId
                encuestaId = it.encuestaId
                createdBy = it.createdBy
                createHost = it.createHost
                codigoCampania = it.codigoCampania
                respuestas = transformReasonModelList(it.respuestas)
            }
        }
    }
}
