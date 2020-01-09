package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal


class PedidoGetResponseEntity{

    @SerializedName("PedidoID")
    var pedidoID: Int? = null

    @SerializedName("PedidoValidado")
    var pedidoValidado: Boolean? = null

    @SerializedName("GananciaEstimada")
    var gananciaEstimada: BigDecimal? = null

    @SerializedName("MontoEscala")
    var montoEscala: BigDecimal? = null

    @SerializedName("DescuentoProl")
    var descuentoProl: BigDecimal? = null

    @SerializedName("ImporteTotal")
    var importeTotal: BigDecimal? = null

    @SerializedName("ImporteTotalDescuento")
    var importeTotalDescuento: BigDecimal? = null

    @SerializedName("MontoAhorroCatalogo")
    var montoAhorroCatalogo: BigDecimal? = null

    @SerializedName("MontoAhorroRevista")
    var montoAhorroRevista: BigDecimal? = null

    @SerializedName("CantidadProductos")
    var cantidadProductos: Int? = null

    @SerializedName("CantidadCuv")
    var cantidadCuv: Int? = null

    @SerializedName("TippingPoint")
    var tippingPoint: BigDecimal? = null

    @SerializedName("MuestraRegalo")
    var muestraRegalo: Boolean? = null

    @SerializedName("PrecioPorNivel")
    var precioPorNivel: Boolean? = null

    @SerializedName("TieneArmaTuPack")
    var isTieneArmaTuPack: Boolean? = null

    @SerializedName("RecogerDNI")
    var recogerDNI: String? = null

    @SerializedName("RecogerNombre")
    var recogerNombre: String? = null

    @SerializedName("Detalle")
    var detalle: List<OrderListItemEntity?>? = null

    @SerializedName("DetalleGanancia")
    var detalleGanancia: List<GananciaListItemEntity?>? = null

    @SerializedName("MontoPagoContadoSIC")
    var montoPagoContadoSIC : BigDecimal? = null

    @SerializedName("MontoDeudaAnteriorSIC")
    var montoDeudaAnteriorSIC : BigDecimal? = null

    @SerializedName("MontoDescuentoSIC")
    var montoDescuentoSIC : BigDecimal? = null

    @SerializedName("MontoFleteSIC")
    var montoFleteSIC : BigDecimal? = null

    @SerializedName("PrecioRegalo")
    var precioRegalo : Boolean? = null

    @SerializedName("FacturaMultipedido")
    var facturarPedidoFM : Boolean? = null

    @SerializedName("ActivaMultiPedido")
    var activaMultiPedido : Boolean? = null
}

class GananciaListItemEntity{

    @SerializedName("Descripcion")
    var descripcion: String? = null

    @SerializedName("MontoGanancia")
    var montoGanancia: BigDecimal? = null

}
