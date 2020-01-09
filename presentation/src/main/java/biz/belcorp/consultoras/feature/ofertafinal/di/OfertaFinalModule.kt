package biz.belcorp.consultoras.feature.ofertafinal.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.interactor.OfferUseCase
import biz.belcorp.consultoras.domain.interactor.OrderUseCase
import biz.belcorp.consultoras.domain.interactor.PremioUseCase
import biz.belcorp.consultoras.domain.repository.*
import dagger.Module
import dagger.Provides

@Module
class OfertaFinalModule {
    @Provides
    @PerActivity
    fun providesOrdersUseCase(orderRepository: OrderRepository,
                              authRepository: AuthRepository,
                              sessionRepository: SessionRepository,
                              userRepository: UserRepository,
                              caminoBrillanteRepository: CaminoBrillanteRepository,
                              origenPedidoRepository: OrigenPedidoRepository,
                              threadExecutor: ThreadExecutor,
                              postExecutionThread: PostExecutionThread): OrderUseCase {
        return OrderUseCase(orderRepository, authRepository, sessionRepository, userRepository,caminoBrillanteRepository,
            origenPedidoRepository, threadExecutor, postExecutionThread)
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
    internal fun providesPremioUseCase(repository: PremioRepository,
                                       offerRepository: OfferRepository,
                                       threadExecutor: ThreadExecutor,
                                       postExecutionThread: PostExecutionThread): PremioUseCase {
        return PremioUseCase(offerRepository, repository, threadExecutor, postExecutionThread)
    }


}
