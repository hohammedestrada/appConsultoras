package biz.belcorp.consultoras.domain.interactor

import javax.inject.Inject

import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.repository.BackupRepository

class BackupUseCase
/**
 * Constructor del caso de uso
 *
 * @param threadExecutor      Ejecutor de un metodo
 * @param postExecutionThread Tipo de ejecucion en un hilo diferente
 */
@Inject
constructor(private val repository: BackupRepository, threadExecutor: ThreadExecutor,
            postExecutionThread: PostExecutionThread) : UseCase(threadExecutor, postExecutionThread) {

    fun backup(observer: BaseObserver<Boolean?>) {
        execute(this.repository.backup(), observer)
    }

    fun reset(observer: BaseObserver<Boolean?>) {
        execute(this.repository.reset(), observer)
    }

    fun restore(observer: BaseObserver<Boolean?>) {
        execute(this.repository.restore(), observer)
    }
}
