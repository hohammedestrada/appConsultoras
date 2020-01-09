package biz.belcorp.consultoras.feature.makeup

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.data.entity.ProductoMasivo
import biz.belcorp.consultoras.domain.entity.Menu
import biz.belcorp.consultoras.domain.entity.User

interface SummaryView: View, LoadingView {

    fun initScreenTrack(model: LoginModel)
    fun trackBackPressed(model: LoginModel)
    fun setUser(user: User?)
    fun onGetMenu(menu: Menu)
    fun postInit(productosAgregados: List<ProductoMasivo>,
                 productosRechazados: List<ProductoMasivo>)
    fun onErrorMessage(exception: Throwable)
}
