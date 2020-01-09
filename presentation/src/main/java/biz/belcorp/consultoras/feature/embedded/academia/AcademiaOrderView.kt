package biz.belcorp.consultoras.feature.embedded.academia

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.view.LoadingView

interface AcademiaOrderView : View, LoadingView {

    fun showPostulant()

    fun showUrl(url: String)

    fun showError()

    fun setMenuTitle(title: String?)

    fun initScreenTrack(model: LoginModel)

    fun trackBack(model: LoginModel)
}
