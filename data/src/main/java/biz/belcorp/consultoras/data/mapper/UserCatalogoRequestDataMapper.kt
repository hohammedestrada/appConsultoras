package biz.belcorp.consultoras.data.mapper

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.entity.UserCatalogoRequestEntity
import biz.belcorp.consultoras.domain.entity.User

/**
 *
 */
@Singleton
class UserCatalogoRequestDataMapper @Inject
internal constructor() {

    /**
     * Transforma la entidad de dominio a una entidad.
     *
     * @param entity Entidad de dominio
     * @return object Entidad
     */
    fun transform(entity: User?, maxCampaign: Int?,mostrarSiguieteAnterior: Boolean = true): UserCatalogoRequestEntity? {
        return entity?.let {
            UserCatalogoRequestEntity().apply {
                campania = if(mostrarSiguieteAnterior)it.campaing else it.nextCampania
                codigoZona = it.zoneCode
                campaignNumber = maxCampaign
                isShowCurrent = true
                topLast = if(mostrarSiguieteAnterior) ACTIVO else INACTIVO
                topNext = if(mostrarSiguieteAnterior) ACTIVO else INACTIVO
                isBrillante = entity.isBrillante
            }
        }
    }

    companion object{
        const val ACTIVO = 1
        const val INACTIVO = 0
    }
}
