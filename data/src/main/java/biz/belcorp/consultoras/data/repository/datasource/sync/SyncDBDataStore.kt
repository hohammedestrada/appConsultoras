package biz.belcorp.consultoras.data.repository.datasource.sync

import biz.belcorp.consultoras.data.db.ConsultorasDatabase
import biz.belcorp.consultoras.data.entity.*
import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.library.net.dto.ServiceDto
import biz.belcorp.library.sql.exception.SqlException
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.kotlinextensions.*
import io.reactivex.Observable
import java.math.BigDecimal

/**
 * Clase de Sync encargada de obtener la data desde el SQLite
 * dicha clase se instancaria una sola vez y se mantendra mientras la aplicacion este viva
 *
 * @version 1.0
 * @since 2017-05-03
 */
/**
 * Constructor
 */
internal class SyncDBDataStore : SyncDataStore {



    override val clientsToSync: Observable<List<ClienteEntity?>?>
        get() = Observable.create { emitter ->
            try {
                val entities = (select
                        from ClienteEntity::class
                        where (ClienteEntity_Table.Sincronizado eq 0)
                    ).list

                for (i in entities.indices) {

                    val clientID = entities[i].id

                    clientID?.let {
                        val movements = (select from ClientMovementEntity::class
                            where (ClientMovementEntity_Table.ClienteLocalID eq clientID)).list

                        movements.indices.forEach { j ->
                            movements[j].amount?.let {
                                if (movements[j].movementType == "A" && it < BigDecimal.ZERO)
                                    movements[j].amount = it.negate()
                            }
                        }

                        entities[i].movimientoEntities = movements

                        val listNotes = (select from AnotacionEntity::class
                            where (AnotacionEntity_Table.ClienteLocalID eq clientID)).list

                        entities[i].anotacionEntities = listNotes

                        val listReminder = (select from RecordatorioEntity::class
                            where (RecordatorioEntity_Table.ClienteLocalID eq clientID)).list

                        entities[i].recordatorioEntities = listReminder
                    }

                }

                emitter.onNext(entities)
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }

    override val anotationsToSync: Observable<List<AnotacionEntity?>?>
        get() = Observable.create { emitter ->
            try {
                val entities = (select
                        from AnotacionEntity::class
                        where (AnotacionEntity_Table.Sincronizado eq 0)
                        and (AnotacionEntity_Table.ClienteID notEq 0)
                    ).queryList()

                emitter.onNext(entities)
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }

    override val notificationsHybrisToSync: Observable<List<HybrisDataEntity?>?>
        get() = Observable.create { emitter ->
            try {
                val entities = (select
                    from HybrisDataEntity::class
                    ).queryList()

                emitter.onNext(entities)
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }

    @Suppress("UNREACHABLE_CODE")
    override fun syncClients(clients: List<ClienteEntity?>?): Observable<Collection<ClienteEntity?>?> {
        return throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun saveSyncClients(clients: List<ClienteEntity?>?): Observable<Boolean?> {
        return Observable.create { emitter ->
            try {
                FlowManager.getDatabase(ConsultorasDatabase::class.java).beginTransactionAsync { databaseWrapper ->
                    clients?.let {

                        databaseWrapper.beginTransaction()

                        for (clienteEntity in clients.filterNotNull()) {

                            if (clienteEntity.estado == 0)
                                FlowManager.getModelAdapter(ClienteEntity::class.java).delete(clienteEntity)
                            else
                                FlowManager.getModelAdapter(ClienteEntity::class.java).save(clienteEntity)

                            clienteEntity.id?.let {
                                val listContact = (select from ContactoEntity::class
                                    where (ContactoEntity_Table.ClienteLocalID eq it)).list
                                FlowManager.getModelAdapter(ContactoEntity::class.java).deleteAll(listContact)
                            }

                            clienteEntity.contactoEntities?.let {
                                if (it.isNotEmpty())
                                    FlowManager.getModelAdapter(ContactoEntity::class.java)
                                        .saveAll(it)
                            }

                            clienteEntity.anotacionEntities?.let {
                                if (it.isNotEmpty())
                                    FlowManager.getModelAdapter(AnotacionEntity::class.java)
                                        .saveAll(it)
                            }

                            clienteEntity.recordatorioEntities?.let {
                                if (it.isNotEmpty())
                                    FlowManager.getModelAdapter(RecordatorioEntity::class.java)
                                        .saveAll(it)
                            }

                            clienteEntity.movimientoEntities?.let {
                                if (it.isNotEmpty())
                                    FlowManager.getModelAdapter(ClientMovementEntity::class.java)
                                        .saveAll(it)
                            }

                            clienteEntity.id?.let {
                                val listNotes = (select from AnotacionEntity::class
                                    where (AnotacionEntity_Table.ClienteLocalID eq it)
                                    and (AnotacionEntity_Table.Sincronizado eq 0)
                                    ).list
                                FlowManager.getModelAdapter(AnotacionEntity::class.java)
                                    .deleteAll(listNotes)

                                val listReminder = (select from RecordatorioEntity::class
                                    where (RecordatorioEntity_Table.ClienteLocalID eq it)
                                    and (RecordatorioEntity_Table.Sincronizado eq 0)
                                    or (RecordatorioEntity_Table.Estado eq -1))
                                    .list
                                FlowManager.getModelAdapter(RecordatorioEntity::class.java)
                                    .deleteAll(listReminder)

                                val listMovements = (select from ClientMovementEntity::class
                                    where (ClientMovementEntity_Table.ClienteLocalID eq it)
                                    .and (ClientMovementEntity_Table.Sincronizado eq 0)
                                    ).list
                                FlowManager.getModelAdapter(ClientMovementEntity::class.java)
                                    .deleteAll(listMovements)
                            }
                        }

                        val listClients = (select from ClienteEntity::class
                            where (ClienteEntity_Table.Sincronizado eq 0)).list

                        FlowManager.getModelAdapter(ClienteEntity::class.java)
                            .deleteAll(listClients)

                        databaseWrapper.setTransactionSuccessful()
                        databaseWrapper.endTransaction()
                    } ?: NullPointerException()

                }.error { _, _ ->
                    emitter.onNext(false)
                    emitter.onComplete()
                }.success {
                    emitter.onNext(true)
                    emitter.onComplete()
                }.build().execute()
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex))
            }
        }
    }

    @Suppress("UNREACHABLE_CODE")
    override fun syncAnotations(anotations: List<AnotacionEntity?>?): Observable<List<AnotacionEntity?>?> {
        return throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun saveSyncAnotations(notes: List<AnotacionEntity?>?): Observable<Boolean?> {
        return Observable.create { emitter ->
            try {
                FlowManager.getDatabase(ConsultorasDatabase::class.java).beginTransactionAsync { databaseWrapper ->
                    notes?.let {
                        databaseWrapper.beginTransaction()

                        FlowManager.getModelAdapter(AnotacionEntity::class.java)
                            .saveAll(notes)

                        val listNotes = (select from AnotacionEntity::class
                            where (AnotacionEntity_Table.Sincronizado eq 0)).list
                        FlowManager.getModelAdapter(AnotacionEntity::class.java).deleteAll(listNotes)

                        databaseWrapper.setTransactionSuccessful()
                        databaseWrapper.endTransaction()
                    }

                }.error { _, _ ->
                    emitter.onNext(false)
                    emitter.onComplete()
                }.success {
                    emitter.onNext(true)
                    emitter.onComplete()
                }.build().execute()
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex))
            }
        }
    }

    override fun updateHybrisData(list: List<HybrisDataEntity?>?): Observable<Boolean?> {
        return Observable.create { emitter ->
            try {
                FlowManager.getModelAdapter(HybrisDataEntity::class.java).deleteAll(list!!)
                emitter.onNext(true)
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex))
            }
        }
    }

    override fun syncPaymentLocalToService(visaLogPaymentEntity: VisaLogPaymentEntity): Observable<ServiceDto<ResultadoPagoEnLineaEntity>> {
        return throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getPaymentLocal(): Observable<VisaLogPaymentEntity> {
        return Observable.create { emitter->
            try {
                    (select from VisaLogPaymentEntity::class)
                    .result?.let {
                        emitter.onNext(it)
                }
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }
            emitter.onComplete()
        }
    }

}
