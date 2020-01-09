package biz.belcorp.consultoras.feature.home.addorders

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.mobile.components.offers.OrderTotal
import kotlinx.android.synthetic.main.fragment_total.*
import biz.belcorp.mobile.components.offers.model.OrderTotalModel
import biz.belcorp.mobile.components.offers.model.GananciaItemModel


class TotalFragment: BaseFragment (), OrderTotal.OnOrderTotalListener {

    var btnAdd = ""
    var listener : Listener? = null

    private lateinit var tvwTotalText: String
    private lateinit var tvwDsctoOfertaNivelText: String
    private lateinit var tvwDsctoOfertaWebText: String
    private lateinit var tvwGananciaTotalText: String

    override fun context(): Context? {
        val activity = activity
        return activity?.applicationContext
    }

    // Overrides Fragment
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Listener) {
            this.listener = context
        }
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        setupOrderComponent()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_total, container,
            false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupOrderComponent() {
        orderTotalComponent.setTextButton(btnAdd)

        var orderModel = OrderTotalModel()

        orderModel.total = tvwTotalText
        orderModel.descNivel = tvwDsctoOfertaNivelText
        orderModel.totalSinDesc = tvwDsctoOfertaWebText
        orderModel.ganancia = tvwGananciaTotalText

        orderTotalComponent.setModel(orderModel)
    }


    override fun didPressedButton() {
        listener?.onClickTotalFragment()
    }

    fun setButtonText(isDayProl: Boolean?, context: Context) {
        isDayProl?.let {
            btnAdd = if (it) context.getString(R.string.add_order_button_reservar)
            else context.getString(R.string.add_order_button_guardar)
        } ?: run { btnAdd = context.getString(R.string.add_order_button_modificar) }

        orderTotalComponent?.let { it.setTextButton(btnAdd) }
    }

    fun setQuantities(tvwTotalText : String,
                      tvwDsctoOfertaNivelText : String,
                      tvwDsctoOfertaWebText : String,
                      tvwGananciaTotalText : String) {

        this.tvwTotalText = tvwTotalText
        this.tvwDsctoOfertaNivelText = tvwDsctoOfertaNivelText
        this.tvwDsctoOfertaWebText = tvwDsctoOfertaWebText
        this.tvwGananciaTotalText = tvwGananciaTotalText

    }


    interface Listener{
        fun onClickTotalFragment()
    }

    companion object {
        fun newInstance(): TotalFragment {
            return TotalFragment()
        }
    }
}
