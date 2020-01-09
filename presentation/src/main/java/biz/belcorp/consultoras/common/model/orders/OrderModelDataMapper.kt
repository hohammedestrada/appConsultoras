package biz.belcorp.consultoras.common.model.orders

import biz.belcorp.consultoras.common.model.client.ClienteModelDataMapper
import biz.belcorp.consultoras.common.model.product.ProductItemModelDataMapper
import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.Cliente
import biz.belcorp.consultoras.domain.entity.FormattedOrder
import biz.belcorp.consultoras.domain.entity.GananciaListItem
import biz.belcorp.consultoras.domain.entity.OrderListItem
import javax.inject.Inject

@PerActivity
class OrderModelDataMapper @Inject
internal constructor(private val productItemModelDataMapper: ProductItemModelDataMapper,
                     private val clienteModelDataMapper: ClienteModelDataMapper) {

    fun transform(input: FormattedOrder?): OrderModel? {
        return input?.let {
            OrderModel(it.pedidoID, it.gananciaEstimada, it.montoEscala,
                it.descuentoProl, it.importeTotal, it.importeTotalDescuento,
                it.montoAhorroCatalogo, it.montoAhorroRevista, it.cantidadProductos,
                it.cantidadCuv, it.tippingPoint, it.pedidoValidado, it.muestraRegalo, it.precioPorNivel,
                productItemModelDataMapper.transform(it.productosDetalle),
                clienteModelDataMapper.transform(it.clientesDetalle),
                it.identifier, it.isDiaProl,
                this.transform(it.gananaciaDetalle),
                false,
                it.montoPagoContadoSIC, it.montoDeudaAnteriorSIC, it.montoDescuentoSIC, it.montoFleteSIC, it.precioRegalo, it.montoMaximoDesviacion, it.facturarPedidoFM, it.activaMultiPedido)
        }
    }

    fun transform(input: OrderModel?): FormattedOrder? {
        return input?.let {
            FormattedOrder().apply{
                pedidoID = it.pedidoID
                gananciaEstimada = it.gananciaEstimada
                montoEscala = it.montoEscala
                descuentoProl = it.descuentoProl
                importeTotal = it.importeTotal
                importeTotalDescuento = it.importeTotalDescuento
                montoAhorroCatalogo = it.montoAhorroCatalogo
                montoAhorroRevista = it.montoAhorroRevista
                cantidadProductos = it.cantidadProductos
                cantidadCuv = it.cantidadCuv
                tippingPoint = it.tippingPoint
                pedidoValidado = it.pedidoValidado
                muestraRegalo = it.muestraRegalo
                precioPorNivel = it.precioPorNivel
                productosDetalle = productItemModelDataMapper.transform(it.productosDetalle)
                    as List<OrderListItem?>?
                clientesDetalle = clienteModelDataMapper.transform(it.clientesDetalle)
                    as List<Cliente?>?
                identifier = it.identifier
                isDiaProl = it.isDiaProl
                montoPagoContadoSIC = it.montoPagoContadoSIC
                montoDeudaAnteriorSIC = it.montoDeudaAnteriorSIC
                montoDescuentoSIC = it.montoDescuentoSIC
                montoFleteSIC = it.montoFleteSIC
                precioRegalo = it.precioRegalo
                facturarPedidoFM = it.facturarPedidoFM
                activaMultiPedido = it.activaMultiPedido
                montoMaximoDesviacion = it.montoMaximoDesviacion
            }
        }
    }

    fun transform(input: Collection<FormattedOrder?>?): List<OrderModel?>? {
        return input?.let {
            it
                .map { transform(it) }
                .filter { null != it }
        } ?: run {
            emptyList<OrderModel>()
        }
    }

    fun transform(input: List<OrderModel?>?): Collection<FormattedOrder?>? {
        return input?.let {
            it
                .map { transform(it) }
                .filter { null != it }
        } ?: run {
            emptyList<FormattedOrder>()
        }
    }

    fun transform(input: List<GananciaListItem?>?): List<GananciaListItemModel?>?{
        return input?.let {
            it
                .map{ transform(it) }
                .filter { null != it }
        } ?: run {
            emptyList<GananciaListItemModel>()
        }
    }

    fun transform(input: GananciaListItem?) : GananciaListItemModel? {
        return input?.let {
            GananciaListItemModel(it.descripcion, it.montoGanancia)
        }
    }

}
