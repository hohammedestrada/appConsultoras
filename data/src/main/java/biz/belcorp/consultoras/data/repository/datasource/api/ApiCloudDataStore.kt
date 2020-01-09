package biz.belcorp.consultoras.data.repository.datasource.api

import biz.belcorp.consultoras.data.entity.DeviceEntity
import biz.belcorp.consultoras.data.net.service.IApiService
import biz.belcorp.consultoras.data.util.Constant
import io.reactivex.Observable

/**
 * Clase de Incentivos encargada de obtener los datos desde un servicio
 *
 * @version 1.0
 * @since 2017-04-14
 */
internal class ApiCloudDataStore
/**
 * Constructor que recibe el servicio ha ser ejecutado
 *
 * @param service Clase encargado de ejecutar un metodo del servicio
 */
(private val service: IApiService) : ApiDataStore {

    override val device: Observable<DeviceEntity?>
        get() = throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)

    /**
     * Servicio de tipo GET que obtiene a los incentivos
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun saveDevice(entity: DeviceEntity?): Observable<Boolean?> {
        return service.saveDevice(entity)
    }

}
