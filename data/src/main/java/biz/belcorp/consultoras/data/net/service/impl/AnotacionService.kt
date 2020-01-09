package biz.belcorp.consultoras.data.net.service.impl

import android.content.Context

import biz.belcorp.consultoras.data.entity.AnotacionEntity
import biz.belcorp.consultoras.data.net.RestApi
import biz.belcorp.consultoras.data.net.service.IAnotacionService
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

class AnotacionService
/**
 * Constructor
 *
 * @param context Contexto que llamo al Servicio
 * @param accessToken Token de la session
 * @param appName Nombre del app
 * @param appCountry Pais de conexion
 */
(context: Context, accessToken: AccessToken?, appName: String?, appCountry: String?)
    : BaseService(context), IAnotacionService {

    private val service: IAnotacionService = RestApi.create(IAnotacionService::class.java, accessToken, appName, appCountry)

    /**
     * Metodo que obtiene el listado de tareas
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun save(@Path(value = "clienteId") clientId: Int?, @Body anotacion: AnotacionEntity?): Observable<AnotacionEntity?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.save(clientId, anotacion)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ anotacionEntity ->
                                anotacionEntity?.let {
                                    it.sincronizado = 1
                                    it.estado = 1
                                    it.clienteLocalID = anotacion?.clienteLocalID
                                    emitter.onNext(it)
                                } ?: emitter.onError(NullPointerException(javaClass.canonicalName))

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

    override fun update(@Path(value = "clienteId") clienteID: Int?, @Path(value = "notaId") anotacionID: Int?, @Body anotacionEntity: AnotacionEntity?): Observable<Boolean?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.update(clienteID, anotacionID, anotacionEntity)
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

    override fun delete(@Path("clienteId") clienteID: Int?, @Path("notaId") anotacionID: Int?): Observable<Boolean?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.delete(clienteID, anotacionID)
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
