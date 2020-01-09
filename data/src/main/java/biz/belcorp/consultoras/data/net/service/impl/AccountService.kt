package biz.belcorp.consultoras.data.net.service.impl

import android.content.Context
import biz.belcorp.consultoras.data.entity.*
import biz.belcorp.consultoras.data.manager.SessionManager
import biz.belcorp.consultoras.data.entity.contenidoresumen.ContenidoResumenEntity

import biz.belcorp.consultoras.data.net.RestApi
import biz.belcorp.consultoras.data.net.service.IAccountService
import biz.belcorp.consultoras.data.util.Constant.Companion.CODE_FEATURE_FLAG_OFERTA_FINAL
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.net.AccessToken
import biz.belcorp.library.net.dto.ServiceDto
import biz.belcorp.library.net.exception.NetworkConnectionException
import biz.belcorp.library.net.exception.ServiceException
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Url

class AccountService
/**
 * Constructor
 *
 * @param context Contexto que llamo al Servicio
 */
(context: Context, accessToken: AccessToken?, appName: String?, appCountry: String?)
    : BaseService(context), IAccountService {

    private val service: IAccountService = RestApi.create(IAccountService::class.java,
        accessToken, appName, appCountry)

    override fun refreshData(): Observable<LoginEntity?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.refreshData()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error -> emitter.onError(getError(error)) },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }
            } else {
                emitter.onError(NetworkConnectionException())
            }
        }
    }

    override fun userResume(@Query("inModel.campaniaID") campaing: String?,
                            @Query("inModel.codigoRegion") codeRegion: String?,
                            @Query("inModel.codigoZona") codeZone: String?,
                            @Query("inModel.codigoSeccion") codeSection: String?,
                            @Query("inModel.cargarCaminoBrillante") cargarCaminoBrillante: Boolean?,
                            @Query("inModel.consecutivoNueva") consecutivoNueva: Int?,
                            @Query("inModel.consultoraNueva") consultoraNueva: Int?,
                            @Query("inModel.simbolo") countryMoneySymbol: String?,
                            @Query("inModel.codigoPrograma") codigoPrograma: String?,
                            @Query("inModel.periodoCaminoBrillante") periodoCaminoBrillante: Int?,
                            @Query("inModel.nivelCaminoBrillante") nivelCaminoBrillante: Int?,
                            @Query("inModel.puntosAlcanzadosCaminoBrillante") puntosAlcanzadosCaminoBrillante: Int?,
                            @Query("inModel.versLogrosCaminoBrillante") versLogrosCaminoBrillante: Int
    )
        : Observable<UserResumeEntity?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.userResume(campaing, codeRegion, codeZone, codeSection, cargarCaminoBrillante, consecutivoNueva, consultoraNueva, countryMoneySymbol, codigoPrograma, periodoCaminoBrillante, nivelCaminoBrillante, puntosAlcanzadosCaminoBrillante, versLogrosCaminoBrillante)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ userResume ->

                            context?.let {  context ->
                                SessionManager.getInstance(context).saveFeatureFlagOfertaFinal(false)
                                userResume?.config?.forEach {userConfig ->
                                    if(userConfig.code == CODE_FEATURE_FLAG_OFERTA_FINAL){
                                        context.let {
                                            SessionManager.getInstance(it).saveFeatureFlagOfertaFinal(true)
                                        }
                                        return@forEach
                                    }
                                }
                            }

                            userResume?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))

                        },
                            { error -> emitter.onError(getError(error)) },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkConnectionException())
            }
        }
    }

    override fun userResumenCoroutine(@Query("inModel.campaniaID") campaing: String?,
                                      @Query("inModel.codigoRegion") codeRegion: String?,
                                      @Query("inModel.codigoZona") codeZone: String?,
                                      @Query("inModel.codigoSeccion") codeSection: String?,
                                      @Query("inModel.cargarCaminoBrillante") cargarCaminoBrillante: Boolean?,
                                      @Query("inModel.consecutivoNueva") consecutivoNueva: Int?,
                                      @Query("inModel.consultoraNueva") consultoraNueva: Int?,
                                      @Query("inModel.simbolo") countryMoneySymbol: String?,
                                      @Query("inModel.codigoPrograma") codigoPrograma: String?,
                                      @Query("inModel.periodoCaminoBrillante") periodoCaminoBrillante: Int?,
                                      @Query("inModel.nivelCaminoBrillante") nivelCaminoBrillante: Int?,
                                      @Query("inModel.puntosAlcanzadosCaminoBrillante") puntosAlcanzadosCaminoBrillante: Int?,
                                      @Query("inModel.versLogrosCaminoBrillante") versLogrosCaminoBrillante: Int
    ): Deferred<UserResumeEntity?> {
     if(isThereInternetConnection){
        return service.userResumenCoroutine(campaing, codeRegion, codeZone, codeSection, cargarCaminoBrillante, consecutivoNueva, consultoraNueva, countryMoneySymbol, codigoPrograma, periodoCaminoBrillante, nivelCaminoBrillante, puntosAlcanzadosCaminoBrillante, versLogrosCaminoBrillante)
     }
     else
         throw NetworkErrorException()
    }

    override fun enviarCorreoCoroutine(entity: EnviarCorreoRequest?): Deferred<ServiceDto<Any>?> {

        if (isThereInternetConnection) {
            return service.enviarCorreoCoroutine(entity)
        }
        else
            throw NetworkErrorException()
    }

    override fun recovery(@Body entity: RecoveryRequestEntity?): Observable<String?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.recovery(entity)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error -> emitter.onError(RestApi.parseError(error)) },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkConnectionException())
            }
        }
    }

    override fun update(@Body entity: UserUpdateRequestEntity?): Observable<String?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.update(entity)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error -> emitter.onError(getError(error)) },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }
            } else {
                emitter.onError(NetworkConnectionException())
            }
        }
    }

    override fun updatePassword(@Query("anteriorContrasenia") anteriorContrasenia: String?,
                                @Query("nuevaContrasenia") nuevaContrasenia: String?)
        : Observable<ServiceDto<Any>?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.updatePassword(anteriorContrasenia, nuevaContrasenia)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error -> emitter.onError(getError(error)) },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkConnectionException())
            }
        }
    }

    override fun updatePassword(updateRequest: PasswordUpdate2RequestEntity)
        : Observable<ServiceDto<Any>?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.updatePassword(updateRequest)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error -> emitter.onError(getError(error)) },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkConnectionException())
            }
        }
    }

    override fun associate(@Body entity: AssociateRequestEntity?): Observable<String?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.associate(entity)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error -> emitter.onError(getError(error)) },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkConnectionException())
            }
        }
    }

    override fun checkHttpCode(@Url url: String?): Observable<String?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.checkHttpCode(url)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            url?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error -> emitter.onError(getError(error)) },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkConnectionException())
            }
        }
    }

    override fun terms(entity: List<TermsRequestEntity?>): Observable<Boolean?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.terms(entity)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error -> emitter.onError(getError(error)) },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkConnectionException())
            }
        }
    }

    override fun uploadFile(@Header(value = "Content-Type") contentType: String?,
                            @Body body: MultipartBody?)
        : Observable<UploadFileResponseEntity?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.uploadFile(contentType, body)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error -> emitter.onError(getError(error)) },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkConnectionException())
            }
        }
    }

    override fun updatePhoto(@Query(value = "NombreArchivo") namePhoto: String?): Observable<Boolean?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.updatePhoto(namePhoto)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error -> emitter.onError(getError(error)) },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkConnectionException())
            }
        }
    }

    override fun deletePhotoServer(@Body entity: UserUpdateRequestEntity?): Observable<Boolean?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.deletePhotoServer(entity)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error -> emitter.onError(getError(error)) },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkConnectionException())
            }
        }
    }

    override fun enviarCorreo(entity: EnviarCorreoRequest?): Observable<ServiceDto<Any>?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.enviarCorreo(entity)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error -> emitter.onError(getError(error)) },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkConnectionException())
            }
        }
    }


    override fun sendSMS(entity: SMSRequestEntity?): Observable<ServiceDto<Any>?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.sendSMS(entity)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 ->
                                emitter.onNext(it1)
                            } ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error ->
                                BelcorpLogger.d("error", error)
                                emitter.onError(getError(error))
                            },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkErrorException())
            }
        }
    }

    override fun confirmSMSCode(entity: SMSRequestEntity?): Observable<ServiceDto<Any>?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.confirmSMSCode(entity)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 ->
                                emitter.onNext(it1)
                            } ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error ->
                                BelcorpLogger.d("error", error)
                                emitter.onError(getError(error))
                            },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkErrorException())
            }
        }
    }

    override fun contratoCredito(entity: CreditAgreementRequestEntity?): Observable<ServiceDto<Any>?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.contratoCredito(entity)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 ->
                                emitter.onNext(it1)
                            } ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error ->
                                BelcorpLogger.d("error", "ERROR!", error)
                                emitter.onError(getError(error))
                            },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkErrorException())
            }
        }
    }

    override fun verificacion(): Observable<ServiceDto<VerificacionEntity?>> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.verificacion()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 ->
                                emitter.onNext(it1)
                            } ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error ->
                                BelcorpLogger.d("error", error)
                                emitter.onError(getError(error))
                            },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkErrorException())
            }
        }
    }

    override fun getAcademyUrl(campaing: String?, email: String?, segmentoConstancia: String?,
                               esLider: Int?, nivelLider: Int?, campaniaInicioLider: String?,
                               seccionGestionLider: String?): Observable<AcademyUrlEntity?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.getAcademyUrl(campaing, email, segmentoConstancia, esLider, nivelLider,
                        campaniaInicioLider, seccionGestionLider)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error -> emitter.onError(getError(error)) },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkErrorException())
            }
        }
    }


    override fun getContenidoResumen(campaing: Int?, codeRegion: String?, codeZone: String?, codeSection: String?, indConsulDig: String?, numeroDocumento: String?, idContenidoDetalle: Int?, primerNombre: String?, primerApellido: String?, fechaNacimiento: String?, correo: String?, esLider: Int?, codigoContenido : String?): Observable<ServiceDto<List<ContenidoResumenEntity?>>> {

        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.getContenidoResumen(campaing, codeRegion, codeZone, codeSection, indConsulDig,numeroDocumento,idContenidoDetalle, primerNombre, primerApellido, fechaNacimiento, correo, esLider, codigoContenido)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 ->
                                emitter.onNext(it1)
                            }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error -> emitter.onError(getError(error)) },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkErrorException())
            }
        }
    }

    override fun getContenidoResumenCoroutine(campaing: Int?, codeRegion: String?, codeZone: String?, codeSection: String?, indConsulDig: String?, numeroDocumento: String?, idContenidoDetalle: Int?, primerNombre: String?, primerApellido: String?, fechaNacimiento: String?, correo: String?, esLider: Int?): Deferred<ServiceDto<List<ContenidoResumenEntity?>>> {
        return if(isThereInternetConnection){
            service.getContenidoResumenCoroutine(campaing, codeRegion, codeZone, codeSection, indConsulDig,numeroDocumento,idContenidoDetalle, primerNombre, primerApellido, fechaNacimiento, correo, esLider)
        }
        else
            throw NetworkConnectionException()

    }
    override fun getContenidoResumenAsync(campaing: Int?, codeRegion: String?, codeZone: String?, codeSection: String?, indConsulDig: String?, numeroDocumento: String?, idContenidoDetalle: Int?, primerNombre: String?, primerApellido: String?, fechaNacimiento: String?, correo: String?, esLider: Int?, codigoContenido: String?): Deferred<ServiceDto<List<ContenidoResumenEntity?>>> {
        if (isThereInternetConnection) {
            return service.getContenidoResumenAsync(campaing, codeRegion, codeZone, codeSection,
                indConsulDig, numeroDocumento, idContenidoDetalle, primerNombre, primerApellido,
                fechaNacimiento, correo, esLider, codigoContenido)
        }
        else
            throw NetworkErrorException()
    }

}
