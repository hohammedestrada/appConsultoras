package biz.belcorp.consultoras.data.repository.datasource.anotacion

import biz.belcorp.consultoras.data.entity.AnotacionEntity
import io.reactivex.Observable

/**
 * Interface que implementa los metodos de anotaciones
 *
 * @version 1.0
 * @since 2017-05-03
 */
interface AnotacionDataStore {

    /**
     * Servicio de que retorna una nota por id
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    operator fun get(id: Int?): Observable<AnotacionEntity?>

    /**
     * Servicio de que retorna una lista de notas por clientLocalID
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun listByClientLocalID(clientLocalID: Int?): Observable<List<AnotacionEntity?>?>

    /**
     * Servicio de que guarda una anotacion
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun save(anotacionEntity: AnotacionEntity?): Observable<AnotacionEntity?>

    /**
     * Servicio de que actualiza una anotacion
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun update(anotacionEntity: AnotacionEntity?): Observable<Boolean?>

    /**
     * Servicio de que elimina una anotacion
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun delete(anotacionEntity: AnotacionEntity?): Observable<Boolean?>

    /**
     * Servicio de que elimina una lista de anotaciones
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun deleteByClientLocalID(clientLocalID: Int?): Observable<Boolean?>

    /**
     * Servicio que obtiene la cantidad de anotaciones por cliente
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun countByClient(maxCount: Int?, clientLocalId: Int?): Observable<Boolean?>
}
