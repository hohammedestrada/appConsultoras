package biz.belcorp.consultoras.data.net.service

import biz.belcorp.consultoras.data.entity.ConcursoEntity
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

interface IIncentivosService {

    /**
     * Servicio de tipo GET que obtiene los incentivos
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.1/Incentivo")
    operator fun get(@Query("CodigoCampania") campaingCode: String?)
        : Observable<List<ConcursoEntity?>?>

    /**
     *
     * api/v1.1/Incentivo/Historico
     * Servicio de tipo GET que obtiene el historico de incentivos
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.1/Incentivo/Historico")
    fun getHistory(@Query("CodigoCampania") campaingCode: String?)
        : Observable<List<ConcursoEntity?>?>
}
