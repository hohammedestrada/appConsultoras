package biz.belcorp.consultoras.domain.entity

import java.math.BigDecimal

open class EstrategiaCarrusel {

    var estrategiaID: Int? = null
    var codigoEstrategia: String? = null
    var cuv: String? = null
    var descripcionCUV: String? = null
    var descripcionCortaCUV: String? = null
    var marcaID: Int? = null
    var descripcionMarca: String? = null
    var precioValorizado: BigDecimal? = null
    var precioFinal: BigDecimal? = null
    var ganancia: BigDecimal? = null
    var fotoProductoSmall: String? = null
    var fotoProductoMedium: String? = null
    var flagNueva: Int? = null
    var tipoEstrategiaID: String? = null
    var indicadorMontoMinimo: Int? = null
    var origenPedidoWebFicha: Int? = null
    var origenPedidoWeb: Int? = null
    var productoDetalle: List<ProductoDetalle>? = null
    var cantidad: Int = 1
    var added: Boolean = false
    var index: Int = 0
    var tieneStock: Boolean = true
    var flagSeleccionado: Int? = null
    var flagPremioDefault: Boolean = false
    var flagFestival: Boolean = false
    var tipoPersonalizacion: String? = null
    var flagPromocion: Boolean? = false
    var nombrePersonalizacion: String? = null // para analytic
}
