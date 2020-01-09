package biz.belcorp.consultoras.data.repository.datasource.auth

import biz.belcorp.consultoras.data.entity.AnalyticsTokenResponseEntity
import biz.belcorp.consultoras.data.entity.CredentialsEntity
import biz.belcorp.consultoras.data.entity.LoginEntity
import biz.belcorp.consultoras.data.entity.SessionEntity
import biz.belcorp.consultoras.data.entity.TokenEntity
import biz.belcorp.consultoras.data.manager.ISessionManager
import biz.belcorp.consultoras.data.net.service.IAuthService
import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.library.net.exception.ExpiredTokenException
import biz.belcorp.library.security.AesEncryption
import biz.belcorp.library.security.BelcorpEncryption
import io.reactivex.Observable
import io.reactivex.functions.Function
import kotlinx.coroutines.Deferred
import okhttp3.Credentials

/**
 * Clase de Login encargada de obtener los datos desde un servicio
 *
 * @version 1.0
 * @since 2017-04-14
 */
class AuthCloudDataStore
/**
 * Constructor que recibe el servicio ha ser ejecutado
 *
 * @param service Clase encargado de ejecutar un metodo del servicio
 */
internal constructor(private val service: IAuthService, private val sessionManager: ISessionManager) : AuthDataStore {

    private val encryption: BelcorpEncryption

    init {
        this.encryption = AesEncryption.newInstance()
    }

    /**
     * Metodo de inicio de sesion de usuario desde un servicio
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun loginOnline(credentialsEntity: CredentialsEntity?): Observable<LoginEntity?> {
        credentialsEntity?.let {
            return service.login("password", it.username, it.password, it.pais, it.tipoAutenticacion, "")
        } ?: return Observable.error(NullPointerException(javaClass.canonicalName))
    }

    override fun loginOffline(credentialsEntity: CredentialsEntity?): Observable<Boolean?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    /**
     * Metodo de refresco del token de usuario desde un servicio
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun refreshToken(): Observable<LoginEntity?> {
        return service.login("refresh_token", "", "", "", 1, sessionManager.accessToken?.refreshToken)
            .onErrorResumeNext(askForRefreshTokenExceptions())
    }

    private fun askForRefreshTokenExceptions(): Function<Throwable, Observable<LoginEntity?>> {
        return Function { t ->
            when (t) {
                is ExpiredTokenException -> {
                    val session: SessionEntity? = sessionManager.get()
                    session?.let {
                        val credentials = CredentialsEntity()
                        credentials.pais = session.country
                        credentials.username = session.username
                        credentials.password = encryption.decrypt(session.password)
                        credentials.tipoAutenticacion = session.authType

                        this@AuthCloudDataStore.loginOnline(credentials)
                    } ?: Observable.error(t)

                }
                else -> Observable.error(t)
            }
        }
    }

    override fun getOToken(password: String): Deferred<TokenEntity> {
        return service.getToken(password, "client_credentials")
    }

    override fun getAnalyticsToken(grantType: String, clientId: String, clientSecret: String): Deferred<AnalyticsTokenResponseEntity?> {
        val username = clientId
        val password = clientSecret
        val authorization = Credentials.basic(username, password)
        return service.getAnalyticsToken(grantType, authorization)
    }

}
