package biz.belcorp.consultoras.data.mapper

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.entity.RecoveryRequestEntity
import biz.belcorp.consultoras.domain.entity.RecoveryRequest

/**
 * Clase encarga de realizar el mapeo de la entidad tarea(tabla o json) a
 * una entidad del dominio
 *
 * @version 1.0
 * @since 2017-04-20
 */

@Singleton
class RecoveryRequestEntityDataMapper @Inject
internal constructor() {

    /**
     * Transforma la entidad a una entidad del dominio.
     *
     * @param entity Entidad tipo tabla o json
     * @return object Entidad de dominio
     */
    fun transform(entity: RecoveryRequestEntity?): RecoveryRequest? {
        return entity?.let {
            RecoveryRequest().apply {
                countryID = it.countryID
                username = it.username
            }
        }
    }

    /**
     * Transforma la entidad de dominio a una entidad.
     *
     * @param obj Entidad de dominio
     * @return object Entidad
     */
    fun transform(obj: RecoveryRequest?): RecoveryRequestEntity? {
        return obj?.let {
            RecoveryRequestEntity().apply {
                countryID = it.countryID
                username = it.username
            }
        }
    }


}
