package biz.belcorp.consultoras.domain.entity

data class OfertaAtpRequest(
    var campaniaId: Int? = null,
    var codigoPrograma: String? = "",
    var consecutivoNueva: Int? = null,
    var montoMaximoPedido: Double? = null,
    var consultoraNueva: Int? = null,
    var nroCampanias: Int? = null,
    var codigoSeccion: String? = null,
    var codigoRegion: String? = null,
    var codigoZona: String? = null,
    var zonaId: Int? = null
)
