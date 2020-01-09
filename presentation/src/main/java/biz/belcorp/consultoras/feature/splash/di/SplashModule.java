package biz.belcorp.consultoras.feature.splash.di;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.executor.PostExecutionThread;
import biz.belcorp.consultoras.domain.executor.ThreadExecutor;
import biz.belcorp.consultoras.domain.interactor.ClienteUseCase;
import biz.belcorp.consultoras.domain.repository.AuthRepository;
import biz.belcorp.consultoras.domain.repository.ClienteRepository;
import biz.belcorp.consultoras.domain.repository.SessionRepository;
import dagger.Module;
import dagger.Provides;

/**
 * Módulo Dagger que proporciona los casos de usos del Splash
 */
@Module
public class SplashModule {

    public SplashModule() {
    }

    @Provides
    @PerActivity
    ClienteUseCase providesSplashUseCase(ClienteRepository clienteRepository,
                                         AuthRepository authRepository,
                                         ThreadExecutor threadExecutor,
                                         PostExecutionThread postExecutionThread,
                                         SessionRepository sessionRepository) {
        return new ClienteUseCase(clienteRepository
            , threadExecutor, authRepository
            , postExecutionThread
            , sessionRepository);
    }

}
