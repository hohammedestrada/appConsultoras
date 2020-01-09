package biz.belcorp.consultoras.domain.repository

import biz.belcorp.consultoras.domain.entity.AuthExtReponse
import biz.belcorp.consultoras.domain.entity.Credentials
import biz.belcorp.consultoras.domain.entity.Login
import io.reactivex.Observable

interface AuthExtRepository {


    fun get(credentials: Credentials?): Observable <AuthExtReponse>
    fun loginOnline(credentials: Credentials?): Observable<Boolean?>
    fun loginOffline(credentials: Credentials?): Observable<Boolean?>
    fun refreshToken(): Observable<Login?>

}
