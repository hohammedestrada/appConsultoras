package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

class BannerLanzamientoEntity {

    @SerializedName("Grupo")
    var grupo: String? = null

    @SerializedName("Codigo")
    var codigo: String? = null

    @SerializedName("Url")
    var url: String? = null

    @SerializedName("Accion")
    var accion: String? = null

    @SerializedName("Orden")
    var orden: String? = null

    @SerializedName("IdContenido")
    var idContenido:String? = null

}
