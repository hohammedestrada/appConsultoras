package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.domain.entity.OfertaAtpRequest

data class OfertaAtpRequestEntity (
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

){
    companion object {

        fun transform(input: OfertaAtpRequest?): OfertaAtpRequestEntity?{
            input?.run {
                return OfertaAtpRequestEntity(
                    campaniaId,
                    codigoPrograma,
                    consecutivoNueva,
                    montoMaximoPedido,
                    consultoraNueva,
                    nroCampanias,
                    codigoSeccion,
                    codigoRegion,
                    codigoZona,
                    zonaId
                )
            }
            return null
        }

    }

}
