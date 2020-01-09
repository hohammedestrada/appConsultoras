package biz.belcorp.consultoras.data.net.service

import biz.belcorp.consultoras.data.entity.*
import biz.belcorp.consultoras.data.entity.campaignInformation.InfoCampaignEntity
import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.library.net.dto.ServiceDto
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import retrofit2.http.*

/**
 *
 */
interface IOrderService {

    /**
     * Servicio de tipo POST que inserta un pedido
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "api/Pedido/Insert")
    fun addOrder(@Body body: OrderEntity?): Observable<ServiceDto<Any>?>

    /**
     * Servicio de tipo POST que deshace la reserva de pedido
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "api/Pedido/DeshacerReservaPedido")
    fun undoReservation(@Body body: OrderEntity?): Observable<ServiceDto<Any>?>

    /**
     * Servicio de tipo GET que obtiene la lista del pedido
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.1/Pedido/MisPedidos")
    fun get(@Query("campania") campania: String?,
            @Query("top") top: Int?,
            @Query("consultoraAsociada") consultoraAsociada: String?,
            @Query("mostrarPaqueteDocumentario") mostrarPaqueteDocumentario: Boolean?)
        : Observable<List<MyOrderEntity?>?>

    /**
     * Servicio de tipo POST que trae el pdf de un pedido
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */

    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.1/Pedido/PaqueteDocumentario")
    fun paqueteDocumentario(@Query("numeroPedido") numeroPedido: String?)
        : Observable<ServiceDto<String>?>

