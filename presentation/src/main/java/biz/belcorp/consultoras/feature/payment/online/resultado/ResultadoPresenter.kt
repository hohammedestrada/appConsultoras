package biz.belcorp.consultoras.feature.payment.online.resultado

import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.domain.interactor.AccountStateUseCase
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.interactor.BaseObserver
import biz.belcorp.consultoras.domain.interactor.UserUseCase
import javax.inject.Inject

class ResultadoPresenter @Inject
internal constructor(private val userUseCase: UserUseCase, private val accountStatusUseCase: AccountStateUseCase) : Presenter<ResultadoView> {

    private var resultadoView: ResultadoView? = null
    lateinit var user: User

    override fun attachView(view: ResultadoView) {
        this.resultadoView = view
    }

    fun fillUser(){
        this.userUseCase[GetUserTrack()]
    }

    fun updateMonto() {
        userUseCase.updateScheduler(BaseObserver())
    }

    override fun resume() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun pause() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun destroy() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    inner class GetUserTrack : BaseObserver<User?>() {
        override fun onNext(t: User?) {
            super.onNext(t)
            if (t != null) {
                user=t
            }
        }
    }
}
