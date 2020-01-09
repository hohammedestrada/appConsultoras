package biz.belcorp.consultoras.data.repository.datasource.dreammeter

import biz.belcorp.consultoras.data.net.dto.dreamMeter.request.DreamMeterRequest
import biz.belcorp.consultoras.data.net.dto.dreamMeter.response.DreamMeterResponse
import biz.belcorp.consultoras.data.net.service.IDreamMeterService
import biz.belcorp.library.net.dto.ServiceDto
import kotlinx.coroutines.Deferred

/**
 *
 */
class DreamMeterCloudDataStore internal constructor(private val service: IDreamMeterService)
    : DreamMeterDataStore {

    override suspend fun getDreamMeter(countryISO: String?, campaignId: Int?, consultantId: String?): Deferred<ServiceDto<DreamMeterResponse>> {
        return service.getDreamMeter(countryISO, campaignId, consultantId)
    }

    override suspend fun saveDreamMeter(dreamMeterRequest: DreamMeterRequest): Deferred<ServiceDto<Boolean>> {
        return service.saveDreamMeter(dreamMeterRequest)
    }

    override suspend fun updateDreamMeter(dreamMeterRequest: DreamMeterRequest): Deferred<ServiceDto<Boolean>> {
        return service.updateDreamMeter(dreamMeterRequest)
    }

    override suspend fun updateStatus(dreamMeterRequest: DreamMeterRequest): Deferred<ServiceDto<Boolean>> {
        return service.updateStatus(dreamMeterRequest)
    }

}
