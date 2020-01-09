package biz.belcorp.consultoras.domain.interactor


import biz.belcorp.consultoras.domain.entity.*
import javax.inject.Inject

import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.repository.*
import io.reactivex.Observable
import io.reactivex.functions.Function4
import io.reactivex.functions.Function5

class AuthExtUseCase @Inject
constructor(private val authRepository: AuthRepository,
            private val sessionRepository: SessionRepository,
            private val repository: AuthExtRepository,
            private val userRepository: UserRepository,
            threadExecutor: ThreadExecutor,
            postExecutionThread: PostExecutionThread) : UseCase(threadExecutor, postExecutionThread) {

    operator fun get(credentials: Credentials,observer: BaseObserver<Boolean>) {
        //execute( this.repository.get(credentials) , observer)
    }

    fun save(login: Login, session: Session, observer: BaseObserver<Boolean>) {
        execute(this.userRepository.save(login)
            .flatMap { r1 -> this.sessionRepository.register(session)
                .map { r2 -> r1 and r2 } },
            observer)
    }

    fun update(login: Login, observer: BaseObserver<Boolean?>) {
        execute(this.userRepository.save(login), observer)
    }

    fun refreshToken(observer: BaseObserver<Login?>) {
        execute(this.authRepository.refreshToken(), observer)
    }

}
