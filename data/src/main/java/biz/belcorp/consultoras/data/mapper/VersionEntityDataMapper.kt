package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.AppEntity
import biz.belcorp.consultoras.domain.entity.App
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Clase encarga de realizar el mapeo de la entidad tarea(tabla o json) a
 * una entidad del dominio
 *
 * @version 1.0
 * @since 2017-04-20
 */

@Singleton
class VersionEntityDataMapper @Inject
internal constructor() {

    /**
     * Transforma la entidad a una entidad del dominio.
     *
     * @param entity Entidad tipo tabla o json
     * @return object Entidad de dominio
     */
    fun transform(entity: AppEntity?): App? {
        return entity?.let{
            App().apply {
                aplicacion = it.aplicacion
                pais = it.pais
                so = it.so
                version = it.version
                minimaVersion = it.minimaVersion
                fechaLanzamiento = it.fechaLanzamiento
                fechaActualizacion = it.fechaActualizacion
                isRequiereActualizacion = it.isRequiereActualizacion
                tipoDescarga = it.tipoDescarga
                url = it.url
            }
        }
    }

    /**
     * Transforma la entidad de dominio a una entidad.
     *
     * @param output Entidad de dominio
     * @return object Entidad
     */
    fun transform(output: App?): AppEntity? {
        return output?.let{
            AppEntity().apply {
                aplicacion = it.aplicacion
                pais = it.pais
                so = it.so
                version = it.version
                minimaVersion = it.minimaVersion
                fechaLanzamiento = it.fechaLanzamiento
                fechaActualizacion = it.fechaActualizacion
                isRequiereActualizacion = it.isRequiereActualizacion
                tipoDescarga = it.tipoDescarga
                url = it.url
            }
        }
    }

    /**
     * Transforma una lista de entidades a una lista de entidades de dominio
     *
     * @param list Lista de entidades
     * @return list Lista de entidades del dominio
     */
    fun transformToDomainEntity(list: Collection<AppEntity?>?): List<App?>? {
        return list?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<App>()
        }
    }

    /**
     * Transforma una lista de entidades de dominio a una lista de entidades
     *
     * @param list Lista de entidades del dominio
     * @return list Lista de entidades
     */

    fun transformToDataEntity(list: Collection<App?>?): List<AppEntity?>? {
        return list?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<AppEntity>()
        }
    }

}
