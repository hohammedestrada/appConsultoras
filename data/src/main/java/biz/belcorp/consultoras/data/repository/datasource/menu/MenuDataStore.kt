package biz.belcorp.consultoras.data.repository.datasource.menu

import biz.belcorp.consultoras.data.entity.MenuEntity
import io.reactivex.Observable

/**
 *
 */

interface MenuDataStore {

    /**
     * Metodo que obtiene los Menus
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun get(paisISO: String?, campaign: String?, revistaDigital: Int?): Observable<List<MenuEntity?>?>

    /**
     * Metodo que obtiene los Menus
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun get(pos: Int?): Observable<List<MenuEntity?>?>

    /**
     * Metodo que obtiene el menú activo de 2 items
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun getActive(code1: String?, code2: String?): Observable<MenuEntity?>

    fun getActive2(code1: String?, code2: String?): MenuEntity?

    fun find(id: Int?): Observable<MenuEntity?>

    fun find(codigo: String?): Observable<MenuEntity?>

    fun save(entity: MenuEntity?): Observable<Boolean?>

    fun delete(id: Int?): Observable<Boolean?>

    fun saveList(menuList: List<MenuEntity?>?): Observable<Boolean?>

    fun updateVisibleAndOrder(menuList: List<MenuEntity?>?): Observable<Boolean?>
}
