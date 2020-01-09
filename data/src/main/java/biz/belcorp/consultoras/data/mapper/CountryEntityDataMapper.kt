package biz.belcorp.consultoras.data.mapper

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.entity.CountryEntity
import biz.belcorp.consultoras.domain.entity.Country

/**
 * Clase encarga de realizar el mapeo de la entidad tarea(tabla o json) a
 * una entidad del dominio
 *
 * @version 1.0
 * @since 2017-04-20
 */

@Singleton
class CountryEntityDataMapper @Inject
internal constructor() {

    /**
     * Transforma la entidad a una entidad del dominio.
     * C
     *
     * @param input Entidad tipo tabla o json
     * @return object Entidad de dominio
     */
    fun transform(input: CountryEntity?): Country? {
        return input?.let {
            Country().apply {
                id = it.id
                name = it.name
                urlImage = it.urlImage
                iso = it.iso
                focusBrand = it.focusBrand
                textHelpUser = it.textHelpUser
                textHelpPassword = it.textHelpPassword
                urlJoinBelcorp = it.urlJoinBelcorp
                configForgotPassword = it.configForgotPassword
                setShowDecimals(it.isShowDecimals())
                urlTerminos = it.urlTerminos
                urlPrivacidad = it.urlPrivacidad

                maxNoteAmount = it.maxNoteAmount
                maxMovementAmount = it.maxMovementAmount
                historicMovementMonth = it.historicMovementMonth

                receiverFeedBack = it.receiverFeedBack
                capturaDatosConsultora = it.isCaptureData

                telefono1 = it.telefono1
                telefono2 = it.telefono2
                urlContratoActualizacionDatos = it.urlContratoActualizacionDatos
                urlContratoVinculacion = it.urlContratoVinculacion

            }
        }
    }

    /**
     * Transforma la entidad de dominio a una entidad.
     *
     * @param input Entidad de dominio
     * @return object Entidad
     */
    fun transform(input: Country?): CountryEntity? {
        return input?.let {
            CountryEntity().apply {
                id = it.id
                name = it.name
                urlImage = it.urlImage
                iso = it.iso
                focusBrand = it.focusBrand
                textHelpUser = it.textHelpUser
                textHelpPassword = it.textHelpPassword
                urlJoinBelcorp = it.urlJoinBelcorp
                configForgotPassword = it.configForgotPassword
                setShowDecimals(it.isShowDecimals())
                urlTerminos = it.urlTerminos
                urlPrivacidad = it.urlPrivacidad

                maxNoteAmount = it.maxNoteAmount
                maxMovementAmount = it.maxMovementAmount
                historicMovementMonth = it.historicMovementMonth

                receiverFeedBack = it.receiverFeedBack
                isCaptureData = it.capturaDatosConsultora

                telefono1 = it.telefono1
                telefono2 = it.telefono2
                urlContratoActualizacionDatos = it.urlContratoActualizacionDatos
                urlContratoVinculacion = it.urlContratoVinculacion
            }
        }
    }

    /**
     * Transforma una lista de entidades a una lista de entidades de dominio
     *
     * @param list Lista de entidades
     * @return Lista de entidades del dominio
     */
    fun transformToDomainEntity(list: Collection<CountryEntity?>?): List<Country?>? {
        return list?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<Country>()
        }
    }


    /**
     * Transforma una lista de entidades de dominio a una lista de entidades
     *
     * @param list Lista de entidades del dominio
     * @return list Lista de entidades
     */
    fun transformToDataEntity(list: Collection<Country?>?): List<CountryEntity?>? {
        return list?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<CountryEntity>()
        }
    }
}
