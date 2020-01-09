package biz.belcorp.consultoras.domain.interactor

import biz.belcorp.consultoras.domain.entity.*
import javax.inject.Inject

import biz.belcorp.consultoras.domain.exception.ExpiredSessionException
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.repository.*
import biz.belcorp.consultoras.domain.util.IntrigueCode
import biz.belcorp.consultoras.domain.util.IntrigueType
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function

class UserUseCase @Inject
constructor(private val repository: UserRepository,
            private val authRepository: AuthRepository,
            private val menuRepository: MenuRepository,
            private val clienteRepository: ClienteRepository,
            private val sessionRepository: SessionRepository,
            private val apiRepository: ApiRepository,
            threadExecutor: ThreadExecutor, postExecutionThread: PostExecutionThread)
    : UseCase(threadExecutor, postExecutionThread) {

    operator fun get(observer: BaseObserver<User?>) {
        execute(this.repository.getWithObservable(), observer)
    }

    suspend fun getUser(): User? {
        return repository.getWithCoroutines()
    }

    suspend fun getMoverBarraNavegacion(): Boolean? {
        return sessionRepository.moverBarraNavegacion()
    }

    fun getLogin(observer: BaseObserver<Login?>) {
        execute(this.repository.getLogin(), observer)
    }

    fun checkGanaMasNativo( observer: BaseObserver<Boolean?>) {
        execute(this.repository.checkGanaMasNativo(), observer)
    }

    fun saveDevice(device: Device, observer: BaseObserver<Boolean?>) {
        execute(this.apiRepository.saveDevice(device), observer)
    }

    fun getDevice(observer: BaseObserver<Device?>) {
        execute(this.apiRepository.device, observer)
    }

    fun updateScheduler(updateObserver: BaseObserver<Boolean>) {
        execute(this.sessionRepository.updateSchedule(), updateObserver)
    }

    suspend fun updateScheduler2(): Boolean {
        return this.sessionRepository.updateSchedule2()
    }

    suspend fun createSearchProduct() : Boolean? {
        return this.sessionRepository.updateSchedule2()
    }

    fun showMaterialTapCliente(showMaterialObserver: BaseObserver<Boolean>) {
        execute(this.sessionRepository.showMaterialTapCliente(), showMaterialObserver)
    }

    fun updateMaterialTapCliente(updateMaterialObserver: BaseObserver<Boolean>) {
        execute(this.sessionRepository.updateMaterialTapCliente(), updateMaterialObserver)
    }

    fun showMaterialTapDeuda(showMaterialObserver: BaseObserver<Boolean>) {
        execute(this.sessionRepository.showMaterialTapDeuda(), showMaterialObserver)
    }

    fun updateMaterialTapDeuda(updateMaterialObserver: BaseObserver<Boolean>) {
        execute(this.sessionRepository.updateMaterialTapDeuda(), updateMaterialObserver)
    }

    fun checkBirthdayByYear(year: Int, observer: BaseObserver<Boolean>) {
        execute(this.sessionRepository.checkBirthdayByYear(year), observer)
    }

    fun updateBirthdayByYear(year: Int, observer: BaseObserver<Boolean>) {
        execute(this.sessionRepository.updateBirthdayByYear(year), observer)
    }

    fun checkAnniversaryByYear(year: Int, observer: BaseObserver<Boolean>) {
        execute(this.sessionRepository.checkAnniversaryByYear(year), observer)
    }

    fun updateAnniversaryByYear(year: Int, observer: BaseObserver<Boolean>) {
        execute(this.sessionRepository.updateAnniversaryByYear(year), observer)
    }

    fun checkChristmasByYear(year: Int, observer: BaseObserver<Boolean>) {
        execute(this.sessionRepository.checkChristmasByYear(year), observer)
    }

    fun updateChristmasByYear(year: Int, observer: BaseObserver<Boolean>) {
        execute(this.sessionRepository.updateChristmasByYear(year), observer)
    }

    fun checkNewYearByYear(year: Int, observer: BaseObserver<Boolean>) {
        execute(this.sessionRepository.checkNewYearByYear(year), observer)
    }

    fun updateNewYearByYear(year: Int, observer: BaseObserver<Boolean>) {
        execute(this.sessionRepository.updateNewYearByYear(year), observer)
    }

    fun checkConsultantDayByYear(year: Int, observer: BaseObserver<Boolean>) {
        execute(this.sessionRepository.checkConsultantDayByYear(year), observer)
    }

    fun updateConsultantDayByYear(year: Int, observer: BaseObserver<Boolean>) {
        execute(this.sessionRepository.updateConsultantDayByYear(year), observer)
    }

    fun checkPasoSextoPedido(observer: BaseObserver<Boolean>) {
        execute(this.sessionRepository.checkPasoSextoPedido(), observer)
    }

    fun checkBelcorpFifty(observer: BaseObserver<Boolean>) {
        execute(this.sessionRepository.checkBelcorpFifty(), observer)
    }

    fun checkPostulant(observer: BaseObserver<Boolean>) {
        execute(this.sessionRepository.checkPostulant(), observer)
    }

    fun checkNewConsultant(observer: BaseObserver<Boolean>) {
        execute(this.sessionRepository.checkNewConsultant(), observer)
    }

    fun updatePasoSextoPedido(observer: BaseObserver<Boolean>) {
        execute(this.sessionRepository.updatePasoSextoPedido(), observer)
    }

    fun updateBelcorpFifty(observer: BaseObserver<Boolean>) {
        execute(this.sessionRepository.updateBelcorpFifty(), observer)
    }

    fun updatePostulant(observer: BaseObserver<Boolean>) {
        execute(this.sessionRepository.updatePostulant(), observer)
    }

    fun updateNewConsultant(observer: BaseObserver<Boolean>) {
        execute(this.sessionRepository.updateNewConsultant(), observer)
    }

    fun getCupon(cuponObserver: BaseObserver<String>) {
        execute(this.sessionRepository.getCupon(), cuponObserver)
    }

    fun updateCupon(campaign: String, cuponObserver: BaseObserver<Boolean>) {
        execute(this.sessionRepository.updateCupon(campaign), cuponObserver)
    }

    fun saveHybrisData(hybrisData: HybrisData, observer: BaseObserver<Boolean?>) {
        execute(this.repository.saveHybrisData(hybrisData), observer)
    }

    fun checkStatusDatamiMessage(observer: BaseObserver<Boolean>) {
        execute(this.sessionRepository.checkDatamiMessage(), observer)
    }

    fun updateStatusDatamiMessage(observer: BaseObserver<Boolean>) {
        execute(this.sessionRepository.updateStatusDatamiMessage(), observer)
    }

    fun updateTipoIngreso(pushNotification: String, observer: BaseObserver<Boolean?>) {
        execute(this.repository.updateTipoIngreso(pushNotification), observer)
    }

    fun getAndSaveInfo(countryISO: String?, campaign: String?, revistaDigital: Int?,
                       observer: BaseObserver<Boolean?>) {

        val observableClients = this.clienteRepository.downloadAndSave(0, campaign)

        val observableMenu = this.menuRepository.get(countryISO, campaign, revistaDigital)

        val zipObservable = Observable.zip(
            observableMenu.onErrorResumeNext(askForMenuExceptions(countryISO, campaign, revistaDigital)),
            observableClients.onErrorResumeNext(askForClientsExceptions(campaign)),
            BiFunction<Boolean?, Boolean?, Boolean?> { t1, t2 ->  t1 and t2})

        execute(this.repository.removeAll()
            .flatMap { r1 -> zipObservable.map {
                r1 and it } },
            observer)

    }

    fun saveUsabilityConfig(config: String, observer: BaseObserver<Boolean>) {
        execute(this.sessionRepository.saveUsabilityConfig(config), observer)
    }

    fun getUsabilityConfig(observer: BaseObserver<String>) {
        execute(this.sessionRepository.getUsabilityConfig(), observer)
    }

    fun save(year: Int, observer: BaseObserver<Boolean>) {
        execute(this.sessionRepository.updateChristmasByYear(year), observer)
    }

    fun saveSearchPrompt(observer: BaseObserver<Boolean>) {
        execute(this.sessionRepository.saveSearchPrompt(), observer)
    }

    fun checkSearchPrompt(observer: BaseObserver<Boolean>) {
        execute(this.sessionRepository.checkSearchPrompt(), observer)
    }

    fun cleanData(observer: BaseObserver<Boolean>) {
        execute(this.sessionRepository.cleanData(), observer)
    }

    fun checkIntrigueStatus(userConfigData: Collection<UserConfigData>, campaign: String, observer: BaseObserver<IntrigueBody>) {
        execute(evaluateIntrigueObserver(userConfigData, campaign), observer)
    }

    fun checkRenewStatus(userConfigData: Collection<UserConfigData>, campaign: String, observer: BaseObserver<RenewBody>) {
        execute(evaluateRenewObserver(userConfigData, campaign), observer)
    }

    /** functions private */

    private fun askForMenuExceptions(countryISO: String?, campaign: String?, revistaDigital: Int?)
        : Function<Throwable, Observable<Boolean>> {
        return Function{ t ->
            when (t) {
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap {
                        this.menuRepository.get(countryISO, campaign, revistaDigital)
                    }
                else -> Observable.error(t)
            }
        }
    }

    private fun askForClientsExceptions(campaingCode: String?)
        : Function<Throwable, Observable<Boolean?>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap { this.clienteRepository.downloadAndSave(0, campaingCode) }
                else -> Observable.error(t)
            }
        }
    }

    private fun evaluateIntrigueObserver(userConfigData: Collection<UserConfigData>, campaign: String): Observable<IntrigueBody> {
        var isIntrigueShow = false
        var value : String? = ""
        userConfigData.forEach {
            if (it.code.equals(IntrigueType.ESIKA) &&
                ((it.value1).equals(IntrigueCode.ALL) || it.value1.equals(campaign))) {
                isIntrigueShow = true
                value = it.value2
                return@forEach
            }
        }
        return Observable.just(IntrigueBody(value, isIntrigueShow))
    }

    private fun evaluateRenewObserver(userConfigData: Collection<UserConfigData>, campaign: String): Observable<RenewBody> {
        var isRenewShow = false
        var image: String? = ""
        var imageLogo: String? = ""
        var message: String? = ""
        userConfigData.forEach {
            if (it.code.equals(IntrigueType.RENEW)) {
                isRenewShow = true
                imageLogo =  it.value1
                image = it.value2
                message = it.value3
                return@forEach
            }
        }
        return Observable.just(RenewBody(image,imageLogo, message, isRenewShow))
    }
}
