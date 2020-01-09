package biz.belcorp.consultoras.data.net.service.impl

import android.content.Context

import biz.belcorp.consultoras.data.entity.ClientMovementEntity
import biz.belcorp.consultoras.data.entity.DebtEntity
import biz.belcorp.consultoras.data.net.RestApi
import biz.belcorp.consultoras.data.net.service.IDebtService
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.library.net.AccessToken
import biz.belcorp.library.net.exception.ServiceException
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.http.Body
import retrofit2.http.Path

/**
 *
 */
class DebtService
/**
 * Constructor
 *
 * @param context Contexto que llamo al servicio
 * @param accessToken Token de la session
 * @param appName Nombre del app
 * @param appCountry Pais de conexion
 */
(context: Context, accessToken: AccessToken?, appName: String?, appCountry: String?)
    : BaseService(context), IDebtService {

    private val service: IDebtService = RestApi.create(IDebtService::class.java, accessToken, appName, appCountry)

    override fun uploadDebt(@Body debtEntity: DebtEntity,
                            @Path(value = "clienteId") clientID: String?)
        : Observable<ClientMovementEntity?> {

        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.uploadDebt(debtEntity, clientID)
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
