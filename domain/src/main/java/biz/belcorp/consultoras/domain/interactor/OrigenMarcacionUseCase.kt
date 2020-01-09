package biz.belcorp.consultoras.domain.interactor


import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.repository.OrigenMarcacionRepository
import javax.inject.Inject

class OrigenMarcacionUseCase @Inject
constructor(private val repository: OrigenMarcacionRepository,
            threadExecutor: ThreadExecutor, postExecutionThread: PostExecutionThread)
    : UseCase(threadExecutor, postExecutionThread) {

    suspend fun getValorLista(tipo: String, codigo: String): String {
        return repository.getValor(tipo, codigo) ?: ""
    }


    suspend fun getValorPalanca(codigo: String): String {
        return repository.getValorPalanca(codigo) ?: NOT_FOUND
    }


    suspend fun getValorSubseccion(codigo: String): String {
        return repository.getValorSubseccion(codigo) ?: ""
    }

    companion object {

        const val NOT_FOUND = "(not available)"

    }

}
