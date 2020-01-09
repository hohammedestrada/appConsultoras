package biz.belcorp.consultoras.common.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import biz.belcorp.consultoras.R
import biz.belcorp.library.util.KeyboardUtil
import biz.belcorp.library.util.NetworkUtil
import kotlinx.android.synthetic.main.dialog_enter_dni.*
import org.jetbrains.anko.runOnUiThread

class EnterDniDialog(
    context: Context,
    private var onClickEnterDniDialog: OnClickEnterDniDialog?) : Dialog(context, R.style.full_screen_dialog) {

    companion object {
        private const val MAX_CHARACTERS = 8
        private const val MIN_NAME_CHARACTERS = 2
        private const val DELAY_TIME = 3000L
    }

    private var handler: Handler? = null
    private var runnable: Runnable? = null
    private var isLoading = false
    var isUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window?.let{
            setContentView(R.layout.dialog_enter_dni)
            it.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
            initView()
        }
    }

    private fun initView() {
        acceptButton.setOnClickListener {
            onClickAccept()
        }

        closeImage.setOnClickListener {
            onClickEnterDniDialog?.let {
                if (!isUpdate) {
                    it.onCloseEnterDniDialog()
                }
            }
            resetDialog()
            cancel()
        }

        successfulView.setOnClickListener {
            handler?.removeCallbacks(runnable)
            finishDialog()
        }

        dniEditText.addTextChangedListener(getTextWatcher())
        nameEditText.addTextChangedListener(getTextWatcher())
    }

    private fun onClickAccept() {
        errorDniView.visibility = if (dniEditText.text.length == MAX_CHARACTERS) View.INVISIBLE else View.VISIBLE
        errorNameView.visibility = if (nameEditText.text.trim().length >= MIN_NAME_CHARACTERS) View.INVISIBLE else View.VISIBLE
        if (dniEditText.text.length == MAX_CHARACTERS && nameEditText.text.trim().length >= MIN_NAME_CHARACTERS &&
            NetworkUtil.isThereInternetConnection(context)) {
            isLoading = true
            KeyboardUtil.dismissKeyboard(context, nameEditText)
            KeyboardUtil.dismissKeyboard(context, dniEditText)
            loadingView.visibility = View.VISIBLE
            onClickEnterDniDialog.let {
                it?.onAcceptDni(dniEditText.text.toString(), nameEditText.text.trim().toString())
            }
        }
    }

    fun resetDialog(isClearData: Boolean = false) {
        errorDniView.visibility = View.INVISIBLE
        errorNameView.visibility = View.INVISIBLE
        loadingView.visibility = View.GONE
        successfulView.visibility = View.GONE
        if (isClearData) {
            dniEditText.text.clear()
            nameEditText.text.clear()
            nameEditText.requestFocus()
        }
    }

    fun setName(name: String) {
        nameEditText.setText(name)
    }

    fun setDni(dni: String) {
        dniEditText.setText(dni)
    }

    private fun getTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                // Empty
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Empty
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val isContinueDni = dniEditText.text.length == MAX_CHARACTERS
                val isContinueName =  nameEditText.text.trim().length >= MIN_NAME_CHARACTERS
                val isContinueEnabled = isContinueDni && isContinueName
                acceptButton.setBackgroundResource(if (isContinueEnabled) R.drawable.bg_active_magenta else R.drawable.bg_blocked_gray)
                acceptButton.setTextColor(ContextCompat.getColor(context, if (isContinueEnabled) R.color.white else R.color.gray_4))
            }
        }
    }

    fun showUpdateDniSuccessful() {
        isUpdate = true
        nameText.text = nameEditText.text.trim()
        dniText.text = dniEditText.text
        loadingView.visibility = View.GONE
        successfulView.visibility = View.VISIBLE
        handler = Handler()
        runnable = Runnable {
            context.runOnUiThread {
                finishDialog()
            }
        }
        handler?.postDelayed(runnable, DELAY_TIME)
    }

    private fun finishDialog() {
        onClickEnterDniDialog.let {
            it?.onFinishUpdateDni(dniEditText.text.toString(), nameEditText.text.toString())
        }
        isLoading = false
        cancel()
    }

    override fun cancel() {
        if (!isLoading) {
            onClickEnterDniDialog?.let {
                if (!isUpdate) {
                    it.onCloseEnterDniDialog()
                }
            }
            super.cancel()
        }
    }

    interface OnClickEnterDniDialog {
        fun onCloseEnterDniDialog()
        fun onAcceptDni(dni: String, nameCollect: String)
        fun onFinishUpdateDni(dni: String, nameCollect: String)
    }
}
