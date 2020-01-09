package biz.belcorp.consultoras.feature.contest.rewards

import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.base.SafeLet
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper
import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.interactor.BaseObserver
import biz.belcorp.consultoras.domain.interactor.UserUseCase
import javax.inject.Inject


@PerActivity
class RewardsPresenter @Inject
internal constructor(
    private val userUseCase: UserUseCase,
    private val loginModelDataMapper: LoginModelDataMapper
    ) : Presenter<RewardsView>, SafeLet {

    private var view: RewardsView? = null

    private var loginModel: LoginModel? = null

    override fun attachView(view: RewardsView) {
        this.view = view
    }

    override fun resume() {
        // EMPTY
    }

    override fun pause() {
        // EMPTY
    }

    override fun destroy() {
        this.userUseCase.dispose()
        this.view = null
    }

    /** Métodos */

    fun trackScreen() {
        userUseCase[UserPropertyObserver()]
    }

    fun trackBack() {
        loginModel?.let { view?.trackBack(it) }
    }


    /** Observers */

    private inner class UserPropertyObserver : BaseObserver<User?>() {
        override fun onNext(t: User?) {
            loginModel = loginModelDataMapper.transform(t)
            loginModel?.let { view?.trackScreen(it) }
        }
    }


}
