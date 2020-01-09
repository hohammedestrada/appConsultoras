package biz.belcorp.consultoras.common.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import biz.belcorp.consultoras.R
import kotlinx.android.synthetic.main.dialog_regalo_por_producto.*

class DialogRegaloProducto(context: Context, var mensaje: String): Dialog(context) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_regalo_por_producto)

        window?.let{
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            popUpMsgClose.setOnClickListener { dismiss() }
            popUpMsgBtnAceptar.setOnClickListener { dismiss() }
            popUpMsgBody.text = mensaje
            popUpMsgTitle.text = context.getString(R.string.importante)
        }
    }
}
