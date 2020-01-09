package biz.belcorp.consultoras.feature.embedded.perfectduo

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.view.LoadingView

interface PerfectDuoView : View, LoadingView {
    fun showUrl(url: String)
    fun trackBack(model: LoginModel)
    fun showButtonToolbar()
}
