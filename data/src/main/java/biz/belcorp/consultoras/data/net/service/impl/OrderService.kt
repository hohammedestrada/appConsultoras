package biz.belcorp.consultoras.data.net.service.impl

import android.content.Context
import android.util.Log
import biz.belcorp.consultoras.data.entity.*
import biz.belcorp.consultoras.data.entity.campaignInformation.InfoCampaignEntity
import biz.belcorp.consultoras.data.net.RestApi
import biz.belcorp.consultoras.data.net.service.IOrderService
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.net.AccessToken
import biz.belcorp.library.net.dto.ServiceDto
import biz.belcorp.library.net.exception.ServiceException
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.Query

/**
 *
 */
class OrderService
/**
 * Constructor
 *
 * @param context Contexto que llamo al servicio
 * @param accessToken Token de la session
 * @param appName Nombre del app
 * @param appCountry Pais de conexion
 */
(context: Context, accessToken: AccessToken?, appName: String?, appCountry: String?)
    : BaseService(context), IOrderService {

    private val service: IOrderService = RestApi.create(IOrderService::class.java, accessToken,
        appName, appCountry)

    /**
     * Metodo que inserta un pedido
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun addOrder(@Body body: OrderEntity?): Observable<ServiceDto<Any>?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.addOrder(body)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error -> emitter.onError(getError(error)) },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkErrorException())
            }
        }
    }

    /**
     * Metodo que deshace la reserva de pedido
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun undoReservation(@Body body: OrderEntity?): Observable<ServiceDto<Any>?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.undoReservation(body)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error -> emitter.onError(getError(error)) },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkErrorException())
            }
        }
    }

    /**
     * Metodo que obtiene la lista del pedido
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override operator fun get(@Query(value = "campania") campania: String?,
                              @Query(value = "top") top: Int?,
                              @Query(value = "consultoraAsociada") consultoraAsociada: String?,
                              @Query(value = "mostrarPaqueteDocumentario") mostrarPaqueteDocumentario: Boolean?)
        : Observable<List<MyOrderEntity?>?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.get(campania, top, consultoraAsociada, mostrarPaqueteDocumentario)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error -> emitter.onError(getError(error)) },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkErrorException())
            }
        }
    }

    /**
     * Servicio de tipo POST que trae el pdf de un pedido
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun paqueteDocumentario(numeroPedido: String?): Observable<ServiceDto<String>?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.paqueteDocumentario(numeroPedido)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ rb ->
                            rb?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error -> emitter.onError(getError(error)) },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    Log.e("ERROR", e.message)
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkErrorException())
            }
        }
    }

    /**
     * Metodo que obtiene la configuración inicial de escalas de descuento
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun config(campaniaID: Int?, codigoRegion: String?, codigoZona: String?)
        : Observable<BarraEntity?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.config(campaniaID, codigoRegion, codigoZona)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error -> emitter.onError(getError(error)) },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkErrorException())
            }
        }
    }

    /**
     * Metodo que agrega el kit de inicio al pedido de la consultora
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun kitInicio(request: KitInicioRequest?): Observable<ServiceDto<Any>?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.kitInicio(request)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error -> emitter.onError(getError(error)) },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkErrorException())
            }
        }
    }


    override fun kitSociasEmpresarias(request: KitSociaEmpresariaRequestEntity?): Deferred<ServiceDto<Any>?> {
        if (isThereInternetConnection) {
            return service.kitSociasEmpresarias(request)
        } else {
            throw NetworkErrorException()
        }
    }

    /**
     * Metodo que obtiene la lista de productos ofrecidos en el carrusel de ofertas
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun estrategiaCarrusel(campaniaID: Int?,
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
                                    tieneMG: Boolean?,
                                    codigoZona: String?,
                                    conDuoPerfecto: Boolean?)
        : Observable<List<EstrategiaCarruselEntity?>?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.estrategiaCarrusel(campaniaID, nroCampanias, consultoraAsociada,
                        fechaInicioFacturacion, fechaFinFacturacion, codigoPrograma, consecutivoNueva, codigoRegion,
                        zonaID, simbolo, rDTieneRDC, rDEsSuscrita, rDEsActiva, rDActivoMdo,
                        usuarioPrueba, tieneMG, codigoZona, conDuoPerfecto)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 ->
                                emitter.onNext(it1)
                            }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error ->
                                val err = getError(error)
                                emitter.onError(err)
                            },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }
            } else {
                emitter.onError(NetworkErrorException())
            }
        }
    }

    /**
     * Metodo que agrega una oferta del carrusel al pedido
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun addOfferCarrusel(request: PedidoInsertRequestEntity?): Observable<ServiceDto<Any>?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.addOfferCarrusel(request)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 ->
                                emitter.onNext(it1)
                            }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error -> emitter.onError(getError(error)) },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }
            } else {
                emitter.onError(NetworkErrorException())
            }
        }
    }

    /**
     * Metodo que obtiene el pedido de la consultora
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
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
                               isMultipedido: Boolean?): Observable<PedidoGetResponseEntity?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.getOrdersList(campaniaID, codigoPrograma, consecutivoNueva,
                        montoMaximoPedido, consultoraNueva, nroCampanias, nivelCaminoBrillante,
                        nombreConsultora, codigoRegion, codigoZona, codigoSeccion,
                        esUltimoDiaFacturacion, pagoContado, fechaInicioFacturacion, fechaFinFacturacion,
                        isMultipedido)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error -> emitter.onError(getError(error)) },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkErrorException())
            }
        }
    }


    /**
     * Metodo que obtiene el pedido de la consultora con corrutinas
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
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
        if (isThereInternetConnection) {
            return service.getOrders(campaniaID,
                codigoPrograma,
                consecutivoNueva,
                montoMaximoPedido,
                consultoraNueva,
                nroCampanias,
                nivelCaminoBrillante,
                nombreConsultora,
                codigoRegion,
                codigoZona,
                codigoSeccion)
        }else{
            throw NetworkErrorException()
        }
    }

    /**
     * Metodo que obtiene el producto consultando el cuv
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun searchCUV(campaniaID: Int?,
                           cuv: String?,
                           nroCampanias: Int?,
                           consultoraAsociada: String?,
                           codigoPrograma: String?,
                           consecutivoNueva: Int?,
                           zoneId: Int?,
                           regionId: Int?,
                           nivelCaminoBrillante: Int?): Observable<ServiceDto<ProductCuvEntity>?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.searchCUV(campaniaID, cuv, nroCampanias, consultoraAsociada,
                        codigoPrograma, consecutivoNueva, zoneId, regionId, nivelCaminoBrillante)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error -> emitter.onError(getError(error)) },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkErrorException())
            }
        }
    }


    override fun searchCUVCoroutines(campaniaID: Int?, cuv: String?, nroCampanias: Int?, consultoraAsociada: String?, codigoPrograma: String?, consecutivoNueva: Int?, zoneId: Int?, regionId: Int?, nivelCaminoBrillante: Int?): Deferred<ServiceDto<ProductCuvEntity>?> {
        if (!isThereInternetConnection)
            throw NetworkErrorException()
        else
            return service.searchCUVCoroutines(campaniaID, cuv, nroCampanias, consultoraAsociada,
                codigoPrograma, consecutivoNueva, zoneId, regionId, nivelCaminoBrillante)
    }

    /**
     * Metodo que registra el pedido de la consultora
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun insertPedido(request: PedidoInsertRequestEntity?): Observable<ServiceDto<Any>?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.insertPedido(request)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { _ ->
                                emitter.onNext(it)
                            }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error ->
                                BelcorpLogger.d("error", "ERROR!", error)
                                emitter.onError(getError(error))

                            },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkErrorException())
            }
        }
    }

    override fun insertarPedido(request: PedidoInsertRequestEntity?): Deferred<ServiceDto<Any>?> {
        if (isThereInternetConnection) {
            return service.insertarPedido(request)
        } else {
            throw NetworkErrorException()
        }
    }

    override fun insercionMasivaPedido(request: PedidoMasivoRequest?)
        : Observable<ServiceDto<List<PedidoMasivoResponseEntity>>?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.insercionMasivaPedido(request)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { _ -> emitter.onNext(it) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error ->
                                BelcorpLogger.d("error", "ERROR!", error)
                                emitter.onError(getError(error))
                            },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkErrorException())
            }
        }
    }

    /**
     * Metodo que actualiza el pedido de la consultora
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun updatePedido(request: PedidoUpdateRequest?): Observable<ServiceDto<Any>?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.updatePedido(request)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 ->
                                emitter.onNext(it1)
                            }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error ->
                                BelcorpLogger.d("error", "ERROR!", error)
                                emitter.onError(getError(error))
                            },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkErrorException())
            }
        }
    }

    /**
     * Metodo que elimina el pedido o un item del pedido de la consultora
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun deletePedido(request: PedidoDeleteRequest?): Observable<ServiceDto<Any>?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.deletePedido(request)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error ->
                                BelcorpLogger.d("error", "ERROR!", error)
                                emitter.onError(getError(error))
                            },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkErrorException())
            }
        }
    }

    /**
     * Metodo que realiza la reserva de pedido
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun reservaPedido(request: PedidoReservaRequest?): Observable<ServiceDto<Any>?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.reservaPedido(request)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error ->
                                BelcorpLogger.d("error", "ERROR!", error)
                                emitter.onError(getError(error))
                            },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkErrorException())
            }
        }
    }

    /**
     * Metodo que deshace la reserva de pedido
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun deshacerPedido(request: HashMap<String, Any?>)
        : Observable<ServiceDto<ObservacionPedidoEntity>?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.deshacerPedido(request)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error ->
                                BelcorpLogger.d("error", "ERROR!", error)
                                emitter.onError(getError(error))
                            },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkErrorException())
            }
        }
    }

    /**
     * Metodo que obtiene la lista de productos del carrusel de oferta final
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     * @Query("CodigoPrograma") codigoPrograma: String?,
    @Query("ConsecutivoNueva") consecutivoNueva: Int?
     */
    override fun getFinalOfferList(request: PedidoOfertaFinalRequest?): Observable<ServiceDto<Any>?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.getFinalOfferList(request)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error ->
                                BelcorpLogger.d("error", "ERROR!", error)
                                emitter.onError(getError(error))
                            },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkErrorException())
            }
        }
    }

    /**
     * Metodo que registra un producto del carrusel de oferta final al pedido
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun addFinalOffer(request: PedidoOfertaFinalInsertRequest?)
        : Observable<ServiceDto<Any>?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.addFinalOffer(request)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error ->
                                BelcorpLogger.d("error", "ERROR!", error)
                                emitter.onError(getError(error))
                            },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkErrorException())
            }
        }
    }

    /**
     * Metodo que obtiene la lista de productos sugeridos
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun getSuggestedReplacements(campaniaID: Int?, cuv: String?, nroCampanias: Int?,
                                          consultoraAsociada: String?)
        : Observable<List<ProductCuvEntity?>?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.getSuggestedReplacements(campaniaID, cuv, nroCampanias,
                        consultoraAsociada)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error -> emitter.onError(getError(error)) },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkErrorException())
            }
        }
    }

    /**
     * Metodo que obtiene la lista de ofertas relacionadas
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun getRelatedOffers(request: RelatedOfferRequest?)
        : Observable<RelatedOfferResponseEntity?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.getRelatedOffers(request)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { _ -> emitter.onNext(it) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error ->
                                BelcorpLogger.d("error", "ERROR!", error)
                                emitter.onError(getError(error))
                            },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkErrorException())
            }
        }
    }


    /**
     * Metodo que registra el producto como back order
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun backOrder(request: BackOrderRequest?): Observable<ServiceDto<Any>?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.backOrder(request)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error -> emitter.onError(getError(error)) },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkErrorException())
            }
        }
    }

    /**
     * Metodo que registra un producto del carrusel de oferta final al pedido
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun addSearchedProduct(request: ProductCuvSearchRequestEntity?)
        : Observable<ServiceDto<Any>?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.addSearchedProduct(request)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error -> emitter.onError(getError(error)) },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkErrorException())
            }
        }
    }

    /**
     * Metodo que devuelve la cantidad de pedidos pendientes que tiene una consultora
     *
     * @return observable que contiene la cantidad de pedidos pendientes de una consultora
     */
    override fun getPedidosPendientes(campaniaId: String?): Deferred<PedidoPendienteEntity?> {
        return service.getPedidosPendientes(campaniaId)
    }

    /**
     * Metodo que actualiza el dni para que una persona pueda recoger el pedido por ti
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun updateDni(request: UpdateDniRequestEntity?): Deferred<ServiceDto<Any>?> {
        if (!isThereInternetConnection)
            throw NetworkErrorException()
        else
            return service.updateDni(request)
    }


    /**
     * Metodo que retorna la informacion de las campanias siguientes y
     * anteriores de una consultora
     *
     * @return objeto con la informacion de las campanias
     */
    override fun getInfoCampanias(campaniaActual: String?, cantidadAnterior: Int?, cantidadProxima: Int?): Deferred<InfoCampaignEntity?> {
        if (!isThereInternetConnection)
            throw NetworkErrorException()
        else
            return service.getInfoCampanias(campaniaActual, cantidadAnterior, cantidadProxima)
    }

    override fun updateStateMultipedido(request: UpdateMultipedidoStateEntity?): Deferred<String> {
        if (!isThereInternetConnection)
            throw NetworkErrorException()
        else
            return service.updateStateMultipedido(request)
    }

    /**
     * Metodo que obtiene la información del carrusel condicion mas promociones
     *
     * @return objeto con la lista de condiciones
     */
    override fun getConditions(campaniaId: String?, listCuv: String?): Deferred<List<ConditionsGetResponseEntity?>?> {
        if (!isThereInternetConnection)
            throw NetworkErrorException()
        else
            return service.getConditions(campaniaId, listCuv)
    }
}
