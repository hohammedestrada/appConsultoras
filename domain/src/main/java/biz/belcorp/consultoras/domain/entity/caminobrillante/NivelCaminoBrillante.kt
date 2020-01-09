package biz.belcorp.consultoras.domain.entity.caminobrillante

import java.math.BigDecimal
import java.math.BigInteger

class NivelCaminoBrillante {

    var id: Long? = null
    var codigoNivel: String? = null
    var descripcionNivel: String? = null
    var montoMinimo: Double? = null
    var montoMaximo: Double? = null
    var isTieneOfertasEspeciales: Boolean? = null
    var montoFaltante: BigDecimal? = null
    var urlImagenNivel: String? = null
    var beneficios: List<BeneficioCaminoBrillante>? = null
    var enterateMas: Int? = 0
    var enterateMasParam: String? = null
    var puntaje: Int? = null
    var puntajeAcumulado: Int? = null
    var mensaje: String? = null

    class BeneficioCaminoBrillante {

        var codigoNivel: String? = null
        var codigoBeneficio: String? = null
        var nombreBeneficio: String? = null
        var descripcion: String? = null
        var urlIcono: String? = null

    }



}
