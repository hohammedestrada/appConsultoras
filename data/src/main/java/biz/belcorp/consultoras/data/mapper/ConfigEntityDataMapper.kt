package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.ConfigEntity
import biz.belcorp.consultoras.domain.entity.Config
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Clase encarga de realizar el mapeo de la entidad loginOnline(tabla o json) a
 * una entidad del dominio
 *
 * @version 1.0
 * @since 2017-04-14
 */
@Singleton
class ConfigEntityDataMapper @Inject
internal constructor() {

    /**
     * Transforma la entidad a una entidad del dominio.
     *
     * @param entity Entidad tipo tabla o json
     * @return object Entidad de dominio
     */
    fun transform(entity: ConfigEntity?): Config? {
        return entity?.let {
            Config().apply {
                connectivityType = it.connectivityType
                isNotification = it.isNotification
                isSonido = it.isSonido
            }
        }
    }

    /**
     * Transforma la entidad de dominio a una entidad.
     *
     * @param entity Entidad de dominio
     * @return object Entidad
     */
    fun transform(entity: Config?): ConfigEntity? {
        return entity?.let {
            ConfigEntity().apply {
                connectivityType = it.connectivityType
                isNotification = it.isNotification
                isSonido = it.isSonido
            }
        }
    }

}
