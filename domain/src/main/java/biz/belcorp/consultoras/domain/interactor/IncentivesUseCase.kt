package biz.belcorp.consultoras.domain.interactor

import javax.inject.Inject

import biz.belcorp.consultoras.domain.entity.Concurso
import biz.belcorp.consultoras.domain.entity.IncentivesRequest
import biz.belcorp.consultoras.domain.exception.ExpiredSessionException
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.repository.AuthRepository
import biz.belcorp.consultoras.domain.repository.IncentivosRepository
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
class IncentivesUseCase @Inject
constructor(threadExecutor: ThreadExecutor, postExecutionThread: PostExecutionThread,
            private val incentivosRepository: IncentivosRepository,
            private val authRepository: AuthRepository)
    : UseCase(threadExecutor, postExecutionThread) {

    private val offline: Observable<Collection<Concurso?>?>
        get() = this.incentivosRepository.incentives

    /**
     * Servicio de tipo GET que obtiene los incentivos por Campaña
     *
     * @return observer Objeto que se ejecutara en un hilo diferente al principal
     */
    fun getAndSave(observer: DisposableObserver<Collection<Concurso?>?>, params: IncentivesRequest) {
        execute(this.incentivosRepository.getIncentives(params)
                .onErrorResumeNext(askForExceptions(params)), observer)
    }

    /**
     * Servicio de tipo GET que obtiene el concurso por código
     *
     * @return observer Objeto que se ejecutara en un hilo diferente al principal
     */
    fun getContest(contestCode: String, observer: DisposableObserver<Concurso?>) {
        execute(this.incentivosRepository.getContest(contestCode), observer)
    }

    private fun askForExceptions(params: IncentivesRequest): Function<Throwable, Observable<Collection<Concurso?>?>> {
        return Function{ t ->
            when (t) {
                is NetworkErrorException -> offline
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap { this.incentivosRepository.getIncentives(params) }
                    .onErrorResumeNext(askForExceptions(params))
                else -> Observable.error(t)
            }
        }
    }
}
