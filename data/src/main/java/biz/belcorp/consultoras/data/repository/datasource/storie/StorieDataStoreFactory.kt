package biz.belcorp.consultoras.data.repository.datasource.storie

import android.content.Context
import biz.belcorp.consultoras.data.manager.ISessionManager
import biz.belcorp.consultoras.data.net.service.impl.StorieService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorieDataStoreFactory
@Inject
internal constructor(context: Context, private val sessionManager: ISessionManager) {

    private val context: Context = context.applicationContext

    fun createCloudDataStore(): StorieDataSource{
       val servicio = StorieService(context, sessionManager.accessToken,
            sessionManager.appName, sessionManager.country)
        return StorieCloudDataStore(servicio)
    }



}
