package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

class AnalyticsTokenRequestEntity {
    @SerializedName("grant_type")
    var grantType: String? = null
}
