package biz.belcorp.consultoras.data.mapper

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.entity.CredentialsEntity
import biz.belcorp.consultoras.domain.entity.Credentials

/**
 * Clase encarga de realizar el mapeo de la entidad loginOnline(tabla o json) a
 * una entidad del dominio
 *
 * @version 1.0
 * @since 2017-04-14
 */
@Singleton
class CredentialsEntityDataMapper @Inject
internal constructor() {

    /**
     * Transforma la entidad a una entidad del dominio.
     *
     * @param entity Entidad tipo tabla o json
     * @return object Entidad de dominio
     */
    fun transform(entity: CredentialsEntity?): Credentials? {
        return entity?.let {
            Credentials().apply {
                pais = it.pais
                username = it.username
                password = it.password
                tipoAutenticacion = it.tipoAutenticacion
            }
        }
    }

    /**
     * Transforma la entidad de dominio a una entidad.
     *
     * @param entity Entidad de dominio
     * @return object Entidad
     */
    fun transform(entity: Credentials?): CredentialsEntity? {
        return entity?.let {
            CredentialsEntity().apply {
                pais = it.pais
                username = it.username
                password = it.password
                tipoAutenticacion = it.tipoAutenticacion
            }
        }
    }

}
