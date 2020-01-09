package biz.belcorp.consultoras.data.repository

import biz.belcorp.consultoras.data.entity.*
import biz.belcorp.consultoras.data.mapper.*
import biz.belcorp.consultoras.data.repository.datasource.anotacion.AnotacionDataStoreFactory
import biz.belcorp.consultoras.data.repository.datasource.cliente.ClienteDataStoreFactory
import biz.belcorp.consultoras.data.repository.datasource.recordatory.RecordatoryDataStoreFactory
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.repository.ClienteRepository
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import java.math.BigDecimal
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Clase de Client encargada de obtener la data desde una fuente de datos (interna o externa)
 * dicha clase se instancaria una sola vez y se mantendra mientras la aplicacion este viva
 *
 * @version 1.0
 * @since 2017-05-03
 */
@Singleton
class ClienteDataRepository
/**
 * Constructor que se le injecta las dependencias que necesita
 *
 * @param clienteDataStoreFactory Clase encargada de obtener los datos
 * @param clienteEntityDataMapper Clase encargade de realizar el parseo de datos
 */
@Inject
internal constructor(private val clienteDataStoreFactory: ClienteDataStoreFactory,
                     private val clienteEntityDataMapper: ClienteEntityDataMapper,
                     private val clientMovementEntityDataMapper: ClientMovementEntityDataMapper,
                     private val anotacionDataStoreFactory: AnotacionDataStoreFactory,
                     private val anotacionEntityDataMapper: AnotacionEntityDataMapper,
                     private val recordatoryDataStoreFactory: RecordatoryDataStoreFactory,
                     private val recordatorioEntityDataMapper: RecordatorioEntityDataMapper,
                     private val notificacionClienteEntityDataMapper: NotificacionClienteEntityDataMapper,
                     private val notificacionRecordatorioEntityDataMapper: NotificacionRecordatorioEntityDataMapper
) : ClienteRepository {

    override val deudoresDB: Observable<Collection<Cliente?>?>
        get() {
            val deudores = clienteDataStoreFactory.createDB().deudoresDB
            return deudores.map { clienteEntityDataMapper.transform(it) }
        }

    override val clientes: Observable<Collection<Cliente?>?>
        get() {
            val clienteDataStore = clienteDataStoreFactory.createDB()
            return clienteDataStore.clientes.map { clienteEntityDataMapper.transform(it) }
        }

    override val notificacionesCliente: Observable<List<NotificacionCliente?>?>
        get() {
            val clienteDataStore = clienteDataStoreFactory.createDB()
            return clienteDataStore.notificacionesCliente
                .map { notificacionClienteEntityDataMapper.transform(it) }
        }

    override val notificationesRecordatorio: Observable<List<NotificacionRecordatorio?>?>
        get() {
            val clienteDataStore = clienteDataStoreFactory.createDB()
            return clienteDataStore.notificacionesRecordatorio
                .map { notificacionRecordatorioEntityDataMapper.transform(it) }
        }

    /**
     * Servicio de tipo POST que sube clientes a la nube
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun subida(clientes: List<Cliente?>?): Observable<List<Cliente?>?> {

        val c1 = clienteEntityDataMapper.transform(clientes)

        val localDataStore = clienteDataStoreFactory.createDB()
        val cloudDataStore = clienteDataStoreFactory.createCloudDataStore()

        return cloudDataStore.subida(c1, 1)
            .flatMap { c2 ->
                localDataStore.subida(clienteEntityDataMapper.transformLocal(c1, c2), 1)
                    .map { c3 -> clienteEntityDataMapper.transform(c3) }
            }
    }

    override fun guardar(clientes: List<Cliente?>?, sincronizado: Int?): Observable<List<Cliente?>?> {
        val clienteDataStore = clienteDataStoreFactory.createDB()
        return clienteDataStore.subida(clienteEntityDataMapper.transform(clientes), sincronizado)
            .map { clienteEntities -> clienteEntityDataMapper.transform(clienteEntities) }
    }

    override fun eliminar(clientes: List<Cliente?>?): Observable<String?> {
        return clienteDataStoreFactory.createDB().eliminar(clienteEntityDataMapper.transform(clientes))
    }

    /**
     * Servicio de tipo GET que obtiene a los clientes
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun bajada(clientId: Int?, campaingCode: String?): Observable<Collection<Cliente?>?> {

        val serviceStore = clienteDataStoreFactory.createCloudDataStore()
        val localStore = clienteDataStoreFactory.createDB()

        val observableClients = serviceStore.bajada(clientId, campaingCode)
            .onErrorReturn { throwable ->
                val list = ArrayList<ClienteEntity>()
                val entity = ClienteEntity()
                entity.error = throwable
                list.add(entity)
                list
            }

        val observableDeudores = serviceStore.deudoresWS
            .onErrorReturn { throwable ->
                val list = ArrayList<DeudorRequestEntity>()
                val entity = DeudorRequestEntity()
                entity.error = throwable
                list.add(entity)
                list
            }

        val zip = Observable.zip(
            observableClients
            , observableDeudores
            , BiFunction<List<ClienteEntity?>?, List<DeudorRequestEntity?>?, List<ClienteEntity?>?> { clienteEntities, deudorRequestEntities ->
            clienteEntityDataMapper.transformCombine(clienteEntities, deudorRequestEntities).orEmpty()
        }
        )


        return zip.flatMap { c ->
            if (!c.isEmpty() && null != c[0]?.error) {
                Observable.error(c[0]?.error)
            } else {
                localStore.subida(c, 1)
                    .map { t -> clienteEntityDataMapper.transform(t) }
            }
        }

    }

    override fun bajadaClientes(clientId: Int?, campaingCode: String?): Observable<List<Cliente?>?> {
        val serviceStore = clienteDataStoreFactory.createCloudDataStore()
        val localStore = clienteDataStoreFactory.createDB()

        return serviceStore.bajada(clientId, campaingCode).flatMap { c ->
            localStore.subida(c, 1).map { t -> clienteEntityDataMapper.transform(t) }
        }
    }

    override fun downloadAndSave(clientId: Int?, campaingCode: String?): Observable<Boolean?> {
        val serviceStore = clienteDataStoreFactory.createCloudDataStore()
        val localStore = clienteDataStoreFactory.createDB()

        val observableClients = serviceStore.bajada(clientId, campaingCode)
            .onErrorReturn { throwable ->
                val list = ArrayList<ClienteEntity>()
                val entity = ClienteEntity()
                entity.error = throwable
                list.add(entity)
                list
            }

        val observableDeudores = serviceStore.deudoresWS
            .onErrorReturn { throwable ->
                val list = ArrayList<DeudorRequestEntity>()
                val entity = DeudorRequestEntity()
                entity.error = throwable
                list.add(entity)
                list
            }

        val zip = Observable.zip(
            observableClients
            , observableDeudores
            , BiFunction<List<ClienteEntity?>?, List<DeudorRequestEntity?>?, List<ClienteEntity?>?> { clienteEntities, deudorRequestEntities ->
            clienteEntityDataMapper.transformCombine(clienteEntities, deudorRequestEntities).orEmpty()
        }
        )

        return zip.flatMap { c ->
            if (!c.isEmpty() && null != c[0]?.error) {
                Observable.error(c[0]?.error)
            } else {
                localStore.saveClients(c, 1)
            }
        }
    }

    override fun getAndSave(clientId: Int?, campaingCode: String?): Observable<List<Cliente?>?> {
        val serviceStore = clienteDataStoreFactory.createCloudDataStore()
        val localStore = clienteDataStoreFactory.createDB()

        return serviceStore.bajada(clientId, campaingCode)
            .flatMap { r1 ->
                localStore.subida(r1, 1)
                    .map { clienteEntities2 -> clienteEntityDataMapper.transform(clienteEntities2) }
            }
    }

    override fun getDeudoresWS(clientes: List<Cliente?>?): Observable<Collection<Cliente?>?> {
        val deudores = clienteDataStoreFactory.createCloudDataStore().deudoresWS
        return deudores.map {
            clienteEntityDataMapper.transformD(it)
        }.map {
            clienteEntityDataMapper.transform(clienteEntityDataMapper.transform(clientes), it)
        }
    }

    /**
     * Servicio local que obtiene a los clientes por sincronizar
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun existClientsToSync(): Observable<Boolean?> {
        val clientDataStore = clienteDataStoreFactory.createDB()
        return clientDataStore.existClientsToSync()
    }

    override fun getClientesByFavorito(favorito: Int?): Observable<Collection<Cliente?>?> {
        val clienteDataStore = clienteDataStoreFactory.createDB()
        return clienteDataStore.getClientesByFavorito(favorito).map { clienteEntityDataMapper.transform(it) }
    }

    override fun validateClient(client: Cliente?): Observable<Int?> {
        val clienteDataStore = clienteDataStoreFactory.createDB()
        return clienteDataStore.validateClient(clienteEntityDataMapper.transform(client))
    }

    override fun saveAnotacion(anotacion: Anotacion?): Observable<Anotacion?> {
        val anotacionDataStore = anotacionDataStoreFactory.createCloudDataStore()
        return anotacionDataStore.save(anotacionEntityDataMapper.transform(anotacion)).map { anotacionEntityDataMapper.transform(it) }
    }

    override fun saveAnotacionOnDB(anotacion: Anotacion?): Observable<Anotacion?> {
        val anotacionDataStore = anotacionDataStoreFactory.createDB()
        return anotacionDataStore.save(anotacionEntityDataMapper.transform(anotacion)).map { anotacionEntityDataMapper.transform(it) }
    }

    override fun updateAnotacion(anotacion: Anotacion?): Observable<Boolean?> {
        val anotacionDataStore = anotacionDataStoreFactory.createCloudDataStore()
        return anotacionDataStore.update(anotacionEntityDataMapper.transform(anotacion))
    }

    override fun saveRecordatory(recordatorio: Recordatorio?): Observable<Recordatorio?> {
        val wsDataStore = recordatoryDataStoreFactory.createCloudDataStore()
        val localDataStore = recordatoryDataStoreFactory.createDBDataStore()
        return wsDataStore.saveRecordatory(recordatorioEntityDataMapper.transform(recordatorio))
            .flatMap { r ->
                localDataStore.saveRecordatory(r).map { recordatorioEntityDataMapper.transform(it) } }
    }

    override fun saveRecordatoryOffline(recordatorio: Recordatorio?): Observable<Recordatorio?> {
        val recordatoryLocalDataStore = recordatoryDataStoreFactory.createDBDataStore()
        return recordatoryLocalDataStore.saveRecordatory(recordatorioEntityDataMapper.transform(recordatorio))
            .map { recordatorioEntityDataMapper.transform(it) }
    }

    override fun updateRecordatory(recordatorio: Recordatorio?): Observable<Boolean?> {
        val recordatoryDataStore = recordatoryDataStoreFactory.createCloudDataStore()
        val recordatoryLocalDataStore = recordatoryDataStoreFactory.createDBDataStore()
        return recordatoryDataStore.updateRecordatory(recordatorioEntityDataMapper.transform(recordatorio))
            .flatMap { recordatoryLocalDataStore.updateRecordatory(recordatorioEntityDataMapper.transform(recordatorio)) }
    }

    override fun updateRecordatoryOffline(recordatorio: Recordatorio?): Observable<Boolean?> {
        val recordatoryLocalDataStore = recordatoryDataStoreFactory.createDBDataStore()
        return recordatoryLocalDataStore.updateRecordatory(recordatorioEntityDataMapper.transform(recordatorio))
    }

    override fun deleteRecordatory(recordatorioId: Int?, clienteId: Int?): Observable<Boolean?> {
        val wsDataStore = recordatoryDataStoreFactory.createCloudDataStore()
        val localDataStore = recordatoryDataStoreFactory.createDBDataStore()

        return localDataStore.getRecordatory(recordatorioId)
            .flatMap { r1 ->
                wsDataStore.deleteRecordatory(r1.recordatorioId, clienteId)
                    .flatMap { localDataStore.deleteRecordatory(recordatorioId, clienteId) }
            }
    }

    override fun deleteRecordatoryOffline(recordatorioId: Int?, clienteId: Int?): Observable<Boolean?> {
        val recordatoryLocalDataStore = recordatoryDataStoreFactory.createDBDataStore()
        return recordatoryLocalDataStore.getRecordatory(recordatorioId).map<RecordatorioEntity> { recordatorioEntity ->
            recordatorioEntity.estado = -1
            recordatorioEntity
        }.flatMap { recordatorioEntity ->
            if (recordatorioEntity.recordatorioId != null || recordatorioEntity.recordatorioId != 0) {
                updateRecordatoryOffline(recordatorioEntityDataMapper.transform(recordatorioEntity))
            } else {
                recordatoryLocalDataStore.deleteRecordatory(recordatorioId, clienteId)
            }
        }
    }

    override fun updateAnotacionOnDB(anotacion: Anotacion?): Observable<Boolean?> {
        val anotacionDataStore = anotacionDataStoreFactory.createDB()
        return anotacionDataStore.update(anotacionEntityDataMapper.transform(anotacion))
    }

    override fun deleteAnotacion(anotacion: Anotacion?): Observable<Boolean?> {
        val anotacionDataStore = anotacionDataStoreFactory.createCloudDataStore()
        return anotacionDataStore.delete(anotacionEntityDataMapper.transform(anotacion))
    }

    override fun deleteAnotacionOnDB(anotacion: Anotacion?): Observable<Boolean?> {
        val anotacionDataStore = anotacionDataStoreFactory.createDB()
        return anotacionDataStore.delete(anotacionEntityDataMapper.transform(anotacion))
    }

    override fun saveTotalDebt(clienteLocalID: Int?, totalDebt: BigDecimal?, flagOnline: Boolean?): Observable<Boolean?> {
        val clienteDataStore = clienteDataStoreFactory.createDB()
        return clienteDataStore.saveTotalDebt(clienteLocalID, totalDebt, flagOnline)
    }

    override fun findClienteById(id: Int?): Observable<Cliente?> {
        val clienteDataStore = clienteDataStoreFactory.createDB()
        return clienteDataStore.findClienteById(id).map { clienteEntityDataMapper.transform(it) }
    }

    override fun findClientByClientId(clienteId: Int?): Observable<Cliente?> {
        val clienteDataStore = clienteDataStoreFactory.createDB()
        return clienteDataStore.findClientByClientId(clienteId).map { clienteEntityDataMapper.transform(it) }
    }

    override fun getMovements(clientId: Int?): Observable<Collection<ClientMovement?>?> {
        val clienteDataStore = clienteDataStoreFactory.createCloudDataStore()
        return clienteDataStore.getMovements(clientId).map { clientMovementEntityDataMapper.transform(it) }
    }

    override fun getOfflineMovements(clientLocalID: Int?): Observable<Collection<ClientMovement?>?> {
        val clienteDataStore = clienteDataStoreFactory.createDB()
        return clienteDataStore.getMovements(clientLocalID).map { clientMovementEntityDataMapper.transform(it) }
    }

    override fun getMovementById(id: Int?): Observable<ClientMovement?> {
        val clienteDataStore = clienteDataStoreFactory.createDB()
        return clienteDataStore.getMovementByID(id).map { clientMovementEntityDataMapper.transform(it) }
    }

    override fun saveMovement(clientId: Int?, movement: ClientMovement?): Observable<ClientMovement?> {
        val clienteDataStore = clienteDataStoreFactory.createCloudDataStore()
        return clienteDataStore.saveMovement(clientId, clientMovementEntityDataMapper.transform(movement))
            .map { clientMovementEntityDataMapper.transform(it) }
    }

    override fun saveMovementOffline(movement: ClientMovement?): Observable<ClientMovement?> {
        val clienteDataStore = clienteDataStoreFactory.createDB()
        return clienteDataStore.saveMovement(0, clientMovementEntityDataMapper.transform(movement)).map { clientMovementEntityDataMapper.transform(it) }
    }

    override fun saveMovement(movement: ClientMovement?): Observable<ClientMovement?> {
        val clienteDataStore = clienteDataStoreFactory.createDB()
        return clienteDataStore.saveMovement(0, clientMovementEntityDataMapper.transform(movement)).map { clientMovementEntityDataMapper.transform(it) }
    }

    override fun updateMovement(movement: ClientMovement?): Observable<String?> {
        val clienteDataStore = clienteDataStoreFactory.createCloudDataStore()
        return clienteDataStore.updateMovement(clientMovementEntityDataMapper.transform(movement))
    }

    override fun deleteMovement(clienteMovement: ClientMovement?): Observable<Boolean?> {
        val clienteDataStore = clienteDataStoreFactory.createCloudDataStore()
        return clienteDataStore.deleteMovement(clientMovementEntityDataMapper.transform(clienteMovement))
    }

    override fun deleteMovementOffline(clienteMovement: ClientMovement?): Observable<BigDecimal?> {
        val clienteDataStore = clienteDataStoreFactory.createDB()
        return clienteDataStore.deleteMovementOffline(clientMovementEntityDataMapper.transform(clienteMovement))
    }

    override fun saveMovement(movements: List<ClientMovement?>?): Observable<String?> {
        val clienteDataStore = clienteDataStoreFactory.createDB()
        return clienteDataStore.saveMovement(clientMovementEntityDataMapper.transform(movements))
    }

    /**
     * Servicio local que consulta si existen anotaciones por sincronizar
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun existAnotationsToSync(): Observable<Boolean?> {
        val clientDataStore = clienteDataStoreFactory.createDB()
        return clientDataStore.existAnotationsToSync()
    }


    override fun updateFlagSyncro(clientId: Int?, flag: Int?): Observable<Boolean?> {
        val clientDataStore = clienteDataStoreFactory.createDB()
        return clientDataStore.updateSyncFlag(clientId, flag)
    }

    override fun updateProductos(clientMovement: ClientMovement?): Observable<Boolean?> {
        val clienteDataStore = clienteDataStoreFactory.createCloudDataStore()
        return clienteDataStore.updateProductos(clientMovementEntityDataMapper.transform(clientMovement))
    }

}
