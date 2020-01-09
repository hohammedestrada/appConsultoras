package biz.belcorp.consultoras.data.net.service

import biz.belcorp.consultoras.data.entity.TrackingEntity
import biz.belcorp.consultoras.data.util.Constant
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * Interface que implementa los metodos del servicio de Incentivos
 * basandose en REST API
 *
 * @version 1.0
 * @since 2017-05-03
 */

interface ITrackingService {

    /**
     * Servicio de tipo GET que obtiene los incentivos
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.1/Tracking")
    fun get(@Query("top") top: Int?): Observable<List<TrackingEntity?>?>
}
