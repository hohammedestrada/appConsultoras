package biz.belcorp.consultoras.domain.repository

import biz.belcorp.consultoras.domain.entity.Notificacion
import io.reactivex.Observable

interface NotificacionRepository {

    /**
     * Obtiene la lista de notificaciones por el id de consultora.
     */
    fun listByConsultoraId(dias: Int) : Observable<List<Notificacion?>?>

    /**
     * Elimina una notificacion por id.
     */
    fun deleteByID(id: Int?) : Observable<Boolean?>

    /**
     * Actualiza una notificación por id.
     */
    fun save(notificacion: Notificacion, notificacionStatus: Boolean): Observable<Notificacion?>

    /**
     * Actualiza el estado de una notificación una notificationId.
     */
    fun updateEstadoByNotificationId(notificationId: Int, estado: Int): Observable<Boolean?>

    suspend fun getCountNotLookingNoti(): Long

    fun getCountNotLookingNotiObservable(): Observable<Long>

}
