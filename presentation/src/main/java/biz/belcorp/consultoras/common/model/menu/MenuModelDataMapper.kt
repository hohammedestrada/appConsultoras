package biz.belcorp.consultoras.common.model.menu

import java.util.ArrayList
import java.util.Collections

import javax.inject.Inject

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.Menu
import biz.belcorp.library.util.StringUtil

/**
 * Clase encarga de realizar el mapeo de la entidad menu(tabla o json) a
 * una entidad del dominio
 *
 * @version 1.0
 * @since 2017-04-14
 */
@PerActivity
class MenuModelDataMapper @Inject
internal constructor() {

    /**
     * Transforma la entidad a una entidad del dominio.
     *
     * @param model Entidad tipo tabla o json
     * @return object Entidad de dominio
     */
    fun transform(model: MenuModel?): Menu? {
        var menu: Menu? = null

        if (null != model) {
            menu = Menu()
            menu.codigo = model.codigo
            menu.codigoMenuPadre = model.codigoMenuPadre
            menu.descripcion = model.descripcion
            menu.menuAppId = model.menuAppId
            menu.orden = model.orden
            menu.posicion = model.posicion
            menu.subMenus = transform(model.subMenus)
            menu.isVisible = model.isVisible
            menu.FlagMenuNuevo = model.isMenuNuevo
        }
        return menu
    }

    /**
     * Transforma la entidad de dominio a una entidad.
     *
     * @param model Entidad de dominio
     * @return object Entidad
     */
    fun transform(model: Menu?): MenuModel? {
        var menu: MenuModel? = null

        if (null != model) {
            menu = MenuModel()
            menu.codigo = model.codigo
            menu.codigoMenuPadre = model.codigoMenuPadre?.let { it -> it } ?: kotlin.run { StringUtil.Empty }
            menu.descripcion = model.descripcion?.let { it -> it } ?: kotlin.run { StringUtil.Empty }
            menu.menuAppId = model.menuAppId
            menu.orden = model.orden
            menu.posicion = model.posicion
            menu.subMenus = transformToDomainModel(
                model.subMenus?.let {
                    it.filterNotNull()
                }
            )
            menu.isVisible = model.isVisible
            menu.isMenuNuevo = model.FlagMenuNuevo?.let { it -> it } ?: kotlin.run { 0 }
        }
        return menu
    }

    /**
     * Transforma una lista de entidades a una lista de entidades de modelo
     *
     * @param list Lista de entidades
     * @return list Lista de entidades del dominio
     */
    fun transform(list: Collection<MenuModel>?): List<Menu> {
        val menus = ArrayList<Menu>()

        if (null == list) {
            return emptyList()
        }

        for (model in list) {
            val menu = transform(model)
            if (null != menu) {
                menus.add(menu)
            }
        }

        return menus
    }

    /**
     * Transforma una lista de entidades de modelo a una lista de entidades
     *
     * @param list Lista de entidades del modelo
     * @return list Lista de entidades
     */

    fun transformToDomainModel(list: List<Menu>?): MutableList<MenuModel> {
        val menus = ArrayList<MenuModel>()

        if (null == list) {
            return mutableListOf()
        }

        for (model in list) {
            val menu = transform(model)
            if (null != menu) {
                menus.add(menu)
            }
        }

        return menus
    }


}
