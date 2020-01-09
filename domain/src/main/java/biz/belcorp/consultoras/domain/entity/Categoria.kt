package biz.belcorp.consultoras.domain.entity

data class Categoria(
    var codigo: String?,
    var nombre: String?,
    var descripcion: String?,
    var ordenApp: Int?,
    var colorTextoApp: String?,
    var colorFondoApp: String?,
    var cantidad: Int?,
    var urlImagenGrupo: String?
)
