package biz.belcorp.consultoras.domain.interactor


import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.repository.OrigenPedidoRepository
import javax.inject.Inject

class OrigenPedidoUseCase @Inject
constructor(private val repository: OrigenPedidoRepository,
            threadExecutor: ThreadExecutor, postExecutionThread: PostExecutionThread)
    : UseCase(threadExecutor, postExecutionThread) {

    suspend fun getValor(tipo: String, codigo: String): Int {
        return repository.getValor(tipo, codigo) ?: 0
    }
}
