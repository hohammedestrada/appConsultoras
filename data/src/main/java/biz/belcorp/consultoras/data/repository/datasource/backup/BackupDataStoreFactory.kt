package biz.belcorp.consultoras.data.repository.datasource.backup

import android.content.Context

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.manager.IBackupManager

@Singleton
class BackupDataStoreFactory @Inject
internal constructor(context: Context, private val backupManager: IBackupManager) {

    private val context: Context = context.applicationContext

    fun create(): BackupDataStore {
        return createLocal()
    }

    private fun createLocal(): BackupDataStore {
        return BackupLocalDataStore(backupManager, context)
    }

}
