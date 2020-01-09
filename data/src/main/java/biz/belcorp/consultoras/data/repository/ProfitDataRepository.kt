package biz.belcorp.consultoras.data.repository

import biz.belcorp.consultoras.domain.repository.ProfitRepository
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Clase de Client encargada de obtener la data desde una fuente de datos (interna o externa)
 * dicha clase se instancaria una sola vez y se mantendra mientras la aplicacion este viva
 *
 * @version 1.0
 * @since 2017-05-03
 */
@Singleton
class ProfitDataRepository
/**
 * Clase de Client encargada de obtener la data desde una fuente de datos (interna o externa)
 * dicha clase se instancaria una sola vez y se mantendra mientras la aplicacion este viva
 *
 * @version 1.0
 * @since 2017-05-03
 */
@Inject
internal constructor() : ProfitRepository {

    override fun getProfits(data: String?): Observable<String?> {
        throw UnsupportedOperationException("no implementado aún")
    }

}
