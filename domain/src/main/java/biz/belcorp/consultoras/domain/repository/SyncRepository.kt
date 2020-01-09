package biz.belcorp.consultoras.domain.repository

import biz.belcorp.consultoras.domain.entity.BasicDto
import io.reactivex.Observable

import biz.belcorp.consultoras.domain.entity.HybrisData
import biz.belcorp.consultoras.domain.entity.ResultadoPagoEnLinea

/**
 * Clase de Sync encargada de obtener la data a sincronizar desde la capa de datos
 * dicha clase se instancaria una sola vez y se mantendra mientras la aplicacion este viva
 *
 * @version 1.0
 * @since 2017-07-27
 */

interface SyncRepository {

    /**
     * Servicio de tipo POST que verifica los clientes a sincronizar y lo sube al servidor
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun syncClients(): Observable<Boolean?>

    /**
     * Servicio de tipo POST que verifica las anotaciones a sincronizar y lo sube al servidor
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun syncAnotations(): Observable<Boolean?>

    /**
     * Servicio que realiza lista las llamadas Hybris para Notificaciones.
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun getNotificationsHybris(): Observable<List<HybrisData?>?>

    /**
     * Servicio que actializa lista las llamadas Hybris para Notificaciones.
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun updateHybrisData(hybrisData: List<HybrisData?>?): Observable<Boolean?>


    fun syncPaymentLocalToService(): Observable<BasicDto<ResultadoPagoEnLinea>>

}
