package biz.belcorp.consultoras.feature.announcement

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.view.LoadingView

interface AnnouncementView:  View, LoadingView {

    // Analytics
    fun initScreenTrack(model: LoginModel)
    fun trackBackPressed(model: LoginModel)

}
