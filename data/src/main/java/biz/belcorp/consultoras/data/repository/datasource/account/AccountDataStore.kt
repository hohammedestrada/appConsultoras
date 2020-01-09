package biz.belcorp.consultoras.data.repository.datasource.account

import biz.belcorp.consultoras.data.entity.*
import biz.belcorp.consultoras.data.entity.contenidoresumen.ContenidoResumenEntity
import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.library.net.dto.ServiceDto
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody

interface AccountDataStore {

    fun refreshData(): Observable<LoginEntity?>

    fun userResume(campaign: String?, codeRegion: String?, codeZone: String?,
                   codeSection: String?, cargarCaminoBrillante: Boolean?, consecutivoNueva: Int?, consultoraNueva: Int?, countryMoneySymbol: String?, codigoPrograma: String?,
                   periodoCaminoBrillante: Int?, nivelCaminoBrillante: Int?, puntosAlcanzadosCaminoBrillante: Int?, versLogroCaminoBrillante: Int): Observable<UserResumeEntity?>

    suspend fun userResumeCoroutine(campaign: String?, codeRegion: String?, codeZone: String?,
                                    codeSection: String?, cargarCaminoBrillante: Boolean?, consecutivoNueva: Int?, consultoraNueva: Int?, countryMoneySymbol: String?, codigoPrograma: String?,
                                    periodoCaminoBrillante: Int?, nivelCaminoBrillante: Int?, puntosAlcanzadosCaminoBrillante: Int?, versLogroCaminoBrillante: Int): Deferred<UserResumeEntity?>

    fun recovery(recoveryRequest: RecoveryRequestEntity?): Observable<String?>

    fun update(updateRequest: UserUpdateRequestEntity?): Observable<String?>

    fun updatePassword(updateRequest: PasswordUpdateRequestEntity?): Observable<ServiceDto<Any>?>

    fun deletePhotoServer(updateRequest: UserUpdateRequestEntity?): Observable<Boolean?>

    fun update(userEntity: UserEntity?): Observable<Boolean?>

    fun updatePhoto(name: String?): Observable<Boolean?>

    fun associate(associateRequest: AssociateRequestEntity?): Observable<String?>

    fun terms(termsRequest: List<TermsRequestEntity?>): Observable<Boolean?>

    fun uploadFile(contentType: String?, requestFile: MultipartBody?)
        : Observable<UploadFileResponseEntity?>

    fun sendSMS(smsRequest: SMSRequestEntity?): Observable<ServiceDto<Any>?>

    fun confirmSMSCode(smsRequest: SMSRequestEntity?): Observable<ServiceDto<Any>?>

    fun enviarCorreo(enviarCorreoRequest: EnviarCorreoRequest?): Observable<ServiceDto<Any>?>

    suspend fun enviarCorreoCoroutine(enviarCorreoRequest: EnviarCorreoRequest?): Deferred<ServiceDto<Any>?>

    fun contratoCredito(entity: CreditAgreementRequestEntity?): Observable<ServiceDto<Any>?>

    fun verificacion(): Observable<VerificacionEntity?>

    fun saveVerificacion(verificacionEntity: VerificacionEntity?): Observable<VerificacionEntity?>

    fun getAcademyUrl(campaing: String?, email: String?, segmentoConstancia: String?, esLider: Int?,
                      nivelLider: Int?, campaniaInicioLider: String?, seccionGestionLider: String?)
        : Observable<AcademyUrlEntity?>


    /** Config Consultant Country */

    fun saveConfigResume(configResume: List<UserConfigResumeEntity>?): Observable<Boolean?>

    fun getCodigoPaisGanaMas(): String?

    fun configResumeActive(code: String): Observable<Boolean?>{
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }
    suspend fun configResumeActiveCoroutine(code: String): Boolean?{
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    fun getConfigResumeAsObservable(code: String): Observable<List<UserConfigDataEntity>?>

    suspend fun getConfigResumen(code: String): Deferred<List<UserConfigDataEntity>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    suspend fun getConfigResumenByCodeId(codeId:String): Deferred<UserConfigDataEntity?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    fun getConfigAsObservable(code: String): Observable<List<UserConfigDataEntity>?>

    fun getContenidoResumen(request: ResumenRequestEntity): Observable<ServiceDto<List<ContenidoResumenEntity?>>> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    suspend fun getContenidoResumenCoroutine(request: ResumenRequestEntity): Deferred<ServiceDto<List<ContenidoResumenEntity?>>> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    suspend fun saveConfig(code1: String, code2: String, value1: String, value3: String): Deferred<Boolean>

    suspend fun saveContenidoResumenCoroutine(contenido: List<ContenidoResumenEntity?>): Deferred<Boolean>

    suspend fun checkContenidoResumenIfExist(codeResumen: String, codeDetail: String): Deferred<Boolean>{
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    suspend fun getContenidoResumenAsync(request: ResumenRequestEntity): Deferred<ServiceDto<List<ContenidoResumenEntity?>>> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    fun clearCache()

}
