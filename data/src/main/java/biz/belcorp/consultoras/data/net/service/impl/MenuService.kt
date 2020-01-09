package biz.belcorp.consultoras.data.net.service.impl

import android.content.Context

import biz.belcorp.consultoras.data.entity.MenuEntity
import biz.belcorp.consultoras.data.net.RestApi
import biz.belcorp.consultoras.data.net.service.IMenuService
import biz.belcorp.library.net.AccessToken
import biz.belcorp.library.net.exception.NetworkConnectionException
import biz.belcorp.library.net.exception.ServiceException
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.http.Query

class MenuService
/**
 * Constructor
 *
 * @param context     Contexto que llamo al Servicio
 * @param accessToken Token para invocar los servicios
 * @param appName     Nombre del aplicativo
 * @param appCountry  ISO del pais
 */
(context: Context, accessToken: AccessToken?, appName: String?, appCountry: String?)
    : BaseService(context), IMenuService {

    private val service: IMenuService = RestApi.create(IMenuService::class.java, accessToken, appName, appCountry)

    /**
     * Metodo que obtiene los datos de configuracion
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun get(@Query("campania")campaign: String?,
                     @Query("revistaDigitalSuscripcion") revistaDigital: Int?,
                     @Query("verMenu") menuVersion: Int?)
        : Observable<List<MenuEntity?>?> {

        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service[campaign, revistaDigital, menuVersion]
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
                emitter.onError(NetworkConnectionException())
            }
        }
    }

}
