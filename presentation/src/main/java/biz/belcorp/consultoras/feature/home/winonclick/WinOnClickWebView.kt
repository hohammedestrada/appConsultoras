package biz.belcorp.consultoras.feature.home.winonclick

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.view.LoadingView

interface WinOnClickWebView : View, LoadingView {
    fun initScreenTrack(model: LoginModel)
    fun trackBack(model: LoginModel)
    fun showUrlVideoView(url: String)
    fun showUrlYoutube(url: String)
    fun showError()
}
