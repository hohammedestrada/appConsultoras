package biz.belcorp.consultoras.domain.entity

data class GroupFilter(
    var nombre: String?,
    var excluyente: Boolean?,
    var filtros: List<Filtro?>?
)
