package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

/**
 *
 */
class UserClientsResumeEntity {

    @SerializedName("Total")
    var total: Int? = null
    @SerializedName("Mensaje")
    var mensaje: String? = null
}
