package biz.belcorp.consultoras.domain.repository

import biz.belcorp.consultoras.domain.entity.*
import io.reactivex.Observable

/**
 * Interface que implementa los metodos para obtener los datos del usuario
 * tomando en cuenta la entidad que devuelve la respuesta del REST o el SQL
 *
 * @version 1.0
 * @since 2017-05-15
 */
interface ApiRepository {

    /**
     * Metodo que guarda la data de un usuario
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    val device: Observable<Device?>

    /**
     * Metodo que guarda la data de un usuario
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun saveDevice(device: Device?): Observable<Boolean?>

}
