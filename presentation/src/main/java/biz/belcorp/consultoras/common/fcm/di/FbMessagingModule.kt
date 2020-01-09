package biz.belcorp.consultoras.common.fcm.di

import biz.belcorp.consultoras.di.PerService
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.interactor.NotificacionUseCase
import biz.belcorp.consultoras.domain.interactor.SyncUseCase
import biz.belcorp.consultoras.domain.repository.AuthRepository
import biz.belcorp.consultoras.domain.repository.ClienteRepository
import biz.belcorp.consultoras.domain.repository.NotificacionRepository
import biz.belcorp.consultoras.domain.repository.SessionRepository
import biz.belcorp.consultoras.domain.repository.SyncRepository
import dagger.Module
import dagger.Provides

/**
 * Módulo Dagger que proporciona los casos de usos del Sync
 */
@Module
class FbMessagingModule{

    @Provides
    @PerService
    fun providesNotificacionUseCase(notificacionRepository: NotificacionRepository,
                                    threadExecutor: ThreadExecutor,
                                    postExecutionThread: PostExecutionThread): NotificacionUseCase {
        return NotificacionUseCase(notificacionRepository, threadExecutor, postExecutionThread)
    }

}
