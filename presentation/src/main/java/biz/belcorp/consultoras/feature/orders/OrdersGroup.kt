package biz.belcorp.consultoras.feature.orders

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.model.client.ClienteModel
import biz.belcorp.consultoras.common.model.orders.OrderModel
import kotlinx.android.synthetic.main.view_orders_list.view.*
import java.text.DecimalFormat

class OrdersGroup : LinearLayout {

    var orderListListener: OrdersList.OrderListListener? = null

    lateinit var clientsOrderListAdapter: ClientsOrderListAdapter

    private var moneySymbol: String = ""
    private var consultantName: String = ""
    private var decimalFormat: DecimalFormat = DecimalFormat()

    var isReservation: Boolean = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    fun init() {
        View.inflate(context, R.layout.view_orders_list, this)
        rvwOrders.isNestedScrollingEnabled = false

        clientsOrderListAdapter = ClientsOrderListAdapter(ArrayList())

        rvwOrders.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvwOrders.adapter = clientsOrderListAdapter
    }

    /**super methods */
    fun setData(order: OrderModel?, clientModelList: List<ClienteModel?>?) {
        init()
        clientsOrderListAdapter.listener = orderListListener
        clientsOrderListAdapter.isReservation = isReservation
        clientsOrderListAdapter.setFormat(decimalFormat, moneySymbol, consultantName)
        clientsOrderListAdapter.order = order!!
        clientsOrderListAdapter.setList(clientModelList)
    }

    fun setDecimalFormat(decimalFormat: DecimalFormat, moneySymbol: String, consultantName: String) {
        this.decimalFormat = decimalFormat
        this.moneySymbol = moneySymbol
        this.consultantName = consultantName
        clientsOrderListAdapter.setFormat(decimalFormat, moneySymbol, consultantName)
    }

}
