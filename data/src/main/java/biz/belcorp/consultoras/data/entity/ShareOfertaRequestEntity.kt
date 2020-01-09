package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.domain.entity.ShareOfertaRequest

data class ShareOfertaRequestEntity(
    var campaniaID: Int? = null,
    var cuv: String? = null,
    var tipoPersonalizacion: String? = null,
    var imagenUrl: String? = null,
    var marcaId: Int? = null,
    var nombreMarca: String? = null,
    var nombreOferta: String? = null
) {

    companion object {
        fun transform(input: ShareOfertaRequest?): ShareOfertaRequestEntity? {
            input?.run {
                val output = ShareOfertaRequestEntity()
                output.campaniaID = campaniaID
                output.cuv = cuv
                output.tipoPersonalizacion = tipoPersonalizacion
                output.imagenUrl = imagenUrl
                output.marcaId = marcaId
                output.cuv = cuv
                output.nombreMarca = nombreMarca
                output.nombreOferta = nombreOferta
                return output
            }
            return null
        }
    }
}
