package biz.belcorp.consultoras.domain.repository

import biz.belcorp.consultoras.domain.entity.Config
import biz.belcorp.consultoras.domain.entity.ConfigReponse
import io.reactivex.Observable

/**
 * Interface de ConfigRepository
 *
 * @version 1.0
 * @since 2017-04-14
 */

interface ConfigRepository {

    /**
     * Metodo que obtiene la configuracion inicial
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun get(): Observable<ConfigReponse?>

    /**
     * Metodo que obtiene la configuracion inicial
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun getFromLocal(): Observable<ConfigReponse?>

    fun save(appName: String): Observable<Boolean>

    fun save(config: Config?): Observable<Boolean?>
}
