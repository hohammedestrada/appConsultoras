package biz.belcorp.consultoras.domain.interactor

import biz.belcorp.consultoras.domain.entity.gallery.GalleryResponse
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.repository.GaleryRepository
import javax.inject.Inject


class GaleryUseCase @Inject constructor(threadExecutor: ThreadExecutor,
                                        postExecutionThread: PostExecutionThread,
                                        private val galeryRepository: GaleryRepository) :
    UseCase(threadExecutor, postExecutionThread) {

    suspend fun getGalery(campaign : String?): GalleryResponse? {
        return galeryRepository.getGalery(campaign)
    }
}
