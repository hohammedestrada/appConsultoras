package biz.belcorp.consultoras.domain.entity

import java.math.BigDecimal


class PedidoGetResponse{

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
    var detalle: List<OrderListItem?>? = null
    var identifier : String? = null
    var isDiaProl : Boolean? = null
    var isTieneArmaTuPack: Boolean? = null
    var recogerDNI: String? = null
    var recogerNombre: String? = null
    var facturarPedidoFM : Boolean = false
    var activaMultiPedido : Boolean = false
}
