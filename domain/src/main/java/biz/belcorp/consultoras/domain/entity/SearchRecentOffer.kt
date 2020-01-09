package biz.belcorp.consultoras.domain.entity


data class SearchRecentOffer(
    var cuv: String?,
    var nombreOferta: String?,
    var precioCatalogo: Double?,
    var precioValorizado: Double?,
    var imagenURL: String?,
    var tipoOferta: String?,
    var flagFestival: Boolean?,
    var flagPromocion: Boolean?,
    var agotado: Boolean?,
    var flagCatalogo: Boolean?
)
