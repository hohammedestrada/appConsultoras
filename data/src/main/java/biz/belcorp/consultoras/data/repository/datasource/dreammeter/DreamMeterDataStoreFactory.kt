package biz.belcorp.consultoras.data.repository.datasource.dreammeter

import android.content.Context
import biz.belcorp.consultoras.data.BuildConfig
import biz.belcorp.consultoras.data.manager.ISessionManager
import biz.belcorp.consultoras.data.net.service.impl.DreamMeterService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DreamMeterDataStoreFactory @Inject
internal constructor(private val context: Context, private val sessionManager: ISessionManager) {

    fun create(): DreamMeterDataStore {
        return createCloud()
    }

    fun createDB(): DreamMeterDataStore {
        return DreamMeterDBDataStore()
    }

    fun createCloud(): DreamMeterDataStore {
        val dreamMeterService = DreamMeterService(context, BuildConfig.BASE_OAPI, null, sessionManager.oAccessToken,
            sessionManager.appName, sessionManager.country)
        return DreamMeterCloudDataStore(dreamMeterService)
    }

}
