package biz.belcorp.consultoras.data.repository.datasource.cliente

import android.content.Context
import android.text.TextUtils
import biz.belcorp.consultoras.data.db.ConsultorasDatabase
import biz.belcorp.consultoras.data.entity.*
import biz.belcorp.consultoras.data.entity.AnotacionEntity_Table.ID
import biz.belcorp.consultoras.data.exception.SessionException
import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.library.sql.exception.SqlException
import com.raizlabs.android.dbflow.annotation.Collate
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.kotlinextensions.*
import com.raizlabs.android.dbflow.sql.language.OperatorGroup
import com.raizlabs.android.dbflow.sql.language.OrderBy
import com.raizlabs.android.dbflow.sql.language.Update
import com.raizlabs.android.dbflow.sql.language.property.PropertyFactory
import io.reactivex.Observable
import io.reactivex.Observable.create
import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList

/**
 * Clase de Cliente encargada de obtener la data desde el SQLite
 * dicha clase se instancaria una sola vez y se mantendra mientras la aplicacion este viva
 *
 * @version 1.0
 * @since 2017-05-03
 */
internal class ClienteDBDataStore
/**
 * Constructor
 *
 * @param context contexto que llamo al SQLite
 */
(val context: Context) : ClienteDataStore {

    override val clientes: Observable<List<ClienteEntity?>?>
        get() = create { emitter ->
            try {
                val entities = (select
                    from ClienteEntity::class
                    where (ClienteEntity_Table.Estado eq 1)
                    orderBy (OrderBy.fromProperty(ClienteEntity_Table.Nombres)
                    collate Collate.NOCASE).ascending()
                    ).list

                emitter.onNext(entities)
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }

    override val deudoresWS: Observable<List<DeudorRequestEntity?>?>
        get() = throw UnsupportedOperationException("No se va a implementar")

    override val deudoresDB: Observable<List<ClienteEntity?>?>
        get() = create { emitter ->
            try {
                val entities = (select
                    from ClienteEntity::class
                    where (ClienteEntity_Table.Estado eq 1)
                    orderBy (OrderBy.fromProperty(ClienteEntity_Table.Nombres)
                    collate Collate.NOCASE).ascending()
                    ).list

                val it = entities.iterator()
                var total: Double
                var totalDeuda: BigDecimal?
                while (it.hasNext()) {
                    totalDeuda = it.next().totalDeuda
                    if (totalDeuda != null) {
                        total = java.lang.Double.parseDouble(totalDeuda.toString())
                        if (total <= 0) {
                            it.remove()
                        }
                    } else {
                        it.remove()
                    }
                }

                emitter.onNext(entities)
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }

    override val notificacionesCliente: Observable<List<NotificacionClienteEntity?>?>
        get() = create { emitter ->
            try {
                val entities = (select
                    from NotificacionClienteEntity::class
                    where (NotificacionClienteEntity_Table.Estado eq 0)
                    ).list

                var clienteEntity: ClienteEntity?

                entities.forEach {
                    it.estado = 1

                    FlowManager.getModelAdapter(NotificacionClienteEntity::class.java)
                        .save(it)

                    clienteEntity = (
                        select
                            from ClienteEntity::class
                            where (ID.eq(it.clienteLocalID))
                        )
                        .querySingle()

                    it.clienteEntity = clienteEntity

                }

                emitter.onNext(entities)
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }

    override val notificacionesRecordatorio: Observable<List<NotificacionRecordatorioEntity?>?>
        get() = create { emitter ->
            try {
                val entities = (select
                    from NotificacionRecordatorioEntity::class
                    where (NotificacionRecordatorioEntity_Table.Estado eq 0)
                    ).list

                var recordatorioEntity: RecordatorioEntity?
                var clienteEntity: ClienteEntity?

                entities.requireNoNulls().forEach { entity ->
                    entity.estado = 1

                    FlowManager.getModelAdapter(NotificacionRecordatorioEntity::class.java)
                        .save(entity)

                    entity.recordatorioLocalID?.let {
                        recordatorioEntity = (select
                            from RecordatorioEntity::class
                            where (RecordatorioEntity_Table.Id eq it)
                            ).querySingle()

                        entity.recordatorioEntity = recordatorioEntity

                        recordatorioEntity?.clienteLocalID?.let {
                            clienteEntity = (select
                                from ClienteEntity::class
                                where (ClienteEntity_Table.ID eq it)
                                ).result

                            entity.clienteEntity = clienteEntity
                        }

                    }

                }

                emitter.onNext(entities)
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }

    /**
     * Metodo que obtiene el listado de clientes desde el SQLite
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun bajada(clientId: Int?, campaingCode: String?): Observable<List<ClienteEntity?>?> {
        return create { emitter ->
            try {

                val entities = (select
                    from ClienteEntity::class
                    where (ClienteEntity_Table.Estado eq 1)
                    orderBy (OrderBy.fromProperty(ClienteEntity_Table.Nombres)
                    collate Collate.NOCASE).ascending()
                    ).list

                emitter.onNext(entities)
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    /**
     * Servicio de tipo POST que sube los clientes
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun subida(clientes: List<ClienteEntity?>?, sincronizado: Int?): Observable<List<ClienteEntity?>?> {

        return create { emitter ->
            try {

                clientes?.let {
                    FlowManager.getDatabase(ConsultorasDatabase::class.java).beginTransactionAsync { databaseWrapper ->

                        databaseWrapper.beginTransaction()

                        saveClientsDB(it, sincronizado)

                        databaseWrapper.setTransactionSuccessful()
                        databaseWrapper.endTransaction()

                    }
                        .error { _, error -> emitter.onError(SessionException(error)) }
                        .success { _ ->
                            emitter.onNext(clientes)
                            emitter.onComplete()
                        }.build().execute()
                }
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }
        }
    }

    override fun saveClients(clients: List<ClienteEntity?>?, syncFlag: Int?): Observable<Boolean?> {
        return create { emitter ->
            try {
                clients?.let {

                    FlowManager.getDatabase(ConsultorasDatabase::class.java).beginTransactionAsync { databaseWrapper ->

                        databaseWrapper.beginTransaction()

                        saveClientsDB(it, syncFlag)

                        databaseWrapper.setTransactionSuccessful()
                        databaseWrapper.endTransaction()

                    }
                        .error { _, error -> emitter.onError(SessionException(error)) }
                        .success { _ ->
                            emitter.onNext(true)
                            emitter.onComplete()
                        }.build().execute()
                }
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }
        }
    }

    override fun eliminar(clienteEntityList: List<ClienteEntity?>?): Observable<String?> {
        return create { emitter ->
            try {
                clienteEntityList?.let {
                    it.forEach { cliente ->
                        cliente?.let {
                            cliente.estado = -1
                            when {
                                cliente.sincronizado == 0
                                    && null != cliente.clienteID
                                    && cliente.clienteID != 0 ->
                                    FlowManager.getModelAdapter(ClienteEntity::class.java)
                                        .save(cliente)
                                else -> FlowManager.getModelAdapter(ClienteEntity::class.java)
                                    .delete(cliente)
                            }
                        } ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                    }
                } ?: emitter.onError(NullPointerException(javaClass.canonicalName))


                emitter.onNext("OK")
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    override fun delete(clienteID: Int?): Observable<Boolean?> {
        return create { emitter ->
            try {
                findClientByClientId(clienteID).map {
                    FlowManager.getModelAdapter(ClienteEntity::class.java)
                        .delete(it)
                    emitter.onNext(true)
                }
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }
            emitter.onComplete()
        }
    }

    override fun validateClient(client: ClienteEntity?): Observable<Int?> {
        return create { emmiter ->
            try {
                client?.let {

                    var validate: Int = 0

                    var name = it.nombres

                    val andMayGroup = OperatorGroup.clause()

                    val andGroup = OperatorGroup.clause()
                    andGroup.and(ClienteEntity_Table.Nombres.like(it.nombres?.toLowerCase()))

                    if (!TextUtils.isEmpty(it.apellidos)) {
                        andGroup.and(ClienteEntity_Table.Apellidos.like(it.apellidos?.toLowerCase()))
                        name += " " + it.apellidos
                    } else {
                        andGroup.and(ClienteEntity_Table.Apellidos.isNull)
                    }

                    val orGroup = OperatorGroup.clause()

                    val query = StringBuilder()
                    query.append(ClienteEntity_Table.Nombres)
                    query.append(" || coalesce (' ' || nullif(")
                    query.append(ClienteEntity_Table.Apellidos)
                    query.append(",''),'')")
                    orGroup.or(andGroup)
                    orGroup.or(PropertyFactory.from(String::class.java, query.toString())
                        .like(name!!.toLowerCase())
                    )
                    andMayGroup.and(orGroup)

                    if (null != it.id && it.id != 0)
                        andMayGroup.and(ClienteEntity_Table.ID.notEq(it.id))

                    val list = (select
                        from ClienteEntity::class
                        where (andMayGroup)
                        ).list

                    if (!list.isEmpty()) validate = 1

                    if (validate == 0) {
                        val cel = getCellphoneFromClienteEntity(it)
                        val telf = getTelephoneFromClienteEntity(it)

                        var scel: String? = ""
                        var stelf: String? = ""

                        if (null != cel) scel = cel.valor
                        if (null != telf) stelf = telf.valor

                        val entity: ClienteEntity?
                        if (null != it.id && it.id != 0)
                            entity = getClienteByMobileOrPhone(it.id, scel, stelf)
                        else
                            entity = getClienteByMobileOrPhone(scel, stelf)

                        if (null != entity) validate = 2
                    }
                    emmiter.onNext(validate)
                } ?: NullPointerException()
            } catch (ex: Exception) {
                emmiter.onError(SqlException(ex.cause))
            }

            emmiter.onComplete()
        }
    }


    override fun existClientsToSync(): Observable<Boolean?> {
        return create { emitter ->
            try {
                val result = (select
                    from ClienteEntity::class
                    where (ClienteEntity_Table.Sincronizado eq 0)
                    ).list.isEmpty()

                emitter.onNext(!result)
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    override fun save(client: ClienteEntity?): Observable<String?> {
        return create { emitter ->
            try {
                client?.let {
                    FlowManager.getModelAdapter(ClienteEntity::class.java)
                        .save(client)
                    client.anotacionEntities?.let { it1 ->
                        FlowManager.getModelAdapter(AnotacionEntity::class.java)
                            .saveAll(it1)
                    }
                    client.contactoEntities?.let { it1 ->
                        FlowManager.getModelAdapter(ContactoEntity::class.java)
                            .saveAll(it1)
                    }
                    client.recordatorioEntities?.let { it1 ->
                        FlowManager.getModelAdapter(RecordatorioEntity::class.java)
                            .saveAll(it1)
                    }

                    emitter.onNext("OK")
                } ?: NullPointerException()

            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    override fun getClientesByFavorito(favorito: Int?): Observable<List<ClienteEntity?>?> {
        return create { emitter ->
            try {
                val entities = (select
                    from ClienteEntity::class
                    where (ClienteEntity_Table.Favorito.eq(favorito))
                    orderBy (OrderBy.fromProperty(ClienteEntity_Table.Nombres)
                    collate Collate.NOCASE).ascending()
                    ).list

                emitter.onNext(entities)
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    /**
     * obtiene un cliente por id
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun findClienteById(id: Int?): Observable<ClienteEntity?> {
        return create { emitter ->
            try {

                val entity = (select
                    from ClienteEntity::class
                    where (ClienteEntity_Table.ID.eq(id))
                    and (ClienteEntity_Table.Estado eq 1)
                    ).result

                entity?.let { emitter.onNext(it) }
                    ?: emitter.onError(NullPointerException("Cliente No Encontrado"))
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    /**
     * obtiene un cliente por clienteId
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun findClientByClientId(clienteId: Int?): Observable<ClienteEntity?> {
        return create { emitter ->
            try {

                val entity = (select
                    from ClienteEntity::class
                    where (ClienteEntity_Table.ClienteID.eq(clienteId))
                    and (ClienteEntity_Table.Estado eq 1)
                    ).result

                entity?.let { emitter.onNext(it) }
                    ?: emitter.onError(NullPointerException("Cliente No Encontrado"))
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    override fun getMovements(clienteLocalID: Int?): Observable<List<ClientMovementEntity?>?> {
        return create { emitter ->
            try {

                val entities = (select
                    from ClientMovementEntity::class
                    where ClientMovementEntity_Table.ClienteLocalID.eq(clienteLocalID)
                    and (ClientMovementEntity_Table.Estado notEq -1)
                    orderBy (OrderBy.fromProperty(ClientMovementEntity_Table.Date)).descending()
                    ).list

                emitter.onNext(entities)
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    override fun getMovementByID(id: Int?): Observable<ClientMovementEntity?> {
        return create { emitter ->
            try {

                val entities = (select
                    from ClientMovementEntity::class
                    where ClientMovementEntity_Table.ID.eq(id)
                    ).result

                entities?.let { emitter.onNext(it) }
                    ?: emitter.onError(NullPointerException("No se encontró movimiento"))
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    override fun saveMovement(clienteLocalID: Int?, movement: ClientMovementEntity?): Observable<ClientMovementEntity?> {
        return create { emitter ->

            try {
                movement?.let {
                    FlowManager.getModelAdapter(ClientMovementEntity::class.java)
                        .save(movement)
                    emitter.onNext(movement)
                } ?: NullPointerException()

            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    override fun updateMovement(movement: ClientMovementEntity?): Observable<String?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun deleteMovement(movement: ClientMovementEntity?): Observable<Boolean?> {
        return create { emitter ->

            try {
                movement?.let {
                    emitter.onNext(FlowManager.getModelAdapter(ClientMovementEntity::class.java)
                        .delete(movement))
                } ?: emitter.onError(NullPointerException(javaClass.canonicalName))
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    override fun deleteMovementOffline(movement: ClientMovementEntity?): Observable<BigDecimal?> {
        return create { emitter ->
            try {
                movement?.let {
                    val delete = FlowManager.getModelAdapter(ClientMovementEntity::class.java)
                        .update(movement)
                    if (delete) {

                        val clienteEntity = (select
                            from ClienteEntity::class
                            where ClienteEntity_Table.ID.eq(movement.clienteLocalID)
                            ).result

                        clienteEntity?.let {
                            it.totalDeuda?.let { it1 ->
                                emitter.onNext(it1)
                            } ?: emitter.onError(NullPointerException("Total deuda nula"))
                        } ?: emitter.onError(NullPointerException("Cliente no encontrado"))
                    }
                } ?: emitter.onError(NullPointerException(javaClass.canonicalName))
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    override fun saveMovement(movements: List<ClientMovementEntity?>?): Observable<String?> {
        return create { emitter ->
            try {
                movements?.let {
                    if (!movements.isEmpty()) {
                        val clientId: Int? = it[0]?.clientID
                        // Delete movements with MovementId and state sync 1
                        val movementsList = getMovementsSyncByClientId(clientId)
                        movementsList?.filterNotNull()?.let { movementsList ->
                            if (movementsList.isNotEmpty()) {
                                FlowManager.getModelAdapter(ClientMovementEntity::class.java)
                                    .deleteAll(movementsList)
                            }
                        }
                        val clienteEntity = getClienteByClienteId(clientId)
                        // Save Movements
                        clientId?.let { it1 ->
                            for (clientMovementEntity in it.filterNotNull()) {
                                clientMovementEntity.clienteLocalID = clienteEntity?.id
                                val movementDB = getMovementByMovementID(clientMovementEntity.movementID)
                                if (movementDB == null) {
                                    //movimiento nuevo
                                    FlowManager.getModelAdapter(ClientMovementEntity::class.java)
                                        .insert(clientMovementEntity)
                                } else {
                                    //actualizar movimiento
                                    clientMovementEntity.id = movementDB.id
                                    FlowManager.getModelAdapter(ClientMovementEntity::class.java)
                                        .update(clientMovementEntity)
                                }
                                clientMovementEntity.productList?.let { it2 ->
                                    FlowManager.getModelAdapter(ProductResponseEntity::class.java).saveAll(it2)
                                }
                            }
                        }
                    }
                }

                emitter.onNext("OK")
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    override fun saveTotalDebt(clienteLocalID: Int?, totalDebt: BigDecimal?, flagOnline: Boolean?): Observable<Boolean?> {
        return create { emitter ->
            try {
                var balance = BigDecimal.ZERO

                val movements = (select
                    from ClientMovementEntity::class
                    where ClientMovementEntity_Table.ClienteLocalID.eq(clienteLocalID)
                    and (ClientMovementEntity_Table.Estado notEq -1)
                    ).list

                for (movement in movements) {
                    movement.movementType?.let { type ->
                        movement.amount?.let {
                            if (type.startsWith("A") && it > BigDecimal.ZERO)
                                balance = balance.add(it.negate())
                            else
                                balance = balance.add(it)
                        } ?: emitter.onError(NullPointerException("Movimiento con monto nulo"))
                    }
                }

                val update = Update(ClienteEntity::class.java)
                    .set(ClienteEntity_Table.TotalDeuda.eq(balance))
                    .where(ClienteEntity_Table.ID.eq(clienteLocalID))

                update.execute()

                emitter.onNext(true)
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    override fun existAnotationsToSync(): Observable<Boolean?> {
        return create { emitter ->
            try {
                val result = (select
                    from AnotacionEntity::class
                    where (AnotacionEntity_Table.Sincronizado eq 0)
                    and (AnotacionEntity_Table.ClienteID notEq 0)
                    ).list.isEmpty()

                emitter.onNext(!result)
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    override fun updateSyncFlag(clientLocalId: Int?, flag: Int?): Observable<Boolean?> {
        return create { emitter ->
            try {
                val update = Update(ClienteEntity::class.java)
                    .set(ClienteEntity_Table.Sincronizado.eq(flag))
                    .where(ClienteEntity_Table.ID.eq(clientLocalId))

                update.execute()

                emitter.onNext(true)
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    override fun updateProductos(clientMovementEntity: ClientMovementEntity?): Observable<Boolean?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun processBirthdaysNotifications(clienteEntities: List<ClienteEntity?>?): Observable<List<ClienteEntity?>?> {
        var notificacionClienteEntity: NotificacionClienteEntity
        val year = Calendar.getInstance().get(Calendar.YEAR)
        clienteEntities?.let {
            for (clienteEntity in clienteEntities.filterNotNull()) {
                if (findNotificacionClienteByIDAndYear(clienteEntity.id,
                        year) == null) {
                    notificacionClienteEntity = NotificacionClienteEntity()
                    notificacionClienteEntity.clienteLocalID = clienteEntity.id
                    notificacionClienteEntity.year = year
                    notificacionClienteEntity.estado = 0
                    FlowManager.getModelAdapter(NotificacionClienteEntity::class.java)
                        .save(notificacionClienteEntity)
                }
            }
        }
        return Observable.just(clienteEntities)
    }

    override fun processRecordatoryNotifications(clienteEntities: List<ClienteEntity?>?): Observable<List<ClienteEntity?>?> {
        var notificacionRecordatorioEntity: NotificacionRecordatorioEntity
        clienteEntities?.let {
            clienteEntities.filterNotNull().forEach { clienteEntity ->
                clienteEntity.recordatorioEntities?.filterNotNull()?.forEach { recordatorioEntity ->
                    if (findNotificacionRecordatorioByID(recordatorioEntity.id) == null) {
                        notificacionRecordatorioEntity = NotificacionRecordatorioEntity()
                        notificacionRecordatorioEntity.recordatorioLocalID = recordatorioEntity.id
                        notificacionRecordatorioEntity.estado = 0
                        FlowManager.getModelAdapter(NotificacionRecordatorioEntity::class.java)
                            .save(notificacionRecordatorioEntity)
                    }
                }
            }
        }
        return Observable.just(clienteEntities)
    }

    /** */

    private fun saveClientsDB(it: List<ClienteEntity?>, syncFlag: Int?) {

        val year = Calendar.getInstance().get(Calendar.YEAR)

        var clientDB: ClienteEntity? = null
        var cel: ContactoEntity?
        var telf: ContactoEntity?
        var scel: String? = ""
        var stelf: String? = ""
        it.forEach { clienteEntity ->
            clienteEntity?.let {
                cel = getCellphoneFromClienteEntity(it)
                telf = getTelephoneFromClienteEntity(it)
                if (cel != null) {
                    scel = cel?.valor
                }
                if (telf != null) {
                    stelf = telf?.valor
                }

                it.id?.let {
                    clientDB = getClienteById(it)
                } ?: run {
                    it.clienteID?.let {
                        if (it != 0) {
                            clientDB = getClienteByClienteId(it)
                        }
                    }
                }


                if (clientDB == null) {
                    clientDB = getClienteByMobileOrPhone(scel, stelf)
                }
                if (clientDB != null) {
                    //el cliente existe en la bd y tiene clienteID del servidor
                    if (clientDB?.clienteID != null && clientDB?.clienteID != 0) {
                        it.id = clientDB?.id
                        it.contactoEntities?.forEach { contactoEntity -> contactoEntity?.clienteLocalID = clientDB?.id }
                        it.anotacionEntities?.forEach { anotacionEntity -> saveAnotation(anotacionEntity, clientDB?.id, syncFlag) }
                        it.recordatorioEntities?.forEach { recordatoryEntity -> updateItemRecordatory(recordatoryEntity, clientDB?.id, syncFlag) }
                        it.sincronizado = syncFlag
                        FlowManager.getModelAdapter(ClienteEntity::class.java)
                            .save(it)
                        saveContacts(it.contactoEntities, clientDB?.id)
                        //el cliente existe en la bd pero no tiene clienteID del servidor
                    } else {
                        for (contactoEntity in it.contactoEntities!!) {
                            contactoEntity?.clienteLocalID = clientDB?.id
                        }
                        it.id = clientDB?.id
                        it.sincronizado = syncFlag
                        FlowManager.getModelAdapter(ClienteEntity::class.java)
                            .save(it)
                        saveContacts(it.contactoEntities, it.id)
                        for (anotacionEntity in it.anotacionEntities!!) {
                            saveAnotation(anotacionEntity, clientDB?.id, syncFlag)
                        }
                        saveReminders(it.recordatorioEntities, it.id, syncFlag)
                    }
                    saveNotificationClientByIDAndYear(it.id, year)
                } else {
                    it.sincronizado = syncFlag
                    val id = FlowManager.getModelAdapter(ClienteEntity::class.java).insert(it).toInt()
                    it.contactoEntities?.forEach { contactoEntity ->
                        contactoEntity?.let {
                            contactoEntity.clienteLocalID = id
                            contactoEntity.clienteID = it.clienteID
                            FlowManager.getModelAdapter(ContactoEntity::class.java)
                                .save(contactoEntity)
                        }
                    }

                    it.anotacionEntities?.forEach { anotacionEntity ->
                        anotacionEntity?.let {
                            anotacionEntity.clienteLocalID = id
                            anotacionEntity.clienteID = it.clienteID
                            anotacionEntity.sincronizado = syncFlag
                            FlowManager.getModelAdapter(AnotacionEntity::class.java)
                                .save(anotacionEntity)
                        }
                    }

                    it.recordatorioEntities?.forEach { recordatorioEntity ->
                        recordatorioEntity?.let {
                            recordatorioEntity.clienteLocalID = id
                            recordatorioEntity.clienteID = it.clienteID
                            recordatorioEntity.sincronizado = syncFlag
                            FlowManager.getModelAdapter(RecordatorioEntity::class.java)
                                .save(recordatorioEntity)

                            if (findNotificacionRecordatorioByID(recordatorioEntity.id) == null) {
                                val notificacionRecordatorioEntity = NotificacionRecordatorioEntity()
                                notificacionRecordatorioEntity.recordatorioLocalID = recordatorioEntity.id
                                notificacionRecordatorioEntity.estado = 0
                                FlowManager.getModelAdapter(NotificacionRecordatorioEntity::class.java)
                                    .save(notificacionRecordatorioEntity)
                            }
                        }
                    }

                    saveNotificationClientByIDAndYear(id, year)
                }

                cel = null
                scel = ""
                telf = null
                stelf = ""
                clientDB = null
            }
        }
    }

    private fun saveNotificationClientByIDAndYear(id: Int?, year: Int?) {

        if (findNotificacionClienteByIDAndYear(id,
                year) == null) {
            val notificacionClienteEntity = NotificacionClienteEntity()
            notificacionClienteEntity.clienteLocalID = id
            notificacionClienteEntity.year = year
            notificacionClienteEntity.estado = 0
            FlowManager.getModelAdapter(NotificacionClienteEntity::class.java)
                .save(notificacionClienteEntity)
        }
    }

    private fun getTelephoneFromClienteEntity(clienteEntity: ClienteEntity?): ContactoEntity? {
        return clienteEntity?.contactoEntities?.firstOrNull { it?.tipoContactoID == 2 }
    }

    private fun getCellphoneFromClienteEntity(clienteEntity: ClienteEntity?): ContactoEntity? {
        return clienteEntity?.contactoEntities?.firstOrNull { it?.tipoContactoID == 1 }
    }

    private fun getClienteByClienteId(clienteID: Int?): ClienteEntity? {
        return clienteID?.let {
            (select
                from ClienteEntity::class
                where (ClienteEntity_Table.ClienteID eq clienteID)
                and (ClienteEntity_Table.Estado eq 1)
                ).result
        }
    }

    private fun getClienteById(id: Int?): ClienteEntity? {
        return id?.let {
            (select
                from ClienteEntity::class
                where (ClienteEntity_Table.ID eq id)
                ).result
        }
    }

    private fun getClienteByMobileOrPhone(mobile: String?, phone: String?): ClienteEntity? {
        var cel: ContactoEntity? = null
        var telf: ContactoEntity? = null

        if (mobile != "") cel = getContactNumber(mobile, 1)
        if (phone != "") telf = getContactNumber(phone, 2)

        return if (cel != null && telf != null) {
            getClienteById(cel.clienteLocalID)
        } else if (cel == null && telf != null) {
            getClienteById(telf.clienteLocalID)
        } else if (cel != null) {
            getClienteById(cel.clienteLocalID)
        } else {
            null
        }
    }

    private fun getClienteByMobileOrPhone(clientId: Int?, mobile: String?, phone: String?): ClienteEntity? {
        var cel: ContactoEntity? = null
        var telf: ContactoEntity? = null

        if (mobile != "") cel = getContactNumber(clientId, mobile, 1)
        if (phone != "") telf = getContactNumber(clientId, phone, 2)

        return if (cel != null && telf != null) {
            getClienteById(cel.clienteLocalID)
        } else if (cel == null && telf != null) {
            getClienteById(telf.clienteLocalID)
        } else if (cel != null) {
            getClienteById(cel.clienteLocalID)
        } else {
            null
        }
    }

    private fun getContactNumber(number: String?, type: Int?): ContactoEntity? {
        return number?.let {
            type?.let {
                (select
                    from ContactoEntity::class
                    where (ContactoEntity_Table.TipoContactoID eq type)
                    and (ContactoEntity_Table.Valor eq number)
                    ).result
            }
        }
    }

    private fun getContactNumber(clientId: Int?, number: String?, type: Int?): ContactoEntity? {
        return clientId?.let {
            number?.let {
                type?.let {
                    (select
                        from ContactoEntity::class
                        where (ContactoEntity_Table.TipoContactoID eq type)
                        and (ContactoEntity_Table.Valor eq number)
                        and (ContactoEntity_Table.ClienteLocalID notEq clientId)
                        ).result
                }
            }
        }
    }

    private fun getAnotacionByAnotacionID(anotacionID: Int?): AnotacionEntity? {
        return anotacionID?.let {
            (select
                from AnotacionEntity::class
                where (AnotacionEntity_Table.AnotacionID eq anotacionID)
                ).result
        }
    }

    private fun getMovementByMovementID(movementID: Int?): ClientMovementEntity? {
        return movementID?.let {
            (select
                from ClientMovementEntity::class
                where (ClientMovementEntity_Table.MovementID eq movementID)
                ).result
        }
    }

    private fun getAnotacionByID(id: Int?): AnotacionEntity? {
        return id?.let {
            (select
                from AnotacionEntity::class
                where (AnotacionEntity_Table.ID eq it)
                ).result
        }
    }

    private fun getMovementsSyncByClientId(clientLocalID: Int?): List<ClientMovementEntity?>? {
        return clientLocalID?.let {
            (select
                from ClientMovementEntity::class
                where (ClientMovementEntity_Table.ClienteLocalID eq it)
                and (ClientMovementEntity_Table.MovementID notEq 0)
                and (ClientMovementEntity_Table.Sincronizado eq 1)
                ).list
        }
    }

    private fun findNotificacionClienteByIDAndYear(clienteLocalID: Int?, year: Int?): NotificacionClienteEntity? {
        return clienteLocalID?.let {
            year?.let { it2 ->
                (select
                    from NotificacionClienteEntity::class
                    where (NotificacionClienteEntity_Table.ClienteLocalID eq it)
                    and (NotificacionClienteEntity_Table.Year eq it2)
                    ).result
            }
        }
    }

    private fun findNotificacionRecordatorioByID(id: Int?): Any? {
        return id?.let {
            (select
                from NotificacionRecordatorioEntity::class
                where (NotificacionRecordatorioEntity_Table.RecordatorioLocalID eq it)
                ).result
        }
    }


    private fun saveContacts(contactoEntities: List<ContactoEntity?>?, clienteLocalID: Int?) {
        clienteLocalID?.let {
            val contactosDB = (select
                from ContactoEntity::class
                where (ContactoEntity_Table.ClienteLocalID eq it)
                ).list
            FlowManager.getModelAdapter(ContactoEntity::class.java).deleteAll(contactosDB)
            contactoEntities?.let {
                FlowManager.getModelAdapter(ContactoEntity::class.java).insertAll(it)
            }
        }

    }

    private fun saveAnotation(anotacionEntity: AnotacionEntity?, clientId: Int?, sincronizado: Int?) {
        anotacionEntity?.let {
            var notaDB = getAnotacionByID(anotacionEntity.id)
            if (notaDB == null) {
                notaDB = getAnotacionByAnotacionID(it.anotacionID)
            }

            if (notaDB != null)
                anotacionEntity.id = notaDB.id

            it.clienteLocalID = clientId
            it.sincronizado = sincronizado
            FlowManager.getModelAdapter(AnotacionEntity::class.java)
                .save(it)
        }
    }

    private fun updateItemRecordatory(recordatoryEntity: RecordatorioEntity?, clientId: Int?, sincronizado: Int?): RecordatorioEntity?  {
        return recordatoryEntity?.let {
            var entityDB = (select
                from RecordatorioEntity::class
                where (RecordatorioEntity_Table.Id.eq(it.id))
                ).result

            if (null == entityDB) {
                entityDB = (select
                    from RecordatorioEntity::class
                    where (RecordatorioEntity_Table.RecordatorioId.eq(it.recordatorioId))
                    ).result
            }

            if (entityDB != null)
                recordatoryEntity.id = entityDB.id

            recordatoryEntity.clienteLocalID = clientId
            recordatoryEntity.sincronizado = sincronizado

            recordatoryEntity
        }
    }

    private fun saveReminders(list: List<RecordatorioEntity?>?, clientId: Int?, sincronizado: Int?) {

        list?.filterNotNull()?.forEach { recordatorioEntity ->
            recordatorioEntity.let {
                var entityDB = (select
                    from RecordatorioEntity::class
                    where (RecordatorioEntity_Table.Id.eq(it.id))
                    ).result

                if (null == entityDB)
                    entityDB = (select
                        from RecordatorioEntity::class
                        where (RecordatorioEntity_Table.RecordatorioId.eq(it.recordatorioId))
                        ).result

                if (entityDB != null)
                    recordatorioEntity.id = entityDB.id

                recordatorioEntity.clienteLocalID = clientId
                recordatorioEntity.sincronizado = sincronizado

                FlowManager.getModelAdapter(RecordatorioEntity::class.java)
                    .save(recordatorioEntity)

            }
        }
    }

    override fun getClientesByListIds(clientIds: List<Int>): Observable<List<ClienteEntity>>{
        return create { emitter ->
            try {

                /*
                val entities = (select
                    from ClienteEntity::class
                    where (ClienteEntity_Table.Estado eq 1)
                    and (ClienteEntity_Table.ClienteID.`in`(clientIds))
                    orderBy (OrderBy.fromProperty(ClienteEntity_Table.Nombres)
                    collate Collate.NOCASE).ascending()
                    ).list
                */

                val entities = ArrayList<ClienteEntity>()

                clientIds.forEach {
                    val entity = (select
                        from ClienteEntity::class
                        where (ClienteEntity_Table.Estado eq 1)
                        and (ClienteEntity_Table.ClienteID eq it)
                        ).querySingle()
                    if (entity != null) entities.add(entity)
                }

                emitter.onNext(entities)
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

}
