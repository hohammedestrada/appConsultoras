package biz.belcorp.consultoras.data.net.service.impl

import android.content.Context

import biz.belcorp.consultoras.data.entity.RecordatorioEntity
import biz.belcorp.consultoras.data.net.RestApi
import biz.belcorp.consultoras.data.net.service.IRecordatoryService
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.library.net.AccessToken
import biz.belcorp.library.net.exception.ServiceException
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.http.Body
import retrofit2.http.Path

/**
 * Servicio de Anotacion
 * que implementa las transacciones basicas
 *
 * @version 1.0
 * @since 2017-04-14
 */

class RecordatoryService
/**
 * Constructor
 *
 * @param context     Contexto que llamo al Servicio
 * @param accessToken Token para invocar los servicios
 * @param appName     Nombre del aplicativo
 * @param appCountry  ISO del pais
 */
(context: Context, accessToken: AccessToken?, appName: String?,
 appCountry: String?) : BaseService(context), IRecordatoryService {

    private val service: IRecordatoryService = RestApi.create(IRecordatoryService::class.java, accessToken, appName, appCountry)

    /**
     * Metodo que inserta un recordatorio
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */

    override fun saveRecordatory(@Path(value = "clienteId") clientId: Int?,
                                 @Body recordatorioEntity: RecordatorioEntity?)
        : Observable<RecordatorioEntity?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.saveRecordatory(clientId, recordatorioEntity)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ it ->
                                recordatorioEntity?.let { r ->
                                    it?.clienteLocalID = r.clienteLocalID
                                    it?.sincronizado = 1
                                    emitter.onNext(it!!)
                                }?: emitter.onError(NullPointerException(javaClass.canonicalName))
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
     * Metodo que actualiza un recordatorio
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun updateRecordatory(@Path(value = "clienteId") clientId: Int?,
                                   @Path(value = "recordatorioId") recordatorioId: Int?,
                                   @Body recordatorioEntity: RecordatorioEntity?)
        : Observable<Boolean?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.updateRecordatory(clientId, recordatorioId, recordatorioEntity)
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
     * Metodo que elimina un recordatorio
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun deleteRecordatory(@Path(value = "recordatorioId") recordatorioId: Int?,
                                   @Path(value = "clienteId") clienteId: Int?)
        : Observable<Boolean?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.deleteRecordatory(recordatorioId, clienteId)
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
