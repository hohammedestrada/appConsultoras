package biz.belcorp.consultoras.data.net.service.impl


import android.content.Context
import biz.belcorp.consultoras.data.entity.AuthExtResponseEntity

import biz.belcorp.consultoras.data.entity.LoginEntity
import biz.belcorp.consultoras.data.net.RestApi
import biz.belcorp.consultoras.data.net.service.IAuthExtService
import biz.belcorp.consultoras.data.net.service.IAuthService
import biz.belcorp.library.net.exception.NetworkConnectionException
import biz.belcorp.library.net.exception.ServiceException
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.http.Field

class AuthExtService
/**
 * Constructor
 *
 * @param context Contexto que llamo al Servicio
 */
(context: Context) : BaseService(context), IAuthExtService {


//    override fun get(grantType: String?, username: String?, password: String?, pais: String?, tipoAutenticacion: Int?, refreshToken: String?): Observable<AuthExtResponseEntity?> {

//    }

    private val service: IAuthExtService = RestApi.create(IAuthExtService::class.java)

    /** */

    override fun login(@Field(value = "grant_type") grantType: String?,
                       @Field(value = "username") username: String?,
                       @Field(value = "password") password: String?,
                       @Field(value = "pais") pais: String?,
                       @Field(value = "tipoAutenticacion") tipoAutenticacion: Int?,
                       @Field(value = "refresh_token") refreshToken: String?)
        : Observable<LoginEntity?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.login(grantType, username, password, pais, tipoAutenticacion, refreshToken)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ it?.let { it1 ->
                            emitter.onNext(it1)
                        }
                            ?: emitter.onError(NullPointerException(javaClass.canonicalName)) },
                            { error -> emitter.onError(RestApi.parseError(error)) },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }
            } else {
                emitter.onError(NetworkConnectionException())
            }
        }
    }

    override fun get(grantType: String?, username: String?, password: String?, pais: String?, tipoAutenticacion: Int?, refreshToken: String?): Observable<AuthExtResponseEntity?> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
