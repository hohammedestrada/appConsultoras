package biz.belcorp.consultoras.common.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import biz.belcorp.consultoras.R
import kotlinx.android.synthetic.main.dialog_rate.*

class RateDialog(context: Context, themeResId: Int = 0) : Dialog(context, themeResId) {

    var onClickRateDialog: OnClickRateDialog? = null

    companion object {
        var rateDialog: RateDialog? = null

        fun getInstance(context: Context): RateDialog? {
            if (rateDialog == null) {
                rateDialog = RateDialog(context)
            }
            return rateDialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDialog()
        setOnClick()
    }

    private fun initDialog() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
        setContentView(R.layout.dialog_rate)
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    private fun setOnClick() {
        alreadyButton.setOnClickListener {
            onClickRateDialog?.onClickAlReadyOption()
            dismiss()
        }
        yesButton.setOnClickListener {
            onClickRateDialog?.onClickYesOption()
            dismiss()
        }
        afterButton.setOnClickListener {
            onClickRateDialog?.onClickAfterOption()
            dismiss()
        }
    }

    interface OnClickRateDialog {
        fun onClickYesOption()
        fun onClickAlReadyOption()
        fun onClickAfterOption()
    }
}
