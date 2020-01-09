package biz.belcorp.consultoras.feature.orderdetail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.model.client.ClienteModel
import biz.belcorp.consultoras.common.model.orders.OrderModel
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.feature.home.addorders.AddOrdersActivity
import biz.belcorp.consultoras.feature.home.addorders.TotalFragment
import biz.belcorp.consultoras.feature.orders.OrderDetailPresenter
import biz.belcorp.consultoras.feature.orders.di.OrderDetailComponent
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.library.util.CountryUtil
import biz.belcorp.mobile.components.offers.OrderTotal
import biz.belcorp.mobile.components.offers.model.OrderTotalModel
import biz.belcorp.mobile.components.offers.model.GananciaItemModel
import kotlinx.android.synthetic.main.fragment_order_detail.*
import kotlinx.android.synthetic.main.fragment_total.*
import java.math.BigDecimal
import java.text.DecimalFormat
import javax.inject.Inject

class OrderDetailFragment : BaseFragment(), OrderDetailView,
    TotalFragment.Listener, OrderTotal.OnOrderTotalListener {

    @Inject
    lateinit var presenter: OrderDetailPresenter

    private var montoMinimoPedido: Double = 0.toDouble()
    private var montoMaximoPedido: Double = 0.toDouble()
    private var moneySymbol: String = ""
    private var consultantName: String = ""
    private var decimalFormat: DecimalFormat = DecimalFormat()

    private var listener: Listener? = null


    private val totalFragment = TotalFragment.newInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_order_detail, container, false)
    }


    @Throws(IllegalStateException::class)
    override fun onInjectView(): Boolean {
        getComponent(OrderDetailComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        this.presenter.attachView(this)

        orderTotalComponent.orderTotalListener = this

        presenter.data()
    }

    /** Functions */

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Listener) {
            listener = context
        }
    }


    override fun onResume() {
        super.onResume()
        presenter.initTrack()
    }

    override fun context(): Context {
        return activity as Context
    }

    override fun setData(iso: String, moneySymbol: String, consultantName: String, montoMinimoPedido: Double,
                         montoMaximoPedido: Double) {
        this.decimalFormat = CountryUtil.getDecimalFormatByISO(iso, true)
        this.moneySymbol = moneySymbol
        this.consultantName = consultantName
        this.montoMinimoPedido = montoMinimoPedido
        this.montoMaximoPedido = montoMaximoPedido

        context?.let {
            totalFragment.setButtonText(null, it)
        }

        ordersGroup.setDecimalFormat(decimalFormat, moneySymbol, consultantName)
        presenter.getOrdersList()
    }

    override fun didPressedButton() {
        onClickTotalFragment()
    }

    @SuppressLint("SetTextI18n")
    override fun onFormattedOrder(order: OrderModel?, clientModelList: List<ClienteModel?>?) {
        order?.let {
            tvwTotalProductos.text = resources.getQuantityString(R.plurals.add_order_unidad, order.cantidadProductos!!, order.cantidadProductos)

            var orderModel = OrderTotalModel()
            var itemGanancia : GananciaItemModel
            var listGanancia = ArrayList<GananciaItemModel>()

            orderModel.isPagoContado = order.isPagoContado
            orderModel.total = "$moneySymbol ${decimalFormat.format(it.importeTotalDescuento)}"
            orderModel.totalSinDesc = "$moneySymbol ${decimalFormat.format(it.importeTotal)}"
            orderModel.descNivel = "$moneySymbol ${decimalFormat.format(it.descuentoProl)}"
            orderModel.ganancia = "$moneySymbol ${decimalFormat.format(it.gananciaEstimada)}"
            orderModel.misDescuentos = "$moneySymbol ${decimalFormat.format(it.montoDescuentoSIC ?: BigDecimal.ZERO)}"
            orderModel.gastosTransporte = "$moneySymbol ${decimalFormat.format(it.montoFleteSIC ?: BigDecimal.ZERO)}"
            orderModel.deudaAnterior = "$moneySymbol ${decimalFormat.format(it.montoDeudaAnteriorSIC ?: BigDecimal.ZERO)}"
            orderModel.pagoContado = "$moneySymbol ${decimalFormat.format(it.montoPagoContadoSIC ?: BigDecimal.ZERO)}"

            it.gananciaDetalle?.let {it1 ->
                it1.forEach { it2 ->
                    itemGanancia = GananciaItemModel(it2?.descripcion, "$moneySymbol ${decimalFormat.format(it2?.montoGanancia)}")
                    listGanancia.add(itemGanancia)
                }
                orderModel.listGanancia = listGanancia
            }

            orderTotalComponent.setModel(orderModel)

            totalFragment.setQuantities(orderModel.total.toString(), orderModel.descNivel.toString(), orderModel.totalSinDesc.toString(), orderModel.ganancia.toString())
            totalFragment.listener = this@OrderDetailFragment

            clientModelList?.makeTitle()

            orderTotalComponent.setTextButton(R.string.add_order_button_modificar)

            ordersGroup.isReservation = true
            ordersGroup.setData(order, clientModelList)
        }
    }

    fun trackBackPressed() {
        presenter.trackBackPressed()
    }

    private fun List<ClienteModel?>.makeTitle(){

        var count = 0

        for(i in this){
            if(i?.clienteID != 0) count++
        }

        if (count == 0 ) tvwTitulo.text = resources.getText(R.string.order_detail_title_zero)
        else tvwTitulo.text = resources.getQuantityString(R.plurals.order_detail_title, count, count)
    }

    override fun onOrderReservedBack(){
        activity?.finish()
        val intent = Intent(activity, AddOrdersActivity::class.java)
        intent.putExtra(GlobalConstant.FROM_MODIFICAR, true)
        startActivity(intent)
    }

    override fun onOrderError(it: String) {
        Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
    }

    override fun initScreenTrack(model: LoginModel) {
        Tracker.trackScreen(GlobalConstant.SCREEN_ORDERS_ORDER_DETAIL, model)
    }

    override fun trackBackPressed(model: LoginModel) {
        Tracker.trackBackEvent(GlobalConstant.SCREEN_ORDERS_ORDER_DETAIL, model)
        listener?.onBackFromFragment()
    }

    override fun onClickTotalFragment() {
        presenter.deshacerReserva()
    }


    /** Listeners */

    interface Listener {
        fun onBackFromFragment()
    }

}
