package biz.belcorp.consultoras.common.model.orders

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.ObservacionPedido
import javax.inject.Inject

@PerActivity
class ObservacionPedidoModelDataMapper @Inject
internal constructor() {

    fun transform(input: ObservacionPedido?): ObservacionPedidoModel? {
        return input?.let {
            ObservacionPedidoModel(it.cuv, it.descripcion, it.caso, it.cuvObs, it.conjuntoID,
                it.pedidoDetalleID)
        }
    }

    fun transform(input: ObservacionPedidoModel?): ObservacionPedido? {
        return input?.let {
            ObservacionPedido().apply{
                cuv = it.cuv
                descripcion = it.descripcion
                caso = it.caso
                cuvObs = it.cuvObs
                conjuntoID = it.conjuntoID
                pedidoDetalleID = it.pedidoDetalleID
            }
        }
    }

    fun transform(input: Collection<ObservacionPedido?>?): List<ObservacionPedidoModel?>? {
        return input?.let {
            it
                .map { transform(it) }
                .filter { null != it }
        } ?: run {
            emptyList<ObservacionPedidoModel>()
        }
    }

    fun transform(input: List<ObservacionPedidoModel?>?): Collection<ObservacionPedido?>? {
        return input?.let {
            it
                .map { transform(it) }
                .filter { null != it }
        } ?: run {
            emptyList<ObservacionPedido>()
        }
    }

}
