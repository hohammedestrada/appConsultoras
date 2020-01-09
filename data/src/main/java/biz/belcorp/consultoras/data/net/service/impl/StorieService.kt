package biz.belcorp.consultoras.data.net.service.impl

import android.content.Context
import biz.belcorp.consultoras.data.entity.ContenidoUpdateRequest
import biz.belcorp.consultoras.data.net.RestApi
import biz.belcorp.consultoras.data.net.service.IStorie
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.library.net.AccessToken
import biz.belcorp.library.net.exception.NetworkConnectionException
import biz.belcorp.library.net.exception.ServiceException
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Deferred

class StorieService
(context: Context, accessToken: AccessToken?, appName: String?, appCountry: String?)
    : BaseService(context), IStorie {


    private val service: IStorie = RestApi.create(IStorie::class.java, accessToken, appName, appCountry)

    override fun actualizarEstadoContenido(request: ContenidoUpdateRequest): Observable<String?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {

                    service.actualizarEstadoContenido(request)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error -> emitter.onError(RestApi.parseError(error)) },
                            { emitter.onComplete() })

                }catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }
            }
            else {
                emitter.onError(NetworkConnectionException())
            }

        }

    }

    override fun actualizarEstadoContenidoCorroutine(request: ContenidoUpdateRequest): Deferred<String?> {
       return if (isThereInternetConnection) {
            service.actualizarEstadoContenidoCorroutine(request)
        }
        else{
            throw NetworkErrorException()
        }
    }

}
