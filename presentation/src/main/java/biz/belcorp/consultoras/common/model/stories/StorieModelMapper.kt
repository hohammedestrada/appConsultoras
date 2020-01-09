package biz.belcorp.consultoras.common.model.stories

import biz.belcorp.consultoras.domain.entity.ContenidoResumen

class StorieModelMapper {
    companion object {

        fun transform(input: ContenidoResumen): StorieModel {
            return StorieModel().apply {

                codigoResumen = input.codigoResumen

                urlMiniatura = input.urlMiniatura

                totalContenido = input.totalContenido

                contenidoVisto = input.contenidoVisto

                contenidoDetalle = transform(input.contenidoDetalle).apply {
                    sortedWith(compareBy{it.ordenDetalleResumen})
                }
            }
        }

        private fun transform(input: List<ContenidoResumen.ContenidoDetalle>?): List<StorieModel.ContenidoDetalleModel> {
            return input?.let {
                it
                    .map { it1 -> transform(it1) }
                    .filter { it1 -> null != it1 }
            } ?: run {
                emptyList<StorieModel.ContenidoDetalleModel>()
            }
        }

        private fun transform(input: ContenidoResumen.ContenidoDetalle): StorieModel.ContenidoDetalleModel {
            return input.let {
                StorieModel.ContenidoDetalleModel().apply {

                    grupo = it.grupo

                    typeContenido = it.typeContenido

                    idContenido = it.idContenido.toInt()

                    codigoDetalleResumen = it.codigoDetalleResumen

                    urlDetalleResumen = it.urlDetalleResumen

                    accion = it.accion

                    ordenDetalleResumen = it.ordenDetalleResumen

                    visto = it.visto
                }

            }
        }
    }



}
