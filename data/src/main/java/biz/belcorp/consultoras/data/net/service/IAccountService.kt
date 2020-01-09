package biz.belcorp.consultoras.data.net.service

import biz.belcorp.consultoras.data.entity.*
import biz.belcorp.consultoras.data.entity.contenidoresumen.ContenidoResumenEntity
import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.library.net.dto.ServiceDto
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import retrofit2.http.*

interface IAccountService {

    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/Account/RefreshData")
    fun refreshData(): Observable<LoginEntity?>

    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.3/Consultora/Resumen")
    fun userResume(@Query("inModel.campaniaID") campaing: String?,
                   @Query("inModel.codigoRegion") codeRegion: String?,
                   @Query("inModel.codigoZona") codeZone: String?,
                   @Query("inModel.codigoSeccion") codeSection: String?,
                   @Query("inModel.cargarCaminoBrillante") cargarCaminoBrillante: Boolean?,
                   @Query("inModel.consecutivoNueva") consecutivoNueva: Int?,
                   @Query("inModel.consultoraNueva") consultoraNueva: Int?,
                   @Query("inModel.simbolo") countryMoneySymbol: String?,
                   @Query("inModel.codigoPrograma") codigoPrograma: String?,
                   @Query("inModel.periodoCB") periodoCaminoBrillante: Int?,
                   @Query("inModel.nivelCB") nivelCaminoBrillante: Int?,
                   @Query("inModel.puntosAlcanzadosCB") puntosAlcanzadosCaminoBrillante: Int?,
                   @Query("inModel.versLogrosCB") versLogrosCaminoBrillante: Int
    ): Observable<UserResumeEntity?>

    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.3/Consultora/Resumen")
    fun userResumenCoroutine(@Query("inModel.campaniaID") campaing: String?,
                             @Query("inModel.codigoRegion") codeRegion: String?,
                             @Query("inModel.codigoZona") codeZone: String?,
                             @Query("inModel.codigoSeccion") codeSection: String?,
                             @Query("inModel.cargarCaminoBrillante") cargarCaminoBrillante: Boolean?,
                             @Query("inModel.consecutivoNueva") consecutivoNueva: Int?,
                             @Query("inModel.consultoraNueva") consultoraNueva: Int?,
                             @Query("inModel.simbolo") countryMoneySymbol: String?,
                             @Query("inModel.codigoPrograma") codigoPrograma: String?,
                             @Query("inModel.periodoCB") periodoCaminoBrillante: Int?,
                             @Query("inModel.nivelCB") nivelCaminoBrillante: Int?,
                             @Query("inModel.puntosAlcanzadosCB") puntosAlcanzadosCaminoBrillante: Int?,
                             @Query("inModel.versLogrosCB") versLogrosCaminoBrillante: Int
    ): Deferred<UserResumeEntity?>


    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "api/Account/RecuperarContrasenia")
    fun recovery(@Body entity: RecoveryRequestEntity?): Observable<String?>

    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "api/v1.0/Account/ActualizarMisDatos")
    fun update(@Body entity: UserUpdateRequestEntity?): Observable<String?>

    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "api/v1.1/Account/CambiarContrasenia")
    fun updatePassword(@Query("anteriorContrasenia") anteriorContrasenia: String?,
                       @Query("nuevaContrasenia") nuevaContrasenia: String?)
        : Observable<ServiceDto<Any>?>

    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "api/v1.2/Account/CambiarContrasenia")
    fun updatePassword(@Body updateRequest: PasswordUpdate2RequestEntity)
        : Observable<ServiceDto<Any>?>

    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "api/Account/AsociarCuentaExterna")
    fun associate(@Body entity: AssociateRequestEntity?): Observable<String?>

    @GET
    fun checkHttpCode(@Url url: String?): Observable<String?>

    @Headers(Constant.TRANSFORM)
    @POST(value = "api/v1.2/Account/TerminosCondiciones")
    fun terms(@Body entity: List<TermsRequestEntity?>): Observable<Boolean?>

    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "api/v1.1/FileManager?TipoArchivo=01")
    fun uploadFile(@Header("Content-Type") contentType: String?, @Body body: MultipartBody?)
        : Observable<UploadFileResponseEntity?>

    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @PATCH(value = "api/v1.1/Account/FotoPerfil")
    fun updatePhoto(@Query("NombreArchivo") namePhoto: String?): Observable<Boolean?>

    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @HTTP(method = "DELETE", path = "api/v1.1/FileManager", hasBody = true)
    fun deletePhotoServer(@Body entity: UserUpdateRequestEntity?): Observable<Boolean?>

    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "api/v1.2/Account/EnviarSmsCodigo")
    fun sendSMS(@Body entity: SMSRequestEntity?): Observable<ServiceDto<Any>?>

    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "api/v1.2/Account/ConfirmarSmsCodigo")
    fun confirmSMSCode(@Body entity: SMSRequestEntity?): Observable<ServiceDto<Any>?>

    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "api/v1.2/Account/EnviarCorreo")
    fun enviarCorreo(@Body entity: EnviarCorreoRequest?): Observable<ServiceDto<Any>?>


    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "api/v1.2/Account/EnviarCorreo")
    fun enviarCorreoCoroutine(@Body entity: EnviarCorreoRequest?): Deferred<ServiceDto<Any>?>

    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "api/v1.2/Account/ContratoCredito")
    fun contratoCredito(@Body entity: CreditAgreementRequestEntity?): Observable<ServiceDto<Any>?>

    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.2/Account/Verificacion")
    fun verificacion(): Observable<ServiceDto<VerificacionEntity?>>

    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.2/Consultora/MiAcademia")
    fun getAcademyUrl(@Query("inModel.campaniaID") campaing: String?,
                      @Query("inModel.email") email: String?,
                      @Query("inModel.segmentoConstancia") segmentoConstancia: String?,
                      @Query("inModel.esLider") esLider: Int?,
                      @Query("inModel.nivelLider") nivelLider: Int?,
                      @Query("inModel.campaniaInicioLider") campaniaInicioLider: String?,
                      @Query("inModel.seccionGestionLider") seccionGestionLider: String?)
        : Observable<AcademyUrlEntity?>


    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @GET("api/v1.3/Consultora/Contenido/Resumen")
    fun getContenidoResumen(
        @Query("inModel.campaniaID") campaing: Int?,
        @Query("inModel.codigoRegion") codeRegion: String?,
        @Query("inModel.codigoZona") codeZone: String?,
        @Query("inModel.codigoSeccion") codeSection: String?,
        @Query("inModel.indicadorConsultoraDigital") indConsulDig: String?,
        @Query("inModel.numeroDocumento") numeroDocumento: String?,
        @Query("inModel.idContenidoDetalle") idContenidoDetalle: Int?,
        @Query("inModel.primerNombre") primerNombre: String?,
        @Query("inModel.primerApellido") primerApellido: String?,
        @Query("inModel.fechaNacimiento") fechaNacimiento: String?,
        @Query("inModel.correo") correo: String?,
        @Query("inModel.esLider") esLider: Int?,
        @Query("inModel.codigoContenido") codigoContenido: String?

    ): Observable<ServiceDto<List<ContenidoResumenEntity?>>>

    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @GET("api/v1.3/Consultora/Contenido/Resumen")
    fun getContenidoResumenCoroutine(
        @Query("inModel.campaniaID") campaing: Int?,
        @Query("inModel.codigoRegion") codeRegion: String?,
        @Query("inModel.codigoZona") codeZone: String?,
        @Query("inModel.codigoSeccion") codeSection: String?,
        @Query("inModel.indicadorConsultoraDigital") indConsulDig: String?,
        @Query("inModel.numeroDocumento") numeroDocumento: String?,
        @Query("inModel.idContenidoDetalle") idContenidoDetalle: Int?,
        @Query("inModel.primerNombre") primerNombre: String?,
        @Query("inModel.primerApellido") primerApellido: String?,
        @Query("inModel.fechaNacimiento") fechaNacimiento: String?,
        @Query("inModel.correo") correo: String?,
        @Query("inModel.esLider") esLider: Int?

    ): Deferred<ServiceDto<List<ContenidoResumenEntity?>>>

    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @GET("api/v1.3/Consultora/Contenido/Resumen")
    fun getContenidoResumenAsync(
        @Query("inModel.campaniaID") campaing: Int?,
        @Query("inModel.codigoRegion") codeRegion: String?,
        @Query("inModel.codigoZona") codeZone: String?,
        @Query("inModel.codigoSeccion") codeSection: String?,
        @Query("inModel.indicadorConsultoraDigital") indConsulDig: String?,
        @Query("inModel.numeroDocumento") numeroDocumento: String?,
        @Query("inModel.idContenidoDetalle") idContenidoDetalle: Int?,
        @Query("inModel.primerNombre") primerNombre: String?,
        @Query("inModel.primerApellido") primerApellido: String?,
        @Query("inModel.fechaNacimiento") fechaNacimiento: String?,
        @Query("inModel.correo") correo: String?,
        @Query("inModel.esLider") esLider: Int?,
        @Query("inModel.codigoContenido") codigoContenido: String?
    ): Deferred<ServiceDto<List<ContenidoResumenEntity?>>>

    fun clearCache()

}
