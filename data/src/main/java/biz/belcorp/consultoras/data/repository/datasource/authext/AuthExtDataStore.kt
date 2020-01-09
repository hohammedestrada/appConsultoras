package biz.belcorp.consultoras.data.repository.datasource.authext


import biz.belcorp.consultoras.data.entity.AuthExtResponseEntity
import biz.belcorp.consultoras.data.entity.CredentialsEntity
import biz.belcorp.consultoras.data.entity.LoginEntity
import io.reactivex.Observable

/**
 * Interface que implementa los metodos para obtener los datos del loginOnline
 * tomando en cuenta la entidad que devuelve la respuesta del REST o el SQL
 *
 * @version 1.0
 * @since 2017-04-14
 */
interface AuthExtDataStore {


    fun get(credentialsEntity: CredentialsEntity?): Observable<AuthExtResponseEntity?>

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

}
