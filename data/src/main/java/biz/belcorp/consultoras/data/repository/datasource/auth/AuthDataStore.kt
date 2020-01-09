package biz.belcorp.consultoras.data.repository.datasource.auth

import biz.belcorp.consultoras.data.entity.AnalyticsTokenResponseEntity
import biz.belcorp.consultoras.data.entity.CredentialsEntity
import biz.belcorp.consultoras.data.entity.LoginEntity
import biz.belcorp.consultoras.data.entity.TokenEntity
import io.reactivex.Observable
import kotlinx.coroutines.Deferred

/**
 * Interface que implementa los metodos para obtener los datos del loginOnline
 * tomando en cuenta la entidad que devuelve la respuesta del REST o el SQL
 *
 * @version 1.0
 * @since 2017-04-14
 */
interface AuthDataStore {

    /**
     * Metodo de loginOnline
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun loginOnline(credentialsEntity: CredentialsEntity?): Observable<LoginEntity?>

    /**
     * Metodo de loginOffline
     *
     * @param credentialsEntity Objeto que contiene los metodos para el ingreso a la aplicación
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun loginOffline(credentialsEntity: CredentialsEntity?): Observable<Boolean?>

    /**
     * Metodo de refreshToken
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun refreshToken(): Observable<LoginEntity?>

    /**
     * Metodo de getToken
     *
     * @param password objeto que contiene el clientId y clientSecret encondeado en base 64
     *
     * @return deffered Objeto que retorna el token
     */
    fun getOToken(password: String): Deferred<TokenEntity>

    fun getAnalyticsToken(grantType: String, clientId: String, clientSecret: String): Deferred<AnalyticsTokenResponseEntity?>

}
