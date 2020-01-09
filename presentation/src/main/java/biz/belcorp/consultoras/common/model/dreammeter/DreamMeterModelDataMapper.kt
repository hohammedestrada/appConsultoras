package biz.belcorp.consultoras.common.model.dreammeter

import biz.belcorp.consultoras.domain.entity.ContenidoResumen

class DreamMeterModelDataMapper {

    companion object {
        fun transform(input: ContenidoResumen): DreamMederModel {
            return DreamMederModel().apply {

                input.contenidoDetalle?.let {
                    if(it.isNotEmpty()){
                        description = it[0].codigoDetalleResumen
                        type = it[0].typeContenido
                    }
                }
            }
        }
    }
}
