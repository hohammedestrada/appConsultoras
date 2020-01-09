package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.VerificacionEntity
import biz.belcorp.consultoras.domain.entity.Verificacion
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Clase encarga de realizar el mapeo de la entidad menu(tabla o json) a
 * una entidad del dominio
 *
 * @version 1.0
 * @since 2017-04-14
 */
@Singleton
class VerificacionEntityDataMapper @Inject
internal constructor() {

    /**
     * Transforma la entidad a una entidad del dominio.
     *
     * @param entity Entidad tipo tabla o json
     * @return object Entidad de dominio
     */
    fun transform(entity: VerificacionEntity?): Verificacion? {
        return entity?.let {
            Verificacion().apply {
                mostrarOpcion = it.mostrarOpcion
                opcionVerificacionCorreo = it.opcionVerificacionCorreo
                mensajeSaludo = it.mensajeSaludo
                idEstadoActividad = it.idEstadoActividad
                correoEnmascarado = it.correoEnmascarado
                origenID = it.origenID
                horaRestanteCorreo = it.horaRestanteCorreo
                opcionVerificacionSMS = it.opcionVerificacionSMS
                intentosRestanteSms = it.intentosRestanteSms
                primerNombre = it.primerNombre
                celularEnmascarado = it.celularEnmascarado
                origenDescripcion = it.origenDescripcion
                opcionCambioClave = it.opcionCambioClave
                horaRestanteSms = it.horaRestanteSms
            }
        }
    }

    /**
     * Transforma la entidad de dominio a una entidad.
     *
     * @param entity Entidad de dominio
     * @return object Entidad
     */
    fun transform(entity: Verificacion?): VerificacionEntity? {
        return entity?.let {
            VerificacionEntity().apply {
                mostrarOpcion = it.mostrarOpcion
                opcionVerificacionCorreo = it.opcionVerificacionCorreo
                mensajeSaludo = it.mensajeSaludo
                idEstadoActividad = it.idEstadoActividad
                correoEnmascarado = it.correoEnmascarado
                origenID = it.origenID
                horaRestanteCorreo = it.horaRestanteCorreo
                opcionVerificacionSMS = it.opcionVerificacionSMS
                intentosRestanteSms = it.intentosRestanteSms
                primerNombre = it.primerNombre
                celularEnmascarado = it.celularEnmascarado
                origenDescripcion = it.origenDescripcion
                opcionCambioClave = it.opcionCambioClave
                horaRestanteSms = it.horaRestanteSms
            }
        }
    }

    /**
     * Transforma una lista de entidades a una lista de entidades de dominio
     *
     * @param list Lista de entidades
     * @return list Lista de entidades del dominio
     */
    fun transform(list: Collection<VerificacionEntity?>?): List<Verificacion?>? {
        return list?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<Verificacion>()
        }
    }

}
