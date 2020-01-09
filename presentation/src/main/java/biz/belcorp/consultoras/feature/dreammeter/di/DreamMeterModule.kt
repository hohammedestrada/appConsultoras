package biz.belcorp.consultoras.feature.dreammeter.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.interactor.DreamMeterUseCase
import biz.belcorp.consultoras.domain.repository.DreamMeterRepository
import dagger.Module
import dagger.Provides

@Module
class DreamMeterModule {

    @Provides
    @PerActivity
    fun providesDreamMeterUseCase(dreamMeterRepository: DreamMeterRepository,
                                       threadExecutor: ThreadExecutor,
                                       postExecutionThread: PostExecutionThread): DreamMeterUseCase {
        return DreamMeterUseCase(dreamMeterRepository, threadExecutor, postExecutionThread)
    }

}
