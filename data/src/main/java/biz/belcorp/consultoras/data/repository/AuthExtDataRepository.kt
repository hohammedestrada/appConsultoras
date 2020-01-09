package biz.belcorp.consultoras.data.repository


import android.util.Log
import biz.belcorp.consultoras.data.entity.UserResumeEntity
import biz.belcorp.consultoras.data.mapper.CredentialsEntityDataMapper
import biz.belcorp.consultoras.data.mapper.LoginEntityDataMapper
import biz.belcorp.consultoras.data.repository.datasource.account.AccountDataStoreFactory
import biz.belcorp.consultoras.data.repository.datasource.auth.AuthDataStoreFactory
import biz.belcorp.consultoras.data.repository.datasource.caminobrillante.CaminoBrillanteDataStoreFactory
import biz.belcorp.consultoras.data.repository.datasource.product.ProductDataStoreFactory
import biz.belcorp.consultoras.data.repository.datasource.session.SessionDataStoreFactory
import biz.belcorp.consultoras.domain.entity.AuthExtReponse
import biz.belcorp.consultoras.domain.entity.Credentials
import biz.belcorp.consultoras.domain.entity.Login
import biz.belcorp.consultoras.domain.repository.AuthExtRepository
import biz.belcorp.consultoras.domain.repository.AuthRepository
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function4
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthExtDataRepository @Inject
internal constructor(private val authDataStoreFactory: AuthDataStoreFactory,
                     private val credentialsRequestEntityDataMapper: CredentialsEntityDataMapper,
                     private val loginEntityDataMapper: LoginEntityDataMapper,
                     private val sessionDataStoreFactory: SessionDataStoreFactory)
    : AuthExtRepository {



    override fun get(credentials: Credentials?): Observable<AuthExtReponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun loginOnline(credentials: Credentials?): Observable<Boolean?> {


        val authDataStore = this.authDataStoreFactory.create()
        val sessionDataStore = this.sessionDataStoreFactory.create()

        return authDataStore.loginOnline(credentialsRequestEntityDataMapper.transform(credentials))
            .flatMap { login ->
                sessionDataStore.saveAccessToken(
                    biz.belcorp.library.net.AccessToken(
                        login.tokenType,
                        login.accessToken,
                        login.refreshToken
                    ))
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
}
