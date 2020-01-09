package biz.belcorp.consultoras.domain.interactor

import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.exception.ExpiredSessionException
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.repository.AuthRepository
import biz.belcorp.consultoras.domain.repository.ClienteRepository
import biz.belcorp.consultoras.domain.repository.SessionRepository
import io.reactivex.Observable
import io.reactivex.functions.Function
import io.reactivex.observers.DisposableObserver
import java.math.BigDecimal
import javax.inject.Inject

/**
 * Clase que define los casos de uso de un cliente
 * para cumplir lo descrito en la historia de usuario
 *
 * @version 1.0
 * @since 2017-05-03
 */
class ClienteUseCase
/**
 * Constructor que se le injecta las dependencias que necesita
 *
 * @param clienteRepository   Repositorio que tiene acceso al origen de datos
 * @param threadExecutor      Ejecutor de un metodo
 * @param postExecutionThread Tipo de ejecucion en un hilo diferente
 * @param sessionRepository   Repositorio que tiene acceso a la sesion
 */
@Inject
constructor(private val clienteRepository: ClienteRepository
            , threadExecutor: ThreadExecutor
            , private val authRepository: AuthRepository
            , postExecutionThread: PostExecutionThread
            , private val sessionRepository: SessionRepository) : UseCase(threadExecutor, postExecutionThread) {

    /**
     * Sube los clientes online y offline
     */
    fun subida(clients: List<Cliente>, observer: DisposableObserver<List<Cliente?>?>) {
        //observable que se ejecuta x default al haber internet
        val observableOnline = this.clienteRepository.subida(clients)
            .flatMap { c -> this.sessionRepository.updateSchedule().map { c } }

        execute(observableOnline.onErrorResumeNext(askForSubidaExceptions(clients)), observer)
    }

    /**
     * Observable de refreshToken y reintento SUBIDA
     */
    private fun subidaWithRefreshToken(clientes: List<Cliente>): Observable<List<Cliente?>?> {
        val observable = authRepository.refreshToken()
            .flatMap {
                this.clienteRepository.subida(clientes)
                    .flatMap { c ->
                        this.sessionRepository.updateSchedule()
                            .map { _ -> c }
                    }
            }

        return observable.onErrorResumeNext(askForSubidaExceptions(clientes))
    }

    private fun askForSubidaExceptions(clients: List<Cliente>)
        : Function<Throwable, Observable<List<Cliente?>?>> {
        return Function { t ->
            when (t) {
                is NetworkErrorException -> clienteRepository.guardar(clients, 0)
                    .flatMap { c -> sessionRepository.updateSchedule().map { c } }
                is ExpiredSessionException -> subidaWithRefreshToken(clients)
                else -> Observable.error(t)
            }
        }
    }


    /**
     * Guarda los clientes en la bd local
     */
    fun guardar(clientes: List<Cliente>, observer: DisposableObserver<List<Cliente?>?>) {
        execute(clienteRepository.guardar(clientes, 0), observer)
    }

    /**
     * Descarga los clientes de internet
     */
    fun bajada(campaingCode: String, observer: DisposableObserver<Collection<Cliente?>?>) {
        val observableOnline = this.clienteRepository.bajada(0, campaingCode)
        execute(observableOnline.onErrorResumeNext(askForBajadaExceptions(campaingCode)), observer)
    }

    private fun askForBajadaExceptions(campaingCode: String)
        : Function<Throwable, Observable<Collection<Cliente?>?>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap { this.clienteRepository.bajada(0, campaingCode) }
                is NetworkErrorException -> clienteRepository.clientes
                else -> Observable.error(t)
            }
        }
    }

    /**
     * Descarga cliente desde de internet
     */
    operator fun get(clientId: Int, campaingCode: String, observer: DisposableObserver<List<Cliente?>?>) {
        val observableOnline = this.clienteRepository
            .getAndSave(clientId, campaingCode)
        execute(observableOnline
            .onErrorResumeNext(askForGetExceptions(clientId, campaingCode)), observer)
    }


    private fun askForGetExceptions(clientId: Int, campaingCode: String)
        : Function<Throwable, Observable<List<Cliente?>?>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap { this.clienteRepository.getAndSave(clientId, campaingCode) }
                else -> Observable.error(t)
            }
        }
    }

    /**
     * Elimina cliente por servicio
     */
    fun eliminar(clients: List<Cliente>, observer: DisposableObserver<String>) {

        //observable que se ejecuta x default al haber internet
        val observableOnline = this.clienteRepository.subida(clients)
            .flatMap { clienteRepository.eliminar(clients) }
            .flatMap { c -> sessionRepository.updateSchedule().map { c } }

        execute(observableOnline
            .onErrorResumeNext(askForDeleteExceptions(clients)), observer)
    }

    /**
     * Observable de refreshToken y reintento DELETE
     */
    private fun deleteWithRefreshToken(clients: List<Cliente>): Observable<String?> {
        val observable = authRepository.refreshToken()
            .flatMap {
                this.clienteRepository.subida(clients)
                    .flatMap { _ -> clienteRepository.eliminar(clients) }
                    .flatMap { c -> sessionRepository.updateSchedule().map { _ -> c } }
            }

        return observable.onErrorResumeNext(askForDeleteExceptions(clients))
    }

    /**
     * Si la red falla, se tiene que entrar a este observable
     * @param clients  Lista de clientes a eliminar
     * @return observable de clientes
     */
    private fun deleteOffline(clients: List<Cliente>): Observable<String?> {
        return Observable.fromIterable(clients).flatMap {
            for (cliente1 in clients) {
                cliente1.sincronizado = 0
            }
            clienteRepository.eliminar(clients)
                .flatMap { s1 -> sessionRepository.updateSchedule().map { _ -> s1 } }
        }
    }

    fun deleteOfflinePostulant(clients: List<Cliente>, observer: DisposableObserver<String?>) {
        execute(Observable.fromIterable(clients).flatMap {
            for (cliente1 in clients) {
                cliente1.sincronizado = 0
            }
            clienteRepository.eliminar(clients)
                .flatMap { s1 -> sessionRepository.updateSchedule().map { _ -> s1 } }
        }, observer)
    }

    private fun askForDeleteExceptions(clients: List<Cliente>): Function<Throwable, Observable<String?>> {
        return Function { t ->
            when (t) {
                is NetworkErrorException -> deleteOffline(clients)
                is ExpiredSessionException -> deleteWithRefreshToken(clients)
                else -> Observable.error(t)
            }
        }
    }

    /**
     * Obtiene a los clientes favoritos de la bd local
     * observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun getClientesFavoritos(observer: DisposableObserver<Collection<Cliente?>?>) {
        execute(this.clienteRepository.getClientesByFavorito(1), observer)
    }

    /**
     * Obtiene un cliente por id de la bd local
     * observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun findClienteById(id: Int, observer: DisposableObserver<Cliente?>) {
        execute(this.clienteRepository.findClienteById(id), observer)
    }

    /**
     * Obtiene a los clientes de la bd local
     * observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun getClientes(observer: DisposableObserver<Collection<Cliente?>?>) {
        execute(this.clienteRepository.clientes, observer)
    }

    /**
     * Valida un cliente antes de registrarlo
     * observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun validateClient(client: Cliente, observer: DisposableObserver<Int?>) {
        execute(this.clienteRepository.validateClient(client), observer)
    }

    /**
     * Servicio que guarda una anotación de un cliente
     * observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun saveAnotacion(anotacion: Anotacion, observer: DisposableObserver<Anotacion?>) {
        //observable de guardar anotacion online, se llama por defecto
        val observableOnline = clienteRepository.saveAnotacion(anotacion)
            .flatMap { clienteRepository.saveAnotacionOnDB(it) }

        if (anotacion.clienteID != 0) {
            execute(observableOnline
                .onErrorResumeNext(askForSaveAnotationExceptions(anotacion)), observer)
        } else {
            execute(saveAnotacionOffline(anotacion), observer)
        }
    }

    /**
     * Observable de refreshToken y reintento saveAnotacion
     */
    private fun saveAnotacionWithRefreshToken(anotacion: Anotacion): Observable<Anotacion?> {
        val observable = authRepository.refreshToken()
            .flatMap {
                clienteRepository.saveAnotacion(anotacion)
                    .flatMap { it1 ->clienteRepository.saveAnotacionOnDB(it1) }
            }

        return observable.onErrorResumeNext(askForSaveAnotationExceptions(anotacion))
    }

    /**
     * Si la red falla, se tiene que entrar a este observable
     * @param data  Nota a guardar
     * @return observable de clientes
     */
    private fun saveAnotacionOffline(data: Anotacion): Observable<Anotacion?> {
        return Observable.just(data).flatMap {
            it.sincronizado = 0
            clienteRepository.saveAnotacionOnDB(it)
        }
    }

    private fun askForSaveAnotationExceptions(anotacion: Anotacion): Function<Throwable, Observable<Anotacion?>> {
        return Function { t ->
            when (t) {
                is NetworkErrorException -> saveAnotacionOffline(anotacion)
                is ExpiredSessionException -> saveAnotacionWithRefreshToken(anotacion)
                else -> Observable.error<Anotacion>(t)
            }
        }
    }


    /**
     * Servicio que actualiza una anotación de un cliente
     * observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun updateAnotacion(anotacion: Anotacion, observer: DisposableObserver<Boolean?>) {
        val observableOnline = this.clienteRepository.updateAnotacion(anotacion)
            .flatMap { clienteRepository.updateAnotacionOnDB(anotacion) }

        if (anotacion.anotacionID != 0) {
            execute(observableOnline
                .onErrorResumeNext(askForUpdateAnotationExceptions(anotacion)), observer)
        } else {
            execute(updateAnotacionOffline(anotacion), observer)
        }
    }

    /**
     * Observable de refreshToken y reintento updateAnotacion
     */
    private fun updateAnotacionWithRefreshToken(anotacion: Anotacion): Observable<Boolean?> {
        val observable = authRepository.refreshToken()
            .flatMap {
                this.clienteRepository.updateAnotacion(anotacion)
                    .flatMap { _ -> clienteRepository.updateAnotacionOnDB(anotacion) }
            }

        return observable.onErrorResumeNext(askForUpdateAnotationExceptions(anotacion))
    }

    /**
     * Si la red falla, se tiene que entrar a este observable
     * @param anotacion  Nota a actualizar
     * @return observable de clientes
     */
    private fun updateAnotacionOffline(anotacion: Anotacion): Observable<Boolean?> {
        return Observable.just(anotacion).flatMap { anotacion1 ->
            anotacion1.sincronizado = 0
            clienteRepository.updateAnotacionOnDB(anotacion1)
        }
    }

    private fun askForUpdateAnotationExceptions(anotacion: Anotacion): Function<Throwable, Observable<Boolean?>> {
        return Function { t ->
            when (t) {
                is NetworkErrorException -> updateAnotacionOffline(anotacion)
                is ExpiredSessionException -> updateAnotacionWithRefreshToken(anotacion)
                else -> Observable.error<Boolean>(t)
            }
        }
    }

    /**
     * Servicio que elimina una anotación de un cliente
     * observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun deleteAnotacion(anotacion: Anotacion, observer: DisposableObserver<Boolean?>) {

        val observableOnline = this.clienteRepository.deleteAnotacion(anotacion)
            .flatMap { clienteRepository.deleteAnotacionOnDB(anotacion) }

        if (anotacion.anotacionID != 0) {
            execute(observableOnline
                .onErrorResumeNext(askForDelAnotationExceptions(anotacion)), observer)
        } else {
            execute(clienteRepository.deleteAnotacionOnDB(anotacion), observer)
        }
    }

    /**
     * Observable de refreshToken y reintento deleteAnotacion
     */
    private fun deleteAnotacionWithRefreshToken(anotacion: Anotacion): Observable<Boolean?> {
        val observable = authRepository.refreshToken()
            .flatMap {
                this.clienteRepository.deleteAnotacion(anotacion)
                    .flatMap { _ -> clienteRepository.deleteAnotacionOnDB(anotacion) }
            }

        return observable.onErrorResumeNext(askForDelAnotationExceptions(anotacion))
    }

    /**
     * Si la red falla, se tiene que entrar a este observable
     * @param anotacion  Nota a eliminar
     * @return observable de clientes
     */
    private fun delAnotacionOffline(anotacion: Anotacion): Observable<Boolean?> {
        return Observable.just(anotacion).flatMap {
            anotacion.sincronizado = 0
            clienteRepository.updateAnotacionOnDB(anotacion)
        }
    }

    private fun askForDelAnotationExceptions(anotacion: Anotacion): Function<Throwable, Observable<Boolean?>> {
        return Function { t ->
            when (t) {
                is NetworkErrorException -> delAnotacionOffline(anotacion)
                is ExpiredSessionException -> deleteAnotacionWithRefreshToken(anotacion)
                else -> Observable.error<Boolean>(t)
            }
        }
    }

    /**
     * Servicio que inserta un recordatorio para un cliente
     * observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun saveRecordatory(recordatorio: Recordatorio, observer: DisposableObserver<Recordatorio?>) {
        val observableService = this.clienteRepository.saveRecordatory(recordatorio)
        execute(observableService.onErrorResumeNext(askForSaveRecordatoryExceptions(recordatorio)), observer)
    }

    /**
     * Observable de refreshToken y reintento saveRecordatory
     */
    private fun saveRecordatoryWithRefreshToken(recordatorio: Recordatorio): Observable<Recordatorio?> {
        val observable = authRepository.refreshToken()
            .flatMap { this.clienteRepository.saveRecordatory(recordatorio) }
        return observable.onErrorResumeNext(askForSaveRecordatoryExceptions(recordatorio))
    }

    /**
     * Si la red falla, se tiene que entrar a este observable
     * @param recordatorio  Recordatorio a guardar
     * @return observable de clientes
     */
    private fun saveRecordatoryOffline(recordatorio: Recordatorio): Observable<Recordatorio?> {
        return clienteRepository.saveRecordatoryOffline(recordatorio)
            .flatMap { r1 ->
                clienteRepository.updateFlagSyncro(recordatorio.clienteLocalID, 0)
                    .map { r1 }
            }
    }

    fun saveRecordatoryOfflinePostulant(recordatorio: Recordatorio, observer: DisposableObserver<Recordatorio?>) {
        execute(clienteRepository.saveRecordatoryOffline(recordatorio)
            .flatMap { r1 ->
                clienteRepository.updateFlagSyncro(recordatorio.clienteLocalID, 0)
                    .map { r1 }
            }, observer)
    }

    private fun askForSaveRecordatoryExceptions(data: Recordatorio): Function<Throwable, Observable<Recordatorio?>> {
        return Function { t ->
            when (t) {
                is NetworkErrorException -> saveRecordatoryOffline(data)
                is ExpiredSessionException -> saveRecordatoryWithRefreshToken(data)
                else -> Observable.error<Recordatorio>(t)
            }
        }
    }

    /**
     * Servicio que actualiza un recordatorio de un cliente
     * observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun updateRecordatory(recordatorio: Recordatorio, observer: DisposableObserver<Boolean?>) {
        val observableService = this.clienteRepository.updateRecordatory(recordatorio)
        execute(observableService.onErrorResumeNext(askForUpdateRecordatoryExceptions(recordatorio)), observer)
    }

    /**
     * Observable de refreshToken y reintento saveRecordatory
     */
    private fun updateRecordatoryWithRefreshToken(recordatorio: Recordatorio): Observable<Boolean?> {
        val observable = authRepository.refreshToken()
            .flatMap { this.clienteRepository.updateRecordatory(recordatorio) }
        return observable.onErrorResumeNext(askForUpdateRecordatoryExceptions(recordatorio))
    }

    /**
     * Si la red falla, se tiene que entrar a este observable
     * @param recordatorio  Recordatorio a guardar
     * @return observable de clientes
     */
    private fun updateRecordatoryOffline(recordatorio: Recordatorio): Observable<Boolean?> {
        return this.clienteRepository.updateRecordatoryOffline(recordatorio)
            .flatMap { clienteRepository.updateFlagSyncro(recordatorio.clienteLocalID, 0) }
    }

    fun updateRecordatoryOfflinePostulant(recordatorio: Recordatorio, observer: DisposableObserver<Boolean?>) {
        execute(this.clienteRepository.updateRecordatoryOffline(recordatorio)
            .flatMap { clienteRepository.updateFlagSyncro(recordatorio.clienteLocalID, 0) }, observer)
    }

    private fun askForUpdateRecordatoryExceptions(data: Recordatorio): Function<Throwable, Observable<Boolean?>> {
        return Function { t ->
            when (t) {
                is NetworkErrorException -> updateRecordatoryOffline(data)
                is ExpiredSessionException -> updateRecordatoryWithRefreshToken(data)
                else -> Observable.error<Boolean>(t)
            }
        }
    }

    /**
     * Servicio que elimina un recordatorio de un cliente
     * observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun deleteRecordatory(recordatorioId: Int, clienteId: Int, clienteLocalID: Int, observer: DisposableObserver<Boolean?>) {
        val observableService = this.clienteRepository.deleteRecordatory(recordatorioId, clienteId)

        execute(observableService
            .onErrorResumeNext(
                askForDeleteRecordatoryExceptions(recordatorioId, clienteId, clienteLocalID)), observer)
    }

    /**
     * Observable de refreshToken y reintento saveRecordatory
     */
    private fun delRecordatoryWithRefreshToken(recordatorioId: Int, clienteId: Int,
                                               clienteLocalID: Int): Observable<Boolean?> {
        val observable = authRepository.refreshToken()
            .flatMap { this.clienteRepository.deleteRecordatory(recordatorioId, clienteId) }
        return observable.onErrorResumeNext(askForDeleteRecordatoryExceptions(recordatorioId, clienteId, clienteLocalID))
    }

    /**
     * Si la red falla, se tiene que entrar a este observable
     * @return observable de clientes
     */
    private fun delRecordatoryOffline(recordatorioId: Int, clienteId: Int, clienteLocalID: Int): Observable<Boolean?> {
        return this.clienteRepository.deleteRecordatoryOffline(recordatorioId, clienteId)
            .flatMap { clienteRepository.updateFlagSyncro(clienteLocalID, 0) }
    }

    private fun askForDeleteRecordatoryExceptions(recordatorioId: Int,
                                                  clienteId: Int,
                                                  clienteLocalID: Int)
        : Function<Throwable, Observable<Boolean?>> {
        return Function { t ->
            when (t) {
                is NetworkErrorException -> delRecordatoryOffline(recordatorioId, clienteId, clienteLocalID)
                is ExpiredSessionException -> delRecordatoryWithRefreshToken(recordatorioId, clienteId, clienteLocalID)
                else -> Observable.error<Boolean>(t)
            }
        }
    }

    /**
     * Obtiene a los clientes deudores de la bd local
     * observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun getDeudores(observer: DisposableObserver<Collection<Cliente?>?>) {
        execute(this.clienteRepository.deudoresDB, observer)
    }

    /**
     * Servicio que obtiene los movimientos de un cliente
     * observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun getMovementsByClient(observer: DisposableObserver<Collection<ClientMovement?>?>, clienteID: Int?, clienteLocalID: Int?) {
        val observableGetOnline = this.clienteRepository.getMovements(clienteID)
            .flatMap { clientMovements ->
                for (clientMovement in clientMovements.filterNotNull()) {
                    clientMovement.sincronizado = 1
                }
                clienteRepository.saveMovement(clientMovements as List<ClientMovement>)
                    .flatMap { clienteRepository.getOfflineMovements(clienteLocalID) }
            }

        execute(observableGetOnline
            .onErrorResumeNext(
                askForGetTransactionsClientExceptions(clienteID, clienteLocalID)), observer)
    }

    /**
     * Observable de refreshToken y reintento getMovementsByClient
     */
    private fun getTransactionsByClientWithRefreshToken(clienteID: Int?, clienteLocalID: Int?): Observable<Collection<ClientMovement?>?> {
        val observable = authRepository.refreshToken().flatMap {
            clienteRepository.getMovements(clienteID)
                .flatMap { clientMovements ->
                    clienteRepository.saveMovement(clientMovements as List<ClientMovement>)
                        .map { _ -> clientMovements }
                }
        }
        return observable.onErrorResumeNext(askForGetTransactionsClientExceptions(clienteID, clienteLocalID))
    }

    /**
     * Si la red falla, se tiene que entrar a este observable
     * @return observable de clientes
     */
    fun getTransactionsClientOffline(clienteLocalID: Int?)
        : Observable<Collection<ClientMovement?>?> {
        return this.clienteRepository.getOfflineMovements(clienteLocalID)
    }

    fun getTransactionsClientOfflinePostulant(observer: DisposableObserver<Collection<ClientMovement?>?>, clienteLocalID: Int?) {
        execute(this.clienteRepository.getOfflineMovements(clienteLocalID), observer)
    }

    private fun askForGetTransactionsClientExceptions(clienteID: Int?,
                                                      clienteLocalID: Int?)
        : Function<Throwable, Observable<Collection<ClientMovement?>?>> {
        return Function { t ->
            when (t) {
                is NetworkErrorException -> getTransactionsClientOffline(clienteLocalID)
                is ExpiredSessionException -> getTransactionsByClientWithRefreshToken(clienteID, clienteLocalID)
                else -> Observable.error(t)
            }
        }
    }

    /**
     * Obtiene los movimientos de un cliente de la bd local
     * observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun getMovementOffline(observer: DisposableObserver<ClientMovement?>, id: Int?) {
        execute(this.clienteRepository.getMovementById(id), observer)
    }

    /**
     * Guarda un movimiento de un cliente
     * observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun saveMovementByClient(observer: DisposableObserver<ClientMovement>, transaction: ClientMovement) {
        val observableSaveOnline = this.clienteRepository
            .saveMovement(transaction.clientID, transaction).flatMap { clientMovement ->
                clientMovement.sincronizado = 1
                clientMovement.estado = 0
                clientMovement.clienteLocalID = transaction.clienteLocalID
                clienteRepository.saveMovementOffline(clientMovement)
                    .flatMap { clientMovement1 ->
                        clienteRepository.saveTotalDebt(clientMovement1.clienteLocalID, clientMovement1.saldo!!, true)
                            .map { clientMovement }
                    }
            }

        execute(observableSaveOnline
            .onErrorResumeNext(askForSaveTransactionExceptions(transaction)), observer)
    }

    /**
     * Observable de refreshToken y reintento saveMovementByClient
     */
    private fun saveTransactionWithRefreshToken(transaction: ClientMovement): Observable<ClientMovement> {
        val observable = authRepository.refreshToken().flatMap {
            this.clienteRepository
                .saveMovement(transaction.clientID, transaction).flatMap { clientMovement ->
                    clientMovement.sincronizado = 1
                    clientMovement.clienteLocalID = transaction.clienteLocalID
                    clienteRepository.saveMovementOffline(clientMovement).flatMap { clientMovement1 ->
                        clienteRepository.saveTotalDebt(clientMovement1.clienteLocalID, clientMovement1.saldo!!, true)
                            .map { _ -> clientMovement }
                    }
                }
        }
        return observable.onErrorResumeNext(askForSaveTransactionExceptions(transaction))
    }

    /**
     * Si la red falla, se tiene que entrar a este observable
     * @return observable de clientes
     */
    private fun saveTransactionOffline(transaction: ClientMovement): Observable<ClientMovement> {
        return Observable.just(transaction)
            .flatMap { clientMovement ->
                clientMovement.sincronizado = 0
                if (clientMovement.type!!.startsWith("A")) {
                    clientMovement.amount = clientMovement.amount!!.negate()
                }
                clienteRepository.saveMovementOffline(clientMovement).flatMap { clientMovement1 ->
                    clienteRepository.saveTotalDebt(clientMovement1.clienteLocalID, clientMovement1.saldo!!, false)
                        .flatMap { clienteRepository.updateFlagSyncro(transaction.clienteLocalID, 0) }
                        .map { clientMovement1 }
                }
            }
    }

    fun saveTransactionOfflinePostulant(observer: DisposableObserver<ClientMovement>, transaction: ClientMovement) {

        execute(Observable.just(transaction)
            .flatMap { clientMovement ->
                clientMovement.sincronizado = 0
                if (clientMovement.type!!.startsWith("A")) {
                    clientMovement.amount = clientMovement.amount!!.negate()
                }
                clienteRepository.saveMovementOffline(clientMovement).flatMap { clientMovement1 ->
                    clienteRepository.saveTotalDebt(clientMovement1.clienteLocalID, clientMovement1.saldo!!, false)
                        .flatMap { clienteRepository.updateFlagSyncro(transaction.clienteLocalID, 0) }
                        .map { clientMovement1 }
                }
            }, observer)
    }

    private fun askForSaveTransactionExceptions(transaction: ClientMovement): Function<Throwable, Observable<ClientMovement>> {
        return Function { t ->
            when (t) {
                is NetworkErrorException -> saveTransactionOffline(transaction)
                is ExpiredSessionException -> saveTransactionWithRefreshToken(transaction)
                else -> Observable.error<ClientMovement>(t)
            }
        }
    }


    /**
     * Servicio que actualiza la nota de un movimiento de un cliente
     * observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun updateMovementNote(transaction: ClientMovement, observer: BaseObserver<ClientMovement>) {
        val observableUpdateOnline = this.clienteRepository.updateMovement(transaction)
            .flatMap { clienteRepository.saveMovement(transaction) }.flatMap { clientMovement1 ->
                clienteRepository.saveTotalDebt(clientMovement1.clienteLocalID, transaction.saldo!!, true)
                    .map { transaction }
            }

        execute(
            observableUpdateOnline
                .onErrorResumeNext(askForUpdateTransactionExceptions(transaction)), observer)
    }

    /**
     * Observable de refreshToken y reintento saveMovementByClient
     */
    private fun updateTransactionWithRefreshToken(transaction: ClientMovement): Observable<ClientMovement> {
        val observable = authRepository.refreshToken()
            .flatMap {
                this.clienteRepository.updateMovement(transaction)
                    .flatMap { _ -> clienteRepository.saveMovement(transaction) }.flatMap { clientMovement1 ->
                        clienteRepository.saveTotalDebt(clientMovement1.clienteLocalID, transaction.saldo!!, true)
                            .map { _ -> transaction }
                    }
            }
        return observable.onErrorResumeNext(askForUpdateTransactionExceptions(transaction))
    }

    /**
     * Si la red falla, se tiene que entrar a este observable
     * @return observable de clientes
     */
    private fun updateTransactionOffline(transaction: ClientMovement): Observable<ClientMovement> {
        return clienteRepository.saveMovementOffline(transaction)
            .flatMap { clientMovement1 ->
                transaction.sincronizado = 0
                clienteRepository.saveTotalDebt(clientMovement1.clienteLocalID, transaction.saldo!!, false)
                    .flatMap { clienteRepository.updateFlagSyncro(transaction.clienteLocalID, 0) }
                    .map { transaction }
            }
    }

    fun updateTransactionOfflinePostulant(transaction: ClientMovement, observer: DisposableObserver<ClientMovement>) {
        execute(clienteRepository.saveMovementOffline(transaction)
            .flatMap { clientMovement1 ->
                transaction.sincronizado = 0
                clienteRepository.saveTotalDebt(clientMovement1.clienteLocalID, transaction.saldo!!, false)
                    .flatMap { clienteRepository.updateFlagSyncro(transaction.clienteLocalID, 0) }
                    .map { transaction }
            }, observer)
    }

    private fun askForUpdateTransactionExceptions(transaction: ClientMovement): Function<Throwable, Observable<ClientMovement>> {
        return Function { t ->
            when (t) {
                is NetworkErrorException -> updateTransactionOffline(transaction)
                is ExpiredSessionException -> updateTransactionWithRefreshToken(transaction)
                else -> Observable.error<ClientMovement>(t)
            }
        }
    }


    fun deleteMovement(transaction: ClientMovement, observer: DisposableObserver<Boolean?>) {
        val observableDeleteOnline = clienteRepository.deleteMovement(transaction)
            .flatMap<BigDecimal> {
                transaction.sincronizado = 1
                clienteRepository.deleteMovementOffline(transaction)
            }.flatMap { saldoAnterior -> clienteRepository.saveTotalDebt(transaction.clienteLocalID, saldoAnterior.subtract(transaction.amount), true) }

        execute(observableDeleteOnline
            .onErrorResumeNext(askForDeleteTransactionExceptions(transaction)), observer)
    }

    /**
     * Observable de refreshToken y reintento deleteMovement
     */
    private fun deleteTransactionWithRefreshToken(transaction: ClientMovement): Observable<Boolean?> {
        val observable = authRepository.refreshToken()
            .flatMap {
                clienteRepository.deleteMovement(transaction)
                    .flatMap<BigDecimal> { _ ->
                        transaction.sincronizado = 1
                        clienteRepository.deleteMovementOffline(transaction)
                    }.flatMap { saldoAnterior ->
                        clienteRepository.saveTotalDebt(transaction.clienteLocalID,
                            saldoAnterior.subtract(transaction.amount), true) }
            }
        return observable.onErrorResumeNext(askForDeleteTransactionExceptions(transaction))
    }

    /**
     * Si la red falla, se tiene que entrar a este observable
     * @return observable de clientes
     */
    private fun deleteTransactionOffline(transaction: ClientMovement): Observable<Boolean?> {
        return Observable.just(transaction).flatMap {
            transaction.sincronizado = 0
            clienteRepository.deleteMovementOffline(transaction)
                .flatMap { saldoAnterior -> clienteRepository.saveTotalDebt(transaction.clienteLocalID, saldoAnterior.subtract(transaction.amount), false) }
                .flatMap { _ -> clienteRepository.updateFlagSyncro(transaction.clienteLocalID, 0) }
        }
    }

    private fun askForDeleteTransactionExceptions(transaction: ClientMovement): Function<Throwable, Observable<Boolean?>> {
        return Function { t ->
            when (t) {
                is NetworkErrorException -> deleteTransactionOffline(transaction)
                is ExpiredSessionException -> deleteTransactionWithRefreshToken(transaction)
                else -> Observable.error<Boolean>(t)
            }
        }
    }


    fun deleteMovementOffline(clienteMovement: ClientMovement, observer: DisposableObserver<Boolean?>) {
        execute(
            Observable.just(clienteMovement).flatMap {
                clienteMovement.sincronizado = 0
                clienteRepository.deleteMovementOffline(clienteMovement)
                    .flatMap { saldoAnterior -> clienteRepository.saveTotalDebt(clienteMovement.clienteLocalID, saldoAnterior.subtract(clienteMovement.amount), false) }
                    .flatMap { _ -> clienteRepository.updateFlagSyncro(clienteMovement.clienteLocalID, 0) }
            }, observer)
    }

    fun updateProductos(clientMovement: ClientMovement, observer: DisposableObserver<Boolean?>) {
        execute(clienteRepository.updateProductos(clientMovement)
            .onErrorResumeNext(askForUpdateProductosExceptions(clientMovement)), observer)
    }

    private fun updateProductosWithRefreshToken(data: ClientMovement): Observable<Boolean?> {
        val observable = authRepository.refreshToken()
            .flatMap { this.clienteRepository.updateProductos(data) }

        return observable.onErrorResumeNext(askForUpdateProductosExceptions(data))
    }

    private fun askForUpdateProductosExceptions(data: ClientMovement): Function<Throwable, Observable<Boolean?>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException -> updateProductosWithRefreshToken(data)
                else -> Observable.error<Boolean>(t)
            }
        }
    }

    fun getNotificacionesCliente(observer: DisposableObserver<List<NotificacionCliente?>?>) {
        execute(clienteRepository.notificacionesCliente, observer)
    }

    fun getNotificationesRecordatorio(observer: DisposableObserver<List<NotificacionRecordatorio?>?>) {
        execute(clienteRepository.notificationesRecordatorio, observer)
    }


}
