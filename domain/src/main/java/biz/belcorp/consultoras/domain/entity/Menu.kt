package biz.belcorp.consultoras.domain.entity

class Menu {

    var menuAppId: Int? = null
    var codigo: String = ""
    var descripcion: String? = null
    var orden: Int? = null
    var codigoMenuPadre: String? = null
    var posicion: Int? = null
    var subMenus: List<Menu?>? = null
    var isVisible: Boolean = false
    var FlagMenuNuevo: Int? = 0

}
