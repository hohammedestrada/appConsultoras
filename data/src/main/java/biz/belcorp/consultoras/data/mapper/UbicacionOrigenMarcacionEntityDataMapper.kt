package biz.belcorp.consultoras.data.mapper


import biz.belcorp.consultoras.data.entity.*
import biz.belcorp.consultoras.domain.entity.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UbicacionOrigenMarcacionEntityDataMapper @Inject
internal constructor() {

    fun transform(list: List<UbicacionOrigenMarcacionEntity?>?): List<UbicacionOrigenMarcacion?>? {
        return list?.let {
            it.map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<UbicacionOrigenMarcacion>()
        }
    }

    fun transform(entity: UbicacionOrigenMarcacionEntity?): UbicacionOrigenMarcacion? {
        return entity?.let{
            UbicacionOrigenMarcacion().apply {
                codigo=it.codigo
                descripcion=it.descripcion
            }
        }
    }



}
