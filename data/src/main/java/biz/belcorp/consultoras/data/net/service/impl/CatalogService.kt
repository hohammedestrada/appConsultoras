package biz.belcorp.consultoras.data.net.service.impl

import android.content.Context

import biz.belcorp.consultoras.data.entity.CatalogoEntity
import biz.belcorp.consultoras.data.net.RestApi
import biz.belcorp.consultoras.data.net.service.ICatalogService
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.library.net.AccessToken
import biz.belcorp.library.net.dto.ServiceDto
import biz.belcorp.library.net.exception.ServiceException
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Deferred
import retrofit2.http.Query

/**
 *
 */
class CatalogService
/**
 * Constructor
 *
 * @param context Contexto que llamo al servicio
 */
(context: Context, accessToken: AccessToken?, appName: String?, appCountry: String?)
    : BaseService(context), ICatalogService {

    private val service: ICatalogService = RestApi.create(ICatalogService::class.java, accessToken, appName, appCountry)

    override fun get(@Query(value = "campaniaActual") currentCampaign: String?,
                     @Query(value = "codigoZona") zoneCode: String?,
                     @Query(value = "topAnterior") topLast: Int?,
                     @Query(value = "topSiguiente") topNext: Int?,
                     @Query(value = "nroCampanias") maximumCampaign: Int?,
                     @Query(value = "mostrarCampaniaActual") showCurrent: Boolean?,
                     @Query(value = "esBrillante") isBrillante: Boolean?)
        : Observable<List<CatalogoEntity?>?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service[currentCampaign, zoneCode, topLast, topNext, maximumCampaign, showCurrent, isBrillante]
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName)) },
                                    { error -> emitter.onError(getError(error)) },
                                    { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(getError(e))
                }

            } else if (!emitter.isDisposed) {
                emitter.onError(NetworkErrorException())
            }
        }
    }

    override fun getCoroutine(currentCampaign: String?, zoneCode: String?, topLast: Int?, topNext: Int?, maximumCampaign: Int?, showCurrent: Boolean?, isBrillante: Boolean?): Deferred<List<CatalogoEntity?>?> {
        if (!isThereInternetConnection)
            throw NetworkErrorException()
        else
            return service.getCoroutine(currentCampaign, zoneCode, topLast, topNext, maximumCampaign, showCurrent, isBrillante)
    }

    override fun getUrlDescarga(descripcion: String?): Deferred<String?> {
        return service.getUrlDescarga(descripcion)
    }

    override fun getObservableUrlDescarga(descripcion: String?): Observable<String?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.getObservableUrlDescarga(descripcion)
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
