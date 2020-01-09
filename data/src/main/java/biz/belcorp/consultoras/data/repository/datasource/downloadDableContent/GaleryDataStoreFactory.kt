package biz.belcorp.consultoras.data.repository.datasource.downloadDableContent

import android.content.Context

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.manager.ISessionManager
import biz.belcorp.consultoras.data.net.service.impl.GaleryService

@Singleton
class GaleryDataStoreFactory
@Inject
internal constructor(context: Context, private val sessionManager: ISessionManager) {

    private val context: Context = context.applicationContext

    fun createCloudDataStore(): GaleryDataStore {
        val service = GaleryService(context, sessionManager.accessToken,
            sessionManager.appName, sessionManager.country)
        return GaleryCloudDataStore(service)
    }

}
