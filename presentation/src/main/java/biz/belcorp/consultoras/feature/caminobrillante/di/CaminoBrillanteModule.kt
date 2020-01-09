package biz.belcorp.consultoras.feature.caminobrillante.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.interactor.CaminoBrillanteUseCase
import biz.belcorp.consultoras.domain.repository.CaminoBrillanteRepository
import dagger.Module
import dagger.Provides

@Module
class CaminoBrillanteModule {

    @Provides
    @PerActivity
    fun providesCaminoBrillanteUseCase(caminobrillanteRepository: CaminoBrillanteRepository,
                                       threadExecutor: ThreadExecutor,
                                       postExecutionThread: PostExecutionThread): CaminoBrillanteUseCase {
        return CaminoBrillanteUseCase(caminobrillanteRepository, threadExecutor, postExecutionThread)
    }

}
