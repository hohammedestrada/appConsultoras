package biz.belcorp.consultoras.feature.embedded.success

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.view.LoadingView

interface WalkSuccessWebView : View, LoadingView {
    fun showUrl(url: String)
    fun showError()
    fun trackBack(model: LoginModel)
    fun initScreenTrack(model: LoginModel)
}
