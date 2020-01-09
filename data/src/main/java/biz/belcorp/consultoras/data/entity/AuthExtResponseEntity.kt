package biz.belcorp.consultoras.data.entity


import com.google.gson.annotations.SerializedName

import java.util.ArrayList

/**
 * Clase de configuracion
 */

class AuthExtResponseEntity {

    @SerializedName("access_token")
    var accessToken: String? = null

    @SerializedName("token_expires")
    var tokenExpires: String? = null


    init {
        this.accessToken = ""
        this.tokenExpires = ""

    }

}
