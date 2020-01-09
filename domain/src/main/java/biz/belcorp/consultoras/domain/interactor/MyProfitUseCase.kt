package biz.belcorp.consultoras.domain.interactor

import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.repository.ProfitRepository
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject

/**
 * Clase que define los casos de uso de profits
 * para cumplir lo descrito en la historia de usuario
 *
 * @version 1.0
 * @since 2017-05-03
 */
class MyProfitUseCase @Inject
constructor(private val profitRepository: ProfitRepository,
            threadExecutor: ThreadExecutor,
            postExecutionThread: PostExecutionThread) : UseCase(threadExecutor, postExecutionThread) {

        fun getProfits(data: String?, observer: DisposableObserver<String?>) {
            execute(profitRepository.getProfits(data), observer)
        }

}
