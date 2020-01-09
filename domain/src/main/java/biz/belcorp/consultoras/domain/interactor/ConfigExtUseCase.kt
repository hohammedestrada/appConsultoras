package biz.belcorp.consultoras.domain.interactor


import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.repository.ConfigExtRepository
import javax.inject.Inject


class ConfigExtUseCase
@Inject
constructor(private val repository: ConfigExtRepository, threadExecutor: ThreadExecutor,
            postExecutionThread: PostExecutionThread) : UseCase(threadExecutor, postExecutionThread) {

    suspend fun getWithCoroutines(token: String) {
        repository.getWithCoroutines(token).await()
    }

}
