package biz.belcorp.consultoras.data.repository.datasource.config

import biz.belcorp.consultoras.data.entity.ConfigEntity
import biz.belcorp.consultoras.data.entity.ConfigResponseEntity
import io.reactivex.Observable

/**
 *
 */

interface ConfigDataStore {

    /**
     * Metodo que obtiene los datos de Configuracion
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun get(): Observable<ConfigResponseEntity?>

    fun save(entity: ConfigResponseEntity?): Observable<Boolean?>

    fun save(entity: ConfigEntity?): Observable<Boolean?>

}
