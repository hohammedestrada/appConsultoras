package biz.belcorp.consultoras.data.repository

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.mapper.MenuEntityDataMapper
import biz.belcorp.consultoras.data.repository.datasource.menu.MenuDataStoreFactory
import biz.belcorp.consultoras.domain.entity.Menu
import biz.belcorp.consultoras.domain.repository.MenuRepository
import io.reactivex.Observable

/**
 * Clase de Menu encargada de obtener la data desde una fuente de datos (interna o externa)
 * dicha clase se instancaria una sola vez y se mantendra mientras la aplicacion este viva
 *
 * @version 1.0
 * @since 2017-04-14
 */
@Singleton
class MenuDataRepository
/**
 * Constructor que se le injecta las dependencias que necesita
 *
 * @param menuDataStoreFactory Clase encargada de obtener los datos
 * @param menuEntityDataMapper Clase encargade de realizar el parseo de datos
 */
@Inject
internal constructor(private val menuDataStoreFactory: MenuDataStoreFactory
                     , private val menuEntityDataMapper: MenuEntityDataMapper) : MenuRepository {

    /**
     * Metodo que devuelve los menus de un país
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun get(pais: String?, campaign: String?, revistaDigital: Int?): Observable<Boolean?> {
        val wsStore = this.menuDataStoreFactory.create()
        val localStore = this.menuDataStoreFactory.createDB()
        return wsStore.get(pais, campaign, revistaDigital).flatMap{ localStore.saveList(it)}

    }

    /**
     * Metodo que devuelve los menus desde la base de datos
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun getFromDatabase(pos: Int?): Observable<List<Menu?>?> {
        val dataStore = this.menuDataStoreFactory.createDB()
        return dataStore.get(pos).map { this.menuEntityDataMapper.transform(it) }
    }

    /**
     * Guarda los menus obtenidos desde el servidor
     * @param menuList
     */
    override fun updateVisibleAndOrden(menuList: List<Menu?>?): Observable<Boolean?> {
        val dataStore = this.menuDataStoreFactory.createDB()
        return dataStore.updateVisibleAndOrder(menuEntityDataMapper.transformToDomainEntity(menuList))
    }

    /**
     * Obtiene la descripcion de un menu desde la base de datos local
     * @return menu
     */
    override fun getMenuByCode(code: String?): Observable<Menu?> {
        val dataStore = this.menuDataStoreFactory.createDB()
        return dataStore.find(code).map { this.menuEntityDataMapper.transform(it) }
    }

    /**
     * Obtiene el menú activo de 2 menús desde la base de datos local
     * @return menu
     */
    override fun getActive(code1: String?, code2: String?): Observable<Menu?> {
        val dataStore = this.menuDataStoreFactory.createDB()
        return dataStore.getActive(code1, code2).map { this.menuEntityDataMapper.transform(it) }
    }

    override suspend fun getActive2(code1: String?, code2: String?): Menu? {
        val dataStore = this.menuDataStoreFactory.createDB()
        return this.menuEntityDataMapper.transform(dataStore.getActive2(code1, code2))
    }


}
