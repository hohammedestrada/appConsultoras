package biz.belcorp.consultoras.data.repository.datasource.tracking

import biz.belcorp.consultoras.data.entity.TrackingEntity
import io.reactivex.Observable

/**
 * Interface que implementa los metodos para obtener los datos de un cliente
 * tomando en cuenta la entidad que devuelve la respuesta del REST o el SQL
 *
 * @version 1.0
 * @since 2017-05-03
 */
interface TrackingDataStore {

    /**
     * Servicio de que obtiene a los incentivos
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun get(top: Int?): Observable<List<TrackingEntity?>?>

    fun save(list: List<TrackingEntity?>?): Observable<List<TrackingEntity?>?>
}
