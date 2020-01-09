package biz.belcorp.consultoras.feature.orders

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.model.product.ProductItem
import kotlinx.android.synthetic.main.view_orders_list.view.*
import java.text.DecimalFormat

class OrdersList : LinearLayout, OrdersListAdapter.OrderListener {


    var orderListListener: OrderListListener? = null

    var isReservation: Boolean = false

    private lateinit var ordersListAdapter: OrdersListAdapter
    private var listTagItem: Map<Int, String> = mutableMapOf()

    private var mode = MODE_ONLINE

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs)
    }

    fun init(context: Context, attributeSet: AttributeSet?) {
        View.inflate(getContext(), R.layout.view_orders_list, this)
        rvwOrders.isNestedScrollingEnabled = false

        if (attributeSet != null) {
            val a = context.theme.obtainStyledAttributes(
                attributeSet,
                R.styleable.OrdersList,
                0, 0)

            try {
                mode = a.getInt(R.styleable.OrdersList_orders_mode, MODE_ONLINE)
            } finally {
                a.recycle()
            }
        }

        ordersListAdapter = OrdersListAdapter(this)

        rvwOrders.layoutManager = LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)
        rvwOrders.adapter = ordersListAdapter
    }

    /**super methods */

    fun setData(list: List<ProductItem?>?) {
        ordersListAdapter.setList(list)
    }

    fun setMoney(moneySymbol: String, monto: Double) {
        ordersListAdapter.setMoney(moneySymbol, monto)
    }

    fun setDecimalFormat(decimalFormat: DecimalFormat, moneySymbol: String, consultantName: String, type: Int) {
        ordersListAdapter.setFormat(decimalFormat, moneySymbol, consultantName, isReservation, type)
    }

    fun setPrecioRegalo(precioRegalo: Boolean?) {
        ordersListAdapter.setPrecioRegalo(precioRegalo)
    }

    fun setListItemTag(listItemTag: MutableMap<Int, String?>) {
        ordersListAdapter.setListItemTag(listItemTag)
    }

    override fun scrollDelete(height: Int, position: Int) {
        orderListListener?.onscrolldownDelete(height, position)
    }

    override fun scrollList(height: Int) {
        orderListListener?.scrolldown(height)
    }

    override fun onDelete(item: ProductItem) {
        orderListListener?.onDeleteItem(item)
    }

    override fun onEdit(item: ProductItem) {
        orderListListener?.onUpdateItem(item)
    }

    override fun onBackorder(item: ProductItem) {
        orderListListener?.onBackOrder(item)
    }

    override fun loadPromotion(cuv: String?) {
        orderListListener?.loadPromotion(cuv)
    }

    interface OrderListListener {
        fun onDeleteItem(item: ProductItem)
        fun onUpdateItem(item: ProductItem)
        fun scrolldown(size: Int)
        fun onscrolldownDelete(size: Int, position: Int)
        fun onBackOrder(item: ProductItem)
        fun loadPromotion(cuv: String?)
    }

    companion object {
        const val MODE_ONLINE = 1
        const val MODE_PREVIEW = 2
    }

}
