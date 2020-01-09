package biz.belcorp.consultoras.data.repository.datasource.config

import biz.belcorp.consultoras.data.entity.ConfigEntity
import biz.belcorp.consultoras.data.entity.ConfigResponseEntity
import biz.belcorp.consultoras.data.net.service.IConfigService
import biz.belcorp.consultoras.data.util.Constant
import io.reactivex.Observable

/**
 * Clase de Config encargada de obtener los datos desde un servicio
 *
 * @version 1.0
 * @since 2017-04-25
 */

class ConfigCloudDataStore
/**
 * Constructor que recibe el servicio ha ser ejecutado
 *
 * @param service Clase encargado de ejecutar un metodo del servicio
 */
(private val service: IConfigService) : ConfigDataStore {

    /**
     * Metodo que obtiene la entidad config de un servicio
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun get(): Observable<ConfigResponseEntity?> {
        return service.get()
    }

    override fun save(entity: ConfigResponseEntity?): Observable<Boolean?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun save(entity: ConfigEntity?): Observable<Boolean?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }


}
