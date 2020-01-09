package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

class ContenidoUpdateRequest {

    @SerializedName("CampaniaID")
    var campaniaID: Int? = null

    @SerializedName("CodigoRegion")
    var codigoRegion: String? = null

    @SerializedName("CodigoZona")
    var codigoZona: String? = null

    @SerializedName("CodigoSeccion")
    var codigoSeccion: String? = null

    @SerializedName("indicadorConsultoraDigital")
    var indicadorConsultoraDigital: String? = null

    @SerializedName("NumeroDocumento")
    var numeroDocumento: String? = null

    @SerializedName("IdContenidoDetalle")
    var idContenidoDetalle: Int? = null

    @SerializedName("CodigoContenidoDetalle")
    var CodigoContenidoDetalle: String? = null

}
