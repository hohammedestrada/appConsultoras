package biz.belcorp.consultoras.feature.changeemail

import javax.inject.Inject

import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper
import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.BasicDto
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.interactor.AccountUseCase
import biz.belcorp.consultoras.domain.interactor.BaseObserver
import biz.belcorp.consultoras.domain.interactor.UserUseCase
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.library.log.BelcorpLogger

/**
 * @author andres.escobar on 4/08/2017.
 */
@PerActivity
class ChangeEmailPresenter @Inject
constructor(private val accountUseCase: AccountUseCase,
            private val userUseCase: UserUseCase,
            private val loginModelDataMapper: LoginModelDataMapper) : Presenter<ChangeEmailView> {

    private var changeEmailView: ChangeEmailView? = null

    override fun attachView(view: ChangeEmailView) {
        changeEmailView = view
    }

    override fun resume() {
        // EMPTY
    }

    override fun pause() {
        // EMPTY
    }

    override fun destroy() {
        this.changeEmailView = null
    }


    internal fun initScreenTrack() {
        userUseCase.get(UserPropertyObserver())
    }

    fun enviarCorreo(correoNuevo: String){
        changeEmailView?.showLoading()
        accountUseCase.enviarCorreo(correoNuevo, EnviarCorreoObserver() )
    }



    fun getUrlTyC() {
        accountUseCase.getPdfTermsUrl(GetUrlTyCObserver())
    }


    private inner class UserPropertyObserver : BaseObserver<User?>() {

        override fun onNext(user: User?) {
            changeEmailView?.initScreenTrack(loginModelDataMapper.transform(user)!!)
        }
    }

    private inner class GetUrlTyCObserver : BaseObserver<String>() {

        override fun onNext(url: String) {
            changeEmailView?.setUrlTyc(url)
        }
    }

    private inner class EnviarCorreoObserver : BaseObserver<BasicDto<Boolean>?>() {

        override fun onNext(t: BasicDto<Boolean>?) {
            changeEmailView?.hideLoading()
            if(t?.code.equals(GlobalConstant.CODE_OK)){
                changeEmailView?.onEmailUpdated()
            } else {
                t?.message?.let {
                    changeEmailView?.onError(it)
                    BelcorpLogger.d(it)
                }
            }

        }

        override fun onError(exception: Throwable) {
            changeEmailView?.hideLoading()
            exception?.message?.let {
                changeEmailView?.onError(it)
                BelcorpLogger.d(it)
            }
        }

    }

}
