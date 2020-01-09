package biz.belcorp.consultoras.feature.finaloffer.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.interactor.OrderUseCase
import biz.belcorp.consultoras.domain.repository.*
import dagger.Module
import dagger.Provides

/**
 * Módulo Dagger que proporciona los casos de usos del Home
 */
@Module
class FinalOfferModule {

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
        return OrderUseCase(orderRepository, authRepository, sessionRepository, userRepository,caminoBrillanteRepository, origenPedidoRepository,
            threadExecutor, postExecutionThread)
    }
}
