package biz.belcorp.consultoras.domain.repository

import biz.belcorp.consultoras.domain.entity.*
import io.reactivex.Observable

/**
 * Interface que implementa los metodos para obtener los datos del usuario
 * tomando en cuenta la entidad que devuelve la respuesta del REST o el SQL
 *
 * @version 1.0
 * @since 2017-05-15
 */
interface UserRepository {

    /**
     * Metodo que obtiene la data de un usuario
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun getWithObservable(): Observable<User?>


    /**
     * Metodo que obtiene la data de un usuario
     *
     * @return Objeto que se ejecutara en un hilo diferente al principal
     */
    suspend fun getWithCoroutines(): User?

    /**
     * Metodo que obtiene la data de un usuario
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun getLogin(): Observable<Login?>

    /**
     * Metodo que guarda la data de un usuario desde Login
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun save(login: Login?): Observable<Boolean?>

    /**
     * Metodo que remueve la data del usuario
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun removeAll(): Observable<Boolean?>

    /**
     * Metodo que actualiza las aceptaciones de terminos y politicas de privacidad del usuario
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun updateAcceptances(terms: Boolean?, privacity: Boolean?): Observable<Boolean?>

    /**
     * Metodo que actualiza las aceptaciones de terminos y politicas de privacidad del usuario
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun updateAcceptancesCreditAgreement(creditAgreement: Boolean?): Observable<Boolean?>

    /**
     * Servicio que guarda la data de Notificacion Hybris
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun saveHybrisData(hybrisData: HybrisData?): Observable<Boolean?>

    fun updateTipoIngreso(pushNotification: String): Observable<Boolean?>

    /**
     * Servicio que verifica si se mostrara gana mas nativo
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun checkGanaMasNativo(): Observable<Boolean?>

}
