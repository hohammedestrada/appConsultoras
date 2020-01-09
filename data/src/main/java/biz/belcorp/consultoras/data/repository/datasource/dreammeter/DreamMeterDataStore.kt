package biz.belcorp.consultoras.data.repository.datasource.dreammeter

import biz.belcorp.consultoras.data.net.dto.dreamMeter.request.DreamMeterRequest
import biz.belcorp.consultoras.data.net.dto.dreamMeter.response.DreamMeterResponse
import biz.belcorp.library.net.dto.ServiceDto
import kotlinx.coroutines.Deferred

interface DreamMeterDataStore {

    suspend fun saveDreamMeter(dreamMeterRequest: DreamMeterRequest): Deferred<ServiceDto<Boolean>>

    suspend fun updateDreamMeter(dreamMeterRequest: DreamMeterRequest): Deferred<ServiceDto<Boolean>>

    suspend fun getDreamMeter(countryISO: String?, campaignId: Int?, consultantId: String?): Deferred<ServiceDto<DreamMeterResponse>>

    suspend fun updateStatus(dreamMeterRequest: DreamMeterRequest): Deferred<ServiceDto<Boolean>>

}