    /**
     * Servicio de tipo GET que obtiene la configuración inicial de escalas de descuento
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.1/Pedido/Configuracion")
    fun config(@Query("inModel.campaniaID") campaniaID: Int?
               , @Query("inModel.codigoRegion") codigoRegion: String?
               , @Query("inModel.codigoZona") codigoZona: String?)
        : Observable<BarraEntity?>

    /**
     * Servicio de tipo GET que obtiene la lista de productos ofrecidos en el carrusel de ofertas
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.2/Pedido/EstrategiaCarrusel")
    fun estrategiaCarrusel(
        @Query("inModel.campaniaID") campaniaID: Int?,
        @Query("inModel.nroCampanias") nroCampanias: Int?,
        @Query("inModel.consultoraAsociada") consultoraAsociada: String?,
        @Query("inModel.fechaInicioFacturacion") fechaInicioFacturacion: String?,
        @Query("inModel.fechaFinFacturacion") fechaFinFacturacion: String?,
        @Query("inModel.codigoPrograma") codigoPrograma: String?,
        @Query("inModel.consecutivoNueva") consecutivoNueva: Int?,
        @Query("inModel.codigoRegion") codigoRegion: String?,
        @Query("inModel.zonaID") zonaID: Int?,
        @Query("inModel.simbolo") simbolo: String?,
        @Query("inModel.rDTieneRDC") rDTieneRDC: Boolean?,
        @Query("inModel.rDEsSuscrita") rDEsSuscrita: Boolean?,
        @Query("inModel.rDEsActiva") rDEsActiva: Boolean?,
        @Query("inModel.rDActivoMdo") rDActivoMdo: Boolean?,
        @Query("inModel.usuarioPrueba") usuarioPrueba: Boolean?,
        @Query("inModel.tieneMG") tieneMG: Boolean?,
        @Query("inModel.codigoZona") codigoZona: String?,
        @Query("inModel.conDuoPerfecto") conDuoPerfecto: Boolean?
    ): Observable<List<EstrategiaCarruselEntity?>?>

    /**
     * Servicio de tipo POST que agrega una oferta del carrusel al pedido
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "api/v1.1/Pedido/EstrategiaCarrusel")
    fun addOfferCarrusel(@Body request: PedidoInsertRequestEntity?): Observable<ServiceDto<Any>?>

    /**
     * Servicio de tipo POST que agrega el kit de inicio al pedido de la consultora
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "api/v1.1/Pedido/KitInicio")
    fun kitInicio(@Body request: KitInicioRequest?): Observable<ServiceDto<Any>?>


    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "api/v1.1/Pedido/KitSociaEmpresaria")
    fun kitSociasEmpresarias(@Body campaniaID: KitSociaEmpresariaRequestEntity?): Deferred<ServiceDto<Any>?>


    /**
     * Servicio de tipo GET que obtiene el pedido de la consultora
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.1/Pedido")
    fun getOrdersList(
        @Query("inModel.campaniaID") campaniaID: Int?,
        @Query("inModel.codigoPrograma") codigoPrograma: String?,
        @Query("inModel.consecutivoNueva") consecutivoNueva: Int?,
        @Query("inModel.montoMaximoPedido") montoMaximoPedido: Double?,
        @Query("inModel.consultoraNueva") consultoraNueva: Int?,
        @Query("inModel.nroCampanias") nroCampanias: Int?,
        @Query("inModel.nivelCaminoBrillante") nivelCaminoBrillante: Int?,
        @Query("inModel.nombreConsultora") nombreConsultora: String?,
        @Query("inModel.codigoRegion") codigoRegion: String?,
        @Query("inModel.codigoZona") codigoZona: String?,
        @Query("inModel.codigoSeccion") codigoSeccion: String?,
        @Query("inModel.esUltimoDiaFacturacion") esUltimoDiaFacturacion: Boolean?,
        @Query("inModel.pagoContado") pagoContado: Boolean?,
        @Query("inModel.fechaInicioFacturacion") fechaInicioFacturacion: String?,
        @Query("inModel.fechaFinFacturacion") fechaFinFacturacion: String?,
        @Query("inModel.facturarPedidoFM") isMultipedido: Boolean?
    ): Observable<PedidoGetResponseEntity?>

    /**
     * Metodo que obtiene el pedido de la consultora con corrutinas
     *
     * @return Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.1/Pedido")
    fun getOrders(
        @Query("inModel.campaniaID") campaniaID: Int?,
        @Query("inModel.codigoPrograma") codigoPrograma: String?,
        @Query("inModel.consecutivoNueva") consecutivoNueva: Int?,
        @Query("inModel.montoMaximoPedido") montoMaximoPedido: Double?,
        @Query("inModel.consultoraNueva") consultoraNueva: Int?,
        @Query("inModel.nroCampanias") nroCampanias: Int?,
        @Query("inModel.nivelCaminoBrillante") nivelCaminoBrillante: Int?,
        @Query("inModel.nombreConsultora") nombreConsultora: String?,
        @Query("inModel.codigoRegion") codigoRegion: String?,
        @Query("inModel.codigoZona") codigoZona: String?,
        @Query("inModel.codigoSeccion") codigoSeccion: String?
        ): Deferred<PedidoGetResponseEntity?>

    /**
     * Servicio de tipo GET que obtiene el producto consultando el cuv
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.1/Pedido/Producto")
    fun searchCUV(
        @Query("inModel.campaniaID") campaniaID: Int?
        , @Query("inModel.cUV") cuv: String?
        , @Query("inModel.nroCampanias") nroCampanias: Int?
        , @Query("inModel.consultoraAsociada") consultoraAsociada: String?
        , @Query("inModel.codigoPrograma") codigoPrograma: String?
        , @Query("inModel.consecutivoNueva") consecutivoNueva: Int?
        , @Query("inModel.zonaId") zoneId: Int?
        , @Query("inModel.regionId") regionId: Int?
        , @Query("inModel.nivelCaminoBrillante") nivelCaminoBrillante: Int?
    ): Observable<ServiceDto<ProductCuvEntity>?>

    /**
     * Servicio de tipo GET que obtiene el producto consultando el cuv
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.1/Pedido/Producto")
    fun searchCUVCoroutines(
        @Query("inModel.campaniaID") campaniaID: Int?
        , @Query("inModel.cUV") cuv: String?
        , @Query("inModel.nroCampanias") nroCampanias: Int?
        , @Query("inModel.consultoraAsociada") consultoraAsociada: String?
        , @Query("inModel.codigoPrograma") codigoPrograma: String?
        , @Query("inModel.consecutivoNueva") consecutivoNueva: Int?
        , @Query("inModel.zonaId") zoneId: Int?
        , @Query("inModel.regionId") regionId: Int?
        , @Query("inModel.nivelCaminoBrillante") nivelCaminoBrillante: Int?
    ): Deferred<ServiceDto<ProductCuvEntity>?>

    /**
     * Servicio de tipo POST que registra el pedido de la consultora
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "api/v1.2/Pedido")
    fun insertPedido(@Body request: PedidoInsertRequestEntity?): Observable<ServiceDto<Any>?>

    /**
     * Servicio de tipo POST que registra el pedido de la consultora
     * Versión con corrutinas (Homologado)
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "api/v1.2/Pedido")
    fun insertarPedido(@Body request: PedidoInsertRequestEntity?): Deferred<ServiceDto<Any>?>

    /**
     * Servicio de tipo POST que hace una inserción masiva de pedidos
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "api/v1.1/Pedido/Masivo")
    fun insercionMasivaPedido(@Body request: PedidoMasivoRequest?)
        : Observable<ServiceDto<List<PedidoMasivoResponseEntity>>?>

    /**
     * Servicio de tipo PUT que actualiza el pedido de la consultora
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @PUT(value = "api/v1.2/Pedido")
    fun updatePedido(@Body request: PedidoUpdateRequest?): Observable<ServiceDto<Any>?>

    /**
     * Servicio de tipo DELETE que elimina el pedido o un item del pedido de la consultora
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @HTTP(method = "DELETE", path = "api/v1.2/Pedido", hasBody = true)
    fun deletePedido(@Body request: PedidoDeleteRequest?): Observable<ServiceDto<Any>?>

    /**
     * Servicio de tipo POST que realiza la reserva de pedido
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "api/v1.1/Pedido/Reserva")
    fun reservaPedido(@Body request: PedidoReservaRequest?): Observable<ServiceDto<Any>?>

    /**
     * Servicio de tipo POST que deshace la reserva de pedido
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "api/v1.1/Pedido/DeshacerReserva")
    fun deshacerPedido(@Body request: HashMap<String, Any?>)
        : Observable<ServiceDto<ObservacionPedidoEntity>?>

    /**
     * Servicio de tipo POST que obtiene la lista de productos del carrusel de oferta final
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "api/v1.1/Pedido/OfertaFinalCarrusel")
    fun getFinalOfferList(@Body request: PedidoOfertaFinalRequest?): Observable<ServiceDto<Any>?>

    /**
     * Servicio de tipo POST que registra un producto del carrusel de oferta final al pedido
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "api/v1.1/Pedido/OfertaFinalCarrusel/Insertar")
    fun addFinalOffer(@Body request: PedidoOfertaFinalInsertRequest?)
        : Observable<ServiceDto<Any>?>

    /**
     * Servicio de tipo GET que obtiene la lista de productos sugeridos
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.1/Pedido/ProductoSugerido")
    fun getSuggestedReplacements(
        @Query("inModel.campaniaID") campaniaID: Int?,
        @Query("inModel.cUV") cuv: String?,
        @Query("inModel.nroCampanias") nroCampanias: Int?,
        @Query("inModel.consultoraAsociada") consultoraAsociada: String?)
        : Observable<List<ProductCuvEntity?>?>

    /**
     * Servicio de tipo GET que obtiene la lista de ofertas relacionadas
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "api/v1.2/Pedido/OfertasRelacionadas")
    fun getRelatedOffers(@Body request: RelatedOfferRequest?): Observable<RelatedOfferResponseEntity?>


    /**
     * Servicio de tipo PUT que registra el producto como back order
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @PUT(value = "api/v1.1/Pedido/BackOrder")
    fun backOrder(@Body request: BackOrderRequest?): Observable<ServiceDto<Any>?>


    /**
     * Servicio de tipo POST que registra un producto del carrusel de oferta final al pedido
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "api/v1.1/Pedido/Buscador/Insertar")
    fun addSearchedProduct(@Body request: ProductCuvSearchRequestEntity?)
        : Observable<ServiceDto<Any>?>

    /**
     * Servicio de tipo PUT que actualiza el dni para que una persona pueda recoger el pedido por ti
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @PUT(value = "api/v1.1/Pedido/Recoger")
    fun updateDni(@Body request: UpdateDniRequestEntity?): Deferred<ServiceDto<Any>?>


    /**
     * Servicio de tipo GET que retorna la cantidad de pedidos pendientes de una consultora
     * con Coroutines
     *
     * @return objeto con la cantidad de pedidos pendientes de una consultora
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.2/Consultora/Pedidos/Pendientes")
    fun getPedidosPendientes(@Query("campania") campaniaId: String?)
        : Deferred<PedidoPendienteEntity?>

    /**
     * Servicio de tipo GET que retorna la informacion de las campanias siguientes y
     * anteriores de una consultora
     *
     * @return objeto con la informacion de las campanias
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.2/Consultora/Informacion/Campania")
    fun getInfoCampanias(@Query("campaniaActual") campaniaActual: String?, @Query("cantidadAnterior") cantidadAnterior: Int?, @Query("cantidadProxima") cantidadProxima: Int?)
        : Deferred<InfoCampaignEntity?>

    /**
     * Servicio de tipo PUT que actualiza el estado de multipedido
     *
     * @return objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @PUT(value = "api/v1.1/Pedido/FacturaMultipedido")
    fun updateStateMultipedido(@Body request: UpdateMultipedidoStateEntity?): Deferred<String>

    /**
     * Servicio de tipo GET que obtiene la información del carrusel condicion mas promociones
     *
     * @return objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.1/Producto/Promociones/Carrusel")
    fun getConditions(
        @Query("campaniaId") campaniaId: String?,
        @Query("listaCuv") listaCuv: String?
    ): Deferred<List<ConditionsGetResponseEntity?>?>
}
