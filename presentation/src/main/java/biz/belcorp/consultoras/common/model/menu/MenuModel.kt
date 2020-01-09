package biz.belcorp.consultoras.common.model.menu

import com.google.gson.annotations.SerializedName

import biz.belcorp.consultoras.feature.home.menu.lateral.SubMenuLateralListAdapter
import biz.belcorp.library.model.BelcorpModel

class MenuModel : BelcorpModel() {

    var menuAppId: Int? = null

    @SerializedName("CodigoMenu")
    var codigo: String = ""

    var descripcion: String = ""

    @SerializedName("Orden")
    var orden: Int? = null

    var codigoMenuPadre: String = ""
    var posicion: Int? = null
    var drawable: Int? = null

    @SerializedName("FlagMenuNuevo")
    var isMenuNuevo: Int = 0

    @SerializedName("Visible")
    var isVisible: Boolean = false

    var subMenus: MutableList<MenuModel> = mutableListOf()
    var subMenuLateralListAdapter: SubMenuLateralListAdapter? = null

    var isState = false
}
