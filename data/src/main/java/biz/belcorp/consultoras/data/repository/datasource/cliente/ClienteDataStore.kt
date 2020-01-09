package biz.belcorp.consultoras.data.repository.datasource.cliente

import biz.belcorp.consultoras.data.entity.*
import java.math.BigDecimal

import io.reactivex.Observable

/**
 * Interface que implementa los metodos para obtener los datos de un cliente
 * tomando en cuenta la entidad que devuelve la respuesta del REST o el SQL
 *
 * @version 1.0
 * @since 2017-05-03
 */
interface ClienteDataStore {

    /**
     * obtiene a los clientes
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    val clientes: Observable<List<ClienteEntity?>?>

    /**
     * obtiene a los clientes deudores
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    val deudoresWS: Observable<List<DeudorRequestEntity?>?>

    /**
     * obtiene a los clientes deudores de la bd local
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    val deudoresDB: Observable<List<ClienteEntity?>?>

    val notificacionesCliente: Observable<List<NotificacionClienteEntity?>?>

    val notificacionesRecordatorio: Observable<List<NotificacionRecordatorioEntity?>?>

    /**
     * Servicio que sube
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun subida(clientes: List<ClienteEntity?>?, sincronizado: Int?): Observable<List<ClienteEntity?>?>

    /**
     * Servicio que sube
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun saveClients(clients: List<ClienteEntity?>?, syncFlag: Int?): Observable<Boolean?>

    /**
     * Servicio que elimina un cliente
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun eliminar(clienteEntityList: List<ClienteEntity?>?): Observable<String?>

    /**
     * Servicio de que obtiene a los clientes
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun bajada(clientId: Int?, campaingCode: String?): Observable<List<ClienteEntity?>?>

    /**
     * obtiene a los clientes favoritos
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun getClientesByFavorito(favorito: Int?): Observable<List<ClienteEntity?>?>

    /**
     * obtiene un cliente por id
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun findClienteById(clienteId: Int?): Observable<ClienteEntity?>

    /**
     * obtiene un cliente por id
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun findClientByClientId(clienteId: Int?): Observable<ClienteEntity?>

    /**
     * Valida el nombre del cliente
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun validateClient(client: ClienteEntity?): Observable<Int?>

    /**
     * obtiene a los clientes por sincronizar
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun existClientsToSync(): Observable<Boolean?>

    /**
     * Servicio que guarda un cliente
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun save(client: ClienteEntity?): Observable<String?>

    /**
     * obtiene los movimientos de un Cliente
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun getMovements(clientId: Int?): Observable<List<ClientMovementEntity?>?>

    /**
     * obtiene un movimiento
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun getMovementByID(id: Int?): Observable<ClientMovementEntity?>

    /**
     * Guarda un movimiento de un Cliente
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun saveMovement(clientId: Int?, movement: ClientMovementEntity?): Observable<ClientMovementEntity?>

    /**
     * Actualiza un movimiento de un Cliente
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun updateMovement(movement: ClientMovementEntity?): Observable<String?>

    /**
     * Elimina un movimiento de un Cliente
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun deleteMovement(movement: ClientMovementEntity?): Observable<Boolean?>

    fun deleteMovementOffline(movement: ClientMovementEntity?): Observable<BigDecimal?>

    /**
     * guarda un movimiento de un Cliente
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun saveMovement(movement: List<ClientMovementEntity?>?): Observable<String?>

    /**
     * guarda el total de la deuda de un cliente
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun saveTotalDebt(clienteLocalID: Int?, totalDebt: BigDecimal?, flagOnline: Boolean?): Observable<Boolean?>

    /**
     * consulta si existen anotaciones por sincronizar
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun existAnotationsToSync(): Observable<Boolean?>

    /**
     * Actualiza el flag de sincronizado
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun updateSyncFlag(clientId: Int?, flag: Int?): Observable<Boolean?>

    fun updateProductos(clientMovementEntity: ClientMovementEntity?): Observable<Boolean?>

    fun processBirthdaysNotifications(clienteEntities: List<ClienteEntity?>?): Observable<List<ClienteEntity?>?>

    fun processRecordatoryNotifications(clienteEntities: List<ClienteEntity?>?): Observable<List<ClienteEntity?>?>

    fun getClientesByListIds(clientIds: List<Int>): Observable<List<ClienteEntity>>

    fun delete(clienteID: Int?): Observable<Boolean?>

}
