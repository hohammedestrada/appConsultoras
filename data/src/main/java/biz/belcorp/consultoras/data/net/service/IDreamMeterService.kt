package biz.belcorp.consultoras.data.net.service

import biz.belcorp.consultoras.data.net.dto.dreamMeter.request.DreamMeterRequest
import biz.belcorp.consultoras.data.net.dto.dreamMeter.response.DreamMeterResponse
import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.library.net.dto.ServiceDto
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface IDreamMeterService {

    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "kpis/consultants/programs")
    fun getDreamMeter(
        @Query("countryISO") countryISO: String?,
        @Query("campaignId") campaignId: Int?,
        @Query("consultantId") consultantId: String?
    ): Deferred<ServiceDto<DreamMeterResponse>>

    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "kpis/consultants/dream_meter")
    fun saveDreamMeter(
        @Body dreamMeterRequest: DreamMeterRequest
    ): Deferred<ServiceDto<Boolean>>

    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @PUT(value = "kpis/consultants/dream_meter")
    fun updateDreamMeter(
        @Body dreamMeterRequest: DreamMeterRequest
    ): Deferred<ServiceDto<Boolean>>

    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @PUT(value = "kpis/consultants/dream_meter/status")
    fun updateStatus(
        @Body dreamMeterRequest: DreamMeterRequest
    ): Deferred<ServiceDto<Boolean>>

}
