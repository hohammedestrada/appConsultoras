package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

class UploadFileResponseEntity {

    @SerializedName("UrlImagen")
    var urlImage: String? = null

    @SerializedName("NombreImagen")
    var nameImage: String? = null
}
