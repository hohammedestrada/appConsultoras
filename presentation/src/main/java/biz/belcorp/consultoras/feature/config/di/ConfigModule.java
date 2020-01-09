package biz.belcorp.consultoras.feature.config.di;


import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.executor.PostExecutionThread;
import biz.belcorp.consultoras.domain.executor.ThreadExecutor;
import biz.belcorp.consultoras.domain.interactor.ConfigUseCase;
import biz.belcorp.consultoras.domain.repository.ConfigRepository;
import dagger.Module;
import dagger.Provides;

/**
 * Módulo Dagger que proporciona los casos de usos del Home
 */
@Module
class ConfigModule {

    ConfigModule() {
        // EMPTY
    }

    @Provides
    @PerActivity
    ConfigUseCase providesPreferenceUseCase(ConfigRepository configRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new ConfigUseCase(configRepository, threadExecutor, postExecutionThread);
    }
}
