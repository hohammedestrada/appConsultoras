package biz.belcorp.consultoras.data.repository.datasource.params

import javax.inject.Inject
import javax.inject.Singleton

/**
 *
 */
@Singleton
class ParamsDataStoreFactory @Inject
internal constructor() {

    fun create(): ParamsDataStore {
        return createLocal()
    }

    fun createLocal(): ParamsDataStore {
        return ParamsDBDataStore()
    }
}
