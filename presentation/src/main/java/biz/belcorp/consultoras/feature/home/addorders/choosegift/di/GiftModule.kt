package biz.belcorp.consultoras.feature.home.addorders.choosegift.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.interactor.ProductUseCase
import biz.belcorp.consultoras.domain.repository.AuthRepository
import biz.belcorp.consultoras.domain.repository.ProductRepository
import biz.belcorp.consultoras.domain.repository.SessionRepository
import dagger.Module
import dagger.Provides

@Module
class GiftModule {
    @Provides
    @PerActivity
    fun providesGiftModuleUseCase(threadExecutor: ThreadExecutor
                                  , postExecutionThread: PostExecutionThread
                                  , productRepository: ProductRepository
                                  , authRepository: AuthRepository): ProductUseCase{
        return ProductUseCase(threadExecutor,postExecutionThread,productRepository,authRepository)
    }
}
