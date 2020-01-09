package biz.belcorp.consultoras.data.entity.survey

import com.google.gson.annotations.SerializedName

class ReasonEntity {

    @SerializedName("MotivoID")
    var motivoID: Int? = null

    @SerializedName("Motivo")
    var motivo: String? = null

    @SerializedName("TipoMotivo")
    var tipoMotivo: Int? = null
}
