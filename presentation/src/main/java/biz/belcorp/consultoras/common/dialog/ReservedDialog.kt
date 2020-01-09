package biz.belcorp.consultoras.common.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.feature.home.addorders.updatemail.UpdateEmailOrderView
import kotlinx.android.synthetic.main.dialog_reserved.*

class ReservedDialog(context: Context,
                     private val statusOrder: String?,
                     private val statusMessage: String?,
                     private val email : String?,
                     val listener: ReservedDialogListener?) : Dialog(context, R.style.update_mail_dialog), LoadingView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_reserved)
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        tvwTitle.text = statusOrder
        tvwMessage.text = statusMessage
        updateEmailOrderView.setEmail(email)
        updateEmailOrderView.updateEmailOrderListener = object : UpdateEmailOrderView.UpdateEmailOrderListener {
            override fun onClickUpdateEmail(email: String) {
                showLoading()
                listener?.updateEmail(this@ReservedDialog,email)
            }
        }

        ibtCloseDialog.setOnClickListener {
            listener?.closeDialog(this)
        }
    }

    override fun showLoading() {
        viewLoading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        viewLoading.visibility = View.GONE
    }


    class Builder(val context: Context) {

        private var statusOrder: String? = null
        private var statusMessage: String? = null
        private var email: String? = null
        private var reservedDialogListener: ReservedDialogListener? = null

        fun withStatusTitle(statusOrder: String?): Builder {
            this.statusOrder = statusOrder
            return this
        }

        fun withMessage(statusMessage: String?): Builder {
            this.statusMessage = statusMessage
            return this
        }

        fun withEmail(email: String?):Builder{
            this.email = email
            return this
        }

        fun setOnAccept(listener: ReservedDialogListener?): Builder {
            this.reservedDialogListener = listener
            return this
        }


        fun show() = ReservedDialog(context, statusOrder, statusMessage, email, reservedDialogListener).show()

    }

    interface ReservedDialogListener {
        fun updateEmail(dialog: ReservedDialog,email: String)
        fun closeDialog(dialog: ReservedDialog)
    }


}
