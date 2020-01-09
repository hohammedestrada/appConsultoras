package biz.belcorp.consultoras.domain.entity

class SearchResponse {
    var total: Int? = null
    var productos: Collection<ProductCUV?>? = null
    var filtros: Collection<SearchFilter?>? = null
    var promocion:  Promotion?  = null
}
