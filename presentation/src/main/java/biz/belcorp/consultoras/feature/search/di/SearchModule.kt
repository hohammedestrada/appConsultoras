package biz.belcorp.consultoras.feature.search.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.interactor.OrderUseCase
import biz.belcorp.consultoras.domain.repository.*
import dagger.Module
import dagger.Provides

@Module
class SearchModule {

    @Provides
    @PerActivity
    fun providesOrdersUseCase(orderRepository: OrderRepository,
                              authRepository: AuthRepository,
                              sesionRepository: SessionRepository,
                              userRepository: UserRepository,
                              caminoBrillanteRepository: CaminoBrillanteRepository,
                              origenPedidoRepository: OrigenPedidoRepository,
                              threadExecutor: ThreadExecutor,
                              postExecutionThread: PostExecutionThread): OrderUseCase {
        return OrderUseCase(orderRepository, authRepository, sesionRepository, userRepository, caminoBrillanteRepository, origenPedidoRepository,
            threadExecutor, postExecutionThread)
    }

}
