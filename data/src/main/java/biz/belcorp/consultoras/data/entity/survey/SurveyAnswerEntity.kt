package biz.belcorp.consultoras.data.entity.survey

import com.google.gson.annotations.SerializedName

class SurveyAnswerEntity {

    @SerializedName("EncuestaCalificacionId")
    var encuestaCalificacionId : Int? = null

    @SerializedName("EncuestaId")
    var encuestaId : Int? = null

    @SerializedName("CreatedBy")
    var createdBy : String? = null

    @SerializedName("CreateHost")
    var createHost : String? = null

    @SerializedName("CodigoCampania")
    var codigoCampania : String? = null

    @SerializedName("respuestas")
    var respuestas : MutableList<ReasonEntity>? = null
}
