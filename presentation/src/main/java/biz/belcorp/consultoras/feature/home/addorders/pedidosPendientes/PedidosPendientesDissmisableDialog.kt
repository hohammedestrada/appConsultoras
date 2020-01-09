package biz.belcorp.consultoras.feature.home.addorders.pedidosPendientes

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import biz.belcorp.consultoras.R
import kotlinx.android.synthetic.main.dialog_update_email.*

class PedidosPendientesDissmisableDialog(context: Context,private var pedidos:Int?,private var callToAction : Boolean?,
                                         private var listener: PedidosPendientesDissmisableDialog.PedidosPendientesListener?)
    : Dialog(context, R.style.update_mail_dialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_pedido_pendientes_dismissable)
        this.setCanceledOnTouchOutside(true)
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        btn_update.setOnClickListener {
            listener?.goToPendingOrders()
            dismiss()
        }

        tvw_cancelar.setOnClickListener {
            listener?.onCancelDialog()
            dismiss()
        }

        if(callToAction?:false) tvw_cancelar.text = context.resources.getString(R.string.lo_vere_despues_calltoaction)

        buildTitle(pedidos)

        tvw_cancelar.paintFlags = tvw_cancelar.paintFlags or Paint.UNDERLINE_TEXT_FLAG
    }

    private fun  buildTitle(pedidos: Int?){

        var title = "Tienes $pedidos pedidos por aprobar"

        if(pedidos?.equals(1) == true) {
            title = "Tiene 1 pedido por aprobar"
        }

        textView6.text = title
    }

    // Listener
    interface PedidosPendientesListener {
        fun goToPendingOrders()
        fun onCancelDialog()
        fun onCloseDialog()
    }
}
