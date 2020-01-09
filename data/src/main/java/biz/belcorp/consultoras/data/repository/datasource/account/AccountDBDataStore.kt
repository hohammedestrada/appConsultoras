package biz.belcorp.consultoras.data.repository.datasource.account

import android.content.Context
import biz.belcorp.consultoras.data.entity.*
import biz.belcorp.consultoras.data.entity.contenidoresumen.ContenidoDetalleEntity
import biz.belcorp.consultoras.data.entity.contenidoresumen.ContenidoDetalleEntity_Table
import biz.belcorp.consultoras.data.entity.contenidoresumen.ContenidoResumenEntity
import biz.belcorp.consultoras.data.entity.contenidoresumen.ContenidoResumenEntity_Table
import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.consultoras.domain.entity.ContenidoResumen
import biz.belcorp.library.net.dto.ServiceDto
import biz.belcorp.library.sql.exception.SqlException
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.kotlinextensions.*
import com.raizlabs.android.dbflow.sql.language.Delete
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import okhttp3.MultipartBody
import com.raizlabs.android.dbflow.kotlinextensions.update as updateDB

/**
 * Clase de Cliente encargada de obtener la data desde el SQLite
 * dicha clase se instancaria una sola vez y se mantendra mientras la aplicacion este viva
 *
 * @version 1.0
 * @since 2017-05-03
 */
internal class AccountDBDataStore
/**
 * Constructor
 *
 * @param context contexto que llamo al SQLite
 */
