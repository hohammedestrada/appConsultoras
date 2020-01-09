package biz.belcorp.consultoras.feature.payment.online.metodopago

import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.common.model.pagoonline.BancoConfigModelMapper
import biz.belcorp.consultoras.common.model.pagoonline.BancosConfig
import biz.belcorp.consultoras.common.model.pagoonline.PagoOnlineConfigMapper
import biz.belcorp.consultoras.common.model.pagoonline.PagoOnlineConfigModel
import biz.belcorp.consultoras.data.mapper.VisaConfigEntityDataMapper
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.interactor.AccountUseCase
import biz.belcorp.consultoras.domain.interactor.BaseObserver
import biz.belcorp.consultoras.domain.interactor.UserUseCase
import biz.belcorp.consultoras.domain.interactor.PagoOnlineUseCase
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject

class PaymentOnlinePresenter  @Inject
internal constructor(private val payment: PagoOnlineUseCase,
                     private val userUseCase: UserUseCase,
                     private val accountUseCase: AccountUseCase,
                     private val pagoOnlineConfigMapper: PagoOnlineConfigMapper,
                     private val visaConfigEntityDataMapper: VisaConfigEntityDataMapper,
                     private val bancoMapper: BancoConfigModelMapper):Presenter<PaymentOnlineView>{

    private var paymentonlineview: PaymentOnlineView? = null
    var user: User? = null
    override fun attachView(view: PaymentOnlineView) {
        paymentonlineview=view
    }

    override fun resume() { }

    override fun pause() { }

    override fun destroy() {
       this.payment.dispose()
        paymentonlineview = null
    }

    fun getInitialConfiguration(){
        paymentonlineview?.showLoading()
        payment.getConfigurationInitial(user?.expirationDate?.trim(), user?.indicadorConsultoraDigital, ConfigurationInitialObserver())
    }

    fun getVisaConfiguration(key:String){
        paymentonlineview?.showLoading()
        payment.getConfigurationVisa(ConfigurationVisa(key))
    }

    fun getVisaNextCounter(nextCounterURL:String, authorization:String){
        paymentonlineview?.showLoading()
        payment.getVisaNextCounter(nextCounterURL, authorization, VisaNextCounter())
    }

    fun formatBancs(metodoPago: List<PagoOnlineConfigModel.MetodoPago>?, medioPago: List<PagoOnlineConfigModel.MedioPago>?, listaBancoOnline:List<PagoOnlineConfigModel.Banco>?): BancosConfig {
        return bancoMapper.transform(metodoPago,medioPago,listaBancoOnline)
    }

    fun fillUser(){
        this.userUseCase[GetUserTrack()]
    }

    inner class ConfigurationInitialObserver: BaseObserver<BasicDto<PagoOnlineConfig>>(){
        override fun onNext(t: BasicDto<PagoOnlineConfig>) {
            t.let {
                paymentonlineview?.getInitialConfig(pagoOnlineConfigMapper.transform(it.data!!))
                val estadoCuenta = it.data!!.estadoCuenta
                accountUseCase.getLoginData(object : DisposableObserver<Login?>(){
                    override fun onComplete() {
                        paymentonlineview?.hideLoading()
                    }
                    override fun onNext(t: Login) {
                        if (t.detail != null && t.detail!!.size > 5) {
                            val estadoCuentaDetail = t.detail!![5]
                            when(estadoCuentaDetail){
                                null -> return
                                else ->{
                                    val deuda = estadoCuentaDetail.value
                                    if(deuda != estadoCuenta!!.deuda)
                                    {
                                        userUseCase.updateScheduler(BaseObserver())
                                    }
                                }
                            }
                        }
                    }
                    override fun onError(e: Throwable) {
                        paymentonlineview?.hideLoading()
                    }
                })

            }
        }

        override fun onError(exception: Throwable) {
            super.onError(exception)
            paymentonlineview?.hideLoading()
        }
    }

    inner class ConfigurationVisa (private val key: String): BaseObserver<BasicDto<VisaConfig>>(){
        override fun onNext(t: BasicDto<VisaConfig>) {
            t.let{
                paymentonlineview?.getVisaConfig(visaConfigEntityDataMapper.transform(it.data!!, key))
                paymentonlineview?.hideLoading()
            }
        }

        override fun onError(exception: Throwable) {
            super.onError(exception)
            paymentonlineview?.hideLoading()
        }
    }

    inner class VisaNextCounter : BaseObserver<String>(){
        override fun onNext(t: String) {
            t.let{
                paymentonlineview?.getVisaNextCounter(it)
                paymentonlineview?.hideLoading()
            }
        }

        override fun onError(exception: Throwable) {
            super.onError(exception)
            paymentonlineview?.getVisaNextCounter(null)
            paymentonlineview?.hideLoading()
        }
    }

     inner class GetUserTrack : BaseObserver<User?>() {
        override fun onNext(t: User?) {
            if (t != null) {
                user = t
                this@PaymentOnlinePresenter.getInitialConfiguration()
            }
        }
    }
}
