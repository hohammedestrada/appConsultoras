package biz.belcorp.consultoras.data.net.dto

import com.google.gson.annotations.SerializedName

class AuthErrorDto {
    @SerializedName("error")
    var error: String? = null
    @SerializedName("error_description")
    var description: String? = null
}
