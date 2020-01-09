package biz.belcorp.consultoras.domain.entity

import java.math.BigDecimal

class ClientMovement {

    var id: Int? = null
    var movementID: Int? = null
    var clientID: Int? = null
    var clienteLocalID: Int? = null
    var sincronizado: Int? = null
    var clientCode: Int? = null
    var amount: BigDecimal? = null
    var type: String? = null
    var description: String? = null
    var campaing: String? = null
    var note: String? = null
    var date: String? = null
    var saldo: BigDecimal? = null
    var estado: Int? = null
    var code: String? = null
    var message: String? = null
    var productMovements: List<ProductMovement>? = null
}
