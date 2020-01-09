package biz.belcorp.consultoras.data.repository.datasource.configext

import biz.belcorp.consultoras.data.entity.ConfigExtResponseEntity
import biz.belcorp.consultoras.data.net.service.IConfigExtService
import biz.belcorp.consultoras.data.util.Constant
import kotlinx.coroutines.Deferred

class ConfigExtCloudDataStore
/**
 * Constructor que recibe el servicio ha ser ejecutado
 *
 * @param service Clase encargado de ejecutar un metodo del servicio
 */
(private val service: IConfigExtService) : ConfigExtDataStore {

    /**
     * Metodo que obtiene la entidad config de un servicio
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */

    override fun getWithCoroutines(token: String): Deferred<ConfigExtResponseEntity?> {
        return service.get(token)
    }

    override fun saveWithCoroutines(entity: ConfigExtResponseEntity?): Boolean {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

}
