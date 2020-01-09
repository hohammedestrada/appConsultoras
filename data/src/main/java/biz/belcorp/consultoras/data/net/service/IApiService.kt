package biz.belcorp.consultoras.data.net.service

import biz.belcorp.consultoras.data.entity.DeviceEntity
import biz.belcorp.consultoras.data.util.Constant
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Interface que implementa los metodos del servicio de Incentivos
 * basandose en REST API
 *
 * @version 1.0
 * @since 2017-05-03
 */

interface IApiService {

    /**
     * Servicio de tipo GET que obtiene los incentivos
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM)
    @POST(value = "api/dispositivo/guardar")
    fun saveDevice(@Body entity: DeviceEntity?): Observable<Boolean?>
}
