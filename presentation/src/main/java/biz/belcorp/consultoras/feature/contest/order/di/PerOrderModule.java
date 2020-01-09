package biz.belcorp.consultoras.feature.contest.order.di;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.executor.PostExecutionThread;
import biz.belcorp.consultoras.domain.executor.ThreadExecutor;
import biz.belcorp.consultoras.domain.interactor.IncentivesUseCase;
import biz.belcorp.consultoras.domain.repository.AuthRepository;
import biz.belcorp.consultoras.domain.repository.IncentivosRepository;
import dagger.Module;
import dagger.Provides;

/**
 * Módulo Dagger que proporciona los casos de usos de Incentivos
 */
@Module
class PerOrderModule {

    PerOrderModule() {
        // EMPTY
    }

    @Provides
    @PerActivity
    IncentivesUseCase providesIncentivosUseCase(IncentivosRepository incentivosRepository,
                                                AuthRepository authRepository,
                                                ThreadExecutor threadExecutor,
                                                PostExecutionThread postExecutionThread){
        return new IncentivesUseCase(threadExecutor, postExecutionThread, incentivosRepository,
            authRepository);
    }

}
