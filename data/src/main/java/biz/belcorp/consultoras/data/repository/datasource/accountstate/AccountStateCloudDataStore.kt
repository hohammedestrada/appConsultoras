package biz.belcorp.consultoras.data.repository.datasource.accountstate

import biz.belcorp.consultoras.data.entity.AccountStateEntity
import biz.belcorp.consultoras.data.net.service.IAccountStateService
import biz.belcorp.consultoras.data.util.Constant
import io.reactivex.Observable

/**
 * Clase de Incentivos encargada de obtener los datos desde un servicio
 *
 * @version 1.0
 * @since 2017-04-14
 */
internal class AccountStateCloudDataStore
/**
 * Constructor que recibe el servicio ha ser ejecutado
 *
 * @param service Clase encargado de ejecutar un metodo del servicio
 */
(private val service: IAccountStateService) : AccountStateDataStore {

    /**
     * Servicio de tipo GET que obtiene a los incentivos
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun get(): Observable<List<AccountStateEntity?>?> {
        return service.get()
    }

    override fun save(entityList: List<AccountStateEntity?>?): Observable<List<AccountStateEntity?>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }
}
