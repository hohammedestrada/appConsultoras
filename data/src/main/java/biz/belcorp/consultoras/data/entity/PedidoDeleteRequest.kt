package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

class PedidoDeleteRequest {

    @SerializedName("CampaniaID")
    var campaniaId: Int? = null

    @SerializedName("AceptacionConsultoraDA")
    var aceptacionConsultoraDA: Int? = null

    @SerializedName("PedidoID")
    var pedidoID: Int? = null

    @SerializedName("PedidoDetalleID")
    var pedidoDetalleID: Int? = null

    @SerializedName("TipoOfertaSisID")
    var tipoOfertaSisID: Int? = null

    @SerializedName("CUV")
    var cuv: String? = null

    @SerializedName("Cantidad")
    var cantidad: Int? = null

    @SerializedName("ObservacionPROL")
    var observacionPROL: String? = null

    @SerializedName("Identifier")
    var identifier: String? = null

    @SerializedName("CodigoPrograma")
    var codigoPrograma: String? = null

    @SerializedName("ConsecutivoNueva")
    var consecutivoNueva: Int? = null

    @SerializedName("ZonaValida")
    var zonaValida: Boolean? = null

    @SerializedName("CodigosConcursos")
    var codigosConcursos: String? = null

    @SerializedName("NroCampanias")
    var nroCampanias: Int? = null

    @SerializedName("SetID")
    var setID: Int? = null


    // Cambios para Pedido Reservado
    @SerializedName(value = "SegmentoInternoID")
    var segmentoInternoID: Int? = 0

    @SerializedName(value = "MontoMinimoPedido")
    var montoMinimoPedido: Double? = 0.0

    @SerializedName(value = "MontoMaximoPedido")
    var montoMaximoPedido: Double? = 0.0

    @SerializedName(value = "ConsultoraNueva")
    var consultoraNueva: Int? = 0

    @SerializedName(value = "ValidacionAbierta")
    var isValidacionAbierta: Boolean? = false

    @SerializedName(value = "ValidacionInteractiva")
    var isValidacionInteractiva: Boolean? = false

    @SerializedName(value = "DiaPROL")
    var isDiaProl: Boolean? = false

    @SerializedName(value = "CodigoZona")
    var codigoZona: String? = ""

    @SerializedName(value = "CodigoRegion")
    var codigoRegion: String? = ""

    @SerializedName(value = "CodigoSeccion")
    var codigoSeccion: String? = ""

    @SerializedName(value = "UsuarioPrueba")
    var isUsuarioPrueba: Boolean? = false

    @SerializedName(value = "Simbolo")
    var simbolo: String? = ""

    @SerializedName(value = "FechaInicioFacturacion")
    var fechaInicioFacturacion: String? = ""

    @SerializedName(value = "FechaFinFacturacion")
    var fechaFinFacturacion: String? = ""

    @SerializedName(value = "ReemplazarFestival")
    var reemplazarFestival: Boolean? = false


}
