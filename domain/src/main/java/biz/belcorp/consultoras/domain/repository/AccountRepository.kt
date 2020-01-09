package biz.belcorp.consultoras.domain.repository

import biz.belcorp.consultoras.domain.entity.*
import io.reactivex.Observable
import okhttp3.MultipartBody

interface AccountRepository {

    fun getUpdatedData(): Observable<Login?>

    suspend fun getCodePaisGanaMas(): String?

    fun refreshData(): Observable<Login?>

    fun getResumen(login: Login?): Observable<Login?>

    fun updateResumenOnly(): Observable<Boolean>

    fun recovery(recoveryRequest: RecoveryRequest?): Observable<String?>

    fun update(updateRequest: UserUpdateRequest?): Observable<Boolean?>

    fun updatePassword(updateRequest: PasswordUpdateRequest?): Observable<BasicDto<Boolean>?>

    fun deletePhotoFB(updateRequest: UserUpdateRequest?): Observable<Boolean?>

    fun deletePhotoServer(updateRequest: UserUpdateRequest?): Observable<Boolean?>

    fun associate(associateRequest: AssociateRequest?): Observable<String?>

    fun terms(terms: Boolean?, privacity: Boolean?): Observable<Boolean?>

    fun uploadFile(contentType: String?, requestFile: MultipartBody?): Observable<Boolean?>

    fun enableSDK(): Observable<Boolean?>

    fun sendSMS(smsRequest: SMSResquest): Observable<BasicDto<Boolean>?>

    fun confirmSMSCode(smsRequest: SMSResquest): Observable<BasicDto<Boolean>?>

    fun enviarCorreo(email: String): Observable<BasicDto<Boolean>?>

    suspend fun enviarCorreoCoroutine(email: String): BasicDto<Boolean>?

    fun contratoCredito(request: CreditAgreement): Observable<BasicDto<Boolean>?>

    fun verificacion(): Observable<Verificacion?>

    fun verificacionOffline(): Observable<Verificacion?>

    fun getAcademyUrl(): Observable<AcademyUrl?>

    /** Config user account */

    fun configResumeActive(code: String): Observable<Boolean?>

    suspend fun configResumeActiveCoroutine(code: String): Boolean?

    fun getConfigResume(code: String): Observable<Collection<UserConfigData?>?>

    fun getConfigResumen(request: ResumenRequest): Observable<BasicDto<Collection<ContenidoResumen>>>

    suspend fun getConfigResumenCoroutine(request: ResumenRequest): BasicDto<Collection<ContenidoResumen>>

    suspend fun  checkContenidoResumenIfExist(codeResumen: String, codeDetail: String): Boolean

    fun refreshData2(): Observable<Boolean?>

    fun clearCache()

    suspend fun getConfig(code: String): List<UserConfigData>

    suspend fun getConfigByCodeId(codeId:String): UserConfigData?

    fun getConfigAsObservable(code: String): Observable<Collection<UserConfigData?>?>

    suspend fun saveConfig(code1: String, code2: String, value1: String, value3: String) : Boolean

    suspend fun getContenidoResumenAsync(request: ResumenRequest): BasicDto<Collection<ContenidoResumen>>
}
