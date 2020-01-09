package biz.belcorp.consultoras.domain.interactor

import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.entity.caminobrillante.LogroCaminoBrillante
import biz.belcorp.consultoras.domain.exception.ExpiredSessionException
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.repository.AccountRepository
import biz.belcorp.consultoras.domain.repository.AuthRepository
import biz.belcorp.consultoras.domain.repository.CountryRepository
import biz.belcorp.consultoras.domain.repository.UserRepository
import io.reactivex.Observable
import io.reactivex.functions.Function
import io.reactivex.observers.DisposableObserver
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import javax.inject.Inject

class AccountUseCase @Inject
constructor(private val accountRepository: AccountRepository
            , private val authRepository: AuthRepository
            , threadExecutor: ThreadExecutor
            , private val userRepository: UserRepository
            , private val countryRepository: CountryRepository
            , postExecutionThread: PostExecutionThread)
    : UseCase(threadExecutor, postExecutionThread) {


    /** Obtener solo data actualizada */

    fun getUpdatedData(observer: DisposableObserver<Login?>) {
        execute(this.accountRepository.getUpdatedData()
            .onErrorResumeNext(askForUpdatedDataExceptions()), observer)
    }

    private fun askForUpdatedDataExceptions(): Function<Throwable, Observable<Login?>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException ->
                    authRepository.refreshToken()
                else -> Observable.error(t)
            }
        }
    }

    suspend fun getCodePaisGanaMas(): String? {
        return accountRepository.getCodePaisGanaMas()
    }


    /** Obtener toda la data actualizada */

    fun refreshData(observer: DisposableObserver<Login?>) {
        execute(this.accountRepository.refreshData()
            .onErrorResumeNext(askForExceptions()), observer)
    }

    fun refreshData2(observer: DisposableObserver<Boolean?>, clearCache: Boolean) {
        if(clearCache) {
            accountRepository.clearCache()
        }
        execute(this.accountRepository.refreshData2(), observer)
    }

    private fun askForExceptions(): Function<Throwable, Observable<Login?>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException ->
                    authRepository.refreshToken()
                        .flatMap { accountRepository.getResumen(it) }
                else -> Observable.error(t)
            }
        }
    }

    /** */

    fun getLoginData(observer: DisposableObserver<Login?>) {
        execute(this.userRepository.getLogin()
            .onErrorResumeNext(askForExceptionsLoginData()), observer)
    }

    private fun askForExceptionsLoginData(): Function<Throwable, Observable<Login?>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException ->
                    authRepository.refreshToken()
                        .flatMap { accountRepository.getResumen(it) }
                is NetworkErrorException -> userRepository.getLogin()
                else -> Observable.error(t)
            }
        }
    }

    /** */

    fun recovery(recoveryRequest: RecoveryRequest, observer: DisposableObserver<String?>) {
        execute(this.accountRepository.recovery(recoveryRequest), observer)
    }

    fun associate(associateRequest: AssociateRequest, observer: BaseObserver<String?>) {
        execute(this.accountRepository.associate(associateRequest), observer)
    }

    /** */

    fun update(userUpdateRequest: UserUpdateRequest, observer: BaseObserver<Boolean?>) {
        execute(this.accountRepository.update(userUpdateRequest)
            .onErrorResumeNext(askForUpdateExceptions(userUpdateRequest)), observer)
    }

    private fun  askForUpdateExceptions(userUpdateRequest: UserUpdateRequest)
        : Function<Throwable, Observable<Boolean>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException ->
                    authRepository.refreshToken()
                        .flatMap { this.accountRepository.update(userUpdateRequest) }
                else -> Observable.error(t)
            }
        }
    }
    /** */

    fun updatePassword(passwordUpdateRequest: PasswordUpdateRequest, observer: BaseObserver<BasicDto<Boolean>?>) {
        execute(this.accountRepository.updatePassword(passwordUpdateRequest)
            .onErrorResumeNext(askForUpdatePasswordExceptions(passwordUpdateRequest)), observer)
    }

    private fun  askForUpdatePasswordExceptions(passwordUpdateRequest: PasswordUpdateRequest)
        : Function<Throwable, Observable<BasicDto<Boolean>>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException ->
                    authRepository.refreshToken()
                        .flatMap { this.accountRepository.updatePassword(passwordUpdateRequest) }
                else -> Observable.error(t)
            }
        }
    }

    /** */

    fun terms(terms: Boolean?, privacity: Boolean?, observer: BaseObserver<Boolean?>) {
        execute(this.accountRepository.terms(terms, privacity)
                .flatMap { userRepository.updateAcceptances(terms, privacity) }
            .onErrorResumeNext(askForTermsExceptions(terms, privacity)), observer)
    }


    private fun askForTermsExceptions(terms: Boolean?, privacity: Boolean?)
        : Function<Throwable, Observable<Boolean>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException ->
                    authRepository.refreshToken()
                        .flatMap { this.accountRepository.terms(terms, privacity)
                                .flatMap { _ -> userRepository.updateAcceptances(terms, privacity) } }
                else -> Observable.error(t)
            }
        }
    }

    fun getPdfTermsUrl(observer: BaseObserver<String>) {
        execute(this.userRepository.getWithObservable()
                .flatMap<Country> { user -> countryRepository.find(user.countryId) }
                .flatMap { country -> Observable.just<String>(country.urlTerminos) }, observer)
    }

    fun getPdfPrivacyUrl(observer: BaseObserver<String>) {
        execute(this.userRepository.getWithObservable()
                .flatMap<Country> { user -> countryRepository.find(user.countryId) }
                .flatMap { country -> Observable.just<String>(country.urlPrivacidad) }, observer)
    }

    fun getPdfCreditAgreementUrl(observer: BaseObserver<String>) {
        execute(this.userRepository.getWithObservable()
            .flatMap<Country> { user -> countryRepository.find(user.countryId) }
            .flatMap { country -> Observable.just<String>(country.urlContratoVinculacion) }, observer)
    }


    /** */

    fun uploadFile(contentType: String, requestFile: MultipartBody
                   , observer: BaseObserver<Boolean?>) {
        execute(this.accountRepository.uploadFile(contentType, requestFile)
            .onErrorResumeNext(askForUploadFileExceptions(contentType, requestFile)), observer)
    }

    private fun askForUploadFileExceptions(contentType: String, requestFile: MultipartBody)
        : Function<Throwable, Observable<Boolean>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException ->
                    authRepository.refreshToken()
                        .flatMap { this.accountRepository.uploadFile(contentType, requestFile) }
                else -> Observable.error(t)
            }
        }
    }

    /** */

    fun deletePhotoFB(userUpdateRequest: UserUpdateRequest, observer: BaseObserver<Boolean?>) {
        execute(this.accountRepository.deletePhotoFB(userUpdateRequest)
            .onErrorResumeNext(askForDeletePhotoFBExceptions(userUpdateRequest)), observer)
    }


    private fun askForDeletePhotoFBExceptions(userUpdateRequest: UserUpdateRequest)
        : Function<Throwable, Observable<Boolean>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap { this.accountRepository.deletePhotoFB(userUpdateRequest) }
                else -> Observable.error(t)
            }
        }
    }

    /** */

    fun deletePhotoServer(userUpdateRequest: UserUpdateRequest, observer: BaseObserver<Boolean?>) {
        execute(this.accountRepository.deletePhotoServer(userUpdateRequest)
            .onErrorResumeNext(askForDeletePhotoServerExceptions(userUpdateRequest)), observer)
    }

    private fun askForDeletePhotoServerExceptions(userUpdateRequest: UserUpdateRequest)
        : Function<Throwable, Observable<Boolean>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap { this.accountRepository.deletePhotoServer(userUpdateRequest) }
                else -> Observable.error(t)
            }
        }
    }

    fun enableSDK(observer: BaseObserver<Boolean?>){
        execute(this.accountRepository.enableSDK(), observer)
    }

    fun sendSMS(smsRequest: SMSResquest, observer: BaseObserver<BasicDto<Boolean>?>){
        execute(this.accountRepository.sendSMS(smsRequest), observer)
    }

    fun confirmSMSCode(smsRequest: SMSResquest, observer: BaseObserver<BasicDto<Boolean>?>){
        execute(this.accountRepository.confirmSMSCode(smsRequest), observer)
    }

    /** */

    fun enviarCorreo(correoNuevo: String, observer: BaseObserver<BasicDto<Boolean>?>){
        execute(this.accountRepository.enviarCorreo(correoNuevo)
            .onErrorResumeNext(askForEnviarCorreoExceptions(correoNuevo)), observer)
    }

    suspend fun enviarCorreoCoroutine(correoNuevo: String):BasicDto<Boolean>?{
        return this.accountRepository.enviarCorreoCoroutine(correoNuevo)
    }

        private fun askForEnviarCorreoExceptions(correoNuevo: String)
        : Function<Throwable, Observable<BasicDto<Boolean>?>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap { this.accountRepository.enviarCorreo(correoNuevo) }
                else -> Observable.error(t)
            }
        }
    }

    /** */

    fun contratoCredito(request: CreditAgreement, observer: BaseObserver<BasicDto<Boolean>?>){
        execute(this.accountRepository.contratoCredito(request)
            .onErrorResumeNext(askForContratoCreditoExceptions(request)), observer)
    }

    fun updateAcceptancesCreditAgreement(creditAgreement: Boolean, observer: BaseObserver<Boolean?>) {
        execute(userRepository.updateAcceptancesCreditAgreement(creditAgreement), observer)

    }

    fun getResumenConfig(request:ResumenRequest,observer: BaseObserver<BasicDto<Collection<ContenidoResumen>>>) {
        execute(this.accountRepository.getConfigResumen(request)
            .onErrorResumeNext(askResumenConfigException(request)), observer)
    }



    private fun askForContratoCreditoExceptions(request: CreditAgreement)
        : Function<Throwable, Observable<BasicDto<Boolean>?>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap { this.accountRepository.contratoCredito(request) }
                else -> Observable.error(t)
            }
        }
    }

    private fun askResumenConfigException(request: ResumenRequest)
        : Function<Throwable, Observable<BasicDto<Collection<ContenidoResumen>>>> {
        return Function { t ->

            when (t) {
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap { this.accountRepository.getConfigResumen(request) }

                else -> Observable.error(t)
            }

        }
    }

    /** */
    fun verificacion(observer: BaseObserver<Verificacion?>){
        execute(this.accountRepository.verificacion()
            .onErrorResumeNext(askForVerificacionExceptions()), observer)
    }

    fun verificacionOffline(observer: BaseObserver<Verificacion?>){
        execute(this.accountRepository.verificacionOffline(), observer)
    }

    private fun askForVerificacionExceptions()
        : Function<Throwable, Observable<Verificacion?>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap { this.accountRepository.verificacion() }
                else -> Observable.error(t)
            }
        }
    }

    fun getAdademyUrl(observer: BaseObserver<AcademyUrl?>) {
        val observable = accountRepository.getAcademyUrl()
        execute(observable.onErrorResumeNext(askForAcademyUrlException()), observer)
    }

    private fun askForAcademyUrlException(): Function<Throwable, Observable<AcademyUrl?>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap { accountRepository.getAcademyUrl() }
                else -> Observable.error(t)
            }
        }
    }

    /** Config user */

    fun getConfigActive(code: String, observer: BaseObserver<Boolean?>){
        execute(this.accountRepository.configResumeActive(code), observer)
    }


    fun getConfig(code: String, observer: BaseObserver<Collection<UserConfigData?>?>){
        execute(this.accountRepository.getConfigResume(code), observer)
    }

    fun getConfigActiveWithUpdate(code: String, observer: BaseObserver<Collection<UserConfigData?>?>) {
        val observable = accountRepository.updateResumenOnly()
            .flatMap { accountRepository.getConfigResume(code) }
        execute(observable, observer)
    }

    fun getUserResumen(login: Login?,observer: DisposableObserver<Login?>){
        execute(this.accountRepository.getResumen(login),observer)
    }

    /* Corutinas */
    suspend fun getConfig(code: String) : List<UserConfigData> {
        return this.accountRepository.getConfig(code)
    }

    suspend fun getConfigActiveCoroutine(code: String):Boolean?{
        return this.accountRepository.configResumeActiveCoroutine(code)
    }

    suspend fun getConfigByCodeId(codeId:String): UserConfigData? {
        return this.accountRepository.getConfigByCodeId(codeId)
    }

    suspend fun getResumenConfigCoroutine(request:ResumenRequest):BasicDto<Collection<ContenidoResumen>>{
        return this.accountRepository.getConfigResumenCoroutine(request)
    }

    suspend fun checkContenidoResumenIfExist(codeResumen: String, codeDetail: String): Boolean {
        return this.accountRepository.checkContenidoResumenIfExist(codeResumen,codeDetail)
    }

    fun getConfigAsObservable(code: String) : Observable<Collection<UserConfigData?>?> {
        return this.accountRepository.getConfigAsObservable(code)
    }

    suspend fun saveConfig(code1: String, code2: String, value1: String, value3: String) :Boolean{
        return this.accountRepository.saveConfig(code1, code2, value1, value3)
    }

    suspend fun getResumenConfig(request:ResumenRequest) : BasicDto<Collection<ContenidoResumen>>{
        return this.accountRepository.getContenidoResumenAsync(request)
    }
}
