package biz.belcorp.consultoras.domain.entity.survey

class SurveyAnswer {

    var encuestaCalificacionId : Int? = null

    var encuestaId : Int? = null

    var createdBy : String? = null

    var createHost : String? = null

    var codigoCampania : String? = null

    var respuestas : MutableList<Reason>? = null
}
