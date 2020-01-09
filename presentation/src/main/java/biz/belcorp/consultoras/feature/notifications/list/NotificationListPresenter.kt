package biz.belcorp.consultoras.feature.notifications.list

import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper
import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.Notificacion
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.interactor.*
import javax.inject.Inject

@PerActivity
class NotificationListPresenter @Inject
internal constructor(private val userUseCase: UserUseCase
                     , private val authUseCase: AuthUseCase
                     , private val menuUseCase: MenuUseCase
                     , private val sessionUseCase: SessionUseCase
                     , private val notificacionUseCase: NotificacionUseCase
                     , private val loginModelDataMapper: LoginModelDataMapper) : Presenter<NotificationListView> {


    private var notificationListView: NotificationListView? = null


    // Overrrides BasePresenter
    override fun resume() {}

    override fun pause() {}

    override fun attachView(view: NotificationListView) {
        notificationListView = view
    }

    override fun destroy() {
        this.userUseCase.dispose()
        this.authUseCase.dispose()
        this.menuUseCase.dispose()
        this.sessionUseCase.dispose()
        this.notificacionUseCase.dispose()
        this.notificationListView = null
    }

    // Functions
    fun getUser(){
        userUseCase[GetUserObserver()]
    }

    fun getNotificaciones(dias: Int) {
        notificacionUseCase.listByConsultoraId(dias, GetNotificacionesObserver())
    }

    fun updateNotificationStatus(status: Boolean){
        sessionUseCase.updateNotificationStatus(status, UpdateNotificationStatus())
    }

    fun changeNotificationStatus(notificacion: Notificacion, notificacionStatus: Boolean) {
        notificacionUseCase.save(notificacion, notificacionStatus, ChangeNotificationStatusObserver())
    }

    fun trackBackPressed() {
        userUseCase[UserBackPressedObserver()]
    }


    // Observers
    private inner class UpdateNotificationStatus : BaseObserver<Boolean>()

    private inner class UserBackPressedObserver : BaseObserver<User?>() {
        override fun onNext(t: User?) {
            notificationListView?.trackBackPressed(loginModelDataMapper.transform(t))
        }
    }

    private inner class GetUserObserver : BaseObserver<User?>() {
        override fun onNext(t: User?) {
            notificationListView?.setUser(t)
        }
    }

    private inner class GetNotificacionesObserver : BaseObserver<List<Notificacion?>?>() {
        override fun onNext(t: List<Notificacion?>?) {
            t?.let { notificationListView?.setNotificaciones(it) }
        }
    }

    private inner class ChangeNotificationStatusObserver : BaseObserver<Notificacion?>() {
        override fun onNext(t: Notificacion?) {
            t?.let { notificationListView?.statusChanged(it) }
        }
    }

}
