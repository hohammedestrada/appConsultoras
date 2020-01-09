package biz.belcorp.consultoras.data.net.service

import biz.belcorp.consultoras.data.entity.RecordatorioEntity
import biz.belcorp.consultoras.data.util.Constant
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Interface que implementa los metodos del servicio de un cliente
 * basandose en REST API
 *
 * @version 1.0
 * @since 2017-05-03
 */

interface IRecordatoryService {

    /**
     * Servicio de tipo POST que guarda un recordatorio
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "/api/Cliente/{clienteId}/Recordatorio")
    fun saveRecordatory(@Path("clienteId") clientId: Int?,
                        @Body recordatorioEntity: RecordatorioEntity?): Observable<RecordatorioEntity?>

    /**
     * Servicio de tipo PUT que actualiza un recordatorio
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @PUT(value = "/api/Cliente/{clienteId}/Recordatorio/{recordatorioId}")
    fun updateRecordatory(@Path("clienteId") clientId: Int?
                          , @Path("recordatorioId") recordatorioId: Int?
                          , @Body recordatorioEntity: RecordatorioEntity?): Observable<Boolean?>

    /**
     * Servicio de tipo DELETE que elimina un recordatorio
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @DELETE(value = "/api/Cliente/{clienteId}/Recordatorio/{recordatorioId}")
    fun deleteRecordatory(@Path("recordatorioId") recordatorioId: Int?,
                          @Path("clienteId") clienteId: Int?): Observable<Boolean?>
}
