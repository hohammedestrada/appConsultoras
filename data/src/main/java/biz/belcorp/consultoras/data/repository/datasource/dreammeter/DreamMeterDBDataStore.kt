package biz.belcorp.consultoras.data.repository.datasource.dreammeter

import biz.belcorp.consultoras.data.net.dto.dreamMeter.request.DreamMeterRequest
import biz.belcorp.consultoras.data.net.dto.dreamMeter.response.DreamMeterResponse
import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.library.net.dto.ServiceDto
import kotlinx.coroutines.Deferred

internal class DreamMeterDBDataStore : DreamMeterDataStore {

    override suspend fun getDreamMeter(countryISO: String?, campaignId: Int?, consultantId: String?): Deferred<ServiceDto<DreamMeterResponse>> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override suspend fun saveDreamMeter(dreamMeterRequest: DreamMeterRequest): Deferred<ServiceDto<Boolean>> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override suspend fun updateDreamMeter(dreamMeterRequest: DreamMeterRequest): Deferred<ServiceDto<Boolean>> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override suspend fun updateStatus(dreamMeterRequest: DreamMeterRequest): Deferred<ServiceDto<Boolean>> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

}
