package biz.belcorp.consultoras.data.repository

import biz.belcorp.consultoras.data.mapper.NotificacionEntityDataMapper
import biz.belcorp.consultoras.data.repository.datasource.notificacion.NotificacionDataStoreFactory
import biz.belcorp.consultoras.data.repository.datasource.session.SessionDataStoreFactory
import biz.belcorp.consultoras.data.repository.datasource.user.UserDataStoreFactory
import biz.belcorp.consultoras.domain.entity.Notificacion
import biz.belcorp.consultoras.domain.repository.NotificacionRepository
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificacionDataRepository
@Inject
internal constructor(private val factory: NotificacionDataStoreFactory,
                     private val userDataStoreFactory: UserDataStoreFactory,
                     private val sessionDataStoreFactory: SessionDataStoreFactory,
                     private val mapper: NotificacionEntityDataMapper)
    : NotificacionRepository {


    /**
     * Obtiene la lista de notificaciones por el id de consultora.
     */
    override fun listByConsultoraId(dias: Int): Observable<List<Notificacion?>?> {
        var notificacionDataStore = factory.create()
        var userDataStore = userDataStoreFactory.create()

        return userDataStore.getWithObservable().flatMap { user ->
            notificacionDataStore.listByConsultoraId(user.consultantId, dias).map {
                mapper.transformToList(it)
            }
        }
    }

    /**
     * Elimina una notificacion por id.
     */
    override fun deleteByID(id: Int?): Observable<Boolean?> {
        var notificacionDataStore = factory.create()
        return notificacionDataStore.deleteByID(id)
    }

    /**
     * Guarda una notificación.
     */
    override fun save(notificacion: Notificacion, notificacionStatus: Boolean): Observable<Notificacion?> {
        val userDataStore = userDataStoreFactory.createDB()
        var notificacionDataStore = factory.create()

        val sessionDataStore = sessionDataStoreFactory.createLocal()

        return userDataStore.getWithObservable().flatMap { user ->
            mapper.transform(notificacion)?.let { notificacionEntity ->
                notificacionEntity.consultoraId = user.consultantId
                sessionDataStore.updateNotificationStatus(notificacionStatus).flatMap {
                    notificacionDataStore.save(notificacionEntity).map {
                        mapper.transform(it)
                    }
                }
            }
        }
    }

    /**
     * Actualiza el estado de una notificación una notificationId.
     */
    override fun updateEstadoByNotificationId(notificationId: Int, estado: Int): Observable<Boolean?> {
        var notificacionDataStore = factory.create()
        return notificacionDataStore.updateEstadoByNotificationId(notificationId, estado)
    }

    override suspend fun getCountNotLookingNoti(): Long {
        return factory.createDB().getCountNotificationsWithoutLooking()
    }

    override fun getCountNotLookingNotiObservable(): Observable<Long> = factory.createDB().getCountNotificationsWithoutLookingObservable()

}
