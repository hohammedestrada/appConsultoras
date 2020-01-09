package biz.belcorp.consultoras.feature.embedded.product

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.domain.entity.Menu

interface ProductWebView : View, LoadingView {

    fun initScreenTrack(model: LoginModel)
    fun trackBack(model: LoginModel)
    fun showUrl(url: String)
    fun showPostulantDialog()
    fun showError()
    fun showSearchOption()
    fun onGetMenu(menu: Menu)

}
