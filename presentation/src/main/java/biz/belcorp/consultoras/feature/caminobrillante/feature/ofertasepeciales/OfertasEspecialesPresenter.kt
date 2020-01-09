package biz.belcorp.consultoras.feature.caminobrillante.feature.ofertasepeciales

import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.domain.interactor.UserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class OfertasEspecialesPresenter @Inject
internal constructor(private val userUseCase: UserUseCase) : Presenter<OfertasEspecialesView> {

    private var view: OfertasEspecialesView? = null

    override fun attachView(view: OfertasEspecialesView) {
        this.view = view
    }

    override fun resume() {
    }

    override fun pause() {
    }

    override fun destroy() {
        view?.hideLoading()
        view = null
    }

    fun getUser() {
        GlobalScope.launch {
            userUseCase.getUser()?.let {
                GlobalScope.launch(Dispatchers.Main) {
                    view?.setUser(it)
                }
            }
        }
    }

}
