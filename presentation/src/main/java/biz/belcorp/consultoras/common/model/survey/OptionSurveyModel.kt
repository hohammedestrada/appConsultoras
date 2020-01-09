package biz.belcorp.consultoras.common.model.survey

class OptionSurveyModel {
    var encuestaID: Int? = null

    var calificacionID: Int? = null

    var calificacion: String? = null

    var campaniaID: Int? = null

    var preguntaDescripcion: String? = null

    var motivoEncuesta: MutableList<ReasonModel>? = null

    var isSelected: Boolean = false

    override fun toString(): String {
        return "$encuestaID - $calificacionID - $calificacion - $campaniaID - $preguntaDescripcion - ${motivoEncuesta?.size} "
    }
}