(val context: Context) : AccountDataStore {

    override fun updatePassword(updateRequest: PasswordUpdateRequestEntity?)
        : Observable<ServiceDto<Any>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun refreshData(): Observable<LoginEntity?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun userResume(campaign: String?, codeRegion: String?, codeZone: String?, codeSection: String?, cargarCaminoBrillante: Boolean?, consecutivoNueva: Int?, consultoraNueva: Int?, countryMoneySymbol: String?, codigoPrograma: String?, periodoCaminoBrillante: Int?, nivelCaminoBrillante: Int?, puntosAlcanzadosCaminoBrillante: Int?, versLogroCaminoBrillante: Int): Observable<UserResumeEntity?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override suspend fun userResumeCoroutine(campaign: String?, codeRegion: String?, codeZone: String?, codeSection: String?, cargarCaminoBrillante: Boolean?, consecutivoNueva: Int?, consultoraNueva: Int?, countryMoneySymbol: String?, codigoPrograma: String?, periodoCaminoBrillante: Int?, nivelCaminoBrillante: Int?, puntosAlcanzadosCaminoBrillante: Int?, versLogroCaminoBrillante: Int): Deferred<UserResumeEntity?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun recovery(recoveryRequest: RecoveryRequestEntity?): Observable<String?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun update(updateRequest: UserUpdateRequestEntity?): Observable<String?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun deletePhotoServer(updateRequest: UserUpdateRequestEntity?): Observable<Boolean?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun update(userEntity: UserEntity?): Observable<Boolean?> {
        return Observable.create { emitter ->
            try {
                userEntity?.let {
                    updateDB<UserEntity> {
                        set(UserEntity_Table.Alias.eq(userEntity.alias),
                            UserEntity_Table.Mobile.eq(userEntity.mobile),
                            UserEntity_Table.Phone.eq(userEntity.phone),
                            UserEntity_Table.OtherPhone.eq(userEntity.otherPhone),
                            UserEntity_Table.Email.eq(userEntity.email),
                            UserEntity_Table.NotificacionesWhatsapp.eq(userEntity.isNotificacionesWhatsapp),
                            UserEntity_Table.ShowCheckWhatsapp.eq(userEntity.isShowCheckWhatsapp),
                            UserEntity_Table.SiguienteCampania.eq(userEntity.nextCampania))

                    }.async.execute()
                    emitter.onNext(true)
                } ?: emitter.onError(NullPointerException())
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }
            emitter.onComplete()
        }
    }

    override fun updatePhoto(name: String?): Observable<Boolean?> {
        return Observable.create { emitter ->
            try {
                updateDB<UserEntity> {
                    set(UserEntity_Table.PhotoProfile.eq(name))
                }.async.execute()
                emitter.onNext(true)
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }
            emitter.onComplete()
        }
    }

    override fun associate(associateRequest: AssociateRequestEntity?): Observable<String?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun terms(termsRequest: List<TermsRequestEntity?>): Observable<Boolean?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun uploadFile(contentType: String?, requestFile: MultipartBody?)
        : Observable<UploadFileResponseEntity?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun enviarCorreo(enviarCorreoRequest: EnviarCorreoRequest?)
        : Observable<ServiceDto<Any>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override suspend fun enviarCorreoCoroutine(enviarCorreoRequest: EnviarCorreoRequest?): Deferred<ServiceDto<Any>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun sendSMS(smsRequest: SMSRequestEntity?): Observable<ServiceDto<Any>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun confirmSMSCode(smsRequest: SMSRequestEntity?): Observable<ServiceDto<Any>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun contratoCredito(entity: CreditAgreementRequestEntity?)
        : Observable<ServiceDto<Any>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun verificacion(): Observable<VerificacionEntity?> {
        return Observable.create { emitter ->
            try {
                val verificacionEntity = (select
                    from VerificacionEntity::class
                    where (VerificacionEntity_Table.ID eq 1)
                    ).result
                verificacionEntity?.let {
                    emitter.onNext(verificacionEntity)
                } ?: emitter.onError(NullPointerException())
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    override fun saveVerificacion(verificacionEntity: VerificacionEntity?)
        : Observable<VerificacionEntity?> {
        return Observable.create { emitter ->
            try {
                verificacionEntity?.let {
                    it.id = 1
                    FlowManager.getModelAdapter(VerificacionEntity::class.java)
                        .save(verificacionEntity)
                    emitter.onNext(verificacionEntity)
                } ?: emitter.onError(NullPointerException())
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }
            emitter.onComplete()
        }
    }

    override fun getAcademyUrl(campaing: String?, email: String?, segmentoConstancia: String?,
                               esLider: Int?, nivelLider: Int?, campaniaInicioLider: String?,
                               seccionGestionLider: String?)
        : Observable<AcademyUrlEntity?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun saveConfigResume(configResume: List<UserConfigResumeEntity>?): Observable<Boolean?> {
        return Observable.create { emitter ->
            try {
                Delete.tables(UserConfigDataEntity::class.java)
                Delete.tables(UserConfigResumeEntity::class.java)

                configResume?.forEach {
                    FlowManager.getModelAdapter(UserConfigResumeEntity::class.java)
                        .save(it)

                    val configData = it.configData
                    configData?.forEach { data ->
                        data.codeConfig = it.code
                        FlowManager.getModelAdapter(UserConfigDataEntity::class.java)
                            .save(data)
                    }
                }
                emitter.onNext(true)
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }
            emitter.onComplete()
        }
    }

    override fun getCodigoPaisGanaMas(): String? {
        val result = (select from UserConfigResumeEntity::class).queryList()
        var codePais: String? = null
        if(result.isNotEmpty()){
            codePais = result[0].code
        }
        return codePais
    }


    override fun configResumeActive(code: String): Observable<Boolean?> {
        return Observable.create { emitter ->
            try {
                val config = (select
                    from UserConfigResumeEntity::class
                    where (UserConfigResumeEntity_Table.Codigo eq code)
                    ).result
                config?.let {
                    emitter.onNext(true)
                } ?: emitter.onNext(false)
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    override suspend fun configResumeActiveCoroutine(code: String): Boolean? {
        return try {
            val result = (select
                from UserConfigResumeEntity::class
                where (UserConfigResumeEntity_Table.Codigo eq code)
                ).result
            result?.let {
                true
            } ?: kotlin.run {
                false
            }
        } catch (ex: Exception) {
            false
        }
    }

    override fun getConfigResumeAsObservable(code: String): Observable<List<UserConfigDataEntity>?> {
        return Observable.create { emitter ->
            try {
                val configData = (select
                    from UserConfigDataEntity::class
                    where (UserConfigDataEntity_Table.CodigoConfiguracion eq code)
                    ).list
                configData.let { emitter.onNext(it) }
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    override fun getConfigAsObservable(code: String): Observable<List<UserConfigDataEntity>?> {
        return getConfigResumeAsObservable(code)
    }


    override fun clearCache() {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }
//return the config country from a code
    override suspend fun getConfigResumen(code: String): Deferred<List<UserConfigDataEntity>?> {
        return coroutineScope {
            async {
                (select
                    from UserConfigDataEntity::class
                    where (UserConfigDataEntity_Table.CodigoConfiguracion eq code)
                    ).list
            }

        }
    }

    override suspend fun getConfigResumenByCodeId(codeId: String): Deferred<UserConfigDataEntity?> {
        return coroutineScope {
            async {
                (select
                    from UserConfigDataEntity::class
                    where (UserConfigDataEntity_Table.Codigo eq codeId)
                    ).list.firstOrNull()
            }
        }
    }

    override suspend fun saveConfig(code1: String, code2: String, value1: String, value3: String): Deferred<Boolean> {
        return coroutineScope {
            async {
                updateDB<UserConfigDataEntity> {
                    set(UserConfigDataEntity_Table.Valor1.eq(value1),
                        UserConfigDataEntity_Table.Valor3.eq(value3)
                    ).where(UserConfigDataEntity_Table.CodigoConfiguracion.eq(code1)
                        .and(UserConfigDataEntity_Table.Codigo.eq(code2)))
                }.execute()
                true
            }
        }
    }

    override suspend fun saveContenidoResumenCoroutine(contenido: List<ContenidoResumenEntity?>): Deferred<Boolean> {
        return coroutineScope {
            async {
                try {

                    Delete.tables(ContenidoResumenEntity::class.java)
                    Delete.tables(ContenidoDetalleEntity::class.java)

                    contenido.filterNotNull().forEach {
                        FlowManager.getModelAdapter(ContenidoResumenEntity::class.java).save(it)

                        val contenidoDetail = it.contenidoDetalleEntity
                        contenidoDetail?.forEach { it1 ->
                            FlowManager.getModelAdapter(ContenidoDetalleEntity::class.java).save(it1)
                        }

                    }
                    true

                } catch (ex: Exception) {
                    throw  SqlException()
                }

            }
        }
    }

    override suspend fun checkContenidoResumenIfExist(codeResumen: String, codeDetail: String): Deferred<Boolean> {
        return coroutineScope {
            async {
                val resumen = (select
                    from ContenidoResumenEntity::class
                    where (ContenidoResumenEntity_Table.Codigo eq codeResumen)
                    ).list

                if (resumen.isNotEmpty()) {
                    val detalle = (select
                        from ContenidoDetalleEntity::class
                        where (ContenidoDetalleEntity_Table.Codigo eq codeDetail)
                        ).list

                    !detalle.isNullOrEmpty()
                } else {
                    false
                }
            }
        }
    }
}
