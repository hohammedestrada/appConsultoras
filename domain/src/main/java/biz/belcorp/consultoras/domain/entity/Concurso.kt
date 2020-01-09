package biz.belcorp.consultoras.domain.entity

import java.math.BigDecimal

class Concurso {

    var id: Int? = null
    var campaniaId: String? = null
    var campaniaIDInicio: Int? = null
    var campaniaIDFin: Int? = null
    var codigoConcurso: String? = null
    var tipoConcurso: String? = null
    var puntosAcumulados: Int? = null
    var isIndicadorPremioAcumulativo: Boolean? = null
    var nivelAlcanzado: Int? = null
    var nivelSiguiente: Int? = null
    var campaniaIDPremiacion: String? = null
    var puntajeExigido: Int? = null
    var descripcionConcurso: String? = null
    var estadoConcurso: String? = null
    var urlBannerPremiosProgramaNuevas: String? = null
    var urlBannerCuponesProgramaNuevas: String? = null
    var codigoNivelProgramaNuevas: String? = null
    var importePedido: BigDecimal? = null
    var textoCupon: String? = null
    var textoCuponIndependiente: String? = null

    var niveles: Collection<Nivel?>? = null
    var nivelProgramaNuevas: Collection<NivelProgramaNueva?>? = null
}

class Nivel {
    var id: Int? = null
    var concursoLocalId: Int? = null
    var codigoConcurso: String? = null
    var codigoNivel: Int? = null
    var puntosNivel: Int? = null
    var puntosFaltantes: Int? = null
    var isIndicadorPremiacionPedido: Boolean? = null
    var montoPremiacionPedido: BigDecimal? = null
    var isIndicadorBelCenter: Boolean? = null
    var fechaVentaRetail: String? = null
    var isIndicadorNivelElectivo: Boolean? = null
    var puntosExigidos: Int? = null
    var puntosExigidosFaltantes: Int? = null
    var opciones: List<Opcion?>? = null
}

class Opcion {
    var opcion: Int? = null
    var premios: List<Premio?>? = null
}
