package biz.belcorp.consultoras.feature.home.addorders.updatemail

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import biz.belcorp.consultoras.R
import kotlinx.android.synthetic.main.dialog_confirm_update_email.*

class ConfirmUpdateEmailDialog(context: Context,val email: String,val listener: ConfirmUpdateEmailDialogListener?): Dialog(context,R.style.update_mail_dialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_confirm_update_email)
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)

        ibtCloseDialog.setOnClickListener {
            listener?.close(this)
        }

        tvwNewEmail.text = email

        tvwReenviar.setOnClickListener {
            listener?.resendEmail(tvwNewEmail.text.toString())
        }


    }


    interface ConfirmUpdateEmailDialogListener{
        fun close(dialog:ConfirmUpdateEmailDialog)
        fun resendEmail(email: String)
    }


    class Builder(var context: Context){

        var email: String = ""
        var listener : ConfirmUpdateEmailDialogListener? = null

        fun setOnDismissListener(listener: ConfirmUpdateEmailDialogListener): Builder {
            this.listener = listener
            return this
        }

        fun withEmail(email: String):Builder{
            this.email = email
            return this
        }


        fun show() = ConfirmUpdateEmailDialog(context,email,listener).show()
    }

}
