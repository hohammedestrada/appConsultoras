package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

class PedidoUpdateRequest{

    @SerializedName("PedidoID")
    var pedidoID : Int? = null

    @SerializedName("PedidoDetalleID")
    var pedidoDetalleID : Int? = null

    @SerializedName("CampaniaID")
    var campaniaId : Int? = null

    @SerializedName("Cantidad")
    var cantidad : Int? = null

    @SerializedName("ClienteID")
    var clienteID : Int? = null

    @SerializedName("ClienteDescripcion")
    var clienteDescripcion : String? = null

    @SerializedName("Identifier")
    var identifier : String? = null

    @SerializedName("CodigosConcursos")
    var codigosConcursos : String? = null

    @SerializedName("IPUsuario")
    var ipUsuario : String? = null

    @SerializedName("AceptacionConsultoraDA")
    var aceptacionConsultoraDA : Int? = null

    @SerializedName("MontoMaximoPedido")
    var montoMaximoPedido : Double? = null

    @SerializedName("CodigoPrograma")
    var codigoPrograma : String? = null

    @SerializedName("ConsecutivoNueva")
    var consecutivoNueva : Int? = null

    @SerializedName("FechaInicioFacturacion")
    var fechaInicioFacturacion : String? = null

    @SerializedName("FechaFinFacturacion")
    var fechaFinFacturacion : String? = null

    @SerializedName("CUV")
    var cuv : String? = null

    @SerializedName("PrecioCatalogo")
    var precioCatalogo : BigDecimal? = null

    @SerializedName("TipoEstrategiaID")
    var tipoEstrategiaID : String? = null

    @SerializedName("TipoOfertaSisID")
    var tipoOfertaSisID : Int? = null

    @SerializedName("SetID")
    var setID : Int? = null

    // Cambios para Pedido Reservado
    @SerializedName(value = "SegmentoInternoID")
    var segmentoInternoID: Int? = 0

    @SerializedName(value = "MontoMinimoPedido")
    var montoMinimoPedido: Double? = 0.0

    @SerializedName(value = "ValidacionAbierta")
    var isValidacionAbierta: Boolean? = false

    @SerializedName(value = "ZonaValida")
    var isZonaValida: Boolean? = false

    @SerializedName(value = "ValidacionInteractiva")
    var isValidacionInteractiva : Boolean? = false

    @SerializedName(value = "DiaPROL")
    var isDiaProl: Boolean? = false

    @SerializedName(value = "CodigoZona")
    var codigoZona: String? = ""

    @SerializedName(value = "CodigoRegion")
    var codigoRegion: String? = ""

    @SerializedName(value = "UsuarioPrueba")
    var isUsuarioPrueba: Boolean? = false

    @SerializedName(value = "Simbolo")
    var simbolo : String? = ""

}
