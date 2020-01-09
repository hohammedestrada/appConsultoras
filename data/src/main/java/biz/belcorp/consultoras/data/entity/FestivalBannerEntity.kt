package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

class FestivalBannerEntity {

    @SerializedName("BannerImgDesktop")
    var bannerImgDesktop: String? = null

    @SerializedName("BannerImgMobile")
    var bannerImgMobile: String? = null

    @SerializedName("BannerImgProducto")
    var bannerImgProducto: String? = null

    @SerializedName("BannerFondoColorInicio")
    var bannerFondoColorInicio: String? = null

    @SerializedName("BannerFondoColorFin")
    var bannerFondoColorFin: String? = null

    @SerializedName("BannerFondoColorDireccion")
    var bannerFondoColorDireccion: Int? = null

    @SerializedName("BannerColorTexto")
    var bannerColorTexto: String? = null

    @SerializedName("BannerDescripcion")
    var bannerDescripcion: String? = null
}
