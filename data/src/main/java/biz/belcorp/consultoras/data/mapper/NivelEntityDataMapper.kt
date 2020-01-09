package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.NivelEntity
import biz.belcorp.consultoras.domain.entity.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NivelEntityDataMapper @Inject
internal constructor(private val premioEntityDataMapper: PremioEntityDataMapper) {

    fun transform(input: NivelEntity?): Nivel? {
        return input?.let {
            Nivel().apply {
                id = it.id
                concursoLocalId = it.concursoLocalId
                codigoConcurso = it.codigoConcurso
                codigoNivel = it.codigoNivel
                puntosNivel = it.puntosNivel
                puntosFaltantes = it.puntosFaltantes
                isIndicadorPremiacionPedido = it.isIndicadorPremiacionPedido
                montoPremiacionPedido = it.montoPremiacionPedido
                isIndicadorBelCenter = it.isIndicadorBelCenter
                fechaVentaRetail = it.fechaVentaRetail
                isIndicadorNivelElectivo = it.isIndicadorNivelElectivo
                puntosExigidos = it.puntosExigidos
                puntosExigidosFaltantes = it.puntosExigidosFaltantes
                opciones = premioEntityDataMapper.transformToOpcion(it.premios) as List<Opcion?>?
            }
        }
    }

    fun transform(input: List<NivelEntity?>?): Collection<Nivel?>? {
        return input?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<Nivel>()
        }
    }
}
