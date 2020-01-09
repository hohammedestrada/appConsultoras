package biz.belcorp.consultoras.data.repository.datasource.cliente

import biz.belcorp.consultoras.data.entity.*
import biz.belcorp.consultoras.data.net.service.IClienteService
import biz.belcorp.consultoras.data.util.Constant
import io.reactivex.Observable
import java.math.BigDecimal
import java.util.*

/**
 * Clase de Client encargada de obtener los datos desde un servicio
 *
 * @version 1.0
 * @since 2017-04-14
 */
internal class ClienteCloudDataStore
/**
 * Constructor que recibe el servicio ha ser ejecutado
 *
 * @param service Clase encargado de ejecutar un metodo del servicio
 */
(private val service: IClienteService) : ClienteDataStore {

    override val clientes: Observable<List<ClienteEntity?>?>
        get() = throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)

    override val deudoresWS: Observable<List<DeudorRequestEntity?>?>
        get() = service.deudores()

    override val deudoresDB: Observable<List<ClienteEntity?>?>
        get() = throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)

    override val notificacionesCliente: Observable<List<NotificacionClienteEntity?>?>
        get() = throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)

    override val notificacionesRecordatorio: Observable<List<NotificacionRecordatorioEntity?>?>
        get() = throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)

    /**
     * Servicio de tipo POST que sube los clientes
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun subida(clientes: List<ClienteEntity?>?, sincronizado: Int?): Observable<List<ClienteEntity?>?> {
        return service.subida(clientes)
    }

    override fun saveClients(clients: List<ClienteEntity?>?, syncFlag: Int?): Observable<Boolean?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun eliminar(clienteEntityList: List<ClienteEntity?>?): Observable<String?> {
        throw UnsupportedOperationException("Para usar el eliminar online, se tiene" + "que ussar la subida con el cliente en estado 0")
    }

    override fun delete(clienteID: Int?): Observable<Boolean?> {
        throw UnsupportedOperationException("Para usar el eliminar online, se tiene" + "que ussar la subida con el cliente en estado 0")
    }

    /**
     * Servicio de tipo GET que obtiene a los clientes
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun bajada(clientId: Int?, campaingCode: String?): Observable<List<ClienteEntity?>?> {
        return service.bajada(clientId, campaingCode)
    }

    override fun validateClient(client: ClienteEntity?): Observable<Int?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    /**
     * Servicio que obtiene a los clientes por sincronizar.
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun existClientsToSync(): Observable<Boolean?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun save(client: ClienteEntity?): Observable<String?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getClientesByFavorito(favorito: Int?): Observable<List<ClienteEntity?>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun findClienteById(clienteId: Int?): Observable<ClienteEntity?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun findClientByClientId(clienteId: Int?): Observable<ClienteEntity?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getMovements(clientId: Int?): Observable<List<ClientMovementEntity?>?> {
        return service.getMovements(clientId)
    }

    override fun getClientesByListIds(clientIds: List<Int>): Observable<List<ClienteEntity>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMovementByID(id: Int?): Observable<ClientMovementEntity?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun saveMovement(clientId: Int?, movement: ClientMovementEntity?): Observable<ClientMovementEntity?> {
        return service.saveMovement(clientId, movement)
    }

    override fun updateMovement(movement: ClientMovementEntity?): Observable<String?> {
        movement?.let {
            return service.updateMovement(movement.clientID, movement.movementID, movement)
        } ?: return Observable.error(NullPointerException(javaClass.canonicalName))
    }

    override fun deleteMovement(movement: ClientMovementEntity?): Observable<Boolean?> {
        movement?.let {
            return service.deleteMovement(movement.clientID, movement.movementID)
        } ?: return Observable.error(NullPointerException(javaClass.canonicalName))
    }

    override fun deleteMovementOffline(movement: ClientMovementEntity?): Observable<BigDecimal?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun saveMovement(movement: List<ClientMovementEntity?>?): Observable<String?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun saveTotalDebt(clienteLocalID: Int?, totalDebt: BigDecimal?, flagOnline: Boolean?): Observable<Boolean?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun existAnotationsToSync(): Observable<Boolean?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun updateSyncFlag(clientId: Int?, flag: Int?): Observable<Boolean?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun updateProductos(clientMovementEntity: ClientMovementEntity?): Observable<Boolean?> {
        var productoPedidoEntity: ProductoPedidoEntity
        val productoEntities = ArrayList<ProductoPedidoEntity>()

        clientMovementEntity?.let {
            it.productList?.forEach { productResponseEntity ->
                productoPedidoEntity = ProductoPedidoEntity()
                productoPedidoEntity.pedidoWebFacturadoID = productResponseEntity.productMovementID
                productoPedidoEntity.cantidad = productResponseEntity.quantity
                productoPedidoEntity.precioUnidad = BigDecimal.valueOf(productResponseEntity.price)
                productoEntities.add(productoPedidoEntity)
            }
            return service.updateProductos(productoEntities)
        } ?: throw  NullPointerException()

    }

    override fun processBirthdaysNotifications(clienteEntities: List<ClienteEntity?>?): Observable<List<ClienteEntity?>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun processRecordatoryNotifications(clienteEntities: List<ClienteEntity?>?): Observable<List<ClienteEntity?>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

}
