package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

data class TokenEntity(
    @SerializedName("access_token")
    var access_token: String? = null,
    @SerializedName("token_expires")
    var token_expires: String? = null
)
