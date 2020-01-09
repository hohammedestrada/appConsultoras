package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

data class PasswordUpdate2RequestEntity(
    @SerializedName("NuevaContrasenia")
    var nuevaContrasenia: String? = null)
