package biz.belcorp.consultoras.data.repository.datasource.survey

import android.content.Context
import biz.belcorp.consultoras.data.manager.ISessionManager
import biz.belcorp.consultoras.data.net.service.impl.SurveyService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SurveyDataStoreFactory
@Inject internal constructor(context: Context, private val sessionManager: ISessionManager) {

    private val context: Context = context.applicationContext

    fun createCloudDataStore(): SurveyDataStore {
        val service = SurveyService(context, sessionManager.accessToken,
            sessionManager.appName, sessionManager.country)
        return SurveyCloudDataStore(service)
    }
}
