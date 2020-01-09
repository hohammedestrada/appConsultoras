package biz.belcorp.consultoras.data.repository

import biz.belcorp.consultoras.data.mapper.ConfigExtResponseEntityDataMapper
import biz.belcorp.consultoras.data.repository.datasource.configext.ConfigExtDataStoreFactory
import biz.belcorp.consultoras.domain.repository.ConfigExtRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfigExtDataRepository

@Inject
internal constructor(private val configDataStoreFactory: ConfigExtDataStoreFactory,
                     private val configResponseEntityDataMapper: ConfigExtResponseEntityDataMapper) : ConfigExtRepository {

    override suspend fun getWithCoroutines(token: String): Deferred<Boolean?> {

        val dataStore = this.configDataStoreFactory.create()
        val localDataStore = this.configDataStoreFactory.createDB()

        return coroutineScope {

            async {

                val configExtResponseEntity = dataStore.getWithCoroutines(token).await()
                localDataStore.saveWithCoroutines(configExtResponseEntity)
                true

            }

        }

    }

}
