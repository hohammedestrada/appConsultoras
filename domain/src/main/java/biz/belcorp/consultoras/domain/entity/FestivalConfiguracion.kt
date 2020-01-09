package biz.belcorp.consultoras.domain.entity


class FestivalConfiguracion {
    var idFestival: Int?=null
    var campaniaId: Int?=null
    var PremioGratis: Boolean?=null
    var Tipo: Int?=null
    var Activo: Boolean?=false
    var Titulo: String?=""
    var DescripcionCorta: String?=null
    var DescripcionLarga: String?=null
    var Banner:FestivalBanner?=null
    var Sello:FestivalSello?=null
    var Categoria : List<FestivalCategoria?>?=null

}

