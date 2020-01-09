package biz.belcorp.consultoras.domain.entity

import java.math.BigDecimal

class FormattedOrder {

    var pedidoID: Int? = null
    var gananciaEstimada: BigDecimal? = null
    var montoEscala: BigDecimal? = null
    var descuentoProl: BigDecimal? = null
    var importeTotal: BigDecimal? = null
    var importeTotalDescuento: BigDecimal? = null
    var montoAhorroCatalogo: BigDecimal? = null
    var montoAhorroRevista: BigDecimal? = null
    var cantidadProductos: Int? = null
    var cantidadCuv: Int? = null
    var tippingPoint: BigDecimal? = null
    var pedidoValidado: Boolean = false
    var muestraRegalo: Boolean = false
    var precioPorNivel: Boolean = false
    var productosDetalle: List<OrderListItem?>? = null
    var clientesDetalle: List<Cliente?>? = null
    var identifier: String? = null
    var isDiaProl: Boolean? = null
    var recogerDNI: String? = null
    var recogerNombre: String? = null
    var gananaciaDetalle: List<GananciaListItem?>? = null
    var montoPagoContadoSIC : BigDecimal? = null
    var montoDeudaAnteriorSIC : BigDecimal? = null
    var montoDescuentoSIC : BigDecimal? = null
    var montoFleteSIC : BigDecimal? = null
    var montoMaximoDesviacion : BigDecimal? = null
    var precioRegalo : Boolean? = null
    var facturarPedidoFM : Boolean? = false
    var activaMultiPedido : Boolean? = false
}

class GananciaListItem {
    var descripcion: String? = null
    var montoGanancia: BigDecimal? = null
}
