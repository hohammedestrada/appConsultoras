package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

class RelatedOfferRequest {

    @SerializedName("CampaniaID")
    var campaniaId : Int? = null

    @SerializedName("CodigoZona")
    var codigoZona : String? = null

    @SerializedName("CUV")
    var cuv : String? = null

    @SerializedName("CodigoProducto")
    var codigoProducto : String? = null

    @SerializedName("MinimoResultados")
    var minimoResultados : Int? = null

    @SerializedName("MaximoResultados")
    var maximoResultados : Int? = null

    @SerializedName("CaracteresDescripcion")
    var caracteresDescripcion : Int? = null

    @SerializedName("Personalizaciones")
    var personalizaciones : String? = null

    @SerializedName("FechaInicioFacturacion")
    var fechaInicioFacturacion: String = ""

    @SerializedName("Configuracion")
    var configuracion : SearchConfiguracionEntity? = null

}
