package biz.belcorp.consultoras.feature.catalog.di;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.executor.PostExecutionThread;
import biz.belcorp.consultoras.domain.executor.ThreadExecutor;
import biz.belcorp.consultoras.domain.interactor.CatalogUseCase;
import biz.belcorp.consultoras.domain.repository.AuthRepository;
import biz.belcorp.consultoras.domain.repository.CatalogRepository;
import dagger.Module;
import dagger.Provides;

@Module
class CatalogModule {

    @Provides
    @PerActivity
    CatalogUseCase providesCatalogUseCase(ThreadExecutor threadExecutor,
                                          PostExecutionThread postExecutionThread,
                                          CatalogRepository catalogRepository,
                                          AuthRepository authRepository) {
        return new CatalogUseCase(threadExecutor, postExecutionThread, catalogRepository, authRepository);
    }
}
