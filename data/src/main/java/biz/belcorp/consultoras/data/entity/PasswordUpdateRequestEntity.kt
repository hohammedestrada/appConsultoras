package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

class PasswordUpdateRequestEntity {

    @SerializedName("anteriorContrasenia")
    var anteriorContrasenia: String? = null
    @SerializedName("nuevaContrasenia")
    var nuevaContrasenia: String? = null
}
