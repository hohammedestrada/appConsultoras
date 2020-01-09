package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

import java.util.ArrayList

/**
 * Clase de configuracion
 */

class ConfigResponseEntity {

    @SerializedName("TextoSaludo")
    var textGreeting: String? = null

    @SerializedName("UrlImagenFondoEsika")
    var urlImageEsikaBackground: String? = null

    @SerializedName("UrlImagenLogoEsika")
    var urlImageEsikaLogo: String? = null

    @SerializedName("UrlImagenFondoLbel")
    var urlImageLBelBackground: String? = null

    @SerializedName("UrlImagenLogoLbel")
    var urlImageLBelLogo: String? = null

    @SerializedName("IdContenido")
    var idVideo: String? = null

    @SerializedName("UrlVideo")
    var urlVideo: String? = null

    @SerializedName("ListaPaises")
    var countries: List<CountryEntity?>? = null

    @SerializedName("Apps")
    var apps: List<AppEntity?>? = null

    @SerializedName("OrigenPedidoWeb")
    var origenPedidoWeb: List<OrigenPedidoWebEntity?>? = null

    init {
        this.countries = ArrayList()
        this.apps = ArrayList()
        this.origenPedidoWeb = ArrayList()
    }

}
