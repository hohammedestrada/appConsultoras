package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.ProductResponseEntity
import biz.belcorp.consultoras.domain.entity.ProductMovement
import javax.inject.Inject
import javax.inject.Singleton

/**
 *
 */
@Singleton
class ProductMovementEntityDataMapper @Inject
constructor(){

    fun transform(product: ProductResponseEntity): ProductMovement {
        return product.let {
            ProductMovement().apply {
                code = it.code
                name = it.name
                price = it.price
                quantity = it.quantity
                clientID = it.clientID
                movementID = it.movementId
                productID = it.productMovementID
            }
        }
    }

    fun transform(productMovement: ProductMovement): ProductResponseEntity {
        return productMovement.let {
            ProductResponseEntity().apply {
                code = it.code
                name = it.name
                price = it.price
                quantity = it.quantity
                clientID = it.clientID
                movementId = it.movementID
                productMovementID = it.productID
            }
        }
    }

    fun transformResponse(products: List<ProductResponseEntity>?): List<ProductMovement> {
        return products?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<ProductMovement>()
        }
    }

    fun transform(productMovements: List<ProductMovement>?): List<ProductResponseEntity> {
        return productMovements?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<ProductResponseEntity>()
        }
    }
}
