package biz.belcorp.consultoras.data.repository.datasource.pagoOnline

import android.content.Context
import biz.belcorp.consultoras.data.db.ConsultorasDatabase
import biz.belcorp.consultoras.data.entity.VisaEntity
import biz.belcorp.consultoras.data.entity.VisaEntity_Table
import biz.belcorp.consultoras.data.entity.VisaLogPaymentEntity
import biz.belcorp.consultoras.data.entity.VisaLogPaymentEntity_Table
import biz.belcorp.consultoras.data.exception.SessionException
import biz.belcorp.library.sql.exception.SqlException
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.kotlinextensions.*
import io.reactivex.Observable
import io.reactivex.Observable.create

class PagoOnlineDBDataStore(val context: Context) : PagoOnlineDataStore {

    override fun deleteLocal(idVisaUnico: String, transitionId: String, codigoUsuario: String) {
        FlowManager.getDatabase(ConsultorasDatabase::class.java).beginTransactionAsync { databaseWrapper ->
            try {
                databaseWrapper.beginTransaction()
                val responseVisaLog = (select
                    from VisaLogPaymentEntity::class
                    where (VisaLogPaymentEntity_Table.TransactionId eq transitionId)
                    and (VisaLogPaymentEntity_Table.CodigoUsuario eq codigoUsuario)
                    ).result
                val responseVisa = (select
                    from VisaEntity::class
                    where (VisaEntity_Table.ID_UNICO eq idVisaUnico)
                    ).result
                if (null != responseVisaLog && null != responseVisa) {
                    FlowManager.getModelAdapter(VisaLogPaymentEntity::class.java).delete(responseVisaLog)
                    FlowManager.getModelAdapter(VisaEntity::class.java).delete(responseVisa)
                }
                databaseWrapper.setTransactionSuccessful()
                databaseWrapper.endTransaction()
            } catch (ex: Exception) {
                //TODO MCAPP-14
            }
        }.build().execute()
    }

    override fun saveLocalPayment(visaLogPaymentEntity: VisaLogPaymentEntity, sincronizado: Int)
        : Observable<Boolean> {
        return create { emitter ->
            try {
                visaLogPaymentEntity.let {
                    FlowManager.getDatabase(ConsultorasDatabase::class.java).beginTransactionAsync { databaseWrapper ->
                        databaseWrapper.beginTransaction()
                        guardarPago(it, sincronizado)
                        databaseWrapper.setTransactionSuccessful()
                        databaseWrapper.endTransaction()
                    }
                        .error { _, error -> emitter.onError(SessionException(error)) }
                        .success { _ ->
                            emitter.onNext(true)
                        }.build().execute()
                }
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }
        }
    }

    private fun guardarPago(input: VisaLogPaymentEntity, sincronizado: Int) {
        input.let {
            it.visa.let { itVisa ->
                saveDataVisa(itVisa, sincronizado)
                itVisa!!.sincro = sincronizado
                FlowManager.getModelAdapter(VisaLogPaymentEntity::class.java).save(it)
            }
        }
    }

    private fun saveDataVisa(itVisa: VisaEntity?, sincronizado: Int) {
        itVisa.let {
            it!!.sincro = sincronizado
            FlowManager.getModelAdapter(VisaEntity::class.java).save(it)
        }
    }

}
