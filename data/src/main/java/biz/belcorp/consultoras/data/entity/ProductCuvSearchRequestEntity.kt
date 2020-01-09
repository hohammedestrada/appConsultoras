package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

class ProductCuvSearchRequestEntity {

    @SerializedName("CampaniaID")
    var campaniaId : Int? = null

    @SerializedName("NroCampanias")
    var nroCampanias : Int? = null

    @SerializedName("Cantidad")
    var cantidad : Int? = null

    @SerializedName("LimiteVenta")
    var limiteVenta : Int? = null

    @SerializedName("ConsecutivoNueva")
    var consecutivoNueva : Int? = null

    @SerializedName("ConsultoraAsociada")
    var consultoraAsociada : String? = null

    @SerializedName("CodigoPrograma")
    var codigoPrograma : String? = null

    @SerializedName("IPUsuario")
    var ipUsuario : String? = null

    @SerializedName("OrigenPedidoWeb")
    var origenPedidoWeb : String? = null

    @SerializedName("Identifier")
    var identifier : String? = null

    @SerializedName("UsuarioPrueba")
    var usuarioPrueba : Boolean? = null

    @SerializedName("AceptacionConsultoraDA")
    var aceptacionConsultoraDA : Int? = null

    @SerializedName("Nombre")
    var nombre : String? = null

    @SerializedName("ConsultoraNueva")
    var consultoraNueva : Int? = null

    @SerializedName("CodigosConcursos")
    var codigosConcursos : String? = null

    @SerializedName("CodigoZona")
    var codigoZona : String? = null

    @SerializedName("CUV")
    var cuv : String? = null

    @SerializedName("MarcaID")
    var marcaID: Int? = null

    @SerializedName("Descripcion")
    var descripcion : String? = null

    @SerializedName("PrecioCatalogo")
    var precioCatalogo: BigDecimal? = null

    @SerializedName("TipoPersonalizacion")
    var tipoPersonalizacion: String? = null

    @SerializedName("EstrategiaID")
    var estrategiaId: Int? = null

    @SerializedName("MontoMaximoPedido")
    var montoMaximoPedido: Double? = null

    @SerializedName("FechaInicioFacturacion")
    var fechaInicioFacturacion: String? = null

    @SerializedName("FechaFinFacturacion")
    var fechaFinFacturacion: String? = null

    @SerializedName("TipoEstrategiaID")
    var tipoEstrategiaId: String? = null

}
