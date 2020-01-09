package biz.belcorp.consultoras.data.repository.datasource.order

import biz.belcorp.consultoras.data.entity.*
import biz.belcorp.consultoras.data.entity.campaignInformation.InfoCampaignEntity
import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.library.net.dto.ServiceDto
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import retrofit2.http.Body

/**
 *
 */
interface OrderDataStore {

    /**
     * Servicio de que agrega un pedido
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun addOrder(body: OrderEntity?): Observable<ServiceDto<Any>?>

    /**
     * Servicio de que deshace la reserva del pedido
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun undoReservation(body: OrderEntity?): Observable<ServiceDto<Any>?>

    fun get(campaign: String?, pdfActive: Boolean): Observable<List<MyOrderEntity?>?>

    fun paqueteDocumentario(numeroPedido: String?): Observable<ServiceDto<String>?>

    fun config(campaniaID: Int?, codigoRegion: String?, codigoZona: String?)
        : Observable<BarraEntity?>

    fun kitInicio(request: KitInicioRequest?): Observable<ServiceDto<Any>?>

    fun estrategiaCarrusel(campaniaID: Int?,
                           nroCampanias: Int?,
                           consultoraAsociada: String?,
                           fechaInicioFacturacion: String?,
                           fechaFinFacturacion: String?,
                           codigoPrograma: String?,
                           consecutivoNueva: Int?,
                           codigoRegion: String?,
                           zonaID: Int?,
                           simbolo: String?,
                           rDTieneRDC: Boolean?,
                           rDEsSuscrita: Boolean?,
                           rDEsActiva: Boolean?,
                           rDActivoMdo: Boolean?,
                           usuarioPrueba: Boolean?,
                           tieneMG:Boolean?,
                           codigoZona: String?,
                           conDuoPerfecto: Boolean?)
        : Observable<List<EstrategiaCarruselEntity?>?>

    fun addOfferCarrusel(offer: PedidoInsertRequestEntity?): Observable<ServiceDto<Any>?>

    fun addGift(gift: PedidoInsertRequestEntity?): Observable<ServiceDto<Any>?>

    fun save(entityList: List<MyOrderEntity?>?): Observable<List<MyOrderEntity?>?>

    fun getOrdersList(campaniaID: Int?,
                      codigoPrograma: String?,
                      consecutivoNueva: Int?,
                      montoMaximoPedido: Double?,
                      consultoraNueva: Int?,
                      nroCampanias: Int?,
                      nivelCaminoBrillante: Int?,
                      nombreConsultora: String?,
                      codigoRegion: String?,
                      codigoZona: String?,
                      codigoSeccion: String?,
                      esUltimoDiaFacturacion: Boolean?,
                      pagoContado: Boolean?,
                      fechaInicioFacturacion: String?,
                      fechaFinFacturacion: String?,
                      isMultipedido : Boolean?)
        : Observable<PedidoGetResponseEntity?>

    fun getOrders(campaniaID: Int?,
                      codigoPrograma: String?,
                      consecutivoNueva: Int?,
                      montoMaximoPedido: Double?,
                      consultoraNueva: Int?,
                      nroCampanias: Int?,
                      nivelCaminoBrillante: Int?,
                      nombreConsultora: String?,
                      codigoRegion: String?,
                      codigoZona: String?,
                      codigoSeccion: String?)
        : Deferred<PedidoGetResponseEntity?>

    fun searchCUV(campaniaID: Int?,
                  cuv: String?,
                  nroCampanias: Int?,
                  consultoraAsociada: String?,
                  codigoPrograma: String?,
                  consecutivoNueva: Int?,
                  zonaId: Int?,
                  regionId: Int?,
                  nivelCaminoBrillante: Int?)
        : Observable<ServiceDto<ProductCuvEntity>?>

    fun searchCUVCoroutine(campaniaID: Int?,
                  cuv: String?,
                  nroCampanias: Int?,
                  consultoraAsociada: String?,
                  codigoPrograma: String?,
                  consecutivoNueva: Int?,
                  zonaId: Int?,
                  regionId: Int?,
                  nivelCaminoBrillante: Int?)
        : Deferred<ServiceDto<ProductCuvEntity>?>

    fun insertPedido(request: PedidoInsertRequestEntity?): Observable<ServiceDto<Any>?>

    fun insertarPedido(@Body request: PedidoInsertRequestEntity?): Deferred<ServiceDto<Any>?>

    fun updatePedido(request: PedidoUpdateRequest?): Observable<ServiceDto<Any>?>

    fun deletePedido(request: PedidoDeleteRequest?): Observable<ServiceDto<Any>?>

    fun reservaPedido(request: PedidoReservaRequest?): Observable<ServiceDto<Any>?>

    fun insercionMasivaPedido(request: PedidoMasivoRequest?)
        : Observable<ServiceDto<List<ProductoMasivoEntity>>?>

    fun deshacerPedido(campania: Int?,
                       indicadorGPRSB: Int?,
                       codigoPrograma: String?,
                       consecutivoNueva: Int?,
                       consultoraAsociada: String?)
        : Observable<ServiceDto<ObservacionPedidoEntity>?>

    fun getFinalOfferList(request: PedidoOfertaFinalRequest?): Observable<ServiceDto<Any>?>

    fun addFinalOffer(request: PedidoOfertaFinalInsertRequest?): Observable<ServiceDto<Any>?>

    fun getSuggestedReplacements(campaniaID: Int?,
                                 cuv: String?,
                                 nroCampanias: Int?,
                                 consultoraAsociada: String?)
        : Observable<List<ProductCuvEntity?>?>

    fun getRelatedOffers(suggestOfferRequest: RelatedOfferRequest?)
        : Observable<RelatedOfferResponseEntity?>

    fun backOrder(request: BackOrderRequest?): Observable<ServiceDto<Any>?>

    fun addSearchedProduct(request: ProductCuvSearchRequestEntity?): Observable<ServiceDto<Any>?>

    fun guardarProductoMasivoList(productoMasivoList: ServiceDto<List<ProductoMasivoEntity>>): Observable<ServiceDto<List<ProductoMasivoEntity>>>

    fun updateDni(request: UpdateDniRequestEntity?): Deferred<ServiceDto<Any>?>

    fun getPedidosPendientes(campaniaID: String?): Deferred<PedidoPendienteEntity?>?

    fun getInfoCampanias(campaniaActual: String?, cantidadAnterior: Int?, cantidadProxima: Int?) : Deferred<InfoCampaignEntity?>

    fun kitSociasEmpresarias(request: KitSociaEmpresariaRequestEntity?): Deferred<ServiceDto<Any>?> { throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED) }

    fun updateStateMultipedido(request: UpdateMultipedidoStateEntity?): Deferred<String>

    fun getConditions(campaing: String?, cuvs: String): Deferred<List<ConditionsGetResponseEntity?>?>
}
