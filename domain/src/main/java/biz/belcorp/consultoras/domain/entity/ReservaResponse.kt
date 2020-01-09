package biz.belcorp.consultoras.domain.entity

import java.math.BigDecimal

class ReservaResponse {

    var reserva: Boolean? = null
    var codigoMensaje: String? = null
    var informativas: Boolean? = null
    var montoEscala: BigDecimal? = null
    var montoTotal: BigDecimal? = null
    var observaciones: List<ObservacionPedido?>? = null
    var mensajesProl: Collection<MensajeProl?>? = null

    fun copy(reserva: Boolean?,
             codigoMensaje: String?,
             informativas: Boolean?,
             montoEscala: BigDecimal?,
             montoTotal: BigDecimal?,
             observaciones: List<ObservacionPedido?>?,
             msjProl: Collection<MensajeProl?>?): ReservaResponse {
        return ReservaResponse().apply {
            this.reserva = reserva
            this.codigoMensaje = codigoMensaje
            this.informativas = informativas
            this.montoEscala = montoEscala
            this.montoTotal = montoTotal
            this.observaciones = observaciones
            this.mensajesProl = msjProl
        }
    }

}
