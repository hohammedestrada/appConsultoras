package biz.belcorp.consultoras.data.repository.datasource.caminobrillante

import android.content.Context
import biz.belcorp.consultoras.data.manager.ISessionManager
import biz.belcorp.consultoras.data.net.service.impl.CaminoBrillanteService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CaminoBrillanteDataStoreFactory @Inject
internal constructor(private val context: Context, private val sessionManager: ISessionManager) {

    fun create(): CaminoBrillanteDataStore {
        return createCloud()
    }

    fun createDB(): CaminoBrillanteDataStore {
        return CaminoBrillanteDBDataStore()
    }

    private fun createCloud(): CaminoBrillanteDataStore {
        val caminobrillanteService = CaminoBrillanteService(context, sessionManager.accessToken,
            sessionManager.appName, sessionManager.country)
        return CaminoBrillanteCloudDataStore(caminobrillanteService)
    }

}
