package biz.belcorp.consultoras.data.mapper

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.entity.HybrisDataEntity
import biz.belcorp.consultoras.domain.entity.HybrisData

/**
 * Clase encarga de realizar el mapeo de la entidad loginOnline(tabla o json) a
 * una entidad del dominio
 *
 * @version 1.0
 * @since 2017-04-14
 */
@Singleton
class HybrisDataEntityDataMapper @Inject
internal constructor() {

    /**
     * Transforma la entidad a una entidad del dominio.
     *
     * @param input Entidad tipo tabla o json
     * @return object Entidad de dominio
     */
    fun transform(input: HybrisDataEntity?): HybrisData? {
        return input?.let {
            HybrisData().apply {
                id = it.id
                trackingURL = it.trackingURL
            }
        }
    }

    /**
     * Transforma la entidad de dominio a una entidad.
     *
     * @param input Entidad de dominio
     * @return object Entidad
     */
    fun transform(input: HybrisData?): HybrisDataEntity? {
        return input?.let {
            HybrisDataEntity().apply {
                id = it.id
                trackingURL= it.trackingURL
            }
        }
    }

    /**
     * Transforma una lista de entidades a una lista de entidades de dominio
     *
     * @param list Lista de entidades
     * @return list Lista de entidades del dominio
     */
    fun transform(list: Collection<HybrisDataEntity?>?): List<HybrisData?>? {
        return list?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<HybrisData>()
        }
    }

    /**
     * Transforma una lista de entidades de dominio a una lista de entidades
     *
     * @param list Lista de entidades del dominio
     * @return list Lista de entidades
     */
    fun transformToEntity(list: Collection<HybrisData?>?): List<HybrisDataEntity?>? {
        return list?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<HybrisDataEntity>()
        }
    }
}
