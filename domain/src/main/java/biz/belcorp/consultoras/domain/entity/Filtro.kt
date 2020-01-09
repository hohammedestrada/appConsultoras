package biz.belcorp.consultoras.domain.entity

data class Filtro(
    var codigo: String?,
    var nombre: String?,
    var descripcion: String?,
    var ordenApp: Int?,
    var colorTextoApp: String?,
    var colorFondoApp: String?,
    var valorMinimo: Float?,
    var valorMaximo: Float?,
    var nombreGrupo: String?,
    var idFiltro: Int?,
    var idPadre: Int?,
    var idSeccion: String?
)
