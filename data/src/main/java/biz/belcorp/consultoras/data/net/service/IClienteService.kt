package biz.belcorp.consultoras.data.net.service

import biz.belcorp.consultoras.data.entity.*

import biz.belcorp.consultoras.data.util.Constant
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interface que implementa los metodos del servicio de un cliente
 * basandose en REST API
 *
 * @version 1.0
 * @since 2017-05-03
 */
interface IClienteService {

    /**
     * Servicio de tipo POST que sube
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "api/Cliente/Sincronizar")
    fun subida(@Body clientes: List<ClienteEntity?>?): Observable<List<ClienteEntity?>?>

    /**
     * Servicio de tipo GET que obtiene a los clientes
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/Cliente/Clientes")
    fun bajada(@Query("ClienteID") clientId: Int?,
               @Query("CodigoCampania") campaingCode: String?)
        : Observable<List<ClienteEntity?>?>

    /**
     * Servicio de tipo GET que obtiene los deudores
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/Cliente/Deudores")
    fun deudores(): Observable<List<DeudorRequestEntity?>?>

    /**
     * Servicio de tipo GET que obtiene los movimientos de
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "/api/Cliente/{clienteId}/Movimientos")
    fun getMovements(@Path("clienteId") clientId: Int?)
        : Observable<List<ClientMovementEntity?>?>

    /**
     * Servicio de tipo POST que guarda un movimiento
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "/api/Cliente/{clienteId}/Movimiento")
    fun saveMovement(@Path("clienteId") clientId: Int?,
                     @Body movement: ClientMovementEntity?)
        : Observable<ClientMovementEntity?>

    /**
     * Servicio de tipo POST que guarda un movimiento
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @PUT(value = "/api/Cliente/{clienteId}/Movimiento/{movementId}")
    fun updateMovement(@Path("clienteId") clientId: Int?,
                       @Path("movementId") movementId: Int?,
                       @Body movement: ClientMovementEntity?): Observable<String?>

    /**
     * Servicio de tipo POST que guarda un movimiento
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @DELETE(value = "/api/Cliente/{clienteId}/Movimiento/{movementId}")
    fun deleteMovement(@Path("clienteId") clientID: Int?,
                       @Path("movementId") movementID: Int?): Observable<Boolean?>

    /**
     * Servicio de tipo POST que sube las anotaciones
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "api/Cliente/Notas")
    fun syncAnotations(@Body anotations: List<AnotacionEntity?>?)
        : Observable<List<AnotacionEntity?>?>

    /**
     * Servicio de tipo PUT que actualiza el detalle de un movimiento
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @PUT(value = "api/Cliente/Movimiento/Detalle")
    fun updateProductos(@Body productoEntities: List<ProductoPedidoEntity?>?): Observable<Boolean?>

}
