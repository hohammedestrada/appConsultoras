package biz.belcorp.consultoras.data.repository.datasource.session

import android.content.Context

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.manager.ISessionManager

@Singleton
class SessionDataStoreFactory @Inject
internal constructor(context: Context, private val sessionManager: ISessionManager) {

    private val context: Context = context.applicationContext

    fun create(): SessionDataStore {
        return createLocal()
    }

    fun createLocal(): SessionDataStore {
        return SessionLocalDataStore(this.sessionManager, context)
    }

}
