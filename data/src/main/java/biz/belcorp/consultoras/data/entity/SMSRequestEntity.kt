package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

class SMSRequestEntity {

    @SerializedName("CampaniaID")
    var campaniaID: Int? = null

    @SerializedName("CelularActual")
    var celularActual: String? = null

    @SerializedName("CelularNuevo")
    var celularNuevo: String? = null

    @SerializedName("CodigoSms")
    var codigoSMS: String? = null
    @SerializedName("SoloValidar")
    var soloValidar: Boolean? = null

    @field:SerializedName("OrigenID")
    var origenID: Int? = null

    @field:SerializedName("OrigenDescripcion")
    var origenDescripcion: String? = null

    @field:SerializedName("EstadoActividadID")
    var estadoActividadID: Int? = null

}
