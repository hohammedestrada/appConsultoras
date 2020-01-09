package biz.belcorp.consultoras.data.net.service.impl

import android.content.Context

import biz.belcorp.consultoras.data.entity.DeviceEntity
import biz.belcorp.consultoras.data.net.RestApi
import biz.belcorp.consultoras.data.net.service.IApiService
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.library.net.exception.ServiceException
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.http.Body

/**
 * Servicio de Incentivos
 * que implementa las transacciones basicas
 *
 * @version 1.0
 * @since 2017-04-14
 */

class ApiService
/**
 * Constructor
 *
 * @param context     Contexto que llamo al Servicio
 * @param appName     Nombre del app
 * @param appCountry  Pais de conexion
 */
(context: Context, url: String?, appName: String?, appCountry: String?)
    : BaseService(context), IApiService {

    private val service: IApiService = RestApi.create(IApiService::class.java, url, null, null,
        appName, appCountry)

    /**
     * Metodo que guarda una tarea
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun saveDevice(@Body entity: DeviceEntity?): Observable<Boolean?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.saveDevice(entity)
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
}
