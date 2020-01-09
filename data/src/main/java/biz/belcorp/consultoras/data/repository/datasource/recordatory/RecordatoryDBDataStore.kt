package biz.belcorp.consultoras.data.repository.datasource.recordatory

import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.sql.language.Select

import biz.belcorp.consultoras.data.entity.RecordatorioEntity
import biz.belcorp.consultoras.data.entity.RecordatorioEntity_Table
import biz.belcorp.library.sql.exception.SqlException
import com.raizlabs.android.dbflow.kotlinextensions.*
import io.reactivex.Observable

class RecordatoryDBDataStore : RecordatoryDataStore {

    override fun getRecordatory(recordatorioId: Int?): Observable<RecordatorioEntity?> {
        return Observable.create { emitter ->
            try {
                val querySingle = Select()
                    .from(RecordatorioEntity::class.java)
                    .where(RecordatorioEntity_Table.Id.eq(recordatorioId))
                    .querySingle()
                emitter.onNext(querySingle!!)
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    override fun saveRecordatory(recordatorioEntity: RecordatorioEntity?)
        : Observable<RecordatorioEntity?> {
        return Observable.create { emitter ->
            try {
                recordatorioEntity?.let {
                    val recordatoryId = FlowManager.getModelAdapter(RecordatorioEntity::class.java)
                        .insert(recordatorioEntity).toInt()
                    recordatorioEntity.id = recordatoryId
                    emitter.onNext(recordatorioEntity)
                }?: NullPointerException()
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    override fun updateRecordatory(recordatorioEntity: RecordatorioEntity?): Observable<Boolean?> {
        return Observable.create { emitter ->
            try {
                recordatorioEntity?.let {
                    val update = FlowManager.getModelAdapter(RecordatorioEntity::class.java)
                        .update(recordatorioEntity)
                    emitter.onNext(update)
                } ?: emitter.onError(NullPointerException(javaClass.canonicalName))
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    override fun deleteRecordatory(recordatorioId: Int?, clienteId: Int?): Observable<Boolean?> {
        return Observable.create { emitter ->
            try {
                recordatorioId?.let {
                    val response = (select
                        from RecordatorioEntity::class
                        where (RecordatorioEntity_Table.Id eq recordatorioId)
                        ).result

                    var delete = false
                    if (null != response)
                        delete = FlowManager.getModelAdapter(RecordatorioEntity::class.java).delete(response)
                    emitter.onNext(delete)
                } ?: emitter.onError(NullPointerException(javaClass.canonicalName))
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }
}
