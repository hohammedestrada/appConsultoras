package biz.belcorp.consultoras.feature.home.addorders

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.feature.home.addorders.updatemail.UpdateEmailOrderView
import biz.belcorp.consultoras.util.anotation.HolydayType
import kotlinx.android.synthetic.main.view_reserved.view.*

class Reserved : LinearLayout {

    constructor(context: Context?) : super(context)

    var listener : ReservedListener? = null

    init {
        View.inflate(context, R.layout.view_reserved, this)
        ripple_view.stopRipple()
        initLayoutParams()
    }

    fun postInit(type : Int){
        when(type){
            HolydayType.BELCORP_ORDER_RESERVAR -> {
                txt_message.text = "¡Reservaste tu pedido con éxito!"
            }
            HolydayType.BELCORP_ORDER_GUARDAR -> {
                txt_message.text = "¡Guardaste tu pedido con éxito!"
            }
            else -> {
                txt_message.text = "¡Guardaste tu pedido con éxito!"
            }
        }
    }

    fun initLayoutParams() {
        val params = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
        layoutParams = params

        ibtCloseDialog.setOnClickListener {
            listener?.onCloseAction()
        }
    }

    fun start() {
        ripple_view.startRipple()
    }

    fun setUpdateOrderViewListener(listener : UpdateEmailOrderView.UpdateEmailOrderListener?){
        updateEmailOrderView?.updateEmailOrderListener = listener
    }

    fun updateEmail(pendingEmail:String?){
        ibtCloseDialog.visibility = View.VISIBLE
        updateEmailOrderView.visibility = View.VISIBLE
        updateEmailOrderView.setEmail(pendingEmail)

    }

    interface ReservedListener{
        fun onCloseAction()
    }
}




