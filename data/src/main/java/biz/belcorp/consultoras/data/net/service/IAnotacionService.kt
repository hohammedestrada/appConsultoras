package biz.belcorp.consultoras.data.net.service

import biz.belcorp.consultoras.data.entity.AnotacionEntity
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
interface IAnotacionService {

    /**
     * Servicio de tipo POST que guarda una nota
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "/api/Cliente/{clienteId}/Nota")
    fun save(@Path("clienteId") clientId: Int?, @Body anotacion: AnotacionEntity?)
        : Observable<AnotacionEntity?>

    /**
     * Servicio de tipo PUT que actualiza una nota
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @PUT(value = "/api/Cliente/{clienteId}/Nota/{notaId}")
    fun update(@Path("clienteId") clienteID: Int?, @Path("notaId") anotacionID: Int?,
               @Body anotacionEntity: AnotacionEntity?): Observable<Boolean?>

    /**
     * Servicio de tipo DELETE que elimina una nota
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @DELETE(value = "/api/Cliente/{clienteId}/Nota/{notaId}")
    fun delete(@Path("clienteId") clienteID: Int?, @Path("notaId") anotacionID: Int?)
        : Observable<Boolean?>
}
