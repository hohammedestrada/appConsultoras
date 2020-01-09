package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

class PedidoReservaRequest {

    @SerializedName("CampaniaID")
    var campaniaId : Int? = null

    @SerializedName("AceptacionConsultoraDA")
    var aceptacionConsultoraDA : Int? = null

    @SerializedName("FechaInicioFacturacion")
    var fechaInicioFacturacion : String? = null

    @SerializedName("FechaFinFacturacion")
    var fechaFinFacturacion : String? = null

    @SerializedName("DiasAntes")
    var diasAntes : Int? = null

    @SerializedName("HoraInicioNoFacturable")
    var horaInicioNoFacturable : String? = null

    @SerializedName("HoraCierreNoFacturable")
    var horaCierreNoFacturable : String? = null

    @SerializedName("HoraInicio")
    var horaInicio : String? = null

    @SerializedName("HoraFin")
    var horaFin : String? = null

    @SerializedName("SegmentoInternoID")
    var segmentoInternoID : Int? = null

    @SerializedName("PROLSinStock")
    var isPROLSinStock : Boolean? = null

    @SerializedName("MontoMinimoPedido")
    var montoMinimoPedido : BigDecimal? = null

    @SerializedName("MontoMaximoPedido")
    var montoMaximoPedido : BigDecimal? = null

    @SerializedName("ConsultoraNueva")
    var consultoraNueva : Int? = null

    @SerializedName("ValidacionAbierta")
    var isValidacionAbierta : Boolean? = null

    @SerializedName("ZonaValida")
    var isZonaValida : Boolean? = null

    @SerializedName("ValidacionInteractiva")
    var isValidacionInteractiva : Boolean? = null

    @SerializedName("CodigosConcursos")
    var codigosConcursos : String? = null

    @SerializedName("CodigoPrograma")
    var codigoPrograma : String? = null

    @SerializedName("ConsecutivoNueva")
    var consecutivoNueva : Int? = null

    @SerializedName("DiaPROL")
    var isDiaPROL : Boolean? = null

    @SerializedName("CodigoZona")
    var codigoZona : String? = null

    @SerializedName("MontoMaximoDesviacion")
    var montoMaximoDesviacion : BigDecimal? = null

}
