package biz.belcorp.consultoras.feature.payment.online.resultado

import biz.belcorp.consultoras.base.Presenter
import javax.inject.Inject

class ErrorPresenter @Inject
internal constructor():Presenter<ResultadoView> {
    private  var resultadoView: ResultadoView? = null

    override fun attachView(view: ResultadoView) {
        this.resultadoView = view
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
}
