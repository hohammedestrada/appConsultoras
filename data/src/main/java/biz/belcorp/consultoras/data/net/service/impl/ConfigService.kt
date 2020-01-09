package biz.belcorp.consultoras.data.net.service.impl

import android.content.Context

import biz.belcorp.consultoras.data.entity.ConfigResponseEntity
import biz.belcorp.consultoras.data.net.RestApi
import biz.belcorp.consultoras.data.net.service.IConfigService
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.library.net.exception.ServiceException
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Servicio de Configuracion inicial
 * que implementa las transacciones basicas
 *
 * @version 1.0
 * @since 2017-04-14
 */

class ConfigService
/**
 * Constructor
 *
 * @param context Contexto que llamo al Servicio
 */
(context: Context) : BaseService(context), IConfigService {

    private val service: IConfigService = RestApi.create(IConfigService::class.java)

    /**
     * Metodo que obtiene los datos de configuracion
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun get(): Observable<ConfigResponseEntity?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.get()
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
