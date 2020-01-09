package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

class PedidoOfertaFinalInsertRequest{

    @SerializedName("CampaniaID")
    var campaniaId : Int? = null

    @SerializedName("NroCampanias")
    var nroCampanias : Int? = null

    @SerializedName("Cantidad")
    var cantidad : Int? = null

    @SerializedName("IndicadorGPRSB")
    var indicadorGPRSB : Int? = null

    @SerializedName("ConsecutivoNueva")
    var consecutivoNueva : Int? = null

    @SerializedName("ConsultoraAsociada")
    var consultoraAsociada : String? = null

    @SerializedName("CodigoPrograma")
    var codigoPrograma : String? = null

    @SerializedName("IPUsuario")
    var ipUsuario : String? = null

    @SerializedName("Identifier")
    var identifier : String? = null

    @SerializedName("AceptacionConsultoraDA")
    var aceptacionConsultoraDA : Int? = null

    @SerializedName("MontoMaximoPedido")
    var montoMaximoPedido : Double? = null

    @SerializedName("FechaInicioFacturacion")
    var fechaInicioFacturacion : String? = null

    @SerializedName("FechaFinFacturacion")
    var fechaFinFacturacion : String? = null

    @SerializedName("ConsultoraNueva")
    var consultoraNueva : Int? = null

    @SerializedName("CodigosConcursos")
    var codigosConcursos : String? = null

    @SerializedName("Producto")
    var producto : OfertaFinalEntity? = null

}
