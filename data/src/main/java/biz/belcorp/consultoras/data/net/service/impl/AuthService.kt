package biz.belcorp.consultoras.data.net.service.impl

import android.content.Context
import biz.belcorp.consultoras.data.BuildConfig
import biz.belcorp.consultoras.data.entity.AnalyticsTokenRequestEntity
import biz.belcorp.consultoras.data.entity.AnalyticsTokenResponseEntity

import biz.belcorp.consultoras.data.entity.LoginEntity
import biz.belcorp.consultoras.data.entity.TokenEntity
import biz.belcorp.consultoras.data.net.RestApi
import biz.belcorp.consultoras.data.net.RestApiAuth
import biz.belcorp.consultoras.data.net.service.IAuthService
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.library.net.exception.NetworkConnectionException
import biz.belcorp.library.net.exception.ServiceException
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Deferred
import okhttp3.Credentials
import retrofit2.http.Field

class AuthService
/**
 * Constructor
 *
 * @param context Contexto que llamo al Servicio
 */
(context: Context) : BaseService(context), IAuthService {

    private val service: IAuthService = RestApi.create(IAuthService::class.java)

    private val oService: IAuthService = RestApi.create(IAuthService::class.java, BuildConfig.BASE_OAPI)

    private val service2: IAuthService = RestApiAuth.create(IAuthService::class.java)

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
                        .subscribe({
                            it?.let { it1 ->
                                emitter.onNext(it1)
                            }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
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

    override fun getToken(password: String, grantType: String): Deferred<TokenEntity> {
        if(isThereInternetConnection){
            return oService.getToken(password, grantType)
        }else{
            throw NetworkConnectionException()
        }

    }


    override fun getAnalyticsToken(grantType: String, authorization: String): Deferred<AnalyticsTokenResponseEntity?> {
        if (!isThereInternetConnection)
            throw NetworkErrorException()
        else
            return service2.getAnalyticsToken(grantType, authorization)
    }
}
