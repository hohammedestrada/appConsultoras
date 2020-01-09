package biz.belcorp.consultoras.data.repository

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.mapper.AnotacionEntityDataMapper
import biz.belcorp.consultoras.data.mapper.ClienteEntityDataMapper
import biz.belcorp.consultoras.data.mapper.HybrisDataEntityDataMapper
import biz.belcorp.consultoras.data.mapper.VisaConfigEntityDataMapper
import biz.belcorp.consultoras.data.repository.datasource.sync.SyncDataStoreFactory
import biz.belcorp.consultoras.domain.entity.BasicDto
import biz.belcorp.consultoras.domain.entity.HybrisData
import biz.belcorp.consultoras.domain.entity.ResultadoPagoEnLinea
import biz.belcorp.consultoras.domain.repository.SyncRepository
import io.reactivex.Observable

/**
 * Clase de Client encargada de obtener la data desde una fuente de datos (interna o externa)
 * dicha clase se instancaria una sola vez y se mantendra mientras la aplicacion este viva
 *
 * @version 1.0
 * @since 2017-05-03
 */
@Singleton
class SyncDataRepository @Inject
internal constructor(private val syncDataStoreFactory: SyncDataStoreFactory,
                     private val clienteEntityDataMapper: ClienteEntityDataMapper,
                     private val anotacionEntityDataMapper: AnotacionEntityDataMapper,
                     private val hybrisDataEntityDataMapper: HybrisDataEntityDataMapper,
                     private val visaConfigEntityDataMapper: VisaConfigEntityDataMapper) : SyncRepository {

    override fun syncClients(): Observable<Boolean?> {
        val serviceRepository = syncDataStoreFactory.createCloudDataStore()
        val localRepository = syncDataStoreFactory.createDB()

        return localRepository.clientsToSync
                .flatMap { c1 ->
                    serviceRepository.syncClients(c1)
                            .flatMap{ c2 -> Observable.just(clienteEntityDataMapper.transformLocal(c1, c2)) }
                            .flatMap { localRepository.saveSyncClients(it) }
                }
    }

    override fun syncAnotations(): Observable<Boolean?> {
        val serviceRepository = syncDataStoreFactory.createCloudDataStore()
        val localRepository = syncDataStoreFactory.createDB()

        return localRepository.anotationsToSync
                .flatMap { a1 ->
                    serviceRepository.syncAnotations(a1)
                            .flatMap{ a2 -> Observable.just(anotacionEntityDataMapper.transformLocal(a1, a2)) }
                            .flatMap { localRepository.saveSyncAnotations(it) }
                }

    }

    override fun getNotificationsHybris(): Observable<List<HybrisData?>?> {
        val dataRepository = syncDataStoreFactory.createDB()

        return dataRepository.notificationsHybrisToSync
            .map { hybrisDataEntityDataMapper.transform(it) }

    }

    override fun updateHybrisData(hybrisData: List<HybrisData?>?): Observable<Boolean?> {
        val dataRepository = syncDataStoreFactory.createDB()

        return dataRepository.updateHybrisData(hybrisDataEntityDataMapper.transformToEntity(hybrisData))
    }

    override fun syncPaymentLocalToService(): Observable<BasicDto<ResultadoPagoEnLinea>> {
        val server= syncDataStoreFactory.createCloudDataStore()
        val local = syncDataStoreFactory.createDB()
        return local.getPaymentLocal().flatMap {
            server.syncPaymentLocalToService(it).map {it1->
                visaConfigEntityDataMapper.transformSaldo(it1)
            }
        }
    }
}
