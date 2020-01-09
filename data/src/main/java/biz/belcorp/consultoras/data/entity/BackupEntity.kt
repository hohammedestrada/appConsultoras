package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

import java.io.Serializable

class BackupEntity : Serializable {

    @SerializedName("cliente")
    var cliente: String? = null

    @SerializedName("anotacion")
    var anotacion: String? = null

    @SerializedName("contacto")
    var contacto: String? = null

    @SerializedName("recordatorio")
    var recordatorio: String? = null

    @SerializedName("campania")
    var campania: String? = null

    @SerializedName("concurso")
    var concurso: String? = null

    @SerializedName("movimiento")
    var movimiento: String? = null

    @SerializedName("producto")
    var producto: String? = null

    @SerializedName("config")
    var config: String? = null

    @SerializedName("country")
    var country: String? = null

    @SerializedName("facebook")
    var facebook: String? = null

    @SerializedName("menu")
    var menu: String? = null

    @SerializedName("premio")
    var premio: String? = null

    @SerializedName("user")
    var user: String? = null

    @SerializedName("detalleUser")
    var detalleUser: String? = null

    @SerializedName("origenPedidoWebLocal")
    var origenPedidoWebLocal: String? = null

    @SerializedName("origenMarcacionWebLocal")
    var origenMarcacionWebLocal: String? = null

    @SerializedName("palancaWebLocal")
    var palancaWebLocal: String? = null

    @SerializedName("subseccionWebLocal")
    var subseccionWebLocal: String? = null


    @SerializedName("bannerSello")
    var bannerSello: String? = null

    @SerializedName("festivalCategoria")
    var festivalCategoria: String? = null

    @SerializedName("configFestival")
    var configFestival: String? = null

    @SerializedName("searchRecentOffer")
    var searchRecentOffer: String? = null

    @SerializedName("ofertaFinalEstado")
    var ofertaFinalEstado: String? = null
}
