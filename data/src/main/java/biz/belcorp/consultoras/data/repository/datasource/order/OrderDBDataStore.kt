package biz.belcorp.consultoras.data.repository.datasource.order

import android.content.Context
import biz.belcorp.consultoras.data.entity.*
import biz.belcorp.consultoras.data.entity.campaignInformation.InfoCampaignEntity
import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.library.net.dto.ServiceDto
import biz.belcorp.library.sql.exception.SqlException
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.sql.language.Delete
import com.raizlabs.android.dbflow.sql.language.Select
import io.reactivex.Observable
import kotlinx.coroutines.Deferred

/**
 * Clase Order encargada de obtener la data desde el SQLite
 * dicha clase se instancaria una sola vez y se mantendra mientras la aplicacion este viva
 *
 * @version 1.0
 * @since 2017-05-03
 */
class OrderDBDataStore
/**
 * Constructor
 *
 * @param context contexto que llamo al SQLite
 */
(private val context: Context) : OrderDataStore {
    override fun addGift(gift: PedidoInsertRequestEntity?): Observable<ServiceDto<Any>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun config(campaniaID: Int?, codigoRegion: String?, codigoZona: String?)
        : Observable<BarraEntity?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun kitInicio(request: KitInicioRequest?): Observable<ServiceDto<Any>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun estrategiaCarrusel(campaniaID: Int?, nroCampanias: Int?,
                                    consultoraAsociada: String?, fechaInicioFacturacion: String?, fechaFinFacturacion: String?,
                                    codigoPrograma: String?, consecutivoNueva: Int?,
                                    codigoRegion: String?, zonaID: Int?, simbolo: String?,
                                    rDTieneRDC: Boolean?, rDEsSuscrita: Boolean?,
                                    rDEsActiva: Boolean?, rDActivoMdo: Boolean?,
                                    usuarioPrueba: Boolean?, tieneMG: Boolean?,
                                    codigoZona: String?, conDuoPerfecto: Boolean?
    ): Observable<List<EstrategiaCarruselEntity?>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun addOfferCarrusel(offer: PedidoInsertRequestEntity?): Observable<ServiceDto<Any>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun addOrder(body: OrderEntity?): Observable<ServiceDto<Any>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun undoReservation(body: OrderEntity?): Observable<ServiceDto<Any>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }


    override fun paqueteDocumentario(numeroPedido: String?): Observable<ServiceDto<String>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    /**
     * Metodo que obtiene el listado de incentivos desde el SQLite
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun save(entityList: List<MyOrderEntity?>?): Observable<List<MyOrderEntity?>?> {
        return Observable.create { emitter ->
            try {
                Delete.tables(MyOrderEntity::class.java)
                entityList?.filterNotNull()?.let {
                    for (myOrderEntity in entityList.filterNotNull()) {
                        FlowManager.getModelAdapter(MyOrderEntity::class.java).insert(myOrderEntity)
                    }
                    emitter.onNext(entityList)
                }
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    override operator fun get(campaign: String?, pdfActive: Boolean): Observable<List<MyOrderEntity?>?> {
        return Observable.create { emitter ->
            try {
                val entities: List<MyOrderEntity>

                val from = Select().from(MyOrderEntity::class.java)
                entities = from.queryList()
                emitter.onNext(entities)
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
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
                               isMultipedido : Boolean?): Observable<PedidoGetResponseEntity?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
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
                           codigoSeccion: String?): Deferred<PedidoGetResponseEntity?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun searchCUV(
        campaniaID: Int?
        , cuv: String?
        , nroCampanias: Int?
        , consultoraAsociada: String?
        , codigoPrograma: String?
        , consecutivoNueva: Int?
        , zonaId: Int?
        , regionId: Int?
        , nivelCaminoBrillante: Int?
    ): Observable<ServiceDto<ProductCuvEntity>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun searchCUVCoroutine(
        campaniaID: Int?
        , cuv: String?
        , nroCampanias: Int?
        , consultoraAsociada: String?
        , codigoPrograma: String?
        , consecutivoNueva: Int?
        , zonaId: Int?
        , regionId: Int?
        , nivelCaminoBrillante: Int?
    ): Deferred<ServiceDto<ProductCuvEntity>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun insertPedido(request: PedidoInsertRequestEntity?): Observable<ServiceDto<Any>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun insertarPedido(request: PedidoInsertRequestEntity?): Deferred<ServiceDto<Any>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun updatePedido(request: PedidoUpdateRequest?): Observable<ServiceDto<Any>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun reservaPedido(request: PedidoReservaRequest?): Observable<ServiceDto<Any>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun deletePedido(request: PedidoDeleteRequest?): Observable<ServiceDto<Any>?>{
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun insercionMasivaPedido(request: PedidoMasivoRequest?)
        : Observable<ServiceDto<List<ProductoMasivoEntity>>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun guardarProductoMasivoList(serviceDto: ServiceDto<List<ProductoMasivoEntity>>)
        : Observable<ServiceDto<List<ProductoMasivoEntity>>> {
        return Observable.create { emitter ->
            try {
                Delete.tables(ProductoMasivoEntity::class.java)
                serviceDto.data?.let {
                    for (productoMasivo in it) {
                        FlowManager.getModelAdapter(ProductoMasivoEntity::class.java).insert(productoMasivo)
                    }
                    emitter.onNext(serviceDto)
                }
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    override fun deshacerPedido(campania: Int?, indicadorGPRSB: Int?, codigoPrograma: String?,
                                consecutivoNueva: Int?, consultoraAsociada: String?)
        : Observable<ServiceDto<ObservacionPedidoEntity>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getFinalOfferList(request: PedidoOfertaFinalRequest?): Observable<ServiceDto<Any>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun addFinalOffer(request: PedidoOfertaFinalInsertRequest?): Observable<ServiceDto<Any>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getSuggestedReplacements(campaniaID: Int?, cuv: String?, nroCampanias: Int?,
                                          consultoraAsociada: String?)
        : Observable<List<ProductCuvEntity?>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getRelatedOffers(suggestOfferRequest: RelatedOfferRequest?): Observable<RelatedOfferResponseEntity?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun backOrder(request: BackOrderRequest?): Observable<ServiceDto<Any>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun addSearchedProduct(request: ProductCuvSearchRequestEntity?): Observable<ServiceDto<Any>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getPedidosPendientes(campaniaID: String?): Deferred<PedidoPendienteEntity?>? {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getInfoCampanias(campaniaActual: String?, cantidadAnterior: Int?, cantidadProxima: Int?): Deferred<InfoCampaignEntity?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun updateDni(request: UpdateDniRequestEntity?): Deferred<ServiceDto<Any>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun updateStateMultipedido(request: UpdateMultipedidoStateEntity?): Deferred<String> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getConditions(campaing: String?, cuvs: String): Deferred<List<ConditionsGetResponseEntity?>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }
}
