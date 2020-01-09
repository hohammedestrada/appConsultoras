package biz.belcorp.consultoras.common.model.orders

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.ObservacionPedido
import biz.belcorp.consultoras.domain.entity.ReservaResponse
import javax.inject.Inject

@PerActivity
class ReserveResponseModelDataMapper  @Inject
internal constructor(private val observacionPedidoModelDataMapper: ObservacionPedidoModelDataMapper) {

    fun transform(input: ReservaResponse?): ReserveResponseModel? {
        return input?.let {
            ReserveResponseModel(it.reserva, it.codigoMensaje, it.informativas, it.montoEscala,
                it.montoTotal, observacionPedidoModelDataMapper.transform(it.observaciones))
        }
    }

    fun transform(input: ReserveResponseModel): ReservaResponse {
        return input?.let {
            ReservaResponse().apply{
                reserva = it.reserva
                codigoMensaje = it.codigoMensaje
                informativas = it.informativas
                montoEscala = it.montoEscala
                montoTotal = it.montoTotal
                observaciones = observacionPedidoModelDataMapper.transform(it.observaciones)
                    as List<ObservacionPedido?>?
            }
        }
    }

}
