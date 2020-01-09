package biz.belcorp.consultoras.feature.debt.di;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.executor.PostExecutionThread;
import biz.belcorp.consultoras.domain.executor.ThreadExecutor;
import biz.belcorp.consultoras.domain.interactor.ClienteUseCase;
import biz.belcorp.consultoras.domain.interactor.DebtUseCase;
import biz.belcorp.consultoras.domain.repository.AuthRepository;
import biz.belcorp.consultoras.domain.repository.ClienteRepository;
import biz.belcorp.consultoras.domain.repository.DebtRepository;
import biz.belcorp.consultoras.domain.repository.SessionRepository;
import biz.belcorp.consultoras.domain.repository.UserRepository;
import dagger.Module;
import dagger.Provides;

/**
 *
 */
@Module
class DebtModule {

    @Provides
    @PerActivity
    ClienteUseCase providesClientUseCase(ClienteRepository clienteRepository
        , AuthRepository authRepository
        , SessionRepository sessionRepository
        , ThreadExecutor threadExecutor
        , PostExecutionThread postExecutionThread) {
        return new ClienteUseCase(
            clienteRepository
            ,threadExecutor
            ,authRepository
            ,postExecutionThread
            ,sessionRepository);
    }

    @Provides
    @PerActivity
    DebtUseCase providesDebtUseCase(ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread,
                                    DebtRepository debtRepository,
                                    UserRepository userRepository,
                                    ClienteRepository clienteRepository,
                                    AuthRepository authRepository) {
        return new DebtUseCase(threadExecutor, postExecutionThread, debtRepository
            , userRepository, clienteRepository, authRepository);
    }
}
