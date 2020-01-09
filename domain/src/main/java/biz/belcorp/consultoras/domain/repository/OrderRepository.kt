package biz.belcorp.consultoras.domain.repository

import biz.belcorp.consultoras.data.entity.ProductoMasivo
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.entity.caminobrillante.NivelCaminoBrillante
import biz.belcorp.consultoras.domain.entity.campaignInformation.InfoCampaign
import io.reactivex.Observable

/**
 *
 */
interface OrderRepository {

    fun get(campaign: String?, pdfActive: Boolean): Observable<Collection<MyOrder?>?>

    fun getFromLocal(campaign: String?): Observable<Collection<MyOrder?>?>

    /**
     * Metodo que agrega un pedido
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun addOrder(order: Order?): Observable<BasicDto<Boolean>?>

    /**
     * Metodo que deshace la reserva de un pedido
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun undoReservation(body: Order?): Observable<BasicDto<Boolean>?>

    fun searchCUV(cuv: String): Observable<BasicDto<ProductCUV>?>

    suspend fun searchCUVCoroutine(cuv: String): BasicDto<ProductCUV>?

    fun config(): Observable<PedidoConfig?>

    fun kitInicio(): Observable<BasicDto<Boolean>?>

    fun estrategiaCarrusel(): Observable<Collection<EstrategiaCarrusel?>?>


    fun addOfferCarrusel(item: EstrategiaCarrusel?, identificador: String?)
        : Observable<BasicDto<Collection<MensajeProl?>?>?>


    fun addProductCUV(productCUV: ProductCUV?): Observable<BasicDto<Collection<MensajeProl?>?>?>

    fun addGift(itm: EstrategiaCarrusel?, identificador: String): Observable<BasicDto<Collection<MensajeProl?>?>?>


    suspend fun insertarPedido(request: PedidoInsertRequest): BasicDto<Collection<MensajeProl?>?>?

    fun updateProduct(idPedido: Int?, identificador: String?, orderListItem: OrderListItem)
        : Observable<BasicDto<Collection<MensajeProl?>?>?>


    fun insercionMasivaPedido(productos: List<ProductoMasivo>, identifier: String)
        : Observable<BasicDto<List<ProductoMasivo>>?>

    fun deleteProduct(order: FormattedOrder, orderListItem: OrderListItem): Observable<BasicDto<Boolean>?>

    fun reservaPedido(order: FormattedOrder): Observable<BasicDto<FormattedOrderReserveResponse>?>

    fun reserve(order: FormattedOrder): Observable<BasicDto<ReservaResponse>?>

    fun deshacerReserva(): Observable<BasicDto<Boolean>?>

    fun getOrdersFormatted(): Observable<FormattedOrder?>

    fun getFinalOfferList(response: ReservaResponse)
        : Observable<BasicDto<OfertaFinalResponse>?>


    fun addFinalOffer(item: OfertaFinal?, identificador: String?): Observable<BasicDto<Collection<MensajeProl?>?>?>

    fun getSuggestedReplacements(cuv: String?): Observable<Collection<ProductCUV?>?>

    fun getRelatedOffers(cuv: String?, codigoProducto: String?, minimoResultados: Int?, maximoResultados: Int?, caracteresDescripcion: Int?): Observable<RelatedOfferResponse?>

    fun backOrder(cuv: String?, pedidoID: Int?,
                  pedidoDetalleID: Int?, identifier: String?): Observable<BasicDto<Boolean>?>

    fun addSearchedProduct(product: ProductCUV?, identifier: String?): Observable<BasicDto<Collection<MensajeProl?>?>?>

    fun paqueteDocumentario(numeroPedido: String?): Observable<BasicDto<String>?>

    suspend fun getOrders(campaniaID: Int?, codigoPrograma: String?, consecutivoNueva: Int?,
                          montoMaximoPedido: Double?, consultoraNueva: Int?, nroCampanias: Int?,
                          nivelCaminoBrillante: Int?,
                          nombreConsultora: String?,
                          codigoRegion: String?,
                          codigoZona: String?,
                          codigoSeccion: String?): PedidoGetResponse?

    suspend fun getPedidosPendientes(campaniaId: String?): PedidoPendiente?

    suspend fun getInfoCampanias(campaniaActual: String?, cantidadAnterior: Int?, cantidadProxima: Int?) : InfoCampaign?

    suspend fun updateDni(updateDniRequest : UpdateDniRequest?) : BasicDto<String>?

    suspend fun kitSocias(): BasicDto<Boolean>?

    suspend fun updateStateMultipedido(request: UpdateMultipedidoState?): String

    suspend fun getConditions(campaing: String?, cuvs: String): List<ConditionsGetResponse?>?
}
