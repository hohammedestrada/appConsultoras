package biz.belcorp.consultoras.feature.embedded.tuvoz

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.view.LoadingView

interface TuVozOnlineWebView : View, LoadingView {
    fun initScreenTrack(model: LoginModel)
    fun trackBack(model: LoginModel)
    fun showUrl(url: String)
    fun showError()
    fun showError(message : String)
    fun showTVO()
    fun showEnterDni()
    fun setUrlTyc(url: String)
    fun onUpdateEmail()
}
