package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.ContenidoUpdateRequest
import biz.belcorp.consultoras.domain.entity.ContenidoRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorieEntityMapper  @Inject
internal constructor(){

    fun transform (contenidoRequest: ContenidoRequest): ContenidoUpdateRequest{
        return ContenidoUpdateRequest().apply {
            this.campaniaID = contenidoRequest.campaniaID
            this.codigoRegion = contenidoRequest.codigoRegion
            this.codigoSeccion = contenidoRequest.codigoSeccion
            this.codigoZona = contenidoRequest.codigoZona
            this.idContenidoDetalle = contenidoRequest.idContenidoDetalle
            this.indicadorConsultoraDigital = contenidoRequest.indicadorConsultoraDigital
            this.numeroDocumento = contenidoRequest.numeroDocumento
            this.CodigoContenidoDetalle = contenidoRequest.codigoContenidoDetalle
        }
    }
}
