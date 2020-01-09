package biz.belcorp.consultoras.data.repository.datasource.api

import biz.belcorp.consultoras.data.entity.DeviceEntity
import io.reactivex.Observable

/**
 * Interface que implementa los metodos para obtener los datos del loginOnline
 * tomando en cuenta la entidad que devuelve la respuesta del REST o el SQL
 *
 * @version 1.0
 * @since 2017-05-15
 */
interface ApiDataStore {

    val device: Observable<DeviceEntity?>

    /**
     * Metodo que registra la data del dispositivo
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun saveDevice(entity: DeviceEntity?): Observable<Boolean?>

}
