package biz.belcorp.consultoras.data.repository.datasource.account

import biz.belcorp.consultoras.data.entity.*
import biz.belcorp.consultoras.data.entity.contenidoresumen.ContenidoResumenEntity
import biz.belcorp.consultoras.data.net.service.IAccountService
import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.library.net.dto.ServiceDto
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import java.lang.Exception

/**
 * Clase de Login encargada de obtener los datos desde un servicio
 *
 * @version 1.0
 * @since 2017-04-14
 */
internal class AccountCloudDataStore
/**
 * Constructor que recibe el servicio ha ser ejecutado
 *
 * @param service Clase encargado de ejecutar un metodo del servicio
 */
(private val service: IAccountService) : AccountDataStore {



    override fun refreshData(): Observable<LoginEntity?> {
        return this.service.refreshData()
    }

    override fun userResume(campaign: String?, codeRegion: String?, codeZone: String?, codeSection: String?, cargarCaminoBrillante: Boolean?, consecutivoNueva: Int?, consultoraNueva: Int?, countryMoneySymbol: String?, codigoPrograma: String?, periodoCaminoBrillante: Int?, nivelCaminoBrillante: Int?, puntosAlcanzadosCaminoBrillante: Int?, versLogroCaminoBrillante: Int): Observable<UserResumeEntity?> {
        return this.service.userResume(campaign, codeRegion, codeZone, codeSection, cargarCaminoBrillante, consecutivoNueva, consultoraNueva, countryMoneySymbol, codigoPrograma, periodoCaminoBrillante, nivelCaminoBrillante, puntosAlcanzadosCaminoBrillante, versLogroCaminoBrillante)
    }


    override suspend fun userResumeCoroutine(campaign: String?, codeRegion: String?, codeZone: String?, codeSection: String?, cargarCaminoBrillante: Boolean?, consecutivoNueva: Int?, consultoraNueva: Int?, countryMoneySymbol: String?, codigoPrograma: String?, periodoCaminoBrillante: Int?, nivelCaminoBrillante: Int?, puntosAlcanzadosCaminoBrillante: Int?, versLogroCaminoBrillante: Int): Deferred<UserResumeEntity?> {
        return this.service.userResumenCoroutine(campaign, codeRegion, codeZone, codeSection, cargarCaminoBrillante, consecutivoNueva, consultoraNueva, countryMoneySymbol, codigoPrograma, periodoCaminoBrillante, nivelCaminoBrillante, puntosAlcanzadosCaminoBrillante, versLogroCaminoBrillante)
    }


    override fun recovery(recoveryRequest: RecoveryRequestEntity?): Observable<String?> {
        return this.service.recovery(recoveryRequest)
    }

    override fun update(updateRequest: UserUpdateRequestEntity?): Observable<String?> {
        return this.service.update(updateRequest)
    }

    override fun updatePassword(updateRequest: PasswordUpdateRequestEntity?): Observable<ServiceDto<Any>?> {
        return if (updateRequest?.anteriorContrasenia == null) {
            this.service.updatePassword(updateRequest!!.anteriorContrasenia, updateRequest.nuevaContrasenia)
        } else {
            this.service.updatePassword(PasswordUpdate2RequestEntity(updateRequest.nuevaContrasenia))
        }

    }

    override fun deletePhotoServer(updateRequest: UserUpdateRequestEntity?): Observable<Boolean?> {
        return this.service.deletePhotoServer(updateRequest)
    }

    override fun update(userEntity: UserEntity?): Observable<Boolean?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getConfigAsObservable(code: String): Observable<List<UserConfigDataEntity>?> {
        throw java.lang.UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun updatePhoto(name: String?): Observable<Boolean?> {
        return this.service.updatePhoto(name)
    }

    override fun associate(associateRequest: AssociateRequestEntity?): Observable<String?> {
        return this.service.associate(associateRequest)
    }

    override fun terms(termsRequest: List<TermsRequestEntity?>): Observable<Boolean?> {
        return service.terms(termsRequest)
    }

    override fun uploadFile(contentType: String?, requestFile: MultipartBody?): Observable<UploadFileResponseEntity?> {
        return this.service.uploadFile(contentType, requestFile)
    }

    override fun enviarCorreo(enviarCorreoRequest: EnviarCorreoRequest?): Observable<ServiceDto<Any>?> {
        return this.service.enviarCorreo(enviarCorreoRequest)
    }


    override suspend fun enviarCorreoCoroutine(enviarCorreoRequest: EnviarCorreoRequest?): Deferred<ServiceDto<Any>?> {
        return this.service.enviarCorreoCoroutine(enviarCorreoRequest)
    }

    override fun sendSMS(smsRequest: SMSRequestEntity?): Observable<ServiceDto<Any>?> {
        return this.service.sendSMS(smsRequest)
    }

    override fun confirmSMSCode(smsRequest: SMSRequestEntity?): Observable<ServiceDto<Any>?> {
        return this.service.confirmSMSCode(smsRequest)
    }

    override fun contratoCredito(entity: CreditAgreementRequestEntity?): Observable<ServiceDto<Any>?> {
        return this.service.contratoCredito(entity)
    }

    override fun verificacion(): Observable<VerificacionEntity?> {
        return this.service.verificacion().map {
            when (it.code) {
                "0000" -> {
                    VerificacionEntity().apply {
                        opcionVerificacionSMS = -1
                        opcionVerificacionCorreo = -1
                        opcionCambioClave = -1

                    }
                }
                "1201" -> {
                    it.data?.apply { opcionVerificacionCorreo = -1 }
                }
                "9999" -> {
                    throw Exception("Error en el servicio. Inténtelo nuevamente.")
                }
                else -> {
                    throw Exception("Error en el servicio. Inténtelo nuevamente.")
                }
            }
        }
    }

    override fun saveVerificacion(verificacionEntity: VerificacionEntity?)
        : Observable<VerificacionEntity?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getAcademyUrl(campaing: String?, email: String?, segmentoConstancia: String?,
                               esLider: Int?, nivelLider: Int?, campaniaInicioLider: String?,
                               seccionGestionLider: String?)
        : Observable<AcademyUrlEntity?> {
        return service.getAcademyUrl(campaing, email, segmentoConstancia, esLider, nivelLider,
            campaniaInicioLider, seccionGestionLider)
    }


    override fun getContenidoResumen(request: ResumenRequestEntity): Observable<ServiceDto<List<ContenidoResumenEntity?>>> {
        return service.getContenidoResumen(request.campaing,
            request.codeRegion,
            request.codeZone,
            request.codeSection,
            request.indConsulDig,
            request.numeroDocumento,
            request.idContenidoDetalle,
            request.primerNombre,
            request.primerApellido,
            request.fechaNacimiento,
            request.correo,
            request.esLider,
            request.codigoContenido)
    }

    override suspend fun getContenidoResumenCoroutine(request: ResumenRequestEntity): Deferred<ServiceDto<List<ContenidoResumenEntity?>>> {
          return service.getContenidoResumenCoroutine(request.campaing,
            request.codeRegion,
            request.codeZone,
            request.codeSection,
            request.indConsulDig,
            request.numeroDocumento,
            request.idContenidoDetalle,
            request.primerNombre,
            request.primerApellido,
            request.fechaNacimiento,
            request.correo,
            request.esLider)
    }

    override fun saveConfigResume(configResume: List<UserConfigResumeEntity>?): Observable<Boolean?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getCodigoPaisGanaMas(): String? {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun configResumeActive(code: String): Observable<Boolean?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getConfigResumeAsObservable(code: String): Observable<List<UserConfigDataEntity>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun clearCache() {
        service.clearCache()
    }

    override suspend fun saveConfig(code1: String, code2: String, value1: String, value3: String) : Deferred<Boolean> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override suspend fun saveContenidoResumenCoroutine(contenido: List<ContenidoResumenEntity?>): Deferred<Boolean> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override suspend fun getContenidoResumenAsync(request: ResumenRequestEntity): Deferred<ServiceDto<List<ContenidoResumenEntity?>>> {
        return service.getContenidoResumenAsync(request.campaing, request.codeRegion, request.codeZone,
            request.codeSection, request.indConsulDig, request.numeroDocumento, request.idContenidoDetalle,
            request.primerNombre, request.primerApellido, request.fechaNacimiento, request.correo,
            request.esLider, request.codigoContenido)
    }

}
