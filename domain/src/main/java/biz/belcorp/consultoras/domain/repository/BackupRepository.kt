package biz.belcorp.consultoras.domain.repository

import io.reactivex.Observable

interface BackupRepository {

    fun backup(): Observable<Boolean?>

    fun reset(): Observable<Boolean?>

    fun restore(): Observable<Boolean?>
}
