package biz.belcorp.consultoras.data.net.service

import biz.belcorp.consultoras.data.entity.PremioFinalAgregaEntity
import biz.belcorp.consultoras.data.entity.PremioFinalEntity
import biz.belcorp.consultoras.data.entity.PremioFinalMetaEntity
import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.library.net.dto.ServiceDto
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface IPremioService{

    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.2/Ofertas/OfertaFinal/Premio")
    fun getOfertasFinales(@Query("codigoCampania") codigoCampania: Int?): Deferred<List<PremioFinalEntity?>?>

    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "api/v1.2/Ofertas/OfertaFinal/Premio")
    fun addPremio(@Body body: PremioFinalAgregaEntity?): Deferred<ServiceDto<Any>?>

    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.2/Ofertas/OfertaFinal/MontoMeta")
    fun getMontoMeta(@Query("codigoCampania") codigoCampania: Int?): Deferred<PremioFinalMetaEntity?>

}
