package biz.belcorp.consultoras.feature.notifications.list

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.domain.entity.Notificacion
import biz.belcorp.consultoras.domain.entity.User

interface NotificationListView : View, LoadingView {

    fun setUser(user: User?)
    fun initScreenTrack(model: LoginModel)
    fun trackBackPressed(model: LoginModel)
    fun setNotificaciones(t: List<Notificacion?>)
    fun statusChanged(notificacion: Notificacion)

}
