package biz.belcorp.consultoras.common.model.orders

import java.math.BigDecimal

/**
 *
 */
class OrderListItemModel {

    var id: Int? = null
    var CUV: String? = null
    var descripcionProd: String? = null
    var cantidad: Int? = null
    var precioUnidad: BigDecimal? = null
    var ImporteTotal: BigDecimal? = null
    var clienteID: Int? = null
    var clienteLocalID: Int? = null
    var nombreCliente: String? = null


}
