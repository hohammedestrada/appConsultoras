package biz.belcorp.consultoras.data.repository

import biz.belcorp.consultoras.data.entity.*
import biz.belcorp.consultoras.data.mapper.*
import biz.belcorp.consultoras.data.repository.datasource.account.AccountDataStoreFactory
import biz.belcorp.consultoras.data.repository.datasource.auth.AuthDataStoreFactory
import biz.belcorp.consultoras.data.repository.datasource.caminobrillante.CaminoBrillanteDataStoreFactory
import biz.belcorp.consultoras.data.repository.datasource.country.CountryDataStoreFactory
import biz.belcorp.consultoras.data.repository.datasource.product.ProductDataStoreFactory
import biz.belcorp.consultoras.data.repository.datasource.session.SessionDataStoreFactory
import biz.belcorp.consultoras.data.repository.datasource.user.UserDataStoreFactory
import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.exception.ConsultantApprovedException
import biz.belcorp.consultoras.domain.repository.AccountRepository
import biz.belcorp.library.net.AccessToken
import biz.belcorp.library.net.dto.ServiceDto
import biz.belcorp.library.notification.TipoIngreso
import biz.belcorp.library.security.JwtEncryption
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function4
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountDataRepository @Inject
internal constructor(private val userDataStoreFactory: UserDataStoreFactory,
                     private val countryDataStoreFactory: CountryDataStoreFactory,
                     private val accountDataStoreFactory: AccountDataStoreFactory,
                     private val authDataStoreFactory: AuthDataStoreFactory,
                     private val sessionDataStoreFactory: SessionDataStoreFactory,
                     private val caminobrillanteDataStoreFactory: CaminoBrillanteDataStoreFactory,
                     private val loginEntityDataMapper: LoginEntityDataMapper,
                     private val recoveryRequestEntityDataMapper: RecoveryRequestEntityDataMapper,
                     private val associateRequestEntityDataMapper: AssociateRequestEntityDataMapper,
                     private val userUpdateRequestEntityDataMapper: UserUpdateRequestEntityDataMapper,
                     private val basicDtoDataMapper: BasicDtoDataMapper,
                     private val accountDataMapper: AccountDataMapper,
                     private val verificacionEntityDataMapper: VerificacionEntityDataMapper,
                     private val productDataStoreFactory: ProductDataStoreFactory,
                     private val academyUrlEntityDataMapper: AcademyUrlEntityDataMapper,
                     private val bannerLanzamientoDataMapper: BannerLanzamientoEntityDataMapper)
    : AccountRepository {

    override fun getUpdatedData(): Observable<Login?> {
        val dataStore = this.accountDataStoreFactory.create()
        return dataStore.refreshData().map { loginEntityDataMapper.transform(it) }
    }

    override suspend fun getCodePaisGanaMas(): String? {
        val dataStore = this.accountDataStoreFactory.createDB()
        return dataStore.getCodigoPaisGanaMas()
    }

    override fun refreshData(): Observable<Login?> {
        val dataStore = this.accountDataStoreFactory.create()
        val sessionDataStore = this.sessionDataStoreFactory.create()
        val caminoBrillanteDataStore = caminobrillanteDataStoreFactory.createDB()

        return dataStore.refreshData().flatMap { loginEntity ->
            sessionDataStore.getCargarCaminoBrillante().flatMap { cargarCaminoBrillante ->

                caminoBrillanteDataStore.getNivelConsultoraCaminoBrillanteAsObservable().flatMap { nivelCaminoBrillante ->

                    caminoBrillanteDataStore.getPuntajeAcumuladoAsObservable().flatMap { puntaje ->

                        caminoBrillanteDataStore.getPeriodoCaminoBrillante().flatMap { periodoCaminoBrillante ->

                            if (loginEntity.isIndicadorConsultoraDummy == true) {
                                val zip = Observable.zip(dataStore.userResume(loginEntity.campaing,
                                    loginEntity.regionCode, loginEntity.zoneCode, loginEntity.codigoSeccion,
                                    cargarCaminoBrillante, loginEntity.consecutivoNueva, loginEntity.consultoraNueva, loginEntity.countryMoneySymbol, loginEntity.codigoPrograma,
                                    periodoCaminoBrillante, nivelCaminoBrillante, puntaje, Constant.VERS_LOGROS_CAMINO_BRILLANTE)
                                    .flatMap { resume ->
                                        Observable.zip(
                                            this.accountDataStoreFactory.createDB().saveConfigResume(resume.config),
                                            this.caminobrillanteDataStoreFactory.createDB().saveNivelesConsultora(cargarCaminoBrillante, resume.caminobrillante?.nivelesConsultoraCaminoBrillanteEntity),
                                            this.caminobrillanteDataStoreFactory.createDB().saveNivelesCaminoBrillante(cargarCaminoBrillante, resume.caminobrillante?.nivelesCaminoBrillante),
                                            this.caminobrillanteDataStoreFactory.createDB().saveLogrosCaminoBrillante(cargarCaminoBrillante, resume.caminobrillante?.resumenLogroCaminoBrillanteEntity, resume.caminobrillante?.logrosCaminoBrillanteEntity),
                                            Function4<Boolean?, Boolean?, Boolean?, Boolean?, Boolean> { t1, t2, t3, t4 -> true }
                                        ).map { resume }
                                    },
                                    this.productDataStoreFactory.createCloudDataStore()
                                        .getPersonalization(loginEntity.campaing?.toInt()),
                                    BiFunction<UserResumeEntity?, String?, Login?> { t1, t2 ->
                                        loginEntity.personalizacionesDummy = t2
                                        loginEntityDataMapper.transform(loginEntity, t1)!!
                                    })
                                zip.flatMap { r ->
                                    sessionDataStore.saveLastUpdateCaminoBrillante(System.currentTimeMillis())
                                    observableRefresh(loginEntity, r)
                                }
                            } else {
                                dataStore.userResume(loginEntity.campaing, loginEntity.regionCode, loginEntity.zoneCode,
                                    loginEntity.codigoSeccion, cargarCaminoBrillante, loginEntity.consecutivoNueva, loginEntity.consultoraNueva, loginEntity.countryMoneySymbol, loginEntity.codigoPrograma, periodoCaminoBrillante, nivelCaminoBrillante, puntaje, Constant.VERS_LOGROS_CAMINO_BRILLANTE)
                                    .flatMap { resume ->
                                        Observable.zip(
                                            this.accountDataStoreFactory.createDB().saveConfigResume(resume.config),
                                            this.caminobrillanteDataStoreFactory.createDB().saveNivelesConsultora(cargarCaminoBrillante, resume.caminobrillante?.nivelesConsultoraCaminoBrillanteEntity),
                                            this.caminobrillanteDataStoreFactory.createDB().saveNivelesCaminoBrillante(cargarCaminoBrillante, resume.caminobrillante?.nivelesCaminoBrillante),
                                            this.caminobrillanteDataStoreFactory.createDB().saveLogrosCaminoBrillante(cargarCaminoBrillante, resume.caminobrillante?.resumenLogroCaminoBrillanteEntity, resume.caminobrillante?.logrosCaminoBrillanteEntity),
                                            Function4<Boolean?, Boolean?, Boolean?, Boolean?, Login?> { t1, t2, t3, t4 ->
                                                loginEntityDataMapper.transform(loginEntity, resume)!!
                                            }
                                        ).flatMap { r ->
                                            sessionDataStore.saveLastUpdateCaminoBrillante(System.currentTimeMillis())
                                            observableRefresh(loginEntity, r)
                                        }
                                    }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun refreshData2(): Observable<Boolean?> {
        val dataStore = this.accountDataStoreFactory.create()
        val userDataStore = this.userDataStoreFactory.createDB()

        return dataStore.refreshData().flatMap { loginEntity ->
            userDataStore.save(loginEntityDataMapper.transformUser(loginEntityDataMapper.transform(loginEntity)))
        }
    }

    override fun clearCache() {
        val dataStore = this.accountDataStoreFactory.createCloudDataStore()
        dataStore.clearCache()
    }


    private fun observableRefresh(loginEntity: LoginEntity, domainEntity: Login?): Observable<Login>? {
        val userDataStore = this.userDataStoreFactory.create()

        return userDataStore.getWithObservable().flatMap { userEntity ->
            domainEntity?.tipoIngreso = userEntity.tipoIngreso
            domainEntity?.showCheckWhatsapp = if (userEntity.isShowCheckWhatsapp) 1 else 0
            domainEntity?.checkEnviarWhatsaap = if (userEntity.isNotificacionesWhatsapp) 1 else 0
            domainEntity?.logSiguienteCampania = userEntity.nextCampania

            if (userEntity.userType == 1) {
                userDataStore.save(loginEntityDataMapper.transformUser(domainEntity))
                    .map { domainEntity!! }
            } else if (userEntity.userType == 2 && loginEntity.userType == 1) {

                if (!domainEntity!!.numeroDocumento.isNullOrBlank()) {

                    domainEntity.isAceptaTerminosCondiciones = userEntity.isAceptaTerminosCondiciones
                    domainEntity.isAceptaPoliticaPrivacidad = userEntity.isAceptaPoliticaPrivacidad

                    val credentialsEntity = CredentialsEntity()
                    credentialsEntity.pais = loginEntity.countryISO
                    credentialsEntity.password = ""
                    credentialsEntity.tipoAutenticacion = 3

                    val document = "{\"Documento\":\"${loginEntity.numeroDocumento}\"}"

                    credentialsEntity.username = JwtEncryption.newInstance()
                        .encrypt(Constant.SECRET, document)

                    val authDataStore = this.authDataStoreFactory.create()
                    val sessionDataStore = this.sessionDataStoreFactory.create()

                    domainEntity.exception = ConsultantApprovedException()

                    authDataStore.loginOnline(credentialsEntity)
                        .flatMap { l ->
                            sessionDataStore.saveAccessToken(
                                AccessToken(
                                    l.tokenType,
                                    l.accessToken,
                                    l.refreshToken
                                )).flatMap {
                                this.accountDataStoreFactory.create()
                                    .terms(accountDataMapper.transform(userEntity.isAceptaTerminosCondiciones,
                                        userEntity.isAceptaPoliticaPrivacidad)).flatMap {
                                        userDataStore.save(loginEntityDataMapper.transformUser(domainEntity))
                                            .flatMap { Observable.just(domainEntity) }
                                    }
                            }
                        }
                } else
                    Observable.error(ConsultantApprovedException())
            } else {
                domainEntity!!.isAceptaTerminosCondiciones = userEntity.isAceptaTerminosCondiciones
                domainEntity.isAceptaPoliticaPrivacidad = userEntity.isAceptaPoliticaPrivacidad

                userDataStore.save(loginEntityDataMapper.transformUser(domainEntity))
                    .map { domainEntity }
            }
        }
    }

    override fun getResumen(login: Login?): Observable<Login?> {
        val dataStore = this.accountDataStoreFactory.create()
        val userDataStore = userDataStoreFactory.create()
        val caminoBrillanteDataStore = caminobrillanteDataStoreFactory.createDB()

        return caminoBrillanteDataStore.getPeriodoCaminoBrillante().flatMap { periodo ->
            caminoBrillanteDataStore.getNivelConsultoraCaminoBrillanteAsObservable().flatMap { nivelCaminoBrillante ->
                caminoBrillanteDataStore.getPuntajeAcumuladoAsObservable().flatMap { puntaje ->

                    dataStore.userResume(login!!.campaing, login.regionCode, login.zoneCode,
                        login.codigoSeccion, true, login.consecutivoNueva, login.consultoraNueva, login.countryMoneySymbol, login.codigoPrograma, periodo, nivelCaminoBrillante, puntaje, Constant.VERS_LOGROS_CAMINO_BRILLANTE).flatMap<Login> { userResumeEntity ->
                        val domainEntity = loginEntityDataMapper.transform(login, userResumeEntity)
                        userDataStore.save(loginEntityDataMapper.transformUser(domainEntity))
                            .flatMap { Observable.just(domainEntity) }
                    }
                }
            }
        }
    }

    override fun updateResumenOnly(): Observable<Boolean> {
        val userDataStore = this.userDataStoreFactory.createDB()
        val dataStore = this.accountDataStoreFactory.create()
        val sessionDataStore = this.sessionDataStoreFactory.create()
        val caminoBrillanteDataStore = caminobrillanteDataStoreFactory.createDB()

        return caminoBrillanteDataStore.getPeriodoCaminoBrillante().flatMap { periodo ->

            caminoBrillanteDataStore.getNivelConsultoraCaminoBrillanteAsObservable().flatMap { nivelCaminoBrillante ->

                caminoBrillanteDataStore.getPuntajeAcumuladoAsObservable().flatMap { puntaje ->

                    userDataStore.getWithObservable().flatMap { user ->
                        sessionDataStore.getCargarCaminoBrillante().flatMap { cargarCaminoBrillante ->
                            dataStore.userResume(user.campaing, user.regionCode,
                                user.zoneCode, user.codigoSeccion, cargarCaminoBrillante, user.consecutivoNueva, user.consultoraNueva, user.countryMoneySymbol, user.codigoPrograma, periodo, nivelCaminoBrillante, puntaje, Constant.VERS_LOGROS_CAMINO_BRILLANTE)
                                .flatMap { resume ->
                                    Observable.zip(
                                        this.accountDataStoreFactory.createDB().saveConfigResume(resume.config),
                                        this.caminobrillanteDataStoreFactory.createDB().saveNivelesConsultora(cargarCaminoBrillante, resume.caminobrillante?.nivelesConsultoraCaminoBrillanteEntity),
                                        this.caminobrillanteDataStoreFactory.createDB().saveNivelesCaminoBrillante(cargarCaminoBrillante, resume.caminobrillante?.nivelesCaminoBrillante),
                                        this.caminobrillanteDataStoreFactory.createDB().saveLogrosCaminoBrillante(cargarCaminoBrillante, resume.caminobrillante?.resumenLogroCaminoBrillanteEntity, resume.caminobrillante?.logrosCaminoBrillanteEntity),
                                        Function4<Boolean?, Boolean, Boolean, Boolean, Boolean> { t1, t2, t3, t4 -> sessionDataStore.saveLastUpdateCaminoBrillante(System.currentTimeMillis()) }
                                    )
                                }
                        }

                    }
                }
            }
        }
    }

    override fun recovery(recoveryRequest: RecoveryRequest?): Observable<String?> {
        val dataStore = this.accountDataStoreFactory.create()
        return dataStore.recovery(recoveryRequestEntityDataMapper.transform(recoveryRequest))
    }

    override fun update(updateRequest: UserUpdateRequest?): Observable<Boolean?> {

        val entity = userUpdateRequestEntityDataMapper.transform(updateRequest)

        val accountDataStore = accountDataStoreFactory.createCloudDataStore()
        val accountLocalDataStore = accountDataStoreFactory.createDB()
        return accountDataStore.update(entity)
            .flatMap { accountLocalDataStore.update(userUpdateRequestEntityDataMapper.transformUser(updateRequest)) }
    }

    override fun updatePassword(updateRequest: PasswordUpdateRequest?)
        : Observable<BasicDto<Boolean>?> {

        val entity = userUpdateRequestEntityDataMapper.transform(updateRequest)
        val accountDataStore = accountDataStoreFactory.createCloudDataStore()
        return accountDataStore.updatePassword(entity)
            .map { basicDtoDataMapper.transformBoolean(it as ServiceDto<Boolean>) }
    }

    override fun deletePhotoFB(updateRequest: UserUpdateRequest?): Observable<Boolean?> {
        val cloudAccountDataStore = accountDataStoreFactory.createCloudDataStore()
        val accountLocalDataStore = accountDataStoreFactory.createDB()
        return cloudAccountDataStore.updatePhoto("")
            .flatMap { accountLocalDataStore.updatePhoto("") }
    }

    override fun deletePhotoServer(updateRequest: UserUpdateRequest?): Observable<Boolean?> {
        val cloudAccountDataStore = accountDataStoreFactory.createCloudDataStore()
        val accountLocalDataStore = accountDataStoreFactory.createDB()
        return cloudAccountDataStore.deletePhotoServer(userUpdateRequestEntityDataMapper.transform(updateRequest)) //Servidor
            .flatMap {
                cloudAccountDataStore.updatePhoto("")
                    .flatMap { accountLocalDataStore.updatePhoto("") }
            }
    }

    override fun associate(associateRequest: AssociateRequest?): Observable<String?> {
        val dataStore = this.accountDataStoreFactory.create()
        return dataStore.associate(associateRequestEntityDataMapper.transform(associateRequest))
            .map { result -> result }
    }

    override fun terms(terms: Boolean?, privacity: Boolean?): Observable<Boolean?> {
        val dataStore = this.accountDataStoreFactory.create()
        return dataStore.terms(accountDataMapper.transform(terms, privacity))
    }

    override fun uploadFile(contentType: String?, requestFile: MultipartBody?)
        : Observable<Boolean?> {

        val cloudAccountDataStore = accountDataStoreFactory.createCloudDataStore()
        val accountLocalDataStore = accountDataStoreFactory.createDB()
        return cloudAccountDataStore.uploadFile(contentType, requestFile)
            .flatMap { uploadFile ->
                cloudAccountDataStore.updatePhoto(uploadFile.nameImage)
                    .flatMap { accountLocalDataStore.updatePhoto(uploadFile.urlImage) }
            }
    }

    override fun enableSDK(): Observable<Boolean?> {

        val userDataStore = userDataStoreFactory.create()
        val countryLocalDataStore = countryDataStoreFactory.createDB()

        return userDataStore.getWithObservable().flatMap {

            if (it.isAceptaPoliticaPrivacidad) {
                countryLocalDataStore.find(it.countryISO).flatMap { c ->
                    val capture = c.isCaptureData ?: false
                    if (capture) Observable.just(true)
                    else Observable.just(false)
                }
            } else {
                Observable.just(false)
            }
        }
    }

    override fun enviarCorreo(email: String): Observable<BasicDto<Boolean>?> {
        val accountDataStore = accountDataStoreFactory.create()
        val userDataStore = userDataStoreFactory.createDB()

        return userDataStore.getWithObservable().flatMap {

            accountDataStore.enviarCorreo(EnviarCorreoRequest().apply {
                correo = it.email
                nombreConsultora = it.consultantName
                sobrenombre = it.alias
                primerNombre = it.primerNombre
                puedeActualizar = it.isPuedeActualizar
                this.correoNuevo = email

            }).map { r -> basicDtoDataMapper.transformBoolean(r as ServiceDto<Boolean>) }
        }
    }

    override suspend fun enviarCorreoCoroutine(email: String): BasicDto<Boolean>? {
        val accountDataStore = accountDataStoreFactory.create()
        val userDataStore = userDataStoreFactory.createDB()

        return userDataStore.getWithCoroutine()?.let {
            val respuesta = accountDataStore.enviarCorreoCoroutine(EnviarCorreoRequest().apply {
                correo = it.email
                nombreConsultora = it.consultantName
                sobrenombre = it.alias
                primerNombre = it.primerNombre
                puedeActualizar = it.isPuedeActualizar
                this.correoNuevo = email
            }).await()
            basicDtoDataMapper.transformBoolean(respuesta as ServiceDto<Boolean>)
        }



    }

    override fun sendSMS(smsRequest: SMSResquest): Observable<BasicDto<Boolean>?> {
        val cloudAccountDataStore = accountDataStoreFactory.createCloudDataStore()
        return cloudAccountDataStore.sendSMS(accountDataMapper.transform(smsRequest))
            .map { basicDtoDataMapper.transformBoolean(it as ServiceDto<Boolean>) }
    }

    override fun confirmSMSCode(smsRequest: SMSResquest): Observable<BasicDto<Boolean>?> {
        val cloudAccountDataStore = accountDataStoreFactory.createCloudDataStore()
        return cloudAccountDataStore.confirmSMSCode(accountDataMapper.transform(smsRequest))
            .map { basicDtoDataMapper.transformBoolean(it as ServiceDto<Boolean>) }
    }

    override fun contratoCredito(request: CreditAgreement): Observable<BasicDto<Boolean>?> {
        val cloudStore = accountDataStoreFactory.createCloudDataStore()
        return cloudStore.contratoCredito(accountDataMapper.transform(request))
            .map { basicDtoDataMapper.transformBoolean(it as ServiceDto<Boolean>) }
    }

    override fun verificacion(): Observable<Verificacion?> {
        val cloudStore = accountDataStoreFactory.createCloudDataStore()
        val dbStore = accountDataStoreFactory.createDB()

        val userDataStore = this.userDataStoreFactory.createDB()
        return userDataStore.getWithObservable().flatMap {
            it.tipoIngreso = TipoIngreso.ESTANDAR
            userDataStore.save(it)
        }
            .flatMap {
                cloudStore.verificacion().flatMap { v -> dbStore.saveVerificacion(v) }
                    .map { v -> verificacionEntityDataMapper.transform(v) }
            }
    }

    override fun verificacionOffline(): Observable<Verificacion?> {
        val dbStore = accountDataStoreFactory.createDB()
        return dbStore.verificacion().map { verificacionEntityDataMapper.transform(it) }
    }

    override fun getAcademyUrl(): Observable<AcademyUrl?> {
        val userDataStore = userDataStoreFactory.createDB()
        val dataStore = accountDataStoreFactory.create()

        return userDataStore.getWithObservable().flatMap {
            dataStore.getAcademyUrl(
                it.campaing,
                it.email,
                it.segmentoConstancia,
                it.esLider,
                it.nivelLider,
                it.campaniaInicioLider,
                it.seccionGestionLider)
        }.map { academyUrlEntityDataMapper.transform(it) }
    }

    override fun configResumeActive(code: String): Observable<Boolean?> {
        val dataStore = accountDataStoreFactory.createDB()
        return dataStore.configResumeActive(code)
    }

    override suspend fun configResumeActiveCoroutine(code: String): Boolean? {
      return accountDataStoreFactory.createDB().configResumeActiveCoroutine(code)
    }

    override fun getConfigResume(code: String): Observable<Collection<UserConfigData?>?> {
        val dataStore = accountDataStoreFactory.createDB()
        return dataStore.getConfigResumeAsObservable(code).map { accountDataMapper.transform(it) }
    }

    override fun getConfigResumen(request: ResumenRequest): Observable<BasicDto<Collection<ContenidoResumen>>> {
        val dataStore = this.accountDataStoreFactory.create()
        return dataStore.getContenidoResumen(accountDataMapper.transform(request))
            .map {accountDataMapper.transform(it)}
    }

    override suspend fun getConfigResumenCoroutine(request: ResumenRequest): BasicDto<Collection<ContenidoResumen>> {
        val requestTransformed = accountDataMapper.transform(request)
        val dataSave = this.accountDataStoreFactory.createDB()

        return this.accountDataStoreFactory.create().getContenidoResumenCoroutine(requestTransformed).await().let {
            dataSave.saveContenidoResumenCoroutine(it.data)
            accountDataMapper.transform(it)
        }
    }

    override suspend fun getConfig(code: String): List<UserConfigData> {
        return this.accountDataStoreFactory.createDB().getConfigResumen(code).await().let {
            this.accountDataMapper.transform(it) as List<UserConfigData>
        }
    }

    override suspend fun getConfigByCodeId(codeId: String): UserConfigData? {
        return this.accountDataStoreFactory.createDB().getConfigResumenByCodeId(codeId).await().let {
            this.accountDataMapper.transform(it)
        }
    }

    override fun getConfigAsObservable(code: String): Observable<Collection<UserConfigData?>?> {
        val dataStore = accountDataStoreFactory.createDB()
        return dataStore.getConfigAsObservable(code).map { accountDataMapper.transform(it) }
    }

    override suspend fun saveConfig(code1: String, code2: String, value1: String, value3: String): Boolean {
        val dataStore = accountDataStoreFactory.createDB()
        return dataStore.saveConfig(code1, code2, value1, value3).await()
    }

    override suspend fun checkContenidoResumenIfExist(codeResumen: String, codeDetail: String): Boolean {
        return accountDataStoreFactory.createDB().checkContenidoResumenIfExist(codeResumen,codeDetail).await()
    }
    override suspend fun getContenidoResumenAsync(request: ResumenRequest): BasicDto<Collection<ContenidoResumen>> {
        val dataStore = this.accountDataStoreFactory.create()
        val resumeConfig = dataStore.getContenidoResumenAsync(accountDataMapper.transform(request)).await()
        return accountDataMapper.transform(resumeConfig)

    }
}
