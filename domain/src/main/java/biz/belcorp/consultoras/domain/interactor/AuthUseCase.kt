package biz.belcorp.consultoras.domain.interactor

import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.repository.AuthRepository
import biz.belcorp.consultoras.domain.repository.FacebookRepository
import biz.belcorp.consultoras.domain.repository.SessionRepository
import biz.belcorp.consultoras.domain.repository.UserRepository
import io.reactivex.Observable
import io.reactivex.functions.Function4
import io.reactivex.functions.Function5
import kotlinx.coroutines.Deferred
import javax.inject.Inject

class AuthUseCase @Inject
constructor(private val authRepository: AuthRepository,
            private val sessionRepository: SessionRepository,
            private val facebookRepository: FacebookRepository,
            private val userRepository: UserRepository,
            threadExecutor: ThreadExecutor,
            postExecutionThread: PostExecutionThread) : UseCase(threadExecutor, postExecutionThread) {

    fun data(observer: BaseObserver<Auth?>) {
        execute(Observable.zip(
                this.sessionRepository.isAuthenticated,
                this.sessionRepository.isTutorial,
                this.facebookRepository.get(),
                this.sessionRepository.hasUser(),
                Function4<Boolean?, Boolean?, FacebookProfile?, Boolean?, Auth?> {
                    a: Boolean, t: Boolean, f: FacebookProfile?, u: Boolean ->
                        Auth(a, t, f?.id != null, u)
                }),
                observer)
    }

    fun loginOnline(credentials: Credentials, observer: BaseObserver<Login?>) {
        execute(this.authRepository.loginOnline(credentials), observer)
    }

    fun loginOffline(credentials: Credentials, observer: BaseObserver<Boolean?>) {
        execute(this.authRepository.loginOffline(credentials), observer)
    }

    fun save(login: Login, session: Session, observer: BaseObserver<Boolean>) {
        execute(this.userRepository.save(login)
                .flatMap { r1 -> this.sessionRepository.register(session)
                    .map { r2 -> r1 and r2 } },
                observer)
    }

    fun save(login: Login, session: Session, facebookProfile: FacebookProfile, observer: BaseObserver<Boolean>) {
        execute(this.userRepository.save(login)
                .flatMap { r1 -> this.sessionRepository.register(session)
                    .flatMap { r2 -> this.facebookRepository.save(facebookProfile)
                        .map { r3 -> r1 and r2 and r3 } } },
                observer)
    }

    fun update(login: Login, observer: BaseObserver<Boolean?>) {
        execute(this.userRepository.save(login), observer)
    }

    fun logout(observer: BaseObserver<Boolean>) {
        execute(this.sessionRepository.logout(), observer)
    }

    fun logoutWithData(observer: BaseObserver<Auth?>) {
        execute(this.sessionRepository
                .logout()
                .flatMap {
                    Observable.zip(
                        this.sessionRepository.isAuthenticated,
                        this.sessionRepository.isTutorial,
                        this.facebookRepository.get(),
                        this.sessionRepository.hasUser(),
                        this.sessionRepository.updateSchedule(),
                        Function5<Boolean, Boolean, FacebookProfile?, Boolean?, Boolean, Auth?> {
                            a: Boolean, t: Boolean, f: FacebookProfile?, u: Boolean, _: Boolean?  ->
                            Auth(a , t, f?.id != null, u)
                        })
                }, observer)
    }

    fun checkSchedule(observer: BaseObserver<Boolean>) {
        execute(this.sessionRepository.checkSchedule(), observer)
    }

    fun refreshToken(observer: BaseObserver<Login?>) {
        execute(this.authRepository.refreshToken(), observer)
    }

    fun updateScheduler(updateObserver: BaseObserver<Boolean>) {
        execute(this.sessionRepository.updateSchedule(), updateObserver)
    }

    suspend fun getAnalyticsToken(grantType: String, clientId: String, clientSecret: String): AnalyticsToken? {
        return authRepository.getAnalyticsToken(grantType, clientId, clientSecret).await()
    }

}
