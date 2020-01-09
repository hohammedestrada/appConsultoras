package biz.belcorp.consultoras.data.repository.datasource.order

import biz.belcorp.consultoras.data.entity.*
import biz.belcorp.consultoras.data.entity.campaignInformation.InfoCampaignEntity
import biz.belcorp.consultoras.data.net.service.IOrderService
import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.library.net.dto.ServiceDto
import io.reactivex.Observable
import kotlinx.coroutines.Deferred

/**
 * Clase Order encargada de obtener los datos desde un servicio
 *
 * @version 1.0
 * @since 2017-10-30
 */
internal class OrderCloudDataStore
/**
 * Constructor que recibe el servicio ha ser ejecutado
 *
 * @param service Clase encargado de ejecutar un metodo del servicio
 */
(private val service: IOrderService) : OrderDataStore {


    /**
     * Servicio de tipo POST que agrega un pedido
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun addOrder(body: OrderEntity?): Observable<ServiceDto<Any>?> {
        return service.addOrder(body)
    }

    /**
     * Servicio de tipo POST que deshace la reserva del pedido
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun undoReservation(body: OrderEntity?): Observable<ServiceDto<Any>?> {
        return service.undoReservation(body)
    }

    override fun get(campaign: String?, pdfActive: Boolean): Observable<List<MyOrderEntity?>?> {
        return service.get(campaign, Constant.ORDER_TOP, "", pdfActive)
    }

    override fun paqueteDocumentario(numeroPedido: String?): Observable<ServiceDto<String>?> {
        return service.paqueteDocumentario(numeroPedido)
    }

    override fun config(campaniaID: Int?, codigoRegion: String?, codigoZona: String?): Observable<BarraEntity?> {
        return service.config(campaniaID, codigoRegion, codigoZona)
    }

    override fun kitInicio(request: KitInicioRequest?): Observable<ServiceDto<Any>?> {
        return service.kitInicio(request)
    }

    override fun estrategiaCarrusel(campaniaID: Int?, nroCampanias: Int?,
                                    consultoraAsociada: String?, fechaInicioFacturacion: String?,
                                    fechaFinFacturacion: String?,
                                    codigoPrograma: String?, consecutivoNueva: Int?,
                                    codigoRegion: String?, zonaID: Int?, simbolo: String?,
                                    rDTieneRDC: Boolean?, rDEsSuscrita: Boolean?, rDEsActiva: Boolean?,
                                    rDActivoMdo: Boolean?, usuarioPrueba: Boolean?, tieneMG:Boolean?,
                                    codigoZona: String?, conDuoPerfecto: Boolean?
    ): Observable<List<EstrategiaCarruselEntity?>?> {
        return service.estrategiaCarrusel(campaniaID, nroCampanias, consultoraAsociada,
            fechaInicioFacturacion, fechaFinFacturacion, codigoPrograma, consecutivoNueva, codigoRegion, zonaID, simbolo,
            rDTieneRDC, rDEsSuscrita, rDEsActiva, rDActivoMdo, usuarioPrueba, tieneMG, codigoZona, conDuoPerfecto)
    }

    override fun addOfferCarrusel(offer: PedidoInsertRequestEntity?): Observable<ServiceDto<Any>?> {
        return service.addOfferCarrusel(offer)
    }

    override fun save(entityList: List<MyOrderEntity?>?): Observable<List<MyOrderEntity?>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getOrdersList(campaniaID: Int?,
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
                               isMultipedido : Boolean?): Observable<PedidoGetResponseEntity?>
    {
        return service.getOrdersList(campaniaID, codigoPrograma, consecutivoNueva,
            montoMaximoPedido, consultoraNueva, nroCampanias, nivelCaminoBrillante,
            nombreConsultora, codigoRegion, codigoZona, codigoSeccion, esUltimoDiaFacturacion,
            pagoContado, fechaInicioFacturacion, fechaFinFacturacion, isMultipedido)
    }

    override fun getOrders(campaniaID: Int?,
                               codigoPrograma: String?,
                               consecutivoNueva: Int?,
                               montoMaximoPedido: Double?,
                               consultoraNueva: Int?,
                               nroCampanias: Int?,
                               nivelCaminoBrillante: Int?,
                           nombreConsultora: String?,
                           codigoRegion: String?,
                           codigoZona: String?,
                           codigoSeccion: String?): Deferred<PedidoGetResponseEntity?>
    {
        return service.getOrders(campaniaID, codigoPrograma, consecutivoNueva,
            montoMaximoPedido, consultoraNueva, nroCampanias, nivelCaminoBrillante,
            nombreConsultora, codigoRegion, codigoZona, codigoSeccion)
    }


    override fun searchCUV(campaniaID: Int?,
                           cuv: String?,
                           nroCampanias: Int?,
                           consultoraAsociada: String?,
                           codigoPrograma: String?,
                           consecutivoNueva: Int?,
                           zonaId: Int?,
                           regionId: Int?,
                           nivelCaminoBrillante: Int?): Observable<ServiceDto<ProductCuvEntity>?>
    {
        return service.searchCUV(campaniaID, cuv, nroCampanias, consultoraAsociada,
            codigoPrograma,consecutivoNueva ,zonaId,regionId, nivelCaminoBrillante)
    }

    override fun searchCUVCoroutine(campaniaID: Int?, cuv: String?, nroCampanias: Int?, consultoraAsociada: String?, codigoPrograma: String?, consecutivoNueva: Int?, zonaId: Int?, regionId: Int?, nivelCaminoBrillante: Int?): Deferred<ServiceDto<ProductCuvEntity>?> {
        return service.searchCUVCoroutines(campaniaID, cuv, nroCampanias, consultoraAsociada,
            codigoPrograma,consecutivoNueva ,zonaId,regionId, nivelCaminoBrillante)
    }

    override fun deshacerPedido(campania: Int?, indicadorGPRSB: Int?, codigoPrograma: String?,
                                consecutivoNueva: Int?, consultoraAsociada: String?)
        : Observable<ServiceDto<ObservacionPedidoEntity>?> {

        val request = HashMap<String, Any?>()
        request["CampaniaID"] = campania
        request["CodigoPrograma"] = codigoPrograma
        request["ConsecutivoNueva"] = consecutivoNueva
        request["IndicadorGPRSB"] = indicadorGPRSB
        request["ConsultoraAsociada"] = consultoraAsociada
        return service.deshacerPedido(request)
    }

    override fun addGift(gift: PedidoInsertRequestEntity?): Observable<ServiceDto<Any>?> {
        return service.insertPedido(gift)
    }

    override fun insertPedido(request: PedidoInsertRequestEntity?): Observable<ServiceDto<Any>?> {
        return service.insertPedido(request)
    }

    override fun insertarPedido(request: PedidoInsertRequestEntity?): Deferred<ServiceDto<Any>?> {
        return service.insertarPedido(request)
    }

    override fun updatePedido(request: PedidoUpdateRequest?): Observable<ServiceDto<Any>?> {
        return service.updatePedido(request)
    }

    override fun deletePedido(request: PedidoDeleteRequest?): Observable<ServiceDto<Any>?> {
        return service.deletePedido(request)
    }

    override fun insercionMasivaPedido(request: PedidoMasivoRequest?)
        : Observable<ServiceDto<List<ProductoMasivoEntity>>?>{
        return service.insercionMasivaPedido(request).map {response->
            val newServiceDto = ServiceDto<List<ProductoMasivoEntity>>()
            newServiceDto.code = response.code
            newServiceDto.message = response.message
            newServiceDto.data = mutableListOf<ProductoMasivoEntity>().apply {
                request?.productos?.filterNotNull()?.forEach {requestDataEntity->
                    response.data?.forEach {responseDataEntity->
                        if(requestDataEntity.cuv == responseDataEntity.cuv &&
                            requestDataEntity.clienteId == responseDataEntity.clienteID){
                            requestDataEntity.codigoRespuesta = responseDataEntity.codigoRespuesta
                            requestDataEntity.mensajeRespuesta = responseDataEntity.mensajeRespuesta
                        }
                    }
                }
                request?.productos?.filterNotNull()?.let {
                    addAll(it)
                }
            }
            newServiceDto
        }
    }

    override fun guardarProductoMasivoList(productoMasivoList: ServiceDto<List<ProductoMasivoEntity>>)
        : Observable<ServiceDto<List<ProductoMasivoEntity>>> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun reservaPedido(request: PedidoReservaRequest?): Observable<ServiceDto<Any>?> {
        return service.reservaPedido(request)
    }

    override fun getFinalOfferList(request: PedidoOfertaFinalRequest?): Observable<ServiceDto<Any>?> {
        return service.getFinalOfferList(request)
    }

    override fun addFinalOffer(request: PedidoOfertaFinalInsertRequest?): Observable<ServiceDto<Any>?> {
        return service.addFinalOffer(request)
    }

    override fun getSuggestedReplacements(campaniaID: Int?, cuv: String?, nroCampanias: Int?
                                          , consultoraAsociada: String?)
        : Observable<List<ProductCuvEntity?>?> {
        return service.getSuggestedReplacements(campaniaID, cuv, nroCampanias, consultoraAsociada)
    }

    override fun getRelatedOffers(suggestOfferRequest: RelatedOfferRequest?): Observable<RelatedOfferResponseEntity?> {
        return service.getRelatedOffers(suggestOfferRequest)
    }

    override fun backOrder(request: BackOrderRequest?): Observable<ServiceDto<Any>?> {
        return service.backOrder(request)
    }

    override fun addSearchedProduct(request: ProductCuvSearchRequestEntity?): Observable<ServiceDto<Any>?> {
        return  service.addSearchedProduct(request)
    }
    override fun updateDni(request: UpdateDniRequestEntity?): Deferred<ServiceDto<Any>?> {
        return service.updateDni(request)
    }

    override fun getInfoCampanias(campaniaActual: String?, cantidadAnterior: Int?, cantidadProxima: Int?): Deferred<InfoCampaignEntity?> {
        return service.getInfoCampanias(campaniaActual, cantidadAnterior, cantidadProxima)
    }

    override fun getPedidosPendientes(campaniaID: String?) : Deferred<PedidoPendienteEntity?>?{
        return service.getPedidosPendientes(campaniaID)
    }

    override fun kitSociasEmpresarias(request: KitSociaEmpresariaRequestEntity?): Deferred<ServiceDto<Any>?> {
        return service.kitSociasEmpresarias(request)
    }

    override fun updateStateMultipedido(request: UpdateMultipedidoStateEntity?): Deferred<String> {
        return service.updateStateMultipedido(request)
    }

    override fun getConditions( campaing: String?, cuvs: String): Deferred<List<ConditionsGetResponseEntity?>?> {
        return service.getConditions(campaing, cuvs)
    }

}
