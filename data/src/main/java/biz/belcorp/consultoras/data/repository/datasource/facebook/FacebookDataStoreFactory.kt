package biz.belcorp.consultoras.data.repository.datasource.facebook

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FacebookDataStoreFactory @Inject
internal constructor() {

    fun create(): FacebookDataStore {
        return createDB()
    }

    fun createDB(): FacebookDataStore {
        return FacebookDBDataStore()
    }

}
