package biz.belcorp.consultoras.data.mapper

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.entity.PremioNuevasEntity
import biz.belcorp.consultoras.domain.entity.PremioNueva

@Singleton
class PremioNuevasEntityDataMapper @Inject
internal constructor()// EMPTY
{

    fun transform(input: PremioNuevasEntity?): PremioNueva? {
        return input?.let{
            PremioNueva().apply {
                id = it.id
                nivelProgramaLocalId = it.nivelProgramaLocalId
                codigoConcurso = it.codigoConcurso
                codigoNivel = it.codigoNivel
                CUV = it.cuv
                descripcionProducto = it.descripcionProducto
                indicadorKitNuevas = it.isIndicadorKitNuevas
                precioUnitario = it.precioUnitario
                urlImagenPremio = it.urlImagenProducto
            }
        }
    }

    fun transform(input: List<PremioNuevasEntity?>?): Collection<PremioNueva?>? {
        return input?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<PremioNueva>()
        }
    }

}
