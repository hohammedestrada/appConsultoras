package biz.belcorp.consultoras.common.model.product

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.OrderListItem
import biz.belcorp.consultoras.domain.entity.PedidoComponentes
import biz.belcorp.consultoras.domain.entity.ProductCUV
import javax.inject.Inject

@PerActivity
class ProductItemModelDataMapper @Inject
internal constructor() {

    fun transform(input: OrderListItem?): ProductItem? {
        return input?.let {
            ProductItem(it.id, it.cuv, it.descripcionProd, it.descripcionCortaProd,
                it.cantidad, it.precioUnidad, it.importeTotal, it.clienteID, it.clienteLocalID,
                it.nombreCliente, it.subido, it.isEsKitNueva, it.tipoEstrategiaID,
                it.tipoOfertaSisID, it.observacionPROL, it.observacionPROLType,
                it.observacionPROLList, it.etiquetaProducto, it.indicadorOfertaCUV,
                it.mensajeError, it.setID, it.isEsBackOrder, it.isAceptoBackOrder,it.isFlagNueva,it.isEnRangoProgNuevas, it.isEsDuoPerfecto,
                it.isEsPremioElectivo, it.isArmaTuPack, it.tipoOferta, transform(it.componentes), it.isKitCaminoBrillante,it.isDeleteKit, it.isPromocion,
                it.observacionPromociones,
                it.isFestival, it.flagFestival)
        }
    }

    fun transform(input: PedidoComponentes?): ComponentItem? {
        return input?.let {
            ComponentItem(it.setDetalleId, it.setId, it.cuv, it.nombreProducto, it.cantidad, it.factorRepetecion, it.precioUnidad)
        }
    }


    fun transform(input: ProductItem?): OrderListItem? {
        return input?.let {
            OrderListItem().apply{
                id = it.id
                cuv = it.cuv
                descripcionProd = it.descripcionProd
                descripcionCortaProd = it.descripcionCortaProd
                cantidad = it.cantidad
                precioUnidad = it.precioUnidad
                importeTotal = it.importeTotal
                clienteID = it.clienteID
                clienteLocalID = it.clienteLocalID
                nombreCliente = it.nombreCliente
                subido = it.subido
                isEsKitNueva = it.isEsKitNueva
                tipoEstrategiaID = it.tipoEstrategiaID
                tipoOfertaSisID = it.tipoOfertaSisID
                observacionPROL = it.observacionPROL
                etiquetaProducto = it.etiquetaProducto
                indicadorOfertaCUV = it.indicadorOfertaCUV
                mensajeError = it.mensajeError
                setID = it.conjuntoID
                isEsBackOrder = it.isEsBackOrder
                isAceptoBackOrder = it.isAceptoBackOrder
                isFlagNueva = it.isFlagNueva
                isEnRangoProgNuevas = it.isEnRangoProgNuevas
                isEsDuoPerfecto = it.isEsDuoPerfecto
                isEsPremioElectivo = it.isEsPremioElectivo
                tipoOferta = it.tipoOferta
                isDeleteKit = it.isDeleteKit
                isArmaTuPack = it.isArmaTuPack
                isKitCaminoBrillante = it.isKitCaminoBrillante
                reemplazarFestival = it.reemplazarFestival
                isPromocion = it.isPromocion
                isFestival = it.isFestival
                flagFestival = it.flagFestival
            }
        }
    }

    fun transform(input: List<PedidoComponentes?>?): List<ComponentItem> {
        return ArrayList<ComponentItem>().apply {
            input?.filterNotNull()?.forEach {
                transform(it)?.let { it1 -> this.add(it1) }
            }
        }
    }


    fun transform(input: Collection<OrderListItem?>?): List<ProductItem?>? {
        return input?.let {
            it
                .map { transform(it) }
                .filter { null != it }
        } ?: run {
            emptyList<ProductItem>()
        }
    }

    fun transform(input: List<ProductItem?>?): Collection<OrderListItem?>? {
        return input?.let {
            it
                .map { transform(it) }
                .filter { null != it }
        } ?: run {
            emptyList<OrderListItem>()
        }
    }

}
