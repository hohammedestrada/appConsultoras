package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.*
import biz.belcorp.consultoras.data.util.Constant
import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.util.ObservationOrderCase
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.net.dto.ServiceDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Clase encarga de realizar el mapeo de la entidad Config(tabla o json) a
 * una entidad del dominio
 *
 * @version 1.0
 * @since 2017-04-20
 */
@Singleton
class ReservaResponseEntityDataMapper @Inject
internal constructor(private val observacionPedidoEntityDataMapper: ObservacionPedidoEntityDataMapper
    , private val orderEntityDataMapper: OrderEntityDataMapper) {

    /**
     * Transforma la entidad a una entidad del dominio.
     *
     * @param input Entidad tipo tabla o json
     * @return object Entidad de dominio
     */

    fun transform(input: ReservaResponseEntity?): ReservaResponse? {
        var output: ReservaResponse? = null

        if (null != input) {
            output = ReservaResponse()
            output.reserva = input.reserva
            output.codigoMensaje = input.codigoMensaje
            output.informativas = input.informativas
            output.montoEscala = input.montoEscala
            output.montoTotal = input.montoTotal
            output.observaciones = observacionPedidoEntityDataMapper.transform(input.observaciones)
            output.mensajesProl = transformMensajesProl(input.mensajesProl)

        }
        return output
    }

    fun transform(data: MensajeProlEntity?): MensajeProl? {
        return MensajeProl().apply {
            this.code = data?.code
            this.message = data?.message
            this.imageUrl = data?.imageUrl
        }
    }

    private fun transformMensajesProl(input: List<MensajeProlEntity?>?): Collection<MensajeProl?>? {
        return input?.asSequence()?.map { it1 -> transform(it1) }?.filter { it1 -> null != it1 }?.toList()
            ?: run {
                emptyList<MensajeProl>()
            }
    }

    /**
     * Transforma la entidad de dominio a una entidad.
     *
     * @param input Entidad de dominio
     * @return object Entidad
     */
    fun transform(input: ReservaResponse?): ReservaResponseEntity? {
        var output: ReservaResponseEntity? = null

        if (null != input) {
            output = ReservaResponseEntity()
            output.reserva = input.reserva
            output.codigoMensaje = input.codigoMensaje
            output.informativas = input.informativas
            output.montoEscala = input.montoEscala
            output.montoTotal = input.montoTotal
            output.observaciones = observacionPedidoEntityDataMapper.transform(input.observaciones)
                as List<ObservacionPedidoEntity?>?
        }
        return output
    }

    fun transformReservaResponse(order: FormattedOrder, input: ServiceDto<ReservaResponseEntity>?)
        : BasicDto<FormattedOrderReserveResponse>? {
        return input?.let {
            BasicDto<FormattedOrderReserveResponse>().apply {
                code = it.code
                val aux: ReservaResponseEntity
                val json = Gson().toJson(it.data)
                val type = object : TypeToken<ReservaResponseEntity>() {}.type
                try {
                    aux = Gson().fromJson(json, type)

                    aux?.observaciones?.forEach { obs ->
                        order.productosDetalle?.forEach { it1 ->
                            if (obs?.pedidoDetalleID != 0 && obs?.pedidoDetalleID == it1?.id) {
                                it1?.observacionPROL = obs?.descripcion
                                if (obs?.caso == ObservationOrderCase.BACK_ORDER.case) it1?.isEsBackOrder = true
                            } else if (obs?.cuv.equals(it1?.cuv)) {
                                if (obs?.conjuntoID != 0 && obs?.conjuntoID == it1?.setID) {
                                    it1?.observacionPROLType = 1
                                    it1?.observacionPROLList?.add(obs?.descripcion!!)
                                    if (obs?.caso == ObservationOrderCase.BACK_ORDER.case) it1?.isEsBackOrder = true
                                } else if (obs?.conjuntoID == 0){
                                    if (obs.caso == ObservationOrderCase.BACK_ORDER.case) it1?.isEsBackOrder = true
                                    it1?.observacionPROL = obs.descripcion
                                }
                            }
                        }
                    }

                    order.clientesDetalle?.forEach { c1 ->
                        val finalList = ArrayList<OrderListItem>()
                        order.productosDetalle?.forEach { orderListItem ->
                            orderListItem?.clienteID?.let {it1 ->
                                if (it1 == c1?.clienteID) {
                                    finalList.add(orderListItem)
                                }
                            }
                        }
                        c1?.orderList = finalList
                    }

                    data = FormattedOrderReserveResponse().apply {
                            formattedOrder = order
                            reservaResponse = transform(aux)
                        }
                } catch (e: Exception) {
                    BelcorpLogger.d("Reserva pedido: Data de respuesta de pedido nula")
                }
                message = it.message
            }
        }
    }

    fun transformReservaResponse(input: ServiceDto<ReservaResponseEntity>?): BasicDto<ReservaResponse>? {
        return input?.let {
            BasicDto<ReservaResponse>().apply {
                code = it.code
                val aux: ReservaResponseEntity
                val json = Gson().toJson(it.data)
                val type = object : TypeToken<ReservaResponseEntity>() {}.type
                try {
                    aux = Gson().fromJson(json, type)
                    data = transform(aux)
                } catch (e: Exception) {
                    BelcorpLogger.d("Reserva pedido: Data de respuesta de pedido nula")
                }
                message = it.message
            }
        }
    }

    fun transformUpResponse(input: ServiceDto<List<MensajeProlEntity?>?>): BasicDto<Collection<MensajeProl?>?>? {
        return input?.let {
            BasicDto<Collection<MensajeProl?>?>().apply {
                code = it.code
                message = it.message
                val json = Gson().toJson(it.data)
                val  type = object : TypeToken<List<MensajeProlEntity?>?>() {}.type
                try {
                    val predata: List<MensajeProlEntity?>? = Gson().fromJson(json, type)
                    data = orderEntityDataMapper.transformBy(predata)
                } catch (e: Exception) {
                    BelcorpLogger.d(Constant.ERROR_TRANSFORM)
                }

            }
        }
    }

    fun transformDeleteResponse(input: ServiceDto<Boolean>?): BasicDto<Boolean>? {
        return input?.let {
            BasicDto<Boolean>().apply {
                code = it.code
                data = it.data
                message = it.message
            }
        }
    }

    fun transformToProductoMasivo(input: ServiceDto<List<ProductoMasivoEntity>>?): BasicDto<List<ProductoMasivo>>? {
        return input?.let {
            BasicDto<List<ProductoMasivo>>().apply {
                code = it.code
                data = orderEntityDataMapper.transformToProductoMasivoList(it.data)
                message = it.message
            }
        }
    }
}
