package biz.belcorp.consultoras.domain.entity.survey

class OptionSurvey {

    var encuestaID: Int? = null

    var calificacionID: Int? = null

    var calificacion: String? = null

    var campaniaID: Int? = null

    var preguntaDescripcion: String? = null

    var motivoEncuesta: MutableList<Reason>? = null
}
