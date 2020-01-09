package biz.belcorp.consultoras.domain.entity

import java.math.BigDecimal

class NivelProgramaNueva {

    var id: Int? = null
    var concursoLocalId: Int? = null
    var codigoConcurso: String? = null
    var codigoNivel: String? = null
    var montoExigidoPremio: BigDecimal? = null
    var montoExigidoCupon: BigDecimal? = null

    var premiosNuevas: List<PremioNueva?>? = null
    var cupones: List<Cupon?>? = null
}
