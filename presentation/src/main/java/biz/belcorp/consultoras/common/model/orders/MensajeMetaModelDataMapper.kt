package biz.belcorp.consultoras.common.model.orders

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.MensajeMeta
import biz.belcorp.consultoras.domain.entity.ObservacionPedido
import javax.inject.Inject

@PerActivity
class MensajeMetaModelDataMapper @Inject
internal constructor() {

    fun transform(input: MensajeMeta?): MensajeMetaModel? {
        return input?.let {
            MensajeMetaModel(it.tipoMensaje, it.titulo, it.mensaje)
        }
    }

    fun transform(input: MensajeMetaModel?): MensajeMeta? {
        return input?.let {
            MensajeMeta().apply{
                tipoMensaje = it.tipoMensaje
                titulo = it.titulo
                mensaje = it.mensaje
            }
        }
    }

    fun transform(input: Collection<MensajeMeta?>?): List<MensajeMetaModel?>? {
        return input?.let {
            it
                .map { transform(it) }
                .filter { null != it }
        } ?: run {
            emptyList<MensajeMetaModel>()
        }
    }

    fun transform(input: List<MensajeMetaModel?>?): Collection<MensajeMeta?>? {
        return input?.let {
            it
                .map { transform(it) }
                .filter { null != it }
        } ?: run {
            emptyList<MensajeMeta>()
        }
    }

}
