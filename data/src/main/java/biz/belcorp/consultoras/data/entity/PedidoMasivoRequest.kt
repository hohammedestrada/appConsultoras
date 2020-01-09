package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

class PedidoMasivoRequest {

    @SerializedName("CampaniaID")
    var campaniaId : Int? = null

    @SerializedName("IPUsuario")
    var ipUsuario : String? = null

    @SerializedName("Identifier")
    var identifier : String? = null

    @SerializedName("CodigosConcursos")
    var codigosConcursos : String? = null

    @SerializedName("AceptacionConsultoraDA")
    var aceptacionConsultoraDA : Int? = null

    @SerializedName("MontoMaximoPedido")
    var montoMaximoPedido : BigDecimal? = null

    @SerializedName("CodigoPrograma")
    var codigoPrograma : String? = null

    @SerializedName("ConsecutivoNueva")
    var consecutivoNueva : Int? = null

    @SerializedName("FechaInicioFacturacion")
    var fechaInicioFacturacion : String? = null

    @SerializedName("FechaFinFacturacion")
    var fechaFinFacturacion : String? = null

    @SerializedName("ConsultoraNueva")
    var consultoraNueva : Int? = null

    @SerializedName("UsuarioPrueba")
    var usuarioPrueba : Boolean? = null

    @SerializedName("NombreConsultora")
    var nombreConsultora : String? = null

    @SerializedName("Simbolo")
    var simbolo : String? = null

    @SerializedName("CodigoRegion")
    var codigoRegion : String? = null

    @SerializedName("CodigoZona")
    var codigoZona : String? = null

    @SerializedName("NroCampanias")
    var nroCampanias : Int? = null

    @SerializedName("ConsultoraAsociada")
    var consultoraAsociada : String? = null

    @SerializedName("RegionID")
    var regionID : Int? = null

    @SerializedName("ZonaID")
    var zonaID : Int? = null

    @SerializedName("CodigoSeccion")
    var codigoSeccion : String? = null


    @SerializedName("Productos")
    var productos : List<ProductoMasivoEntity?>? = null

}
