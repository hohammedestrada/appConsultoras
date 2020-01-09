package biz.belcorp.consultoras.data.repository.datasource.notificacion

import biz.belcorp.consultoras.data.entity.NotificacionEntity
import biz.belcorp.consultoras.data.util.Constant
import io.reactivex.Observable

interface NotificacionDataStore {

    /**
     * Obtiene la lista de notificaciones por el id de consultora.
     */
    fun listByConsultoraId(consultoraId: String?, dias: Int) : Observable<List<NotificacionEntity?>?>

    /**
     * Obtiene una notificacion por id.
     */
    fun getByID(id: Int?): Observable<NotificacionEntity?>

    /**
     * Elimina una notificacion por id.
     */
    fun deleteByID(id: Int?) : Observable<Boolean?>

    /**
     * Actualiza una notificación.
     */
    fun save(notificacionEntity: NotificacionEntity): Observable<NotificacionEntity?>

    /**
     * Actualiza el estado de una notificación una notificationId.
     */
    fun updateEstadoByNotificationId(notificationId: Int, estado: Int): Observable<Boolean?>

    fun getCountNotificationsWithoutLookingObservable(): Observable<Long> {throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED) }

    suspend fun getCountNotificationsWithoutLooking(): Long {throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED) }
}
