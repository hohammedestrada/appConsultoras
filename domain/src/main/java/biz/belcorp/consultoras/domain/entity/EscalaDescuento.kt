package biz.belcorp.consultoras.domain.entity

import java.math.BigDecimal


class EscalaDescuento {

    var montoDesde: BigDecimal? = null
    var montoHasta: BigDecimal? = null
    var porDescuento: BigDecimal? = null
    var tipoParametriaOfertaFinal: String? = null
    var precioMinimo: BigDecimal? = null
    var algoritmo: String? = null
}
