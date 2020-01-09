package biz.belcorp.consultoras.feature.verifyemail

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
class VerifyEmailPresenter @Inject
constructor(private val accountUseCase: AccountUseCase,
            private val userUseCase: UserUseCase,
            private val loginModelDataMapper: LoginModelDataMapper) : Presenter<VerifyEmailView> {

    private var verifyEmailView: VerifyEmailView? = null

    override fun attachView(view: VerifyEmailView) {
        verifyEmailView = view
    }

    override fun resume() {
        // EMPTY
    }

    override fun pause() {
        // EMPTY
    }

    override fun destroy() {
        this.verifyEmailView = null
    }


    internal fun initScreenTrack() {
       //userUseCase.getIncentives(UserPropertyObserver())
    }

    fun enviarCorreo(correoNuevo: String){
        verifyEmailView?.showLoading()
        accountUseCase.enviarCorreo(correoNuevo, EnviarCorreoObserver() )
    }



    private inner class UserPropertyObserver : BaseObserver<User?>() {

        override fun onNext(user: User?) {
            verifyEmailView!!.initScreenTrack(loginModelDataMapper.transform(user)!!)
        }
    }

    private inner class EnviarCorreoObserver : BaseObserver<BasicDto<Boolean>?>() {

        override fun onNext(t: BasicDto<Boolean>?) {
            verifyEmailView?.hideLoading()
            if(t?.code.equals(GlobalConstant.CODE_OK)){
                verifyEmailView?.onEmailUpdated()
            } else {
                verifyEmailView?.onError()
                t?.message?.let {
                    BelcorpLogger.d(it)
                }
            }
        }

        override fun onError(exception: Throwable) {
            exception?.message?.let {
                verifyEmailView?.onError(it)
                BelcorpLogger.d(it)
            }
        }

    }

}
