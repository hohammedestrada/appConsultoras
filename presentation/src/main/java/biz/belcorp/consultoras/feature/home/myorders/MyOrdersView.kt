package biz.belcorp.consultoras.feature.home.myorders

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.model.menu.MenuModel
import biz.belcorp.consultoras.common.model.order.MyOrderModel
import biz.belcorp.consultoras.common.view.LoadingView

interface MyOrdersView : View, LoadingView {

    fun onError(throwable: Throwable)

    fun initScreenTrack(transform: LoginModel)

    fun trackBackPressed(transform: LoginModel)

    fun showOrders(list: List<MyOrderModel>)

    fun showMenuOrder(menu: MenuModel?)

    fun openPDF(pdf: String)

    fun showPDFError()

    fun activePDF()

    fun showPedidosPendientesButton(pedidosPendientes :Int, isEnabled : Boolean)
}
