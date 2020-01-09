package biz.belcorp.consultoras.feature.notifications.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.interactor.NotificacionUseCase
import biz.belcorp.consultoras.domain.repository.NotificacionRepository
import dagger.Module
import dagger.Provides

/**
 * Módulo Dagger que proporciona los casos de usos del Home
 */
@Module
class NotificationsModule {

    @Provides
    @PerActivity
    fun providesNotificacionUseCase(notificacionRepository: NotificacionRepository,
                              threadExecutor: ThreadExecutor,
                              postExecutionThread: PostExecutionThread): NotificacionUseCase {
        return NotificacionUseCase(notificacionRepository, threadExecutor, postExecutionThread)
    }

}
