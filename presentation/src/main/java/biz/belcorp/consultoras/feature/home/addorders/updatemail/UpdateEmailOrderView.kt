package biz.belcorp.consultoras.feature.home.addorders.updatemail

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import biz.belcorp.consultoras.R
import biz.belcorp.library.util.KeyboardUtil
import kotlinx.android.synthetic.main.view_update_email_order.view.*

class UpdateEmailOrderView : LinearLayout  {

    @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
    ): super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int)
        : super(context, attrs, defStyleAttr, defStyleRes)

    var updateEmailOrderListener: UpdateEmailOrderListener? = null

    init {

        LayoutInflater.from(context).inflate(R.layout.view_update_email_order,this,true)
        orientation = VERTICAL

        setBackgroundColor(ContextCompat.getColor(context,R.color.bg_grey))

        btnUpdateEmail.isEnabled = false
        edtUpdateEmail.addTextChangedListener(CheckInputValid())
        btnUpdateEmail.setOnClickListener {
            hideKeyboard()
            if(!edtUpdateEmail.text.toString().matches (
                    ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$").toRegex())) {
                tvwErrorMessage.text = context.getString(R.string.my_profile_error_invalid_email)
            }else {
                updateEmailOrderListener?.onClickUpdateEmail(edtUpdateEmail.text.toString())
            }
        }

    }


    private fun hideKeyboard() {

        KeyboardUtil.dismissKeyboard(context, btnUpdateEmail)
    }


    inner class CheckInputValid : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            tvwErrorMessage.text = resources.getString(R.string.error_wrong_email)
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(p0: Editable?) {
            btnUpdateEmail.isEnabled = p0.toString().isNotEmpty()
            if(p0.toString().isNotEmpty()){
                tvwErrorMessage.text = ""
            }else{
                tvwErrorMessage.text = resources.getString(R.string.error_wrong_email)
            }
        }

    }

    fun setEmail(email: String?){
        edtUpdateEmail.setText(email)
    }

    interface UpdateEmailOrderListener {

        fun onClickUpdateEmail(email: String)

    }

}


