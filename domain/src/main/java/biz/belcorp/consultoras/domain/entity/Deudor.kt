package biz.belcorp.consultoras.domain.entity

import java.math.BigDecimal

/**
 * Entidad de dominio Country
 * que recibe o envia los datos a la capa de datos o a la capa de presentacion
 *
 * @version 1.0
 * @since 2017-04-14
 */

class Deudor {

    var clienteID: Int? = null
    var totalDeuda: BigDecimal? = null
    var recordatorio: Recordatorio? = null
}
