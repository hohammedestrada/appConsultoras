package biz.belcorp.consultoras.data.net.service
import biz.belcorp.consultoras.data.entity.ConfigExtResponseEntity
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Header

interface IConfigExtService {

    @GET(value = "analytics/config")
    fun get(
        @Header("x-access-token") token: String?
    ): Deferred<ConfigExtResponseEntity?>

}
