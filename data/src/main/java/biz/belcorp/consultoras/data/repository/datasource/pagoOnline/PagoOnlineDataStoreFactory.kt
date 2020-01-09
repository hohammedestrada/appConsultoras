package biz.belcorp.consultoras.data.repository.datasource.pagoOnline

import android.content.Context
import biz.belcorp.consultoras.data.manager.ISessionManager
import biz.belcorp.consultoras.data.net.service.impl.PagoOnlineService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PagoOnlineDataStoreFactory
@Inject
constructor(context: Context, private val sessionManager: ISessionManager) {

    private val context: Context = context.applicationContext

    fun create(): PagoOnlineDataStore {
        return createCloudDataStore()
    }

    fun createDb(): PagoOnlineDataStore {
        return PagoOnlineDBDataStore(context)
    }

    fun createCloudDataStore(): PagoOnlineDataStore {
        val service = PagoOnlineService(context, sessionManager.accessToken,
            sessionManager.appName, sessionManager.country)
        return PagoOnlineCloudDataStore(service)
    }

    fun createCloudDataStore(nextCounterURL: String): PagoOnlineDataStore {
        val service = PagoOnlineService(context, sessionManager.accessToken,
            sessionManager.appName, sessionManager.country, nextCounterURL)
        return PagoOnlineCloudDataStore(service)
    }

}
