package biz.belcorp.consultoras.feature.stockouts.di;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.executor.PostExecutionThread;
import biz.belcorp.consultoras.domain.executor.ThreadExecutor;
import biz.belcorp.consultoras.domain.interactor.ProductUseCase;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;
import biz.belcorp.consultoras.domain.repository.ApiRepository;
import biz.belcorp.consultoras.domain.repository.AuthRepository;
import biz.belcorp.consultoras.domain.repository.ClienteRepository;
import biz.belcorp.consultoras.domain.repository.MenuRepository;
import biz.belcorp.consultoras.domain.repository.ProductRepository;
import biz.belcorp.consultoras.domain.repository.SessionRepository;
import biz.belcorp.consultoras.domain.repository.UserRepository;
import dagger.Module;
import dagger.Provides;

/**
 * Módulo Dagger que proporciona los casos de usos de Productos agotados
 */
@Module
public class StockoutsModule {

    @Provides
    @PerActivity
    UserUseCase providesUserUseCase(UserRepository userRepository,
                                    AuthRepository authRepository,
                                    MenuRepository menuRepository,
                                    ClienteRepository clienteRepository,
                                    SessionRepository sessionRepository,
                                    ApiRepository apiRepository,
                                    ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread) {
        return new UserUseCase(userRepository, authRepository, menuRepository, clienteRepository,
            sessionRepository, apiRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    ProductUseCase providesProductUseCase(ThreadExecutor threadExecutor,
                                          PostExecutionThread postExecutionThread,
                                          ProductRepository productRepository,
                                          AuthRepository authRepository) {
        return new ProductUseCase(threadExecutor, postExecutionThread, productRepository,
            authRepository);
    }
}
