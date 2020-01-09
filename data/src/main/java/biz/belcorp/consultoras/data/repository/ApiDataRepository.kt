package biz.belcorp.consultoras.data.repository

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.mapper.DeviceEntityDataMapper
import biz.belcorp.consultoras.data.repository.datasource.api.ApiDataStoreFactory
import biz.belcorp.consultoras.domain.entity.Device
import biz.belcorp.consultoras.domain.repository.ApiRepository
import io.reactivex.Observable


@Singleton
class ApiDataRepository @Inject
internal constructor(private val apiDataStoreFactory: ApiDataStoreFactory,
                     private val deviceEntityDataMapper: DeviceEntityDataMapper) : ApiRepository {

    override val device: Observable<Device?>
        get() {
            val localStore = apiDataStoreFactory.createDB()
            return localStore.device.map { deviceEntityDataMapper.transform(it) }
        }

    override fun saveDevice(device: Device?): Observable<Boolean?> {
        val localStore = apiDataStoreFactory.createDB()
        val cloudStore = apiDataStoreFactory.createCloudDataStore()
        return cloudStore.saveDevice(deviceEntityDataMapper.transform(device)!!)
            .flatMap { localStore.saveDevice(deviceEntityDataMapper.transform(device)!!) }
    }

}
