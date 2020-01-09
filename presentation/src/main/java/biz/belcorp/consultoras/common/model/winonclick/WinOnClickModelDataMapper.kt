package biz.belcorp.consultoras.common.model.winonclick

import biz.belcorp.consultoras.domain.entity.ContenidoResumen

class WinOnClickModelDataMapper {

    companion object {
        fun transform(input: ContenidoResumen): WinOnClickModel {
            return WinOnClickModel().apply {

                input.contenidoDetalle?.let {
                    if(it.isNotEmpty()){
                        videoUrl = it[0].urlDetalleResumen
                        type = it[0].codigoDetalleResumen
                     }
                }
            }
        }
    }
}
