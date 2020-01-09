package biz.belcorp.consultoras.feature.sms

import javax.inject.Inject

import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper
import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.BasicDto
import biz.belcorp.consultoras.domain.entity.SMSResquest
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.interactor.AccountUseCase
import biz.belcorp.consultoras.domain.interactor.BaseObserver
import biz.belcorp.consultoras.domain.interactor.UserUseCase

/**
 * @author andres.escobar on 4/08/2017.
 */
@PerActivity
class SMSPresenter @Inject
constructor(private val accountUseCase: AccountUseCase,
            private val userUseCase: UserUseCase,
            private val loginModelDataMapper: LoginModelDataMapper) : Presenter<SMSView> {

    private var smsView: SMSView? = null

    override fun attachView(view: SMSView) {
        smsView = view
    }

    override fun resume() {
        // EMPTY
    }

    override fun pause() {
        // EMPTY
    }

    override fun destroy() {
        this.smsView = null
    }


    internal fun initScreenTrack() {
        //userUseCase[UserPropertyObserver()]
    }

    fun sendSMS(smsRequest: SMSResquest, mostrarLoading: Boolean = true){
        if(mostrarLoading)
            smsView?.showLoading()
        accountUseCase.sendSMS(smsRequest, SendSMSObserver())
    }

    fun confirmSMSCode(smsRequest: SMSResquest){
        smsView?.showLoading()
        accountUseCase.confirmSMSCode(smsRequest, ConfirmSMSCodeObserver())
    }

    fun confirmPhoneChange(smsRequest: SMSResquest){
        smsView?.showLoading()
        accountUseCase.confirmSMSCode(smsRequest, ConfirmPhoneChangeObserver())
    }

    fun getUrlTyC() {
        accountUseCase.getPdfTermsUrl(GetUrlTyCObserver())
    }

    fun refreshData(){
        this.userUseCase.updateScheduler(UpdateObserver())
    }

    private inner class UpdateObserver : BaseObserver<Boolean>()

    private inner class GetUrlTyCObserver : BaseObserver<String>() {
        override fun onNext(url: String) {
            smsView?.onShowTerms(url)
        }
    }

    private inner class UserPropertyObserver : BaseObserver<User?>() {
        override fun onNext(user: User?) {
            smsView?.initScreenTrack(loginModelDataMapper.transform(user))
        }
    }

    private inner class SendSMSObserver : BaseObserver<BasicDto<Boolean>?>() {

        override fun onNext(t: BasicDto<Boolean>?) {
            smsView?.onSMSResponse(t)
            smsView?.hideLoading()
        }

        override fun onError(exception: Throwable) {
            smsView?.onSMSError(exception)
            smsView?.hideLoading()
        }
    }

    private inner class ConfirmSMSCodeObserver : BaseObserver<BasicDto<Boolean>?>() {

        override fun onNext(t: BasicDto<Boolean>?) {
            smsView?.onConfirmSMSCodeResponse(t)
            smsView?.hideLoading()
        }

        override fun onError(exception: Throwable) {
            smsView?.onConfirmSMSCodeError(exception)
            smsView?.hideLoading()
        }
    }

    private inner class ConfirmPhoneChangeObserver : BaseObserver<BasicDto<Boolean>?>() {

        override fun onNext(t: BasicDto<Boolean>?) {
            smsView?.onConfirmPhoneChangeResponse(t)
            smsView?.hideLoading()
        }

        override fun onError(exception: Throwable) {
            smsView?.onConfirmPhoneChangeError(exception)
            smsView?.hideLoading()
        }
    }



}
