package biz.belcorp.consultoras.data.net.service.impl

import android.content.Context

import biz.belcorp.consultoras.data.entity.TrackingEntity
import biz.belcorp.consultoras.data.net.RestApi
import biz.belcorp.consultoras.data.net.service.ITrackingService
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.library.net.AccessToken
import biz.belcorp.library.net.exception.ServiceException
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.http.Query

/**
 * Servicio de Incentivos
 * que implementa las transacciones basicas
 *
 * @version 1.0
 * @since 2017-04-14
 */

class TrackingService
/**
 * Constructor
 *
 * @param context     Contexto que llamo al Servicio
 * @param accessToken Token de la session
 * @param appName     Nombre del app
 * @param appCountry  Pais de conexion
 */
(context: Context, accessToken: AccessToken?, appName: String?, appCountry: String?)
    : BaseService(context), ITrackingService {

    private val service: ITrackingService = RestApi.create(ITrackingService::class.java, accessToken, appName, appCountry)

    /**
     * Metodo que guarda una tarea
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun get(@Query(value = "top") top: Int?): Observable<List<TrackingEntity?>?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.get(top)
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
