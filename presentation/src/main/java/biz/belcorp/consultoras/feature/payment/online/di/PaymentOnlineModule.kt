package biz.belcorp.consultoras.feature.payment.online.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.interactor.PagoOnlineUseCase
import biz.belcorp.consultoras.domain.repository.AuthRepository
import biz.belcorp.consultoras.domain.repository.PagoOnlineRepository
import biz.belcorp.consultoras.domain.repository.SessionRepository
import dagger.Module
import dagger.Provides
@Module
class PaymentOnlineModule {
    @Provides
    @PerActivity
    fun providesPaymentOnlineUseCase(pagoOnlineRepository: PagoOnlineRepository,
                                     authRepository: AuthRepository,
                                     sesionRepository: SessionRepository,
                                     threadExecutor: ThreadExecutor,
                                     postExecutionThread: PostExecutionThread):PagoOnlineUseCase{
    return PagoOnlineUseCase(pagoOnlineRepository,authRepository,sesionRepository,threadExecutor,postExecutionThread)
}
}
