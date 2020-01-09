package biz.belcorp.consultoras.feature.welcome

import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper
import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.interactor.AccountUseCase
import biz.belcorp.consultoras.domain.interactor.BaseObserver
import biz.belcorp.consultoras.domain.interactor.ConfigUseCase
import biz.belcorp.consultoras.domain.interactor.UserUseCase
import javax.inject.Inject

/**
 * @author andres.escobar on 4/08/2017.
 */
@PerActivity
class WelcomePresenter @Inject
constructor(private val accountUseCase: AccountUseCase,
            private val userUseCase: UserUseCase,
            private val loginModelDataMapper: LoginModelDataMapper,
            private val configUseCase: ConfigUseCase) : Presenter<WelcomeView> {

    private var welcomeView: WelcomeView? = null

    override fun attachView(view: WelcomeView) {
        welcomeView = view
    }

    override fun resume() {
        // EMPTY
    }

    override fun pause() {
        // EMPTY
    }

    override fun destroy() {
        this.welcomeView = null
    }


    internal fun initScreenTrack() {
        userUseCase[UserPropertyObserver()]
    }

    fun sendSMS(smsRequest: SMSResquest){
        welcomeView?.showLoading()
        accountUseCase.sendSMS(smsRequest, SendSMSObserver())
    }

    fun verificacion(countryISO: String){
        configUseCase.getOffline(ConfigObserver(countryISO))
        welcomeView?.showLoading()
    }

    private inner class UserPropertyObserver : BaseObserver<User?>() {

        override fun onNext(t: User?) {
            welcomeView?.initScreenTrack(loginModelDataMapper.transform(t)!!)

        }
    }

    private inner class SendSMSObserver : BaseObserver<BasicDto<Boolean>?>() {

        override fun onNext(t: BasicDto<Boolean>?) {
            welcomeView?.onSMSResponse(t)
            welcomeView?.hideLoading()
        }

        override fun onError(exception: Throwable) {
            welcomeView?.onSMSError(exception)
            welcomeView?.hideLoading()
        }
    }

    private inner class ConfigObserver(val countryISO: String) : BaseObserver<ConfigReponse?>() {

        override fun onNext(t: ConfigReponse?) {
            t?.let {
                var encontrado = false
                it.countries?.forEach {
                    if(it?.iso == countryISO){
                        encontrado = true
                        accountUseCase.verificacionOffline(VerificacionObserver(it.telefono1, it.telefono2))
                    }
                }
                if(!encontrado){
                    onError(Exception("País no encontrado"))
                }
            }

        }

        override fun onError(exception: Throwable) {
            welcomeView?.onSMSError(exception)
            welcomeView?.hideLoading()
        }

    }

    private inner class VerificacionObserver(val telefono1: String?, val telefono2: String?) : BaseObserver<Verificacion?>() {

        override fun onNext(t: Verificacion?) {
            t?.let {
                t.telefono1 = telefono1
                t.telefono2 = telefono2
                welcomeView?.onVerificacionResponse(it)
                welcomeView?.hideLoading()
            }

        }

        override fun onError(exception: Throwable) {
            welcomeView?.onSMSError(exception)
            welcomeView?.hideLoading()
        }
    }



}
