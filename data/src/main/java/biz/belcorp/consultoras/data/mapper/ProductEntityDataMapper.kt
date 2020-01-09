package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.ProductEntity
import biz.belcorp.consultoras.domain.entity.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductEntityDataMapper @Inject
internal constructor() {

    fun transform(input: Product?): ProductEntity? {
        return input?.let {
            ProductEntity().apply {
                id = it.id
                cuv = it.cuv
                description = it.description
                catalogo = it.catalogo
                categoria = it.categoria
                numeroPagina = it.numeroPagina
            }
        }
    }

    fun transform(input: ProductEntity?): Product? {
        return input?.let {
            Product().apply {
                id = it.id
                cuv = it.cuv
                description = it.description
                catalogo = it.catalogo
                categoria = it.categoria
                numeroPagina = it.numeroPagina
            }
        }
    }


    fun transform(input: Collection<Product?>?): List<ProductEntity?>? {
        return input?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<ProductEntity>()
        }
    }

    fun transform(input: List<ProductEntity?>?): Collection<Product?>? {
        return input?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<Product>()
        }
    }
}
