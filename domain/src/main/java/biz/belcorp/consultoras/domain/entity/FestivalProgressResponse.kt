package biz.belcorp.consultoras.domain.entity

data class FestivalProgressResponse(
    var cuvPremio: String?,
    var porcentajeProgreso: Int?,
    var montoRestante: Double?,
    var flagPremioAgregado: Boolean?)
