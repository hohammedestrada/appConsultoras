package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

/**
 *
 */
class UserGananciasResumeEntity {

    @SerializedName("Monto")
    var monto: Double? = null
    @SerializedName("MensajeCabecera")
    var mensajeCabecera: String? = null
    @SerializedName("MensajePie")
    var mensajePie: String? = null
}
