package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.domain.entity.AnalyticsToken
import com.google.gson.annotations.SerializedName

class AnalyticsTokenResponseEntity {
    @SerializedName("access_token")
    var accessToken: String? = null
    @SerializedName("token_expires")
    var token_expires: String? = null

    companion object{
        fun transform(input: AnalyticsTokenResponseEntity?): AnalyticsToken? {
            input?.run {
                return AnalyticsToken(
                    accessToken,
                    token_expires)
            }
            return null
        }
    }
}
