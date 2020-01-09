package biz.belcorp.consultoras.data.repository.datasource.user

import biz.belcorp.consultoras.data.entity.HybrisDataEntity
import biz.belcorp.consultoras.data.entity.UserEntity
import biz.belcorp.consultoras.data.util.Constant
import io.reactivex.Observable

/**
 * Interface que implementa los metodos para obtener los datos del loginOnline
 * tomando en cuenta la entidad que devuelve la respuesta del REST o el SQL
 *
 * @version 1.0
 * @since 2017-05-15
 */
interface UserDataStore {

    /**
     * Metodo que refresca la data de un usuario
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun get(): UserEntity?

    fun getWithObservable(): Observable<UserEntity?>

    fun save(entity: UserEntity?): Observable<Boolean?>

    fun removeAll(): Observable<Boolean?>

    fun updateAcceptances(terms: Boolean?, privacity: Boolean?): Observable<Boolean?>

    fun updateAcceptancesCreditAgreement(creditAgreement: Boolean?): Observable<Boolean?>

    suspend fun getWithCoroutine(): UserEntity? {throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)}


    /**
     * Servicio que guarda la data de Notificacion Hybris
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun saveHybrisData(entity: HybrisDataEntity?): Observable<Boolean?>

    fun updateMount(mount: Double):Boolean

}
