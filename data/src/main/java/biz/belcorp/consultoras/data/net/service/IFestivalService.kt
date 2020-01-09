package biz.belcorp.consultoras.data.net.service

import biz.belcorp.consultoras.data.entity.FestivalConfiguracionEntity
import biz.belcorp.consultoras.data.util.Constant
import kotlinx.coroutines.Deferred
import retrofit2.http.*
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface IFestivalService{

    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.0/Festival/Configuracion")
    fun getConfiguracion(@Query("campaniaId") grantType: Int?): Deferred<FestivalConfiguracionEntity?>
}
