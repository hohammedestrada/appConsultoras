package biz.belcorp.consultoras.data.repository

import biz.belcorp.consultoras.data.mapper.DreamMeterDataMapper
import biz.belcorp.consultoras.data.net.dto.dreamMeter.request.DreamMeterRequest
import biz.belcorp.consultoras.data.net.dto.dreamMeter.response.DreamMeterResponse
import biz.belcorp.consultoras.data.repository.datasource.dreammeter.DreamMeterDataStore
import biz.belcorp.consultoras.data.repository.datasource.dreammeter.DreamMeterDataStoreFactory
import biz.belcorp.consultoras.data.repository.datasource.user.UserDataStoreFactory
import biz.belcorp.consultoras.domain.entity.BasicDto
import biz.belcorp.consultoras.domain.entity.dreammeter.DreamMeter
import biz.belcorp.consultoras.domain.repository.AuthRepository
import biz.belcorp.consultoras.domain.repository.DreamMeterRepository
import biz.belcorp.library.net.dto.ServiceDto
import javax.inject.Inject

class DreamMeterDataRepository @Inject
internal constructor(private val dreamMeterDataStoreFactory: DreamMeterDataStoreFactory,
                     private val userDataStoreFactory: UserDataStoreFactory,
                     private val dreamMeterDataMapper: DreamMeterDataMapper,
                     authRepository: AuthRepository)
    : BaseRepository(authRepository), DreamMeterRepository {

    override suspend fun getDreamMeter(): BasicDto<DreamMeter?> {
        var dreamDataStore: DreamMeterDataStore = dreamMeterDataStoreFactory.createCloud()
        val userDataStore = userDataStoreFactory.create()

        val response: ServiceDto<DreamMeterResponse>
        val user = userDataStore.get()

        response = safeApiCall(
            { dreamDataStore = dreamMeterDataStoreFactory.createCloud() },
            { dreamDataStore.getDreamMeter(user?.countryISO, user?.campaing?.toIntOrNull(), user?.consultantCode) }
        )

        return BasicDto<DreamMeter?>().apply {
            code = response.code
            message = response.message
            data = dreamMeterDataMapper.transformDreamMeter(response.data)
        }
    }

    override suspend fun saveDreamMeter(programDreamId: Int?, nameDream: String, amountDream: String): BasicDto<Boolean> {
        val userDataStore = userDataStoreFactory.create()
        var dreamDataStore = dreamMeterDataStoreFactory.createCloud()

        val user = userDataStore.get()
        val dreamMeterRequest = DreamMeterRequest(user?.countryISO, programDreamId, user?.consultantCode, nameDream, amountDream, user?.campaing, user?.montoMaximoPedido)

        val response = safeApiCall(
            { dreamDataStore = dreamMeterDataStoreFactory.createCloud() },
            { dreamDataStore.saveDreamMeter(dreamMeterRequest) }
        )

        return BasicDto<Boolean>().apply {
            code = response.code
            message = response.message
            data = response.data
        }
    }

    override suspend fun updateDreamMeter(programDreamId: Int?, nameDream: String, amountDream: String): BasicDto<Boolean> {
        val user = userDataStoreFactory.create().getWithCoroutine()
        var dreamDataStore = dreamMeterDataStoreFactory.createCloud()

        val dreamMeterRequest = DreamMeterRequest(user?.countryISO, programDreamId, user?.consultantCode, nameDream, amountDream, user?.campaing, user?.montoMaximoPedido)

        val response = safeApiCall(
            { dreamDataStore = dreamMeterDataStoreFactory.createCloud() },
            { dreamDataStore.updateDreamMeter(dreamMeterRequest) }
        )

        return BasicDto<Boolean>().apply {
            code = response.code
            message = response.message
            data = response.data
        }
    }

    override suspend fun updateStatus(programDreamId: Int?, dreamStatus: Boolean): BasicDto<Boolean> {
        val user = userDataStoreFactory.create().getWithCoroutine()
        var dreamDataStore = dreamMeterDataStoreFactory.createCloud()

        val dreamMeterRequest = DreamMeterRequest(user?.countryISO, programDreamId, user?.consultantCode, null, null, user?.campaing, null, dreamStatus)

        val response = safeApiCall({ dreamDataStore = dreamMeterDataStoreFactory.createCloud() }, { dreamDataStore.updateStatus(dreamMeterRequest) })

        return BasicDto<Boolean>().apply {
            code = response.code
            message = response.message
            data = response.data
        }
    }
}
