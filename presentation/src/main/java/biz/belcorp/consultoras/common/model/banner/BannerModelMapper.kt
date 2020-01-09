package biz.belcorp.consultoras.common.model.banner

import biz.belcorp.consultoras.feature.home.marquee.MarqueeItem
import biz.belcorp.consultoras.domain.entity.ContenidoResumen

class BannerModelMapper {

    companion object {
        fun transform(input: ContenidoResumen): BannerModel {
            return BannerModel().apply {

                codigoResumen = input.codigoResumen


                totalContenido = input.totalContenido

                contenidoDetalle = transform(input.contenidoDetalle).apply {
                    sortedWith(compareBy{it.ordenDetalleResumen})
                }
            }
        }

        private fun transform(input: List<ContenidoResumen.ContenidoDetalle>?): List<BannerModel.ContenidoDetalleModel> {
            return input?.let {
                it
                    .map { it1 -> transform(it1) }
            } ?: run {
                emptyList<BannerModel.ContenidoDetalleModel>()
            }
        }

        private fun transform(input: ContenidoResumen.ContenidoDetalle): BannerModel.ContenidoDetalleModel {
            return input.let {
                BannerModel.ContenidoDetalleModel().apply {

                    grupo = it.grupo

                    typeContenido = it.typeContenido

                    idContenido = it.idContenido

                    codigoDetalleResumen = it.codigoDetalleResumen

                    urlDetalleResumen = it.urlDetalleResumen

                    accion = it.accion

                    ordenDetalleResumen = it.ordenDetalleResumen

                    descripcion = it.descripcion
                }

            }
        }

        fun transformMarquees(input: List<BannerModel.ContenidoDetalleModel>?) : List<MarqueeItem>{
            return input?.let {
                it.map { it1 -> transformMarquee(it1) }
            }  ?: run {
                    emptyList<MarqueeItem>()
            }

        }

        private fun transformMarquee(input:BannerModel.ContenidoDetalleModel) : MarqueeItem{
            return input.let{
                MarqueeItem().apply {
                    urlImage = it.urlDetalleResumen
                    id = it.idContenido
                    name = it.codigoDetalleResumen
                }
            }
        }
    }
}
