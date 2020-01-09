package biz.belcorp.consultoras.domain.interactor

import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.exception.ExpiredSessionException
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.repository.AuthRepository
import biz.belcorp.consultoras.domain.repository.ClienteRepository
import biz.belcorp.consultoras.domain.repository.DebtRepository
import biz.belcorp.consultoras.domain.repository.UserRepository
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DebtUseCase
/**
 * Constructor del caso de uso
 *
 * @param threadExecutor      Ejecutor de un metodo
 * @param postExecutionThread Tipo de ejecucion en un hilo diferente
 * @param debtRepository      Repositorio para Movimientos
 * @param userRepository      Repositorio para User
 * @param clienteRepository   Repositorio para clientes
 */
@Inject
constructor(threadExecutor: ThreadExecutor, postExecutionThread: PostExecutionThread,
            private val debtRepository: DebtRepository,
            private val userRepository: UserRepository,
            private val clienteRepository: ClienteRepository,
            private val authRepository: AuthRepository)
    : UseCase(threadExecutor, postExecutionThread) {

    fun updateNewDebt(debtRequest: DebtRequest, observer: DisposableObserver<Boolean?>) {
        val observableUpdateOnline = debtRepository.uploadDebt(debtRequest)
            .flatMap { clientMovement ->
                clientMovement.clienteLocalID = debtRequest.clienteLocalID
                clientMovement.sincronizado = 1
                clientMovement.estado = 0
                clientMovement.code = "0000"
                clienteRepository.saveMovementOffline(clientMovement)
            }
            .flatMap { clientMovement1 -> clienteRepository.saveTotalDebt(debtRequest.clienteLocalID,
                clientMovement1.saldo, true) }

        execute(observableUpdateOnline
            .onErrorResumeNext(askForExceptions(debtRequest)), observer)

    }

    fun getInfoForSharing(clientID: Int, observer: BaseObserver<ShareDebt>) {
        execute(Observable.zip<User, Cliente, ShareDebt>(userRepository.getWithObservable(),
            clienteRepository.findClientByClientId(clientID)
            , BiFunction { user, cliente -> ShareDebt(cliente, user) }).onErrorReturn { null }, observer)
    }

    /** */

    private fun updateNewDebtWithRefreshToken(debtRequest: DebtRequest): Observable<Boolean?> {
        val observable = authRepository.refreshToken().flatMap {
            debtRepository.uploadDebt(debtRequest)
                .flatMap { clientMovement ->
                    clientMovement.clienteLocalID = debtRequest.clienteLocalID
                    clientMovement.sincronizado = 1
                    clientMovement.code = "0000"
                    clienteRepository.saveMovementOffline(clientMovement)
                }
                .flatMap { clientMovement1 ->
                    clienteRepository.saveTotalDebt(debtRequest.clienteLocalID,
                        clientMovement1.saldo, true)
                }
        }

        return observable.onErrorResumeNext(askForExceptions(debtRequest))
    }

    private fun updateNewDebtOffline(debtRequest: DebtRequest): Observable<Boolean?> {

        return Observable.just(debtRequest)
            .flatMap {
                val clientMovement = ClientMovement()
                clientMovement.movementID = 0
                clientMovement.clienteLocalID = debtRequest.clienteLocalID
                clientMovement.clientID = debtRequest.clienteID
                clientMovement.note = debtRequest.nota
                clientMovement.amount = debtRequest.monto
                clientMovement.campaing = debtRequest.codigoCampania
                clientMovement.sincronizado = 0
                clientMovement.clientCode = debtRequest.clienteID
                clientMovement.date = debtRequest.fecha
                clientMovement.description = debtRequest.descripcion
                clientMovement.type = debtRequest.tipoMovimiento
                clientMovement.estado = debtRequest.estado
                clienteRepository.saveMovementOffline(clientMovement)
            }
            .flatMap { clientMovement ->
                clienteRepository.saveTotalDebt(debtRequest.clienteLocalID, null, false)
                    .flatMap { clienteRepository.updateFlagSyncro(clientMovement.clienteLocalID, 0) }
            }
    }

    fun updateNewDebtOfflinePostulant(debtRequest: DebtRequest, observer: DisposableObserver<Boolean?>) {

        execute(Observable.just(debtRequest)
            .flatMap {
                val clientMovement = ClientMovement()
                clientMovement.movementID = 0
                clientMovement.clienteLocalID = debtRequest.clienteLocalID
                clientMovement.clientID = debtRequest.clienteID
                clientMovement.note = debtRequest.nota
                clientMovement.amount = debtRequest.monto
                clientMovement.campaing = debtRequest.codigoCampania
                clientMovement.sincronizado = 0
                clientMovement.clientCode = debtRequest.clienteID
                clientMovement.date = debtRequest.fecha
                clientMovement.description = debtRequest.descripcion
                clientMovement.type = debtRequest.tipoMovimiento
                clientMovement.estado = debtRequest.estado
                clienteRepository.saveMovementOffline(clientMovement)
            }
            .flatMap { clientMovement ->
                clienteRepository.saveTotalDebt(debtRequest.clienteLocalID, null, false)
                    .flatMap { clienteRepository.updateFlagSyncro(clientMovement.clienteLocalID, 0) }
            }, observer)
    }

    private fun askForExceptions(debtRequest: DebtRequest): Function<Throwable, Observable<Boolean?>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException -> updateNewDebtWithRefreshToken(debtRequest)
                is NetworkErrorException -> updateNewDebtOffline(debtRequest)
                else -> Observable.error<Boolean>(t)
            }
        }
    }
}
