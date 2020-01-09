package biz.belcorp.consultoras.feature.home.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.interactor.MenuUseCase
import biz.belcorp.consultoras.domain.interactor.OfferUseCase
import biz.belcorp.consultoras.domain.interactor.OrigenPedidoUseCase
import biz.belcorp.consultoras.domain.repository.AuthRepository
import biz.belcorp.consultoras.domain.repository.OrigenPedidoRepository
import biz.belcorp.consultoras.domain.repository.MenuRepository
import biz.belcorp.consultoras.domain.repository.OfferRepository
import biz.belcorp.consultoras.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import biz.belcorp.consultoras.domain.interactor.SurveyUseCase
import biz.belcorp.consultoras.domain.repository.SurveyRepository



/**
 * Módulo Dagger que proporciona los casos de usos del Home
 */
@Module
class HomeModule {

    @Provides
    @PerActivity
    internal fun providesMenuUseCase(menuRepository: MenuRepository,
                                     authRepository: AuthRepository,
                                     threadExecutor: ThreadExecutor,
                                     postExecutionThread: PostExecutionThread): MenuUseCase {
        return MenuUseCase(menuRepository, authRepository, threadExecutor, postExecutionThread)
    }

    @Provides
    @PerActivity
    internal fun providesOfferUseCase(offerRepository: OfferRepository,
                                      userRepository: UserRepository,
                                      threadExecutor: ThreadExecutor,
                                      postExecutionThread: PostExecutionThread): OfferUseCase {
        return OfferUseCase(offerRepository, userRepository, threadExecutor, postExecutionThread)
    }

    @Provides
    @PerActivity
    internal fun providesOrigenPedidoUseCase(origenPedidoRepository: OrigenPedidoRepository,
                                             threadExecutor: ThreadExecutor,
                                             postExecutionThread: PostExecutionThread): OrigenPedidoUseCase {
        return OrigenPedidoUseCase(origenPedidoRepository, threadExecutor, postExecutionThread)
    }

    @Provides
    @PerActivity
    internal fun providesSurveyUseCase(threadExecutor: ThreadExecutor,
                              postExecutionThread: PostExecutionThread,
                              surveyRepository: SurveyRepository): SurveyUseCase {
        return SurveyUseCase(threadExecutor, postExecutionThread, surveyRepository)
    }

}
