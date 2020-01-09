package biz.belcorp.consultoras.domain.entity.gallery

class FiltroGaleria {
    var Codigo : String = ""
    var Descripcion : String = ""
    var Tipo : Int? = -1
    var Orden : Int = -2
    var Activo : Boolean = false
    var IdPadre : Int = 0
    var CodigoPadre : String = ""
    var OrdenPadre : Int = 0
    var Otros : String = ""
    var OtrosAdd : String = ""
    var EsSeccion : Boolean = false
    var EsExcluyente : Boolean = false
}
