package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

/**
 *
 */
class UserIncentivesResumeEntity {

    @SerializedName("PuntosFaltantes")
    var puntosFaltantes: Int? = null
    @SerializedName("HayIncentivos")
    var hayIncentivos: Boolean? = null
    @SerializedName("PremioNombre")
    var giftName: String? = null
    @SerializedName("Mensaje")
    var mensaje: String? = null
}
