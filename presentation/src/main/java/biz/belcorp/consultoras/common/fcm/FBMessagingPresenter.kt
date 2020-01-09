package biz.belcorp.consultoras.feature.notifications.list

import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper
import biz.belcorp.consultoras.di.PerService
import biz.belcorp.consultoras.domain.entity.Notificacion
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.interactor.*
import javax.inject.Inject

@PerService
class FBMessagingPresenter @Inject
internal constructor(private val notificacionUseCase: NotificacionUseCase) : Presenter<FBMessagingView> {


    private var fbMessagingView: FBMessagingView? = null


    // Overrrides BasePresenter
    override fun resume() {}

    override fun pause() {}

    override fun attachView(view: FBMessagingView) {
        fbMessagingView = view
    }

    override fun destroy() {

        this.notificacionUseCase.dispose()
        this.fbMessagingView = null
    }

    // Functions
    fun saveNotificacion(notificacion: Notificacion, notificacionStatus: Boolean) {
        notificacionUseCase.save(notificacion, notificacionStatus, SaveNotificacionObserver())
    }


    // Observers


    private inner class SaveNotificacionObserver : BaseObserver<Notificacion?>() {
        override fun onNext(t: Notificacion?) {
            t?.let { fbMessagingView?.onNotificacionSaved(true) }
        }
    }


}
