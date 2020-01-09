package biz.belcorp.consultoras.data.net.service

import biz.belcorp.consultoras.data.entity.AnalyticsTokenResponseEntity
import biz.belcorp.consultoras.data.entity.LoginEntity
import biz.belcorp.consultoras.data.entity.TokenEntity
import biz.belcorp.consultoras.data.util.Constant
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface IAuthService {

    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @FormUrlEncoded
    @POST(value = "api/Login")
    fun login(@Field("grant_type") grantType: String?
              , @Field("username") username: String?
              , @Field("password") password: String?
              , @Field("pais") pais: String?
              , @Field("tipoAutenticacion") tipoAutenticacion: Int?
              , @Field("refresh_token") refreshToken: String?): Observable<LoginEntity?>


    @FormUrlEncoded
    @POST("oauth/token")
    fun getToken(@Header("Authorization") password: String, @Field("grant_type") grantType: String): Deferred<TokenEntity>



    @FormUrlEncoded
    @POST(value = "oauth/token")
    fun getAnalyticsToken(@Field("grant_type") grantType: String,
                          @Header("Authorization") authorization: String): Deferred<AnalyticsTokenResponseEntity?>


}
