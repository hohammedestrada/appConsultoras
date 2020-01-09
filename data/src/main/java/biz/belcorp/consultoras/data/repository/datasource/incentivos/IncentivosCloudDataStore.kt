package biz.belcorp.consultoras.data.repository.datasource.incentivos

import biz.belcorp.consultoras.data.entity.ConcursoEntity
import biz.belcorp.consultoras.data.entity.IncentivesRequestEntity
import biz.belcorp.consultoras.data.net.service.IIncentivosService
import biz.belcorp.consultoras.data.util.Constant
import io.reactivex.Observable

/**
 * Clase de Incentivos encargada de obtener los datos desde un servicio
 *
 * @version 1.0
 * @since 2017-04-14
 */
internal class IncentivosCloudDataStore
/**
 * Constructor que recibe el servicio ha ser ejecutado
 *
 * @param service Clase encargado de ejecutar un metodo del servicio
 */
(private val service: IIncentivosService) : IncentivosDataStore {

    override val incentives: Observable<List<ConcursoEntity?>?>
        get() = throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)

    /**
     * Servicio de tipo GET que obtiene a los incentivos
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun get(entity: IncentivesRequestEntity?): Observable<List<ConcursoEntity?>?> {
        entity?.let {
            return service[entity.campaingCode]
        } ?: return Observable.error(NullPointerException(javaClass.canonicalName))
    }

    override fun getHistory(entity: IncentivesRequestEntity?): Observable<List<ConcursoEntity?>?> {
        entity?.let {
            return service.getHistory(entity.campaingCode)
        } ?: return Observable.error(NullPointerException(javaClass.canonicalName))
    }

    override fun getContest(contestCode: String?): Observable<ConcursoEntity?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun save(contests: List<ConcursoEntity?>?): Observable<String?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

}
