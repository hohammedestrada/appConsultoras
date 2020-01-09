package biz.belcorp.consultoras.domain.entity

data class ShareOfertaRequest(
    var campaniaID: Int? = null,
    var cuv: String? = null,
    var tipoPersonalizacion: String? = null,
    var imagenUrl: String? = null,
    var marcaId: Int? = null,
    var nombreMarca: String? = null,
    var nombreOferta: String? = null
)
