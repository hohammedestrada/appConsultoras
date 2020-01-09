package biz.belcorp.consultoras.domain.entity

import java.math.BigDecimal

class AccountState {

    var id: Int? = null
    var fechaRegistro: String? = null
    var descripcionOperacion: String? = null
    var montoOperacion: BigDecimal? = null
    var cargo: BigDecimal? = null
    var abono: BigDecimal? = null
}
