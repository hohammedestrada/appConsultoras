package biz.belcorp.consultoras.data.repository.datasource.authext


import biz.belcorp.consultoras.data.entity.AuthExtResponseEntity
import biz.belcorp.consultoras.data.entity.CredentialsEntity
import biz.belcorp.consultoras.data.entity.LoginEntity
import biz.belcorp.consultoras.data.manager.ISessionManager
import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.library.security.AesEncryption
import biz.belcorp.library.security.BelcorpEncryption
import biz.belcorp.library.sql.exception.SqlException
import io.reactivex.Observable

class AuthExtDBDataStore(private val sessionManager: ISessionManager)
    : AuthExtDataStore {
//    override fun get(credentialsEntity: CredentialsEntity?): Observable<AuthExtResponseEntity?> {

//    }

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

    override fun get(credentialsEntity: CredentialsEntity?): Observable<AuthExtResponseEntity?> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
