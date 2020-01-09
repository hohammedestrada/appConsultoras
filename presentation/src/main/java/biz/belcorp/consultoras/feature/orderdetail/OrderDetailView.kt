package biz.belcorp.consultoras.feature.orderdetail

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.model.client.ClienteModel
import biz.belcorp.consultoras.common.model.orders.OrderModel
import biz.belcorp.consultoras.common.view.LoadingView

interface OrderDetailView : View, LoadingView {

    fun initScreenTrack(model: LoginModel)
    fun trackBackPressed(model: LoginModel)
    fun setData(iso: String, moneySymbol: String, consultantName: String, montoMinimoPedido: Double,
                montoMaximoPedido: Double)
    fun onOrderReservedBack()
    fun onOrderError(it: String)
    fun onFormattedOrder(order: OrderModel?, clientModelList: List<ClienteModel?>?)

}
