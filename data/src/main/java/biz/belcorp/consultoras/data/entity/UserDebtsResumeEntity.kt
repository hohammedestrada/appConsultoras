package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

/**
 *
 */
class UserDebtsResumeEntity {

    @SerializedName("MontoCobro")
    var montoCobro: Double? = null
    @SerializedName("Deudores")
    var deudores: Int? = null
    @SerializedName("Mensaje")
    var mensaje: String? = null
}
