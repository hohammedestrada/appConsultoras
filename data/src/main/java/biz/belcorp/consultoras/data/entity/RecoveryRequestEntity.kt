package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

class RecoveryRequestEntity {

    @SerializedName("PaisID")
    var countryID: Int = 0

    @SerializedName("Correo")
    var username: String? = null
}
