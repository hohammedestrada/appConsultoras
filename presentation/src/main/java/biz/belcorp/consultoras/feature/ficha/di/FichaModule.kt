package biz.belcorp.consultoras.feature.ficha.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.interactor.OrderUseCase
import biz.belcorp.consultoras.domain.interactor.OrigenPedidoUseCase
import biz.belcorp.consultoras.domain.repository.*
import dagger.Module
import dagger.Provides


@Module
class FichaModule {

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
        return OrderUseCase(orderRepository, authRepository, sessionRepository, userRepository, caminoBrillanteRepository, origenPedidoRepository,
            threadExecutor, postExecutionThread)
    }

}
