package biz.belcorp.consultoras.domain.repository

import biz.belcorp.consultoras.domain.entity.Menu
import biz.belcorp.consultoras.domain.entity.Ordenamiento
import io.reactivex.Observable

/**
 * Clase de Menu encargada de obtener la data desde la capa de datos
 * dicha clase se instancaria una sola vez y se mantendra mientras la aplicacion este viva
 *
 * @version 1.0
 * @since 2017-04-14
 */
interface MenuRepository {

    /**
     * Metodo que devuelve los menus de un pais
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
     fun get(pais: String?, campaign: String?, revistaDigital: Int?): Observable<Boolean?>

     fun getFromDatabase(pos: Int?): Observable<List<Menu?>?>

    /**
     * Guarda los menus obtenidos desde el servidor
     *
     * @param menuList lista de menu
     */
    fun updateVisibleAndOrden(menuList: List<Menu?>?): Observable<Boolean?>

    /**
        Obtiene el objeto menu por codigo
     */
    fun getMenuByCode(code: String?): Observable<Menu?>

    /**
    Obtiene el objeto menu activo de 2 menús
     */
    fun getActive(code1: String?, code2: String?): Observable<Menu?>

    suspend fun getActive2(code1: String?, code2: String?): Menu?

}
