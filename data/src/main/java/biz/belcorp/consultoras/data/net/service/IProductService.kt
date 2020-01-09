package biz.belcorp.consultoras.data.net.service

import biz.belcorp.consultoras.data.entity.*
import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.library.net.dto.ServiceDto
import io.reactivex.Observable
import retrofit2.http.*

/**
 * Interface que implementa los metodos del servicio de un producto
 * basandose en REST API
 *
 * @version 1.0
 * @since 2017-11-28
 */
interface IProductService {

    /**
     * Servicio de tipo GET que lista los productos agotados
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "/api/v1.1/Producto/Agotados")
    fun getStockouts(@Query("campania") campaing: Int?,
                     @Query("zonaId") zoneId: Int?,
                     @Query("cuv") cuv: String?,
                     @Query("descripcion") description: String?): Observable<List<ProductEntity?>?>

    /**
     * Servicio de tipo GET que devuelve la personalización dummy de la consultora
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "/api/v1.1/Producto/Buscador/Personalizacion")
    fun getPersonalization(@Query("campaniaID") campaniaId: Int?)
        : Observable<String?>

    /**
     * Servicio de POST que devuelve una lista de productos y el total encontrado
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "/api/v1.2/Producto/Buscador")
    fun search(@Body searchRequest: SearchRequestEntity): Observable<ServiceDto<SearchResponseEntity?>?>

    /**
     * Servicio de GET que devuelve la lista de parametros para las opciones de ordenamiento
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "/api/v1.2/Producto/Buscador/Ordenamiento")
    fun getOrderByParameters(): Observable<ServiceDto<List<SearchOrderByResponseEntity?>?>?>

    /**
     * Servicio de GET que devuelve la lista de regalos
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */

    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "/api/v1.1/Pedido/Regalo/Producto")
    fun listaRegalos(
        @Query("inModel.campaniaID") campaniaID: Int?,
        @Query("inModel.nroCampanias") nroCampanias: Int?,
        @Query("inModel.codigoPrograma") codigoPrograma: String?,
        @Query("inModel.consecutivoNueva") consecutivoNueva: Int?
    ): Observable<List<EstrategiaCarruselEntity?>?>

    /**
     * Servicio de POST que Autoguarda el regalo
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */

    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "/api/v1.1/Pedido/Regalo/AgregaDefault")
    fun autoSaveGift(
        @Body autoGuardadoRequest: GiftSaveRequestEntity


    ): Observable<Boolean?>
}
