package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.ProductoDetalleEntity
import biz.belcorp.consultoras.domain.entity.ProductoDetalle
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductoDetalleEntityDataMapper @Inject
internal constructor() {

    fun transform(input: ProductoDetalle?): ProductoDetalleEntity? {
        return input?.let {
            ProductoDetalleEntity().apply {
                id = it.id
                cuv = it.cuv
                nombreComercial = it.nombreComercial
                factorCuadre = it.factorCuadre
                orden = it.orden
                codigoProducto = it.codigoProducto
                grupo = it.grupo
                precioCatalogo = it.precioCatalogo
                digitable = it.digitable
                cantidad = it.cantidad
            }
        }
    }

    fun transform(input: ProductoDetalleEntity?): ProductoDetalle? {
        return input?.let {
            ProductoDetalle().apply {
                id = it.id
                cuv = it.cuv
                nombreComercial = it.nombreComercial
                factorCuadre = it.factorCuadre
                orden = it.orden
                codigoProducto = it.codigoProducto
                grupo = it.grupo
                precioCatalogo = it.precioCatalogo
                digitable = it.digitable
                cantidad = it.cantidad
            }
        }
    }


    fun transform(input: Collection<ProductoDetalle?>?): List<ProductoDetalleEntity?>? {
        return input?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<ProductoDetalleEntity>()
        }
    }

    fun transform(input: List<ProductoDetalleEntity?>?): Collection<ProductoDetalle?>? {
        return input?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<ProductoDetalle>()
        }
    }
}
