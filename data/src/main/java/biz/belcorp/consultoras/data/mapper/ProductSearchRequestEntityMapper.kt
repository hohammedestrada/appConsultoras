package biz.belcorp.consultoras.data.mapper

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.entity.ProductSearchRequestEntity
import biz.belcorp.consultoras.domain.entity.ProductSearchRequest

@Singleton
class ProductSearchRequestEntityMapper @Inject
internal constructor() {

    fun transform(input: ProductSearchRequest?): ProductSearchRequestEntity? {
        return input?.let {
            ProductSearchRequestEntity().apply{
                campaingId = it.campaingId
                zoneId = it.zoneId
                cuv = it.cuv
                description = it.description
            }
        }
    }

    fun transform(input: ProductSearchRequestEntity?): ProductSearchRequest? {
        return input?.let {
            ProductSearchRequest().apply{
                campaingId = it.campaingId
                zoneId = it.zoneId
                cuv = it.cuv
                description = it.description
            }
        }
    }


    fun transform(input: Collection<ProductSearchRequest?>?): List<ProductSearchRequestEntity?>? {
        return input?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<ProductSearchRequestEntity>()
        }
    }

    fun transform(input: List<ProductSearchRequestEntity?>?): Collection<ProductSearchRequest?>? {
        return input?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<ProductSearchRequest>()
        }
    }
}
