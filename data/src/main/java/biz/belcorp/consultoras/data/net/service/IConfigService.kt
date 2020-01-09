package biz.belcorp.consultoras.data.net.service

import biz.belcorp.consultoras.data.entity.ConfigResponseEntity
import biz.belcorp.consultoras.data.util.Constant
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers

/**
 * Interface que implementa los metodos del servicio de configuracion
 * basandose en REST API
 *
 * @version 1.0
 * @since 2017-04-25
 */

interface IConfigService {

    /**
     * Servicio de tipo GET que obtiene el listado de tareas
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM)
    @GET(value = "api/Configuracion/ConfiguracionLogin")
    fun get(): Observable<ConfigResponseEntity?>
}
