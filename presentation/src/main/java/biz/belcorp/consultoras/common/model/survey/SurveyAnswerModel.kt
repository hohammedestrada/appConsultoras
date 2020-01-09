package biz.belcorp.consultoras.common.model.survey

class SurveyAnswerModel {
    var encuestaCalificacionId : Int? = null

    var encuestaId : Int? = null

    var createdBy : String? = null

    var createHost : String? = null

    var codigoCampania : String? = null

    var respuestas : MutableList<ReasonModel>? = null

    override fun toString(): String {
        return "DATA_SURVEY_ANSWER: $encuestaCalificacionId - $encuestaId - $createdBy $createHost - $codigoCampania - ${respuestas?.size}"
    }
}
