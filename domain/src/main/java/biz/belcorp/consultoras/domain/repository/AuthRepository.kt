package biz.belcorp.consultoras.domain.repository

import biz.belcorp.consultoras.domain.entity.AnalyticsToken
import biz.belcorp.consultoras.domain.entity.Credentials
import biz.belcorp.consultoras.domain.entity.Login
import com.sun.org.apache.xpath.internal.operations.Bool
import io.reactivex.Observable
import kotlinx.coroutines.Deferred

interface AuthRepository {

    fun loginOnline(credentials: Credentials?): Observable<Login?>
    fun loginOffline(credentials: Credentials?): Observable<Boolean?>
    fun refreshToken(): Observable<Login?>
    suspend fun generateOAccessToken() : Deferred<String?>
    suspend fun getAnalyticsToken(grantType: String, clientId: String, clientSecret: String): Deferred<AnalyticsToken?>

}
