package biz.belcorp.consultoras.domain.interactor

import javax.inject.Inject

import biz.belcorp.consultoras.domain.entity.Notificacion
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.repository.NotificacionRepository
import io.reactivex.Observable
import io.reactivex.observers.DisposableObserver

/**
 * Clase que define los casos de uso de un nota
 * para cumplir lo descrito en la historia de usuario
 *
 * @version 1.0
 * @since 2017-05-03
 */
class NotificacionUseCase
@Inject
constructor(private val notificacionRepository: NotificacionRepository,
            threadExecutor: ThreadExecutor,
            postExecutionThread: PostExecutionThread)
    : UseCase(threadExecutor, postExecutionThread)
{

    /**
     * Obtiene la lista de notificaciones por el id de consultora.
     */
    fun listByConsultoraId(dias: Int, observer: DisposableObserver<List<Notificacion?>?>) {
        execute(notificacionRepository.listByConsultoraId(dias), observer)
    }

    /**
     * Elimina una notificacion por id.
     */
    fun deleteByID(id: Int?, observer: DisposableObserver<Boolean?>) {
        execute(notificacionRepository.deleteByID(id), observer)
    }

    /**
     * Guarda una notificación.
     */
    fun save(notificacion: Notificacion, notificacionStatus: Boolean, observer: DisposableObserver<Notificacion?>) {
        execute(notificacionRepository.save(notificacion, notificacionStatus), observer)
    }

    /**
     * Actualiza el estado de una notificación una notificationId.
     */
    fun updateEstadoByNotificationId(notificationId: Int, estado: Int, observer: DisposableObserver<Boolean?>) {
        execute(notificacionRepository.updateEstadoByNotificationId(notificationId, estado), observer)
    }

    fun getCountNotificationsWithoutLookingObservable( observer: DisposableObserver<Long>){
        execute(notificacionRepository.getCountNotLookingNotiObservable(),observer)
    }

    fun getCountNotificationsWithoutLooking(): Int{
        return this.getCountNotificationsWithoutLooking()
    }

}
