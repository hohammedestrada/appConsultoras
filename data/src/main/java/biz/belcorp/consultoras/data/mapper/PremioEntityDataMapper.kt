package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.PremioEntity
import biz.belcorp.consultoras.domain.entity.Opcion
import biz.belcorp.consultoras.domain.entity.Premio
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PremioEntityDataMapper @Inject
internal constructor()// EMPTY
{
    fun transform(input: PremioEntity?): Premio? {
        return input?.let {
            Premio().apply{
                id = it.id
                nivelLocalId = it.nivelLocalId
                codigoPremio = it.codigoPremio
                descripcionPremio = it.descripcionPremio
                numeroPremio = it.numeroPremio
                imagenPremio = it.imagenPremio
            }
        }
    }

    fun transform(input: List<PremioEntity?>?): Collection<Premio?>? {
        return input?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<Premio>()
        }
    }

    fun transformToOpcion(input: List<PremioEntity?>?): Collection<Opcion?>? {
        return input?.let {

            val listNumerosOpciones = getOpciones(it)
            val listOpciones = ArrayList<Opcion>()

            listNumerosOpciones.forEach { it1 ->

                val premiosfilter = it
                    .map { it2 -> transform(it2) }
                    .filter { it2 -> null != it2 && it2.numeroPremio == it1 }

                listOpciones.add(
                    Opcion().apply {
                        opcion = it1
                        premios = premiosfilter
                    }
                )
            }

            listOpciones

        } ?: run { emptyList<Opcion>() }
    }

    private fun getOpciones(list: List<PremioEntity?>?): List<Int> {
        return ArrayList<Int>().apply {
            list?.forEach {
                it?.numeroPremio?.let { it1 ->
                    add(it1)
                }
            }
        }.distinct().sorted()
    }
}
