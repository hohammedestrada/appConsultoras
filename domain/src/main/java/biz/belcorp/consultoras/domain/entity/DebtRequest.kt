package biz.belcorp.consultoras.domain.entity

import java.math.BigDecimal

/**
 *
 */
class DebtRequest {

    var monto: BigDecimal? = null
    var descripcion: String? = null
    var nota: String? = null
    var fecha: String? = null
    var tipoMovimiento: String? = null
    var clienteID: Int? = null
    var clienteLocalID: Int? = null
    var codigoCampania: String? = null
    var estado: Int? = null
}
