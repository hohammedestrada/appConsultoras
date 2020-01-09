package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.BannerLanzamientoEntity
import biz.belcorp.consultoras.domain.entity.BannerLanzamiento
import javax.inject.Inject


class BannerLanzamientoEntityDataMapper @Inject internal constructor() {

    fun transform(input: BannerLanzamiento?): BannerLanzamientoEntity? {
        return input?.let {
            BannerLanzamientoEntity().apply {
                grupo = it.grupo
                codigo = it.codigo
                url = it.url
                accion = it.accion
                orden = it.orden
                idContenido = it.idContenido
            }
        }
    }

    fun transform(input: BannerLanzamientoEntity?): BannerLanzamiento? {
        return input?.let {
            BannerLanzamiento().apply {
                grupo = it.grupo
                codigo = it.codigo
                url = it.url
                accion = it.accion
                orden = it.orden
                idContenido = it.idContenido
            }
        }
    }

    fun transformList(input: List<BannerLanzamientoEntity?>?): List<BannerLanzamiento?>? {
        return input?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<BannerLanzamiento>()
        }
    }

}
