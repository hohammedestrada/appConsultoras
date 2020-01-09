package biz.belcorp.consultoras.domain.entity

import java.math.BigDecimal

class Cupon {

    var id: Int? = null
    var nivelProgramaLocalId: Int? = null
    var codigoConcurso: String? = null
    var codigoNivel: String? = null
    var codigoCupon: String? = null
    var codigoVenta: String? = null
    var descripcionProducto: String? = null
    var unidadesMaximas: Int? = null
    var indicadorCuponIndependiente: Boolean? = null
    var indicadorKit: Boolean? = null
    var numeroCampanasVigentes: Int? = null
    var textoLibre: String? = null
    var precioUnitario: BigDecimal? = null
    var ganancia: BigDecimal? = null
    var urlImagenCupon: String? = null
}
