package biz.belcorp.consultoras.data.repository.datasource.tracking

import biz.belcorp.consultoras.data.entity.TrackingEntity
import biz.belcorp.consultoras.data.net.service.ITrackingService
import biz.belcorp.consultoras.data.util.Constant
import io.reactivex.Observable

/**
 * Clase de Incentivos encargada de obtener los datos desde un servicio
 *
 * @version 1.0
 * @since 2017-04-14
 */
internal class TrackingCloudDataStore
/**
 * Constructor que recibe el servicio ha ser ejecutado
 *
 * @param service Clase encargado de ejecutar un metodo del servicio
 */
(private val service: ITrackingService) : TrackingDataStore {

    /**
     * Servicio de tipo GET que obtiene a los incentivos
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun get(top: Int?): Observable<List<TrackingEntity?>?> {
        return service.get(if (top == 0) Constant.TRACKING_TOP else top)
    }

    override fun save(list: List<TrackingEntity?>?): Observable<List<TrackingEntity?>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }
}
