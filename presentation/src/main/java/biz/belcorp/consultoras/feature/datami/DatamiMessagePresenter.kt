package biz.belcorp.consultoras.feature.datami

import javax.inject.Inject

import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper
import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.interactor.BaseObserver
import biz.belcorp.consultoras.domain.interactor.UserUseCase

@PerActivity
class DatamiMessagePresenter @Inject
internal constructor(private val userUseCase: UserUseCase,
                     private val loginModelDataMapper: LoginModelDataMapper)
    : Presenter<DatamiMessageView> {

    private var datamiMessageView: DatamiMessageView? = null

    override fun attachView(view: DatamiMessageView) {
        datamiMessageView = view
    }

    override fun resume() {
        // EMPTY
    }

    override fun pause() {
        // EMPTY
    }

    override fun destroy() {
        this.userUseCase.dispose()
        this.datamiMessageView = null
    }

    /**  functions */

    fun initScreenTrack() {
        userUseCase[UserPropertyObserver()]
    }

    fun updateStatusDatamiMessage() {
        this.userUseCase.updateStatusDatamiMessage(UpdateStatusDatamiMessageObserver())
    }

    /** observers */

    private inner class UpdateStatusDatamiMessageObserver : BaseObserver<Boolean>() {

        override fun onNext(t: Boolean) {
            datamiMessageView?.closeMessageResult()
        }
    }

    private inner class UserPropertyObserver : BaseObserver<User?>() {

        override fun onNext(t: User?) {
            t?.let {
                datamiMessageView?.initScreenTrack(loginModelDataMapper.transform(it)!!)
            }
        }
    }
}
