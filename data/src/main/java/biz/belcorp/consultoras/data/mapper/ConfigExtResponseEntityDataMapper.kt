package biz.belcorp.consultoras.data.mapper


import android.util.Log
import biz.belcorp.consultoras.data.entity.ConfigExtResponseEntity
import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.entity.ConfigResponseEntity
import biz.belcorp.consultoras.domain.entity.ConfigExtReponse
import biz.belcorp.consultoras.domain.entity.ConfigReponse

/**
 * Clase encarga de realizar el mapeo de la entidad Config(tabla o json) a
 * una entidad del dominio
 *
 * @version 1.0
 * @since 2017-04-20
 */
@Singleton
class ConfigExtResponseEntityDataMapper @Inject
internal constructor(

                     private val ubicacionOrigenMarcacionEntityDataMapper: UbicacionOrigenMarcacionEntityDataMapper
) {

    /**
     * Transforma la entidad a una entidad del dominio.
     *
     * @param entity Entidad tipo tabla o json
     * @return object Entidad de dominio
     */
    fun transform(entity: ConfigExtResponseEntity?): ConfigExtReponse? {
        var obj: ConfigExtReponse? = null

        if (null != entity) {
            obj = ConfigExtReponse()
            obj.listaUbicacion= ubicacionOrigenMarcacionEntityDataMapper.transform(entity.listaUbicacion)
            obj.listaSeccion= ubicacionOrigenMarcacionEntityDataMapper.transform(entity.listaSeccion)
            obj.listaListas= entity.listaListas
            obj.listaPalancas= ubicacionOrigenMarcacionEntityDataMapper.transform(entity.listaPalancas)
            obj.listaSubseccion= ubicacionOrigenMarcacionEntityDataMapper.transform(entity.listaSubseccion)
        }
        return obj
    }


}
