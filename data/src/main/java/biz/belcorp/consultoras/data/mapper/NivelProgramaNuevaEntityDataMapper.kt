package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.NivelProgramaNuevaEntity
import biz.belcorp.consultoras.domain.entity.Cupon
import biz.belcorp.consultoras.domain.entity.NivelProgramaNueva
import biz.belcorp.consultoras.domain.entity.PremioNueva
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NivelProgramaNuevaEntityDataMapper @Inject
internal constructor(private val premioNuevasEntityDataMapper: PremioNuevasEntityDataMapper,
                     private val cuponEntityDataMapper: CuponEntityDataMapper) {

    fun transform(input: NivelProgramaNuevaEntity?): NivelProgramaNueva? {
        return input?.let {
            NivelProgramaNueva().apply {
                id = it.id
                concursoLocalId = it.concursoLocalId
                codigoConcurso = it.codigoConcurso
                codigoNivel = it.codigoNivel
                montoExigidoPremio = it.montoExigidoPremio
                montoExigidoCupon = it.montoExigidoCupon
                premiosNuevas = premioNuevasEntityDataMapper.transform(it.premiosNuevas) as List<PremioNueva?>?
                cupones = cuponEntityDataMapper.transform(it.cupones) as List<Cupon?>?
            }
        }
    }

    fun transform(input: List<NivelProgramaNuevaEntity?>?): Collection<NivelProgramaNueva?>? {
        return input?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<NivelProgramaNueva>()
        }
    }
}
