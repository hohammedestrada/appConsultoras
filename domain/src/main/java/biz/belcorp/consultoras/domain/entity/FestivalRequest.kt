package biz.belcorp.consultoras.domain.entity

data class FestivalRequest(
    var isShowSearch: Boolean? = null,
    var festivalSearch: SearchRequest? = null,
    var zoneId: Int? = null,
    var regionCode: String? = null,
    var startDay: Int? = null
)
