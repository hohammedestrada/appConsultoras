package biz.belcorp.consultoras.sync.di;

import biz.belcorp.consultoras.di.PerService;
import biz.belcorp.consultoras.domain.executor.PostExecutionThread;
import biz.belcorp.consultoras.domain.executor.ThreadExecutor;
import biz.belcorp.consultoras.domain.interactor.SyncUseCase;
import biz.belcorp.consultoras.domain.repository.AuthRepository;
import biz.belcorp.consultoras.domain.repository.ClienteRepository;
import biz.belcorp.consultoras.domain.repository.SessionRepository;
import biz.belcorp.consultoras.domain.repository.SyncRepository;
import dagger.Module;
import dagger.Provides;

/**
 * Módulo Dagger que proporciona los casos de usos del Sync
 */
@Module
class SyncModule {

    @Provides
    @PerService
    SyncUseCase provideSyncUseCase(SyncRepository syncRepository, ClienteRepository clienteRepository,
                                   SessionRepository sessionRepository, AuthRepository authRepository,
                                   ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new SyncUseCase(syncRepository, clienteRepository, sessionRepository, authRepository
            , threadExecutor, postExecutionThread);
    }

}
