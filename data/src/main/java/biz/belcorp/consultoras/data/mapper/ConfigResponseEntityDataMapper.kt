package biz.belcorp.consultoras.data.mapper

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.entity.ConfigResponseEntity
import biz.belcorp.consultoras.domain.entity.ConfigReponse

/**
 * Clase encarga de realizar el mapeo de la entidad Config(tabla o json) a
 * una entidad del dominio
 *
 * @version 1.0
 * @since 2017-04-20
 */
@Singleton
class ConfigResponseEntityDataMapper @Inject
internal constructor(private val countryEntityDataMapper: CountryEntityDataMapper,
                     private val versionEntityDataMapper: VersionEntityDataMapper,
                     private val origenPedidoEntityDataMapper: OrigenPedidoEntityDataMapper
                     ) {

    /**
     * Transforma la entidad a una entidad del dominio.
     *
     * @param entity Entidad tipo tabla o json
     * @return object Entidad de dominio
     */
    fun transform(entity: ConfigResponseEntity?): ConfigReponse? {
        var obj: ConfigReponse? = null

        Log.d("tesxt","conf--"+obj.toString())
        if (null != entity) {
            obj = ConfigReponse()
            obj.textGreeting = entity.textGreeting
            obj.urlImageEsikaBackground = entity.urlImageEsikaBackground
            obj.urlImageEsikaLogo = entity.urlImageEsikaLogo
            obj.urlImageLBelBackground = entity.urlImageLBelBackground
            obj.urlImageLBelLogo = entity.urlImageLBelLogo
            obj.idVideo = entity.idVideo
            obj.urlVideo = entity.urlVideo
            obj.countries = countryEntityDataMapper.transformToDomainEntity(entity.countries)
            obj.apps = versionEntityDataMapper.transformToDomainEntity(entity.apps)
            obj.origenPedidoWeb = origenPedidoEntityDataMapper.transform(entity.origenPedidoWeb)
        }
        return obj
    }

    /**
     * Transforma la entidad de dominio a una entidad.
     *
     * @param obj Entidad de dominio
     * @return object Entidad
     */
    fun transform(obj: ConfigReponse?): ConfigResponseEntity? {
        var entity: ConfigResponseEntity? = null

        if (null != obj) {
            entity = ConfigResponseEntity()
            entity.textGreeting = obj.textGreeting
            entity.urlImageEsikaBackground = obj.urlImageEsikaBackground
            entity.urlImageEsikaLogo = obj.urlImageEsikaLogo
            entity.urlImageLBelBackground = obj.urlImageLBelBackground
            entity.urlImageLBelLogo = obj.urlImageLBelLogo
            entity.urlVideo = obj.urlVideo
            entity.idVideo = obj.idVideo
            entity.countries = countryEntityDataMapper.transformToDataEntity(obj.countries)
            entity.apps = versionEntityDataMapper.transformToDataEntity(obj.apps)
        }
        return entity
    }

}
