package biz.belcorp.consultoras.domain.entity

import java.math.BigDecimal

/**
 *
 */
class MyOrder {

    var id: Int? = null
    var estadoPedidoDesc: String? = null
    var campaniaID: Int? = null
    var fechaRegistro: String? = null
    var importeTotal: BigDecimal? = null
    var rutaPaqueteDocumentario: String? = null
    var numeroPedido: Int? = null
    var estadoEncuesta: Int? = null
}
