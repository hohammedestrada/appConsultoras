package biz.belcorp.consultoras.feature.vinculacion

import javax.inject.Inject

import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper
import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.CreditAgreement
import biz.belcorp.consultoras.domain.entity.BasicDto
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.exception.VersionException
import biz.belcorp.consultoras.domain.interactor.AccountUseCase
import biz.belcorp.consultoras.domain.interactor.BaseObserver
import biz.belcorp.consultoras.domain.interactor.UserUseCase
import biz.belcorp.consultoras.exception.ErrorFactory
import biz.belcorp.consultoras.util.anotation.ResponseResultCode
import biz.belcorp.library.log.BelcorpLogger


@PerActivity
class VinculacionPresenter @Inject
constructor(private val accountUseCase: AccountUseCase,
            private val userUseCase: UserUseCase,
            private val loginModelDataMapper: LoginModelDataMapper)
    : Presenter<VinculacionView> {

    private var vinculacionView: VinculacionView? = null

    override fun attachView(view: VinculacionView) {
        vinculacionView = view
    }

    override fun resume() {
        // EMPTY
    }

    override fun pause() {
        // EMPTY
    }

    override fun destroy() {
        this.vinculacionView = null
    }


    /** */
    fun data() {
        vinculacionView?.showLoading()
        userUseCase[GetUser()]
    }


    fun initScreenTrack() {
        userUseCase[UserPropertyObserver()]
    }

    fun applyCreditAgreement(request: CreditAgreement) {
        vinculacionView?.showLoading()
        accountUseCase.contratoCredito(request, ApplyCreditAgreementObserver())
    }

    fun getPdfCreditAgreement() {
        vinculacionView?.showLoading()
        accountUseCase.getPdfCreditAgreementUrl(GetPdfObserver())
    }


    /** */

    private inner class GetUser : BaseObserver<User?>() {

        override fun onNext(t: User?) {
            vinculacionView?.hideLoading()
            t?.let {
                vinculacionView?.setData(it)
            }
        }

        override fun onError(exception: Throwable) {
            vinculacionView?.hideLoading()
            BelcorpLogger.w(TAG, "ERROR!", exception)
        }
    }

    private inner class UserPropertyObserver : BaseObserver<User?>() {

        override fun onNext(t: User?) {
            vinculacionView?.initScreenTrack(loginModelDataMapper.transform(t))
        }
    }

    private inner class ApplyCreditAgreementObserver : BaseObserver<BasicDto<Boolean>?>() {

        override fun onNext(t: BasicDto<Boolean>?) {
            vinculacionView?.hideLoading()
            t?.let {
                when(t.code){
                    ResponseResultCode.OK ->
                        accountUseCase.updateAcceptancesCreditAgreement(true, AcceptanceObserver())
                    else -> {
                        vinculacionView?.hideLoading()
                        t.message?.let {
                            vinculacionView?.onError(it)
                        }
                    }
                }
            }
        }

        override fun onError(exception: Throwable) {
            vinculacionView?.hideLoading()
            if (exception is VersionException) {
                vinculacionView?.onVersionError(exception.isRequiredUpdate, exception.url)
            } else {
                vinculacionView?.onError(ErrorFactory.create(exception))
            }
        }
    }

    private inner class GetPdfObserver : BaseObserver<String>() {

        override fun onNext(t: String) {
            vinculacionView?.hideLoading()
            t?.let {
                vinculacionView?.onUrlCreditAgreement(t)
            }
        }

        override fun onError(exception: Throwable) {
            vinculacionView?.hideLoading()
            BelcorpLogger.w(TAG, "ERROR!", exception)
        }
    }

    private inner class AcceptanceObserver : BaseObserver<Boolean?>() {

        override fun onNext(t: Boolean?) {
            vinculacionView?.onCreditAgreementAccept()
        }
    }


    /** */

    companion object {
        private const val TAG = "VinculacionPresenter"
    }
}
