package biz.belcorp.consultoras.domain.entity

class OfertaRequest {

    var tipo: String? = null
    var campaniaId: String? = null
    var zonaId: Int? = null
    var codigoZona: String? = null
    var codigoRegion: String? = null
    var esSuscrita: Boolean? = null
    var esActiva: Boolean? = null
    var tieneMG: Boolean? = null
    var diaInicio: Int? = null
    var fechaInicioFacturacion: String? = null
    var flagComponentes: Boolean? = null
    var flagSubCampania: Boolean? = null
    var flagIndividual : Boolean? = null
    var flagFestival : Boolean? = null
    var codigoPrograma: String? = null
    var consecutivoNueva: Int? = null
    var montoMaximoPedido: Double? = null
    var consultoraNueva: Int? = null
    var nroCampanias: Int? = null
    var nombreConsultora: String? = null
    var codigoSeccion: String? = null
    var esUltimoDiaFacturacion: Boolean? = null
    var pagoContado: Boolean? = null
    var fechaFinFacturacion: String? = null

    var variantea: Boolean=false
    var varianteb: Boolean=false
    var variantec: Boolean=false
}
