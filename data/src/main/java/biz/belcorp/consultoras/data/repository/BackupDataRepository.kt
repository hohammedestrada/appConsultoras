package biz.belcorp.consultoras.data.repository

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.repository.datasource.backup.BackupDataStoreFactory
import biz.belcorp.consultoras.domain.repository.BackupRepository
import io.reactivex.Observable

@Singleton
class BackupDataRepository @Inject
internal constructor(private val backupDataStoreFactory: BackupDataStoreFactory) : BackupRepository {

    override fun backup(): Observable<Boolean?> {
        val dataStore = this.backupDataStoreFactory.create()
        return dataStore.backup().map { result -> result }
    }

    override fun reset(): Observable<Boolean?> {
        val dataStore = this.backupDataStoreFactory.create()
        return dataStore.reset().map { result -> result }
    }

    override fun restore(): Observable<Boolean?> {
        val dataStore = this.backupDataStoreFactory.create()
        return dataStore.restore().map { result -> result }
    }
}
