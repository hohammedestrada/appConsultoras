package biz.belcorp.consultoras.domain.interactor

import biz.belcorp.consultoras.domain.entity.ContenidoRequest
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.repository.AuthRepository
import biz.belcorp.consultoras.domain.repository.StorieRepository
import javax.inject.Inject

class StorieUseCase @Inject
constructor(

    threadExecutor: ThreadExecutor
    , postExecutionThread: PostExecutionThread,
    private val storieDataRepository: StorieRepository
    , private val authRepository: AuthRepository) : UseCase(threadExecutor, postExecutionThread) {

    fun saveStateContenido(contenidoRequest: ContenidoRequest, observer: BaseObserver<String?>) {
        execute(storieDataRepository.updateEstadoContenido(contenidoRequest), observer)
    }

    suspend fun saveStateContenidoCoroutine(contenidoRequest: ContenidoRequest): String? {
        return storieDataRepository.updateEstadoContenidoCoroutine(contenidoRequest)
    }
}
