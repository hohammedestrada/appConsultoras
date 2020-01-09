package biz.belcorp.consultoras.feature.home.storie.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.interactor.StorieUseCase
import biz.belcorp.consultoras.domain.repository.AuthRepository
import biz.belcorp.consultoras.domain.repository.StorieRepository
import dagger.Module
import dagger.Provides

@Module
class StorieModule {
    @Provides
    @PerActivity
    fun providesStorieModuleUseCase(threadExecutor: ThreadExecutor
                                  , postExecutionThread: PostExecutionThread
                                  , storierepository: StorieRepository
                                  , authRepository: AuthRepository): StorieUseCase{
        return StorieUseCase(threadExecutor,postExecutionThread,storierepository,authRepository)
    }
}
