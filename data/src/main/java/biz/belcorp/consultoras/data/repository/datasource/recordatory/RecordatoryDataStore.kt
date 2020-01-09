package biz.belcorp.consultoras.data.repository.datasource.recordatory

import biz.belcorp.consultoras.data.entity.RecordatorioEntity
import io.reactivex.Observable

/**
 *
 */
interface RecordatoryDataStore {

    fun getRecordatory(recordatorioId: Int?): Observable<RecordatorioEntity?>

    fun saveRecordatory(recordatorioEntity: RecordatorioEntity?): Observable<RecordatorioEntity?>

    fun updateRecordatory(recordatorioEntity: RecordatorioEntity?): Observable<Boolean?>

    fun deleteRecordatory(recordatorioId: Int?, clienteId: Int?): Observable<Boolean?>

}
