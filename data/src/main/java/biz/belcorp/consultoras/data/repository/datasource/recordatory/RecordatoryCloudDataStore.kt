package biz.belcorp.consultoras.data.repository.datasource.recordatory

import biz.belcorp.consultoras.data.entity.RecordatorioEntity
import biz.belcorp.consultoras.data.net.service.IRecordatoryService
import biz.belcorp.consultoras.data.util.Constant
import io.reactivex.Observable

/**
 *
 */
class RecordatoryCloudDataStore(private val iRecordatoryService: IRecordatoryService)
    : RecordatoryDataStore {

    override fun getRecordatory(recordatorioId: Int?): Observable<RecordatorioEntity?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun saveRecordatory(recordatorioEntity: RecordatorioEntity?)
        : Observable<RecordatorioEntity?> {
        recordatorioEntity?.let {
            return iRecordatoryService.saveRecordatory(recordatorioEntity.clienteID,
                recordatorioEntity)
        } ?: return Observable.error(NullPointerException(javaClass.canonicalName))
    }

    override fun updateRecordatory(recordatorioEntity: RecordatorioEntity?): Observable<Boolean?> {
        recordatorioEntity?.let {
            return iRecordatoryService.updateRecordatory(recordatorioEntity.clienteID,
                recordatorioEntity.recordatorioId, recordatorioEntity)
        } ?: return Observable.error(NullPointerException(javaClass.canonicalName))
    }

    override fun deleteRecordatory(recordatorioId: Int?, clienteId: Int?): Observable<Boolean?> {
        return iRecordatoryService.deleteRecordatory(recordatorioId, clienteId)
    }


}
