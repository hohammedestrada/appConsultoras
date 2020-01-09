package biz.belcorp.consultoras.data.repository.datasource.user

import biz.belcorp.consultoras.data.entity.HybrisDataEntity
import biz.belcorp.consultoras.data.entity.UserEntity
import biz.belcorp.consultoras.data.net.service.IApiService
import biz.belcorp.consultoras.data.util.Constant
import io.reactivex.Observable

/**
 * Clase de Incentivos encargada de obtener los datos desde un servicio
 *
 * @version 1.0
 * @since 2017-04-14
 */
internal class UserCloudDataStore
/**
 * Constructor que recibe el servicio ha ser ejecutado
 *
 * @param service Clase encargado de ejecutar un metodo del servicio
 */
(private val service: IApiService) : UserDataStore {
    /**
     * Servicio de tipo GET que obtiene a los incentivos
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun getWithObservable(): Observable<UserEntity?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun get(): UserEntity? {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun save(entity: UserEntity?): Observable<Boolean?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun removeAll(): Observable<Boolean?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun updateAcceptances(terms: Boolean?, privacity: Boolean?): Observable<Boolean?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun updateAcceptancesCreditAgreement(creditAgreement: Boolean?): Observable<Boolean?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun saveHybrisData(entity: HybrisDataEntity?): Observable<Boolean?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun updateMount(mount: Double):Boolean {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

}
