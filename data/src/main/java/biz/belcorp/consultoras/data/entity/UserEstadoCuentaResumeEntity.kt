package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

/**
 *
 */
class UserEstadoCuentaResumeEntity {

    @SerializedName("Deuda")
    var deuda: Double? = null
    @SerializedName("DeudaFormatted")
    var deudaFormatted: String? = null
    @SerializedName("FechaVencimiento")
    var fechaVencimiento: String? = null
}
