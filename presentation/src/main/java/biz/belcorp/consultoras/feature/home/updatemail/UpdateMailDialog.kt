package biz.belcorp.consultoras.feature.home.updatemail

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import biz.belcorp.consultoras.R
import kotlinx.android.synthetic.main.dialog_update_email.*

class UpdateMailDialog(context: Context,
                       private var listener: UpdateMailDialog.UpdateMailListener?)
    : Dialog(context, R.style.update_mail_dialog) {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_update_email)
        this.setCanceledOnTouchOutside(true)
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        btn_update.setOnClickListener {
            listener?.goToUpdateMail(this)
            dismiss()
        }

        tvw_cancelar.setOnClickListener {
            listener?.onCancelUpdate()
            dismiss()
        }

        ivw_close.setOnClickListener {
            listener?.onCloseDialog()
            dismiss()
        }

        tvw_cancelar.paintFlags = tvw_cancelar.paintFlags or Paint.UNDERLINE_TEXT_FLAG

    }

    // Listener
    interface UpdateMailListener {
        fun goToUpdateMail(dialog: UpdateMailDialog)
        fun onCancelUpdate()
        fun onCloseDialog()
    }


}
