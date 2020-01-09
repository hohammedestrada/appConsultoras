package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

class PedidoOfertaFinalRequest {

    @SerializedName("CampaniaID")
    var campaniaId : Int? = null

    @SerializedName("NroCampanias")
    var nroCampanias : Int? = null

    @SerializedName("ValidacionInteractiva")
    var isValidacionInteractiva : Boolean? = null

    @SerializedName("ZonaValida")
    var isZonaValida : Boolean? = null

    @SerializedName("FechaInicioFacturacion")
    var fechaInicioFacturacion : String? = null

    @SerializedName("MontoMinimoPedido")
    var montoMinimoPedido : BigDecimal? = null

    @SerializedName("ResultadoReserva")
    var resultadoReserva : ReservaResponseEntity? = null

    @SerializedName("CodigoPrograma")
    var codigoPrograma: String?=null

    @SerializedName("ConsecutivoNueva")
    var consecutivoNueva: Int?=null

}
