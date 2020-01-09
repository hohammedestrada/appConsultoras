package biz.belcorp.consultoras.domain.repository

import biz.belcorp.consultoras.domain.entity.Tracking
import io.reactivex.Observable

/**
 * Clase de Tracking encargada de obtener la data desde la capa de datos
 * dicha clase se instancaria una sola vez y se mantendra mientras la aplicacion este viva
 *
 * @version 1.0
 * @since 2017-05-03
 */

interface TrackingRepository {


    fun get(top: Int?): Observable<Collection<Tracking?>?>

    fun getFromLocal(top: Int?): Observable<Collection<Tracking?>?>

}
