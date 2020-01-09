package biz.belcorp.consultoras.data.manager

import biz.belcorp.consultoras.data.entity.BackupEntity

interface IBackupManager {

    fun getBackup(): BackupEntity
    fun saveBackup(entity: BackupEntity): Boolean
}
