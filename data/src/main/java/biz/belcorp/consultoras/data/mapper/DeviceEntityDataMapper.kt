package biz.belcorp.consultoras.data.mapper

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.entity.DeviceEntity
import biz.belcorp.consultoras.domain.entity.Device

/**
 * Clase encarga de realizar el mapeo de la entidad loginOnline(tabla o json) a
 * una entidad del dominio
 *
 * @version 1.0
 * @since 2017-04-14
 */
@Singleton
class DeviceEntityDataMapper @Inject
internal constructor() {

    /**
     * Transforma la entidad a una entidad del dominio.
     *
     * @param input Entidad tipo tabla o json
     * @return object Entidad de dominio
     */
    fun transform(input: DeviceEntity?): Device? {
        return input?.let {
            Device().apply {
                id = it.id
                appId = it.appID
                pais = it.pais
                rolId = it.rolId
                usuario = it.usuario
                uuid = it.uuid
                imei = it.imei
                so = it.so
                modelo = it.modelo
                version = it.version
                tokenFCM = it.tokenFCM
                topicFCM = it.topicFCM
            }
        }
    }

    /**
     * Transforma la entidad de dominio a una entidad.
     *
     * @param input Entidad de dominio
     * @return object Entidad
     */
    fun transform(input: Device?): DeviceEntity? {
        return input?.let {
            DeviceEntity().apply {
                id = it.id
                appID= it.appId
                pais = it.pais
                rolId = it.rolId
                usuario = it.usuario
                uuid = it.uuid
                imei = it.imei
                so = it.so
                modelo = it.modelo
                version = it.version
                tokenFCM = it.tokenFCM
                topicFCM = it.topicFCM
            }
        }
    }
}
