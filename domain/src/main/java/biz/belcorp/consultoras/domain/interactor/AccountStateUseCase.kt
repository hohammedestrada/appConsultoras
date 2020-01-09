package biz.belcorp.consultoras.domain.interactor

import javax.inject.Inject

import biz.belcorp.consultoras.domain.entity.AccountState
import biz.belcorp.consultoras.domain.exception.ExpiredSessionException
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.repository.AccountStateRepository
import biz.belcorp.consultoras.domain.repository.AuthRepository
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
class AccountStateUseCase @Inject
constructor(threadExecutor: ThreadExecutor, postExecutionThread: PostExecutionThread,
            private val accountStatusRepository: AccountStateRepository, private val authRepository: AuthRepository) : UseCase(threadExecutor, postExecutionThread) {

    /** */

    private val withRefreshToken: Observable<Collection<AccountState?>?>
        get() {
            val observable = authRepository.refreshToken()
                    .flatMap { this@AccountStateUseCase.accountStatusRepository.get() }
            return observable.onErrorResumeNext(askForExceptions())
        }

    private val offline: Observable<Collection<AccountState?>?>
        get() = this.accountStatusRepository.fromLocal

    /**
     * Servicio de tipo GET que obtiene los incentivos por Campaña
     *
     * @return observer Objeto que se ejecutara en un hilo diferente al principal
     */
    operator fun get(observer: DisposableObserver<Collection<AccountState?>?>) {
        execute(this.accountStatusRepository.get()
                .onErrorResumeNext(askForExceptions()), observer)
    }

    private fun askForExceptions(): Function<Throwable, Observable<Collection<AccountState?>?>> {
        return Function { t ->
            when (t) {
                is NetworkErrorException ->  this@AccountStateUseCase.offline
                is ExpiredSessionException ->  this@AccountStateUseCase.withRefreshToken
                else ->  Observable.error(t)
            }
        }
    }
}
