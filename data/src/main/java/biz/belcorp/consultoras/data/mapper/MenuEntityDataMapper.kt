package biz.belcorp.consultoras.data.mapper

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.entity.MenuEntity
import biz.belcorp.consultoras.domain.entity.Menu

/**
 * Clase encarga de realizar el mapeo de la entidad menu(tabla o json) a
 * una entidad del dominio
 *
 * @version 1.0
 * @since 2017-04-14
 */
@Singleton
class MenuEntityDataMapper @Inject
internal constructor() {

    /**
     * Transforma la entidad a una entidad del dominio.
     *
     * @param entity Entidad tipo tabla o json
     * @return object Entidad de dominio
     */
    fun transform(entity: MenuEntity?): Menu? {
        return entity?.let {
            Menu().apply {
                codigo = it.codigo ?: ""
                codigoMenuPadre = it.codigoMenuPadre
                descripcion = it.descripcion
                menuAppId = it.menuAppId
                orden = it.orden
                posicion = it.posicion
                isVisible = it.isVisible ?: false
                FlagMenuNuevo = it.FlagMenuNuevo ?: 0
                subMenus = transform(it.subMenus)
            }
        }
    }

    /**
     * Transforma la entidad de dominio a una entidad.
     *
     * @param entity Entidad de dominio
     * @return object Entidad
     */
    fun transform(entity: Menu?): MenuEntity? {
        return entity?.let {
            MenuEntity().apply {
                codigo = it.codigo
                codigoMenuPadre = it.codigoMenuPadre
                descripcion = it.descripcion
                menuAppId = it.menuAppId
                orden = it.orden
                posicion = it.posicion
                isVisible = it.isVisible
                FlagMenuNuevo= it.FlagMenuNuevo
                subMenus = transformToDomainEntity(it.subMenus)
            }
        }
    }


    /**
     * Transforma una lista de entidades a una lista de entidades de dominio
     *
     * @param list Lista de entidades
     * @return list Lista de entidades del dominio
     */
    fun transform(list: Collection<MenuEntity?>?): List<Menu?>? {
        return list?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<Menu>()
        }
    }

    /**
     * Transforma una lista de entidades de dominio a una lista de entidades
     *
     * @param list Lista de entidades del dominio
     * @return list Lista de entidades
     */
    fun transformToDomainEntity(list: Collection<Menu?>?): List<MenuEntity?>? {
        return list?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<MenuEntity>()
        }
    }

}
