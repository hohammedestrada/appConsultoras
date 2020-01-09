package biz.belcorp.consultoras.data.net.service.impl

import android.content.Context
import biz.belcorp.consultoras.data.entity.*

import biz.belcorp.consultoras.data.net.RestApi
import biz.belcorp.consultoras.data.net.service.IClienteService
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.consultoras.domain.exception.SincronizacionClienteYaExistenteException
import biz.belcorp.library.net.AccessToken
import biz.belcorp.library.net.exception.ServiceException
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.http.Body
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Servicio de Cliente
 * que implementa las transacciones basicas
 *
 * @version 1.0
 * @since 2017-04-14
 */
class ClienteService
/**
 * Constructor
 *
 * @param context Contexto que llamo al Servicio
 * @param appName Nombre del App
 */
(context: Context, accessToken: AccessToken?, appName: String?, appCountry: String?)
    : BaseService(context), IClienteService {

    private val service: IClienteService = RestApi.create(IClienteService::class.java, accessToken, appName, appCountry)

    /**
     * Metodo que obtiene el listado de tareas
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun subida(@Body clientes: List<ClienteEntity?>?): Observable<List<ClienteEntity?>?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.subida(clientes)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ responseEntities ->
                                val sbException = generateErrorMessage(responseEntities)
                                sbException?.let {
                                    when {
                                        sbException.isNotEmpty()
                                        -> emitter.onError(SincronizacionClienteYaExistenteException(sbException.toString()))
                                        else -> responseEntities?.let { it1 -> emitter.onNext(it1) }
                                            ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                                    }
                                } ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                            }, { error -> emitter.onError(getError(error)) },
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
     * Metodo que obtiene la lista de clientes
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun bajada(@Query(value = "ClienteID") clientId: Int?,
                        @Query(value = "CodigoCampania") campaingCode: String?)
        : Observable<List<ClienteEntity?>?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.bajada(clientId, campaingCode)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName)) },
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
     * Metodo que obtiene la lista de clientes deudores
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun deudores(): Observable<List<DeudorRequestEntity?>?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.deudores()
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ it?.let { it1 -> emitter.onNext(it1) }?: emitter.onError(NullPointerException(javaClass.canonicalName)) },
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
     * Metodo que devuelve la lista de movimientos de un cliente
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun getMovements(@Path("clienteId") clientId: Int?)
        : Observable<List<ClientMovementEntity?>?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.getMovements(clientId)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName)) },
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
     * Metodo que guarda un movimiento
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun saveMovement(@Path(value = "clienteId") clientId: Int?,
                              @Body movement: ClientMovementEntity?)
        : Observable<ClientMovementEntity?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.saveMovement(clientId, movement)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName)) },
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
     * Metodo que actualiza un movimiento
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun updateMovement(@Path(value = "clienteId") clientId: Int?,
                                @Path(value = "movementId") movementId: Int?,
                                @Body movement: ClientMovementEntity?)
        : Observable<String?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.updateMovement(clientId, movementId, movement)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName)) },
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

    override fun deleteMovement(clientID: Int?, movementID: Int?)
        : Observable<Boolean?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.deleteMovement(clientID, movementID)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName)) },
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
     * Metodo que guarda las anotaciones que quedaron por sincronizar.
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun syncAnotations(@Body anotations: List<AnotacionEntity?>?)
        : Observable<List<AnotacionEntity?>?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.syncAnotations(anotations)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName)) },
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

    override fun updateProductos(@Body productoEntities: List<ProductoPedidoEntity?>?)
        : Observable<Boolean?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.updateProductos(productoEntities)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName)) },
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

    private fun generateErrorMessage(responseEntities: List<ClienteEntity?>?): StringBuilder? {
        val sbException = StringBuilder()

        responseEntities?.let {
            responseEntities
                .filter { it1 -> it1?.codigoRespuesta != "0000" }
                .forEach { it1 -> sbException.append(it1?.mensajeRespuesta).append("\n") }
        }


        return sbException
    }
}
