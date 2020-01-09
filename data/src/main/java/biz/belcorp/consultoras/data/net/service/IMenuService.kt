package biz.belcorp.consultoras.data.net.service

import biz.belcorp.consultoras.data.entity.MenuEntity
import biz.belcorp.consultoras.data.util.Constant
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * Interface que implementa los metodos del servicio de configuracion
 * basandose en REST API
 *
 * @version 1.0
 * @since 2017-04-25
 */

interface IMenuService {

    /**
     * Servicio de tipo GET que obtiene el listado de menus
     * @param revistaDigital estado revista digital del usuario
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.2/Configuracion/ConfiguracionMenuApp")
    operator fun get(@Query("campania")campaign: String?,
                     @Query("revistaDigitalSuscripcion") revistaDigital: Int?,
                     @Query("verMenu") menuVersion: Int?)
        : Observable<List<MenuEntity?>?>
}
