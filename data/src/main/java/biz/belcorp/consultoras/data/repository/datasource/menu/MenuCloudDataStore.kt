package biz.belcorp.consultoras.data.repository.datasource.menu

import biz.belcorp.consultoras.data.entity.MenuEntity
import biz.belcorp.consultoras.data.net.service.IMenuService
import biz.belcorp.consultoras.data.util.Constant
import io.reactivex.Observable

/**
 * Clase de Config encargada de obtener los datos desde un servicio
 *
 * @version 1.0
 * @since 2017-04-25
 */

class MenuCloudDataStore
/**
 * Constructor que recibe el servicio ha ser ejecutado
 *
 * @param service Clase encargado de ejecutar un metodo del servicio
 */
(private val service: IMenuService) : MenuDataStore {

    /**
     * Metodo que obtiene la lista de menús de un servicio
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun get(paisISO: String?, campaign: String?, revistaDigital: Int?): Observable<List<MenuEntity?>?> {
        return service[campaign, revistaDigital, Constant.MENU_VERSION]
    }

    override fun get(pos: Int?): Observable<List<MenuEntity?>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getActive(code1: String?, code2: String?): Observable<MenuEntity?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getActive2(code1: String?, code2: String?): MenuEntity? {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun find(id: Int?): Observable<MenuEntity?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun find(codigo: String?): Observable<MenuEntity?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun save(entity: MenuEntity?): Observable<Boolean?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun delete(id: Int?): Observable<Boolean?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun saveList(menuList: List<MenuEntity?>?): Observable<Boolean?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun updateVisibleAndOrder(menuList: List<MenuEntity?>?): Observable<Boolean?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }
}
