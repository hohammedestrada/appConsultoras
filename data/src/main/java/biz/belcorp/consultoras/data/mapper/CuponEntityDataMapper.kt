package biz.belcorp.consultoras.data.mapper

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.entity.CuponEntity
import biz.belcorp.consultoras.domain.entity.Cupon


@Singleton
class CuponEntityDataMapper @Inject
internal constructor()//  EMPTY
{
    fun transform(input: CuponEntity?): Cupon? {
        return input?.let {
            Cupon().apply {
                id = it.id
                nivelProgramaLocalId = it.nivelProgramaLocalId
                codigoConcurso = it.codigoConcurso
                codigoCupon = it.codigoCupon
                codigoNivel = it.codigoNivel
                codigoVenta = it.codigoVenta
                descripcionProducto = it.descripcionProducto
                unidadesMaximas = it.unidadesMaximas
                indicadorCuponIndependiente = it.isIndicadorCuponIndependiente
                indicadorKit = it.isIndicadorKit
                numeroCampanasVigentes = it.numeroCampanasVigentes
                textoLibre = it.textoLibre
                precioUnitario = it.precioUnitario
                ganancia = it.ganancia
                urlImagenCupon = it.urlImagenProducto
            }
        }
    }

    fun transform(input: List<CuponEntity?>?): Collection<Cupon?>? {
        return input?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<Cupon>()
        }
    }
}
