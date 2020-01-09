package biz.belcorp.consultoras.feature.auth.startTutorial

import biz.belcorp.consultoras.domain.entity.ContenidoResumen

class startTutorialModelMapper{

    fun transform(input : ContenidoResumen.ContenidoDetalle?) : ContenidoResumenDetalleModel?{
        return input?.let {
            ContenidoResumenDetalleModel().apply{
                grupo = it.grupo

                typeContenido = it.typeContenido

                idContenido = it.idContenido

                codigoDetalleResumen = it.codigoDetalleResumen

                urlDetalleResumen = it.urlDetalleResumen

                accion = it.accion

                ordenDetalleResumen = it.ordenDetalleResumen

                visto = it.visto

                descripcion = it.descripcion
            }
        }
    }

    fun transform(input : ContenidoResumen?) : ContenidoResumenModel?{
        return input?.let {
            ContenidoResumenModel().apply {
                codigoResumen = it.codigoResumen

                urlMiniatura = it.urlMiniatura

                totalContenido = it.totalContenido

                contenidoVisto = it.contenidoVisto

                contenidoDetalle = it.contenidoDetalle?.let {
                    transform(it)
                }
            }
        }
    }

    fun transform(input : List<ContenidoResumen.ContenidoDetalle>?) : ArrayList<ContenidoResumenDetalleModel>?{
        return input?.let {
            ArrayList<ContenidoResumenDetalleModel>().apply {
                it.forEach { item ->
                    transform(item)?.let {t ->
                        add(t)
                    }
                }
            }
        }
    }
}
