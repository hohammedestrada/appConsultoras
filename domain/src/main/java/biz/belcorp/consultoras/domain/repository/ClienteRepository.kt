package biz.belcorp.consultoras.domain.repository

import biz.belcorp.consultoras.domain.entity.*
import java.math.BigDecimal

import io.reactivex.Observable

/**
 * Clase de Client encargada de obtener la data desde la capa de datos
 * dicha clase se instancaria una sola vez y se mantendra mientras la aplicacion este viva
 *
 * @version 1.0
 * @since 2017-05-03
 */

interface ClienteRepository {

    /**
     * obtiene a los clientes deudores desde la bd local
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    val deudoresDB: Observable<Collection<Cliente?>?>

    /**
     * obtiene a los clientes
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    val clientes: Observable<Collection<Cliente?>?>

    val notificacionesCliente: Observable<List<NotificacionCliente?>?>

    val notificationesRecordatorio: Observable<List<NotificacionRecordatorio?>?>

    /**
     * Servicio de tipo POST que sube clientes al servidor
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun subida(clientes: List<Cliente?>?): Observable<List<Cliente?>?>

    /**
     * Guarda los clientes en la bd local si no hay internet.
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */

    fun guardar(clientes: List<Cliente?>?, sincronizado: Int?): Observable<List<Cliente?>?>

    /**
     * Elimina clientes en el servidor o en la bd local si no hay internet.
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun eliminar(clientes: List<Cliente?>?): Observable<String?>

    /**
     * obtiene a los clientes de internet o de la bd si no hay internet
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun bajada(clientId: Int?, campaingCode: String?): Observable<Collection<Cliente?>?>

    /**
     * obtiene a los clientes de internet o de la bd si no hay internet
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun bajadaClientes(clientId: Int?, campaingCode: String?): Observable<List<Cliente?>?>

    /**
     * obtiene a los clientes de internet y los almazcena en la bd
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun downloadAndSave(clientId: Int?, campaingCode: String?): Observable<Boolean?>

    /**
     * obtiene a los clientes de internet o de la bd si no hay internet
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun getAndSave(clientId: Int?, campaingCode: String?): Observable<List<Cliente?>?>

    /**
     * obtiene a los clientes deudores desde el servidor
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun getDeudoresWS(clientes: List<Cliente?>?): Observable<Collection<Cliente?>?>

    /**
     * consulta si existen clientes por sincronizar desde la bd local
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun existClientsToSync(): Observable<Boolean?>

    /**
     * obtiene a los clientes favoritos
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun getClientesByFavorito(favorito: Int?): Observable<Collection<Cliente?>?>

    /**
     * obtiene un cliente por id
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun findClienteById(id: Int?): Observable<Cliente?>

    /**
     * obtiene un cliente por id
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun findClientByClientId(clienteId: Int?): Observable<Cliente?>

    /**
     * Valida el nombre del cliente
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun validateClient(client: Cliente?): Observable<Int?>

    /**
     * obtiene los movimientos de un Cliente
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun getMovements(clientId: Int?): Observable<Collection<ClientMovement?>?>

    fun getOfflineMovements(clientLocalID: Int?): Observable<Collection<ClientMovement?>?>

    /**
     * obtiene un movimiento
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun getMovementById(id: Int?): Observable<ClientMovement?>

    /**
     * guarda un movimiento de un Cliente
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun saveMovement(clientId: Int?, movement: ClientMovement?): Observable<ClientMovement?>

    fun saveMovementOffline(movement: ClientMovement?): Observable<ClientMovement?>

    fun saveMovement(movement: ClientMovement?): Observable<ClientMovement?>

    fun updateMovement(movement: ClientMovement?): Observable<String?>

    /**
     * guarda un movimiento de un Cliente
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun saveMovement(movements: List<ClientMovement?>?): Observable<String?>


    fun saveAnotacion(anotacion: Anotacion?): Observable<Anotacion?>

    fun saveAnotacionOnDB(anotacion: Anotacion?): Observable<Anotacion?>

    fun updateAnotacion(anotacion: Anotacion?): Observable<Boolean?>

    /**
     * Guarda el recordatorio del cliente
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun saveRecordatory(recordatorio: Recordatorio?): Observable<Recordatorio?>

    fun saveRecordatoryOffline(recordatorio: Recordatorio?): Observable<Recordatorio?>

    /**
     * Actualiza un recordatorio de un cliente
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun updateRecordatory(recordatorio: Recordatorio?): Observable<Boolean?>

    fun updateRecordatoryOffline(recordatorio: Recordatorio?): Observable<Boolean?>

    /**
     * Elimina el recordatorio de un cliente
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun deleteRecordatory(recordatorioId: Int?, clienteId: Int?): Observable<Boolean?>

    fun deleteRecordatoryOffline(recordatorioId: Int?, clienteId: Int?): Observable<Boolean?>

    fun updateAnotacionOnDB(anotacion: Anotacion?): Observable<Boolean?>

    fun deleteAnotacion(anotacion: Anotacion?): Observable<Boolean?>

    fun deleteAnotacionOnDB(anotacion: Anotacion?): Observable<Boolean?>

    fun saveTotalDebt(clienteLocalID: Int?, totalDebt: BigDecimal?, flagOnline: Boolean?): Observable<Boolean?>

    /**
     * consulta si existen anotaciones por sincronizar desde la bd local
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun existAnotationsToSync(): Observable<Boolean?>

    fun deleteMovement(clienteMovement: ClientMovement?): Observable<Boolean?>

    fun deleteMovementOffline(clienteMovement: ClientMovement?): Observable<BigDecimal?>

    /**
     * actualiza el flag sincronizar del cliente de la bd local
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun updateFlagSyncro(clientId: Int?, flag: Int?): Observable<Boolean?>

    fun updateProductos(clientMovement: ClientMovement?): Observable<Boolean?>

}
