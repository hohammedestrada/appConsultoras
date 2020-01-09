package biz.belcorp.consultoras.data.repository

import biz.belcorp.consultoras.data.entity.AnalyticsTokenResponseEntity
import biz.belcorp.consultoras.data.entity.UserResumeEntity
import biz.belcorp.consultoras.data.mapper.CredentialsEntityDataMapper
import biz.belcorp.consultoras.data.mapper.LoginEntityDataMapper
import biz.belcorp.consultoras.data.repository.datasource.account.AccountDataStoreFactory
import biz.belcorp.consultoras.data.repository.datasource.auth.AuthDataStoreFactory
import biz.belcorp.consultoras.data.repository.datasource.caminobrillante.CaminoBrillanteDataStoreFactory
import biz.belcorp.consultoras.data.repository.datasource.product.ProductDataStoreFactory
import biz.belcorp.consultoras.data.repository.datasource.session.SessionDataStoreFactory
import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.consultoras.domain.entity.AnalyticsToken
import biz.belcorp.consultoras.domain.entity.Credentials
import biz.belcorp.consultoras.domain.entity.Login
import biz.belcorp.consultoras.domain.repository.AuthRepository
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function4
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthDataRepository @Inject
internal constructor(private val authDataStoreFactory: AuthDataStoreFactory,
                     private val credentialsRequestEntityDataMapper: CredentialsEntityDataMapper,
                     private val caminobrillanteDataStoreFactory: CaminoBrillanteDataStoreFactory,
                     private val loginEntityDataMapper: LoginEntityDataMapper,
                     private val sessionDataStoreFactory: SessionDataStoreFactory,
                     private val accountDataStoreFactory: AccountDataStoreFactory,
                     private val productDataStoreFactory: ProductDataStoreFactory)
    : AuthRepository {

    override fun loginOnline(credentials: Credentials?): Observable<Login?> {
        val authDataStore = this.authDataStoreFactory.create()
        val sessionDataStore = this.sessionDataStoreFactory.create()
        val caminoBrillanteDataStore = this.caminobrillanteDataStoreFactory.createDB()

        return authDataStore.loginOnline(credentialsRequestEntityDataMapper.transform(credentials))
            .flatMap { login ->
                sessionDataStore.saveAccessToken(
                    biz.belcorp.library.net.AccessToken(
                        login.tokenType,
                        login.accessToken,
                        login.refreshToken
                    ))
                    .flatMap {

                        sessionDataStore.getCargarCaminoBrillante().flatMap { cargarCaminoBrillante ->

                            caminoBrillanteDataStore.getNivelConsultoraCaminoBrillanteAsObservable().flatMap { nivelCaminoBrillante ->
                                caminoBrillanteDataStore.getPuntajeAcumuladoAsObservable().flatMap { puntaje ->
                                    caminoBrillanteDataStore.getPeriodoCaminoBrillante().flatMap { periodoCaminoBrillante ->

                                        if (login.isIndicadorConsultoraDummy == true)
                                            Observable.zip(this.accountDataStoreFactory.create()
                                                .userResume(login.campaing, login.regionCode, login.zoneCode,
                                                    login.codigoSeccion, cargarCaminoBrillante, login.consecutivoNueva, login.consultoraNueva, login.countryMoneySymbol, login.codigoPrograma, periodoCaminoBrillante, nivelCaminoBrillante, puntaje, Constant.VERS_LOGROS_CAMINO_BRILLANTE).flatMap { resume ->
                                                    Observable.zip(
                                                        this.accountDataStoreFactory.createDB().saveConfigResume(resume.config),
                                                        this.caminobrillanteDataStoreFactory.createDB().saveNivelesConsultora(cargarCaminoBrillante, resume.caminobrillante?.nivelesConsultoraCaminoBrillanteEntity),
                                                        this.caminobrillanteDataStoreFactory.createDB().saveNivelesCaminoBrillante(cargarCaminoBrillante, resume.caminobrillante?.nivelesCaminoBrillante),
                                                        this.caminobrillanteDataStoreFactory.createDB().saveLogrosCaminoBrillante(cargarCaminoBrillante, resume.caminobrillante?.resumenLogroCaminoBrillanteEntity, resume.caminobrillante?.logrosCaminoBrillanteEntity),
                                                        Function4<Boolean?, Boolean?, Boolean?, Boolean?, UserResumeEntity> { t1, t2, t3, t4 -> resume }
                                                    )
                                                },
                                                this.productDataStoreFactory.createCloudDataStore()
                                                    .getPersonalization(login.campaing?.toInt()),
                                                BiFunction { t1, t2 ->
                                                    sessionDataStore.saveLastUpdateCaminoBrillante(System.currentTimeMillis())
                                                    login.personalizacionesDummy = t2
                                                    loginEntityDataMapper.transform(login, t1)!!
                                                })
                                        else
                                            this.accountDataStoreFactory.create().userResume(login.campaing,
                                                login.regionCode, login.zoneCode, login.codigoSeccion, cargarCaminoBrillante, login.consecutivoNueva, login.consultoraNueva, login.countryMoneySymbol, login.codigoPrograma, periodoCaminoBrillante, nivelCaminoBrillante, puntaje, Constant.VERS_LOGROS_CAMINO_BRILLANTE)
                                                .flatMap { resume ->
                                                    Observable.zip(
                                                        this.accountDataStoreFactory.createDB().saveConfigResume(resume.config),
                                                        this.caminobrillanteDataStoreFactory.createDB().saveNivelesConsultora(cargarCaminoBrillante, resume.caminobrillante?.nivelesConsultoraCaminoBrillanteEntity),
                                                        this.caminobrillanteDataStoreFactory.createDB().saveNivelesCaminoBrillante(cargarCaminoBrillante, resume.caminobrillante?.nivelesCaminoBrillante),
                                                        this.caminobrillanteDataStoreFactory.createDB().saveLogrosCaminoBrillante(cargarCaminoBrillante, resume.caminobrillante?.resumenLogroCaminoBrillanteEntity, resume.caminobrillante?.logrosCaminoBrillanteEntity),
                                                        Function4<Boolean?, Boolean?, Boolean?, Boolean?, Login?> { t1, t2, t3, t4 ->
                                                            sessionDataStore.saveLastUpdateCaminoBrillante(System.currentTimeMillis())
                                                            loginEntityDataMapper.transform(login, resume)!!
                                                        }
                                                    )
                                                }
                                    }
                                }
                            }
                        }
                    }
            }
    }

    override fun loginOffline(credentials: Credentials?): Observable<Boolean?> {
        val dataStore = this.authDataStoreFactory.createDB()
        return dataStore.loginOffline(credentialsRequestEntityDataMapper.transform(credentials))
            .map { result -> result }
    }

    override fun refreshToken(): Observable<Login?> {
        val authDataStore = this.authDataStoreFactory.create()
        val sessionDataStore = this.sessionDataStoreFactory.create()
        return authDataStore.refreshToken().map<Login> { loginEntityDataMapper.transform(it) }
            .flatMap { login ->
                sessionDataStore.saveAccessToken(
                    biz.belcorp.library.net.AccessToken(
                        login.tokenType,
                        login.accessToken,
                        login.refreshToken
                    )
                ).map { login }
            }
    }

    override suspend fun generateOAccessToken(): Deferred<String?> {
        return GlobalScope.async {
            val authDataStore = authDataStoreFactory.create()
            val sessionDataStore = sessionDataStoreFactory.create()

            val clientId = sessionDataStore.getApiClientId() ?: ""
            val clientSecret = sessionDataStore.getApiClientSecret() ?: ""

            val password = okhttp3.Credentials.basic(clientId, clientSecret)

            val token = authDataStore.getOToken(password).await().access_token
            sessionDataStore.saveOAccessToken(token)

            token
        }
    }


    override suspend fun getAnalyticsToken(grantType: String, clientId: String, clientSecret: String): Deferred<AnalyticsToken?> {
        val cloudDataStore = authDataStoreFactory.createCloudDataStore()
        return coroutineScope {
            async { AnalyticsTokenResponseEntity.transform(cloudDataStore
                .getAnalyticsToken(grantType, clientId, clientSecret).await())}
        }
    }
}
