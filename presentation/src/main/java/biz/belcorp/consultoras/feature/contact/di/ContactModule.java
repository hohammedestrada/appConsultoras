package biz.belcorp.consultoras.feature.contact.di;

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
 * Módulo Dagger que proporciona los casos de usos del Contactos
 */
@Module
public class ContactModule {

    ContactModule() {
        // EMPTY
    }

    @Provides
    @PerActivity
    ClienteUseCase providesClientUseCase(ClienteRepository clienteRepository
        , AuthRepository authRepository
        , SessionRepository sessionRepository
        , ThreadExecutor threadExecutor
        , PostExecutionThread postExecutionThread) {
        return new ClienteUseCase(clienteRepository
            , threadExecutor
            , authRepository
            , postExecutionThread
            , sessionRepository);
    }
}
