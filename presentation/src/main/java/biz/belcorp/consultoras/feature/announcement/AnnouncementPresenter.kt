package biz.belcorp.consultoras.feature.announcement

import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.interactor.AccountUseCase
import biz.belcorp.consultoras.domain.interactor.BaseObserver
import biz.belcorp.consultoras.domain.interactor.UserUseCase
import javax.inject.Inject

class AnnouncementPresenter @Inject
internal constructor(
    private val userUseCase: UserUseCase,
    private val loginModelDataMapper: LoginModelDataMapper)
    : Presenter<AnnouncementView>{


    private var view: AnnouncementView? = null

    override fun attachView(view: AnnouncementView) {
        this.view = view
    }

    override fun resume() {
        // EMPTY
    }

    override fun pause() {
        // EMPTY
    }

    override fun destroy() {
        // EMPTY
    }

    fun startAnimation(){

    }

    fun initScreenTrack() {
        userUseCase[UserPropertyObserver()]
    }

    fun trackBackPressed() {
        userUseCase[UserBackPressedObserver()]
    }

    private inner class UserPropertyObserver : BaseObserver<User?>() {
        override fun onNext(t: User?) {
            view?.initScreenTrack(loginModelDataMapper.transform(t))
        }
    }

    private inner class UserBackPressedObserver : BaseObserver<User?>() {
        override fun onNext(t: User?) {
            view?.trackBackPressed(loginModelDataMapper.transform(t))
        }
    }


}
