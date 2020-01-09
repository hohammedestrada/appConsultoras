package biz.belcorp.consultoras.feature.notifications.list

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.domain.entity.Notificacion
import biz.belcorp.consultoras.domain.entity.User

interface FBMessagingView : View, LoadingView {

    fun initScreenTrack(model: LoginModel)
    fun trackBackPressed(model: LoginModel)
    fun onNotificacionSaved(t: Boolean)

}
