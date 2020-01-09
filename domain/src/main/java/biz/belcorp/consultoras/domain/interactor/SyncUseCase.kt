package biz.belcorp.consultoras.domain.interactor

import biz.belcorp.consultoras.domain.entity.HybrisData
import javax.inject.Inject

import biz.belcorp.consultoras.domain.exception.ExpiredSessionException
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.repository.AuthRepository
import biz.belcorp.consultoras.domain.repository.ClienteRepository
import biz.belcorp.consultoras.domain.repository.SessionRepository
import biz.belcorp.consultoras.domain.repository.SyncRepository
import io.reactivex.Observable
import io.reactivex.functions.Function
import io.reactivex.observers.DisposableObserver

class SyncUseCase @Inject
constructor(private val syncRepository: SyncRepository,
            private val clienteRepository: ClienteRepository,
            private val sessionRepository: SessionRepository,
            private val authRepository: AuthRepository,
            threadExecutor: ThreadExecutor, postExecutionThread: PostExecutionThread)
    : UseCase(threadExecutor, postExecutionThread) {

    /**
     * Metodo que obtiene el estado de la sincronización
     *
     * @param observer Observer en donde se obtendra la respuesta cuando se haya ejecutado el observable
     */
    fun checkStatusSync(observer: DisposableObserver<Boolean>) {
        execute(this.sessionRepository.checkStatusSync(), observer)
    }

    /**
     * Metodo que actualiza el estado de la sincronización
     *
     * @param observer Observer en donde se obtendra la respuesta cuando se haya ejecutado el observable
     */
    fun updateStatusSync(status: Boolean?, observer: DisposableObserver<Boolean>) {
        execute(this.sessionRepository.updateStatusSync(status), observer)
    }

    /**
     * Metodo que verifica si existen clientes a sincronizar
     *
     * @param observer Observer en donde se obtendra la respuesta cuando se haya ejecutado el observable
     */
    fun existsClientsToSync(observer: DisposableObserver<Boolean?>) {
        execute(this.clienteRepository.existClientsToSync(), observer)
    }

    /**
     * Metodo que sincroniza clientes
     *
     * @param observer Observer en donde se obtendra la respuesta cuando se haya ejecutado el observable
     */
    fun syncClientsData(observer: DisposableObserver<Boolean>) {
        val observableService = this.syncRepository.syncClients()
                .flatMap { sessionRepository.updateSchedule() }
        execute(observableService.onErrorResumeNext(askForExceptionsSyncClients()), observer)
    }

    /**
     * Metodo que verifica si existen clientes a sincronizar
     *
     * @param observer Observer en donde se obtendra la respuesta cuando se haya ejecutado el observable
     */
    fun existsAnotationsToSync(observer: DisposableObserver<Boolean?>) {
        execute(this.clienteRepository.existAnotationsToSync(), observer)
    }

    /**
     * Metodo que sincroniza las anotaciones de clientes
     *
     * @param observer Observer en donde se obtendra la respuesta cuando se haya ejecutado el observable
     */
    fun syncAnnotationsData(observer: DisposableObserver<Boolean?>) {
        execute(syncRepository.syncAnotations()
                .onErrorResumeNext(askForExceptionsSyncAnnotations()), observer)
    }

    /** */

    private fun syncClientsWithRefreshToken(): Observable<Boolean> {
        return authRepository.refreshToken()
                .flatMap {
                    this.syncRepository.syncClients()
                            .flatMap { _ -> sessionRepository.updateSchedule() }
                }
    }

    private fun syncAnnotationsWithRefreshToken(): Observable<Boolean> {
        return authRepository.refreshToken().flatMap { syncRepository.syncAnotations() }
    }

    private fun askForExceptionsSyncClients(): Function<Throwable, Observable<Boolean>> {
        return Function{ t ->
            when (t) {
                is ExpiredSessionException -> syncClientsWithRefreshToken()
                else -> Observable.error<Boolean>(t)
            }
        }
    }

    private fun askForExceptionsSyncAnnotations(): Function<Throwable, Observable<Boolean>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException -> this@SyncUseCase.syncAnnotationsWithRefreshToken()
                else -> Observable.error<Boolean>(t)
            }
        }
    }

    /**
     * Metodo que lista notificaciones hybris a sincronizar
     *
     * @param observer Observer en donde se obtendra la respuesta cuando se haya ejecutado el observable
     */
    fun getNotificationsHybris(observer: DisposableObserver<List<HybrisData?>?>) {
        execute(this.syncRepository.getNotificationsHybris(), observer)
    }

    /**
     * Metodo que actualiza la lista notificaciones hybris que fueron sincronizados
     *
     * @param observer Observer en donde se obtendra la respuesta cuando se haya ejecutado el observable
     */
    fun updateHybrisData(list: List<HybrisData>?, observer: DisposableObserver<Boolean?>) {
        execute(this.syncRepository.updateHybrisData(list), observer)
    }

    fun sincroPayment(){
        this.syncRepository.syncPaymentLocalToService()
    }
}
