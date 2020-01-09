package biz.belcorp.consultoras.data.mapper

import java.util.ArrayList

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.entity.ConcursoEntity
import biz.belcorp.consultoras.domain.entity.Concurso

@Singleton
class ConcursoEntityDataMapper @Inject
internal constructor(private val nivelEntityDataMapper: NivelEntityDataMapper,
                     private val nivelProgramaNuevaEntityDataMapper: NivelProgramaNuevaEntityDataMapper) {

    fun transform(input: ConcursoEntity?): Concurso? {
        return input?.let {
            Concurso().apply {
                id = it.id
                campaniaId = it.campaniaId
                campaniaIDFin = it.campaniaIDFin
                campaniaIDInicio = it.campaniaIDInicio
                codigoConcurso = it.codigoConcurso
                tipoConcurso = it.tipoConcurso
                puntosAcumulados = it.puntosAcumulados
                isIndicadorPremioAcumulativo = it.isIndicadorPremioAcumulativo
                nivelAlcanzado = it.nivelAlcanzado
                nivelSiguiente = it.nivelSiguiente
                campaniaIDPremiacion = it.campaniaIDPremiacion
                puntajeExigido = it.puntajeExigido
                descripcionConcurso = it.descripcionConcurso
                estadoConcurso = it.estadoConcurso
                urlBannerCuponesProgramaNuevas = it.urlBannerCuponesProgramaNuevas
                urlBannerPremiosProgramaNuevas = it.urlBannerPremiosProgramaNuevas
                niveles = nivelEntityDataMapper.transform(it.niveles)
                nivelProgramaNuevas = nivelProgramaNuevaEntityDataMapper.transform(it.nivelesProgramaNuevas)
                codigoNivelProgramaNuevas = it.codigoNivelProgramaNuevas
                importePedido = it.importePedido
                textoCupon = it.textoCupon
                textoCuponIndependiente = it.textoCuponIndependiente
            }
        }
    }

    fun transform(input: List<ConcursoEntity?>?): Collection<Concurso?>? {
        if (null == input) { return emptyList() }
        return input
            .map { transform(it) }
            .filter { null != it }
    }

    fun transform(input1: Collection<ConcursoEntity>, input2: Collection<Concurso>)
        : List<Concurso> {
        val listaConcurso = transformCollection(input1)
        val listaHistorico = transformCollection2(input2)

        val listaMerge = ArrayList<Concurso>()
        listaConcurso?.filterNotNull()?.let { listaMerge.addAll(it) }
        listaHistorico?.filterNotNull()?.let { listaMerge.addAll(it) }

        return listaMerge
    }

    private fun transformCollection(input: Collection<ConcursoEntity?>?): List<Concurso?>? {
        if (null == input) { return emptyList() }
        return input
            .map { transform(it) }
            .filter { null != it }
    }

    private fun transformCollection2(input: Collection<Concurso?>?): List<Concurso?>? {
        if (null == input) { return emptyList() }
        return input.filter { null != it }
    }

    fun transformCombine(input1: Collection<ConcursoEntity>?, input2: Collection<ConcursoEntity>?)
        : List<ConcursoEntity> {
        val listaMerge = ArrayList<ConcursoEntity>()

        if (null != input1 && !input1.isEmpty()) listaMerge.addAll(input1)
        if (null != input2 && !input2.isEmpty()) listaMerge.addAll(input2)

        return listaMerge
    }
}
