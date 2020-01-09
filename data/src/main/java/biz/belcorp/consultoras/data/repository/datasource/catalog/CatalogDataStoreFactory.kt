package biz.belcorp.consultoras.data.repository.datasource.catalog

import android.content.Context

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.manager.ISessionManager
import biz.belcorp.consultoras.data.net.service.impl.CatalogService

/**
 *
 */
@Singleton
class CatalogDataStoreFactory @Inject
internal constructor(private val context: Context, private val sessionManager: ISessionManager) {

    fun create(): CatalogDataStore {
        return createCloud()
    }

    fun createCloud(): CatalogDataStore {
        val catalogService = CatalogService(context, sessionManager.accessToken,
                sessionManager.appName, sessionManager.country)
        return CatalogCloudDataStore(catalogService)
    }

    fun createDB(): CatalogDataStore {
        return CatalogDBDataStore()
    }
}
