package biz.belcorp.consultoras.data.repository.datasource.sync

import biz.belcorp.consultoras.data.entity.*
import biz.belcorp.library.net.dto.ServiceDto
import io.reactivex.Observable

/**
 * Interface que implementa los metodos para sincronizar datos
 * tomando en cuenta la entidad que devuelve la respuesta del REST o el SQL
 *
 * @version 1.0
 * @since 2017-05-03
 */
interface SyncDataStore {

    /**
     * obtiene a los clientes por sincronizar
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    val clientsToSync: Observable<List<ClienteEntity?>?>

    /**
     * obtiene a los anotations por sincronizar
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    val anotationsToSync: Observable<List<AnotacionEntity?>?>


    /**
     * obtiene las notificaciones hybris por sincronizar
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    val notificationsHybrisToSync: Observable<List<HybrisDataEntity?>?>

    /**
     * Servicio que sincroniza clientes
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun syncClients(clients: List<ClienteEntity?>?): Observable<Collection<ClienteEntity?>?>

    /**
     * Servicio que sincroniza clientes
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun saveSyncClients(clients: List<ClienteEntity?>?): Observable<Boolean?>

    /**
     * Servicio que sincroniza las notas del cliente
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun syncAnotations(anotations: List<AnotacionEntity?>?): Observable<List<AnotacionEntity?>?>

    /**
     * Servicio que sincroniza clientes
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun saveSyncAnotations(notes: List<AnotacionEntity?>?): Observable<Boolean?>

    /**
     * Servicio que actualiza la data de Notificacion Hybris
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun updateHybrisData(list: List<HybrisDataEntity?>?): Observable<Boolean?>

    /**
     * Servicio que actualiza la data de Pago En Linea
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun syncPaymentLocalToService(visaLogPaymentEntity: VisaLogPaymentEntity): Observable<ServiceDto<ResultadoPagoEnLineaEntity>>

    /**
     * Servicio que actualiza la data de Pago En Linea
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun getPaymentLocal(): Observable<VisaLogPaymentEntity>
}
