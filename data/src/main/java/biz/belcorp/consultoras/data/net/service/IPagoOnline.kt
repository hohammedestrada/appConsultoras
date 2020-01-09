package biz.belcorp.consultoras.data.net.service

import biz.belcorp.consultoras.data.entity.DataPagoConfigEntity
import biz.belcorp.consultoras.data.entity.DataVisaConfigEntity
import biz.belcorp.consultoras.data.entity.ResultadoPagoEnLineaEntity
import biz.belcorp.consultoras.data.entity.VisaLogPaymentEntity
import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.library.net.dto.ServiceDto
import io.reactivex.Observable
import retrofit2.http.*

interface IPagoOnline {

    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.1/PagoEnLinea")
    fun getInitialConfiguration(@Query("fechaVencimiento") fechaVencimientoPago: String?, @Query("indicadorConsultoraDigital") indicadorConsultoraDigital: Int?): Observable<ServiceDto<DataPagoConfigEntity>>

    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "api/PagoEnLinea/Visa/Configuracion")
    fun getVisaConfiguration(): Observable<ServiceDto<DataVisaConfigEntity>>

    @GET(value = "nextCounter")
    @Headers("Content-Type:application/json")
    fun getVisaNextCounter(@Header("Authorization") authorization: String): Observable<String>

    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "api/PagoEnLinea/Visa")
    fun registerPayment(@Body entity: VisaLogPaymentEntity?): Observable<ServiceDto<ResultadoPagoEnLineaEntity>>

}
