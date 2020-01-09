package biz.belcorp.consultoras.common.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowManager
import biz.belcorp.consultoras.R
import kotlinx.android.synthetic.main.dialog_pending_orders.*

class PendingOrdersDialog(context: Context, private var icon: Int,
                          private var title: String, private var listener: PendingOrdersDialogListener?) : Dialog(context, R.style.full_screen_dialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_pending_orders)
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        setDialog()
    }

    private fun setDialog(){
        tvwMessage.text = title
        ivDialogRipple.setImageResource(icon)
        btnDialog.setOnClickListener {
            listener?.onClickAceptar(this)
        }

        setOnKeyListener { arg0, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                listener?.onBackPressedDialog(this@PendingOrdersDialog)
            }
            true
        }
    }

    class Builder(private var context: Context) {

        private var icon: Int = 0
        private var title: String = ""
        private var listener: PendingOrdersDialogListener? = null

        fun setIcon(icon: Int) = apply {
            this.icon = icon
        }

        fun setTitle(title : String) = apply {
            this.title = title
        }

        fun setListener(listener: PendingOrdersDialogListener?) = apply {
            this.listener = listener
        }

        fun show() = PendingOrdersDialog(context, icon, title, listener).show()
    }

    interface PendingOrdersDialogListener {
        fun onClickAceptar(dialog: PendingOrdersDialog)
        fun onBackPressedDialog(dialog: PendingOrdersDialog) {
            dialog.dismiss()
        }
    }
}
