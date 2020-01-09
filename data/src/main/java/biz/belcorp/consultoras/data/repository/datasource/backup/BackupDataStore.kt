package biz.belcorp.consultoras.data.repository.datasource.backup

import io.reactivex.Observable

interface BackupDataStore {

    fun backup(): Observable<Boolean?>
    fun reset(): Observable<Boolean?>
    fun restore(): Observable<Boolean?>
}
