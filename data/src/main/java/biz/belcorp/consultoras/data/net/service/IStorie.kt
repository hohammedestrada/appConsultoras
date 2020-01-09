package biz.belcorp.consultoras.data.net.service

import biz.belcorp.consultoras.data.entity.ContenidoUpdateRequest
import biz.belcorp.consultoras.data.util.Constant
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface IStorie {
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "api/v1.3/Consultora/Contenido")
    fun actualizarEstadoContenido(@Body request: ContenidoUpdateRequest): Observable<String?>

    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "api/v1.3/Consultora/Contenido")
    fun actualizarEstadoContenidoCorroutine(@Body request: ContenidoUpdateRequest): Deferred<String?>
}
