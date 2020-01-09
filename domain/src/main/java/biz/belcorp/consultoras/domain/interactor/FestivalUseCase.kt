package biz.belcorp.consultoras.domain.interactor

import biz.belcorp.consultoras.domain.entity.FestivalConfiguracion
import biz.belcorp.consultoras.domain.entity.FestivalSello
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.repository.FestivalRepository
import io.reactivex.observers.DisposableObserver
import kotlinx.coroutines.Deferred
import javax.inject.Inject

class FestivalUseCase @Inject
constructor(private val festivalRepository: FestivalRepository
            , threadExecutor: ThreadExecutor
            , postExecutionThread: PostExecutionThread)
    : UseCase(threadExecutor, postExecutionThread) {

     suspend fun getConfiguracion(grantype: Int): FestivalConfiguracion? {
         return festivalRepository.getConfiguracion(grantype).await()
    }

    suspend fun getSelloConfiguracion(): FestivalSello?{
        return festivalRepository.getSelloConfiguracion()
    }

    suspend fun getLocalConfiguration() : FestivalConfiguracion?{
        val result = festivalRepository.getLocalConfiguracion()
        return result
    }

}
