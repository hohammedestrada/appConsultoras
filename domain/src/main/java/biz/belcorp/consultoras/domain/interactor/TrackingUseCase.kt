package biz.belcorp.consultoras.domain.interactor

import javax.inject.Inject

import biz.belcorp.consultoras.domain.entity.Tracking
import biz.belcorp.consultoras.domain.exception.ExpiredSessionException
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.repository.AuthRepository
import biz.belcorp.consultoras.domain.repository.TrackingRepository
import io.reactivex.Observable
import io.reactivex.functions.Function
import io.reactivex.observers.DisposableObserver

/**
 * Clase que define los casos de uso de un cliente
 * para cumplir lo descrito en la historia de usuario
 *
 * @version 1.0
 * @since 2017-05-03
 */
class TrackingUseCase @Inject
constructor(threadExecutor: ThreadExecutor, postExecutionThread: PostExecutionThread,
            private val trackingRepository: TrackingRepository,
            private val authRepository: AuthRepository)
    : UseCase(threadExecutor, postExecutionThread) {

    /**
     * Servicio de tipo GET que obtiene los incentivos por Campaña
     *
     * @return observer Objeto que se ejecutara en un hilo diferente al principal
     */
    operator fun get(top: Int?, observer: DisposableObserver<Collection<Tracking?>?>) {
        execute(this.trackingRepository.get(top).onErrorResumeNext(askForExceptions(top)), observer)
    }

    private fun askForExceptions(top: Int?): Function<Throwable, Observable<Collection<Tracking?>?>> {
        return Function{ t ->
            when (t) {
                is NetworkErrorException ->  this.trackingRepository.getFromLocal(top)
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap { this.trackingRepository.get(top) }
                    .onErrorResumeNext(askForExceptions(top))
                else -> Observable.error(t)
            }
        }
    }
}
