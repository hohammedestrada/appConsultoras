package biz.belcorp.consultoras.feature.galery.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.interactor.GaleryUseCase
import biz.belcorp.consultoras.domain.repository.*
import dagger.Module
import dagger.Provides

@Module
class GalleryDetailModule {

    @Provides
    @PerActivity
    fun providesGaleryUseCase(galeryRepository: GaleryRepository,
                              threadExecutor: ThreadExecutor,
                              postExecutionThread: PostExecutionThread): GaleryUseCase {
        return GaleryUseCase(threadExecutor, postExecutionThread, galeryRepository)
    }

}
