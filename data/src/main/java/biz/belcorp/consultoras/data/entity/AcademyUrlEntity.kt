package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

class AcademyUrlEntity{

    @SerializedName(value = "UrlMiAcademia")
    var urlMiAcademia: String? = null

    @SerializedName(value = "Token")
    var token: String? = null

}
