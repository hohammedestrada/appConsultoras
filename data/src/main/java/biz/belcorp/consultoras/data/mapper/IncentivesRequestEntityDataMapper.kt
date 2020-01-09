package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.IncentivesRequestEntity
import biz.belcorp.consultoras.domain.entity.IncentivesRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IncentivesRequestEntityDataMapper @Inject
internal constructor() {

    /**
     * Transforma la entidad a una entidad del dominio.
     *
     * @param entity Entidad tipo tabla o json
     * @return object Entidad de dominio
     */
    fun transform(entity: IncentivesRequestEntity?): IncentivesRequest? {
        return entity?.let {
            IncentivesRequest().apply {
                countryISO = it.countryISO
                campaingCode = it.campaingCode
                consultantCode = it.consultantCode
                regionCode = it.regionCode
                zoneCode = it.zoneCode
                tipoConcurso = it.tipoConcurso
            }
        }
    }

    /**
     * Transforma la entidad de dominio a una entidad.
     *
     * @param oDomain Entidad de dominio
     * @return object Entidad
     */
    fun transform(oDomain: IncentivesRequest?): IncentivesRequestEntity? {
        return oDomain?.let {
            IncentivesRequestEntity().apply {
                countryISO = it.countryISO
                campaingCode = it.campaingCode
                consultantCode = it.consultantCode
                regionCode = it.regionCode
                zoneCode = it.zoneCode
                tipoConcurso = it.tipoConcurso
            }
        }
    }

}
