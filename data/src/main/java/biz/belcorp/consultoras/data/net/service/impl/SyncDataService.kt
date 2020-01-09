package biz.belcorp.consultoras.data.net.service.impl

import android.content.Context

import biz.belcorp.consultoras.data.entity.AnotacionEntity
import biz.belcorp.consultoras.data.entity.ClienteEntity
import biz.belcorp.consultoras.data.entity.ResultadoPagoEnLineaEntity
import biz.belcorp.consultoras.data.entity.VisaLogPaymentEntity
import biz.belcorp.consultoras.data.net.RestApi
import biz.belcorp.consultoras.data.net.service.ISyncDataService
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.library.net.AccessToken
import biz.belcorp.library.net.dto.ServiceDto
import biz.belcorp.library.net.exception.ServiceException
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.http.Body

/**
 * Servicio de Sync
 * que implementa las transacciones basicas
 *
 * @version 1.0
 * @since 2017-04-14
 */
class SyncDataService
/**
 * Constructor
 *
 * @param context Contexto que llamo al Servicio
 * @param appName Nombre del App
 */
(context: Context, accessToken: AccessToken?, appName: String?,
 appCountry: String?) : BaseService(context), ISyncDataService {


    private val service: ISyncDataService = RestApi.create(ISyncDataService::class.java, accessToken, appName, appCountry)

    /**
     * Metodo que sincroniza los clientes.
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun syncClients(@Body clients: List<ClienteEntity?>?)
        : Observable<Collection<ClienteEntity?>?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.syncClients(clients)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))},
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

    /**
     * Servicio de tipo POST que sube los Pagos en Linea realizados que no pudieron sincronizarse
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun registerPayment(entity: VisaLogPaymentEntity?): Observable<ServiceDto<ResultadoPagoEnLineaEntity>> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.registerPayment(entity)
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
}
