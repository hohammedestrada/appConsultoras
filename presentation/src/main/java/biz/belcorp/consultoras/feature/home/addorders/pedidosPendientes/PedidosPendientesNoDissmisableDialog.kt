package biz.belcorp.consultoras.feature.home.addorders.pedidosPendientes

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.util.GlobalConstant
import kotlinx.android.synthetic.main.dialog_update_email.*

class PedidosPendientesNoDissmisableDialog(context: Context, var pedidos:Int?,
                                           private var listener: PedidosPendientesNoDissmisableDialog.UpdateMailListener?)
    : Dialog(context, R.style.update_mail_dialog) {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_pedido_pendientes_no_dismissable)
        this.setCanceledOnTouchOutside(false)
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        btn_update.setOnClickListener {
            listener?.goToPendingOrders()
            dismiss()
        }

        tvw_cancelar.setOnClickListener {
            listener?.onIgnorarPedidos()
            dismiss()
        }

        ivw_close.setOnClickListener {
            listener?.onCloseDialog()
            dismiss()
        }

        buildTitle(pedidos)

        tvw_cancelar.paintFlags = tvw_cancelar.paintFlags or Paint.UNDERLINE_TEXT_FLAG
    }

    private fun  buildTitle(pedidos: Int?){

        var title = "Tienes $pedidos pedidos por aprobar"

        if(pedidos?.equals(1) == true) {
            title = GlobalConstant.PEDIDIO_APROBAR
        }

        textView6.text = title
    }

    override fun cancel() {
        //super.cancel()
    }



    // Listener
    interface UpdateMailListener {
        fun goToPendingOrders()
        fun onIgnorarPedidos()
        fun onCloseDialog()
    }


}
