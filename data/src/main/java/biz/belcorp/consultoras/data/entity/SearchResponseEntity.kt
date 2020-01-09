package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.domain.entity.SearchResponse
import com.google.gson.annotations.SerializedName

class SearchResponseEntity {

    @SerializedName("Total")
    var total: Int? = null
    @SerializedName("Productos")
    var productos: List<ProductCuvEntity?>? = null
    @SerializedName("Filtros")
    var filtros: List<SearchFilterEntity?>? = null

    @SerializedName("Promocion")
    var promocion: PromotionEntity? = null


    companion object {

        fun transform(input: SearchResponseEntity?): SearchResponse? {
            input?.run {
                val newSearchResponse = SearchResponse()
                newSearchResponse.total = total
                newSearchResponse.productos = ProductCuvEntity.transformList(productos)
                newSearchResponse.filtros = SearchFilterEntity.transformListEntity(filtros)

                newSearchResponse.promocion?.detalle = ProductCuvEntity.transformList(promocion?.detalle)
                newSearchResponse.promocion?.posicion = promocion?.posicion ?: 0
                return newSearchResponse
            }
            return null
        }

    }

}
