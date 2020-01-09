package biz.belcorp.consultoras.data.entity.survey

import com.google.gson.annotations.SerializedName

class OptionSurveyEntity {

    @SerializedName("EncuestaID")
    var encuestaID: Int? = null

    @SerializedName("CalificacionID")
    var calificacionID: Int? = null

    @SerializedName("Calificacion")
    var calificacion: String? = null

    @SerializedName("CampaniaID")
    var campaniaID: Int? = null

    @SerializedName("PreguntaDescripcion")
    var preguntaDescripcion: String? = null

    @SerializedName("MotivoEncuesta")
    var motivoEncuesta: MutableList<ReasonEntity>? = null
}
