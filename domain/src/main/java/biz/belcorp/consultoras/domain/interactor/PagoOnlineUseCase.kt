package biz.belcorp.consultoras.domain.interactor

import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.repository.AuthRepository
import biz.belcorp.consultoras.domain.repository.PagoOnlineRepository
import biz.belcorp.consultoras.domain.repository.SessionRepository
import javax.inject.Inject

class PagoOnlineUseCase
@Inject
constructor(private val pagoOnlineRepository: PagoOnlineRepository
            ,private val authRepository: AuthRepository
            , private val sessionRepository: SessionRepository
            , threadExecutor: ThreadExecutor
            , postExecutionThread: PostExecutionThread) : UseCase(threadExecutor, postExecutionThread)  {

        fun getConfigurationInitial(fechaVencimientoPago:String?, indicadorConsultoraDigital:Int?,observer: BaseObserver<BasicDto<PagoOnlineConfig>>){
            execute(pagoOnlineRepository.getInitialConfiguration(fechaVencimientoPago, indicadorConsultoraDigital),observer)
        }

        fun getConfigurationVisa(observer: BaseObserver<BasicDto<VisaConfig>>){
            execute(pagoOnlineRepository.getInitialConfigurationVisa(),observer)
        }

        fun getVisaNextCounter(nextCounterURL:String,  authorization:String, observer: BaseObserver<String>){
            execute(pagoOnlineRepository.getVisaNextCounter(nextCounterURL, authorization),observer)
        }

        fun savePayment(visaSave: VisaPayment, sincronizado: Int,observer: BaseObserver<BasicDto<ResultadoPagoEnLinea>>){
            execute(pagoOnlineRepository.savePayment(visaSave,sincronizado),observer)
        }
}
