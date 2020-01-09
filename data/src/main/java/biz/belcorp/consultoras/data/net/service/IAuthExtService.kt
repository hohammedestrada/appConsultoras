package biz.belcorp.consultoras.data.net.service

import biz.belcorp.consultoras.data.entity.AuthExtResponseEntity
import biz.belcorp.consultoras.data.entity.LoginEntity
import biz.belcorp.consultoras.data.util.Constant
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface IAuthExtService {

    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @FormUrlEncoded
    @POST(value = "oauth/token")
    fun login(@Field("grant_type") grantType: String?
              , @Field("username") username: String?
              , @Field("password") password: String?
              , @Field("pais") pais: String?
              , @Field("tipoAutenticacion") tipoAutenticacion: Int?
              , @Field("refresh_token") refreshToken: String?): Observable<LoginEntity?>



    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @FormUrlEncoded
    @POST(value = "oauth/token")
    fun get(@Field("grant_type") grantType: String?
              , @Field("username") username: String?
              , @Field("password") password: String?
              , @Field("pais") pais: String?
              , @Field("tipoAutenticacion") tipoAutenticacion: Int?
              , @Field("refresh_token") refreshToken: String?): Observable<AuthExtResponseEntity?>


}
