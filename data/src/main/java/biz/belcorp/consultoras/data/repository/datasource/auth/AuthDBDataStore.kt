package biz.belcorp.consultoras.data.repository.datasource.auth


import biz.belcorp.consultoras.data.entity.AnalyticsTokenResponseEntity
import biz.belcorp.consultoras.data.entity.CredentialsEntity
import biz.belcorp.consultoras.data.entity.LoginEntity
import biz.belcorp.consultoras.data.entity.TokenEntity
import biz.belcorp.consultoras.data.manager.ISessionManager
import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.library.security.AesEncryption
import biz.belcorp.library.security.BelcorpEncryption
import biz.belcorp.library.sql.exception.SqlException
import io.reactivex.Observable
import kotlinx.coroutines.Deferred

class AuthDBDataStore(private val sessionManager: ISessionManager)
    : AuthDataStore {
    private val encryption: BelcorpEncryption

    init {
        this.encryption = AesEncryption.newInstance()
    }

    override fun loginOnline(credentialsEntity: CredentialsEntity?): Observable<LoginEntity?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun loginOffline(credentialsEntity: CredentialsEntity?): Observable<Boolean?> {
        return Observable.create { emitter ->
            try {
                val sessionEntity = sessionManager.get()

                credentialsEntity?.let {
                    sessionEntity?.let { _ ->
                        val result = credentialsEntity.pais == sessionEntity.country &&
                            (credentialsEntity.username == sessionEntity.username ||
                                credentialsEntity.username == sessionEntity.email) &&
                            credentialsEntity.password == encryption.decrypt(sessionEntity.password)
                        emitter.onNext(result)
                    } ?: emitter.onError(NullPointerException("sessionentitiy nulo"))
                    emitter.onComplete()
                } ?: emitter.onError(NullPointerException(javaClass.canonicalName))
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex))
            }
        }
    }

    override fun refreshToken(): Observable<LoginEntity?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getOToken(password: String): Deferred<TokenEntity> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getAnalyticsToken(grantType: String, clientId: String, clientSecret: String): Deferred<AnalyticsTokenResponseEntity?> {
        throw java.lang.UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

}
