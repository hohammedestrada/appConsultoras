package biz.belcorp.consultoras.common.component

import android.app.Service
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import biz.belcorp.consultoras.R
import kotlinx.android.synthetic.main.view_sms.view.*

class SMSLayout : LinearLayout {

    private val imm = context.getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
    private var numbers = mutableListOf<EditText>()
    private var layouts = mutableListOf<LinearLayout>()
    var listener: SMSListener? = null
    var changed: Boolean = true
    var active: Boolean = true

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs)
    }

    fun init(context: Context, attributeSet: AttributeSet?) {
        View.inflate(getContext(), R.layout.view_sms, this)
        numbers = mutableListOf(edtFirst, edtSecond, edtThird, edtFourth, edtFifth, edtSixth)
        layouts = mutableListOf(lnlFirst, lnlSecond, lnlThird, lnlFourth, lnlFifth, lnlSixth)
        startListeners()
    }

    fun clearText(){
        numbers.forEach {
            changed = false
            it.clearText()
        }
        changed = true
        edtFirst.requestFocus()
    }

    fun setCode(code: String?){
        code?.let {
            val letters = code.toCharArray()
            if(letters.size == 6){
                edtFirst.setText(letters[0].toString())
                edtSecond.setText(letters[1].toString())
                edtThird.setText(letters[2].toString())
                edtFourth.setText(letters[3].toString())
                edtFifth.setText(letters[4].toString())
                edtSixth.setText(letters[5].toString())
                edtSixth.requestFocus()
                edtSixth.setCursorAtTheEnd()
                listener?.onCodeComplete()
            }else{
                listener?.onInvalidText()
            }
        }
    }

    fun setAvailability(available:Boolean){
        if (available){
            clearText()
            active = true
        }else{
            clearText()
            numbers.forEach { it.clearFocus() }
            active = false
        }
    }

    fun showStatus(status: Int){

        when(status){
            STATUS_FAILURE -> {
                lnlTouch.isEnabled = true
                imgStatus.setBackgroundResource(R.drawable.ic_sms_error)
                txtStatus.text = resources.getText(R.string.sms_component_failure)
                txtStatus.setTextColor(resources.getColor(R.color.red))
                lnlStatus.visibility = View.VISIBLE
            }
            STATUS_SUCCESS -> {
                lnlTouch.isEnabled = false
                imgStatus.setBackgroundResource(R.drawable.ic_sms_success)
                txtStatus.text = resources.getText(R.string.sms_component_success)
                txtStatus.setTextColor(resources.getColor(R.color.sms_correct))
                lnlStatus.visibility = View.VISIBLE
                numbers.forEach{ it.clearFocus() }
            }
            STATUS_NEUTRAL -> {
                lnlTouch.isEnabled = true
                lnlStatus.visibility = View.GONE
            }

        }
        changeLineColor(status)
    }

    fun getCode(): String{
        return  (edtFirst.text.toString()  +
            edtSecond.text.toString() +
            edtThird.text.toString()  +
            edtFourth.text.toString() +
            edtFifth.text.toString()  +
            edtSixth.text.toString()).trim()
    }

    private fun startListeners(){

        lnlTouch.setOnClickListener{
            if(active) imm.toggleSoftInput(0, 3)
        }

        edtFirst.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(e: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(c: CharSequence?, p1: Int, p2: Int, p3: Int) { if(changed){ checkText(c.toString(), edtFirst) }else{ changed = true } }
        })

        edtSecond.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(e: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(c: CharSequence?, p1: Int, p2: Int, p3: Int) { if(changed){ checkText(c.toString(), edtSecond) }else{ changed = true} }
        })

        edtThird.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(e: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(c: CharSequence?, p1: Int, p2: Int, p3: Int) { if(changed){ checkText(c.toString(), edtThird) }else{ changed = true } }
        })

        edtFourth.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(e: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(c: CharSequence?, p1: Int, p2: Int, p3: Int) { if(changed){ checkText(c.toString(), edtFourth) }else{ changed = true } }
        })

        edtFifth.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(e: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(c: CharSequence?, p1: Int, p2: Int, p3: Int) { if(changed){ checkText(c.toString(), edtFifth) }else{ changed = true } }
        })

        edtSixth.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(e: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(c: CharSequence?, p1: Int, p2: Int, p3: Int) { if(changed){ checkText(c.toString(), edtSixth) }else{ changed = true } }
        })

    }

    private fun checkText(text:String, editText: EditText){

        val number = editText.tag.toString().toInt()
        return when {
            text.length == 2 -> {
                changed = false
                editText.setText(text.substring(0,1))
                editText.setCursorAtTheEnd()
                nextStep(number,text.substring(1,2) )
                changed = true
            }
            text.isEmpty() ->{
                changed = false
                backStep(number)
                changed = true
            }
            else -> {}
        }

    }
    private fun nextStep(step:Int, text: String){
        changed = false
        when(step){
            1 -> { edtSecond.setText(text); edtSecond.requestFocus(); edtSecond.setCursorAtTheEnd()}
            2 -> { edtThird.setText(text); edtThird.requestFocus(); edtThird.setCursorAtTheEnd()}
            3 -> { edtFourth.setText(text); edtFourth.requestFocus(); edtFourth.setCursorAtTheEnd()}
            4 -> { edtFifth.setText(text); edtFifth.requestFocus(); edtFifth.setCursorAtTheEnd()}
            5 -> { edtSixth.setText(text);
                edtSixth.requestFocus();
                edtSixth.setCursorAtTheEnd()

                imm.toggleSoftInput(0, 0)
                handler.postDelayed({
                    listener?.onCodeComplete()
                }, 500)

            }
        }
    }

    private fun backStep(step:Int) {
        changed = false
        when (step) {
            2 -> { edtFirst.requestFocus(); edtFirst.setCursorAtTheEnd()}
            3 -> { edtSecond.requestFocus(); edtSecond.setCursorAtTheEnd()}
            4 -> { edtThird.requestFocus(); edtThird.setCursorAtTheEnd()}
            5 -> { edtFourth.requestFocus(); edtFourth.setCursorAtTheEnd()}
            6 -> {
                edtFifth.requestFocus()
                edtFifth.setCursorAtTheEnd()
                showStatus(STATUS_NEUTRAL)
            }
        }
    }

    fun firstFocus(){
        edtFirst.requestFocus()
        imm.hideSoftInputFromWindow(edtFirst.windowToken, 0)
    }

    private fun changeLineColor(color: Int){
        layouts.forEach{
            when(color){
                STATUS_FAILURE -> it.setBackgroundResource(R.drawable.bg_sms_failure)
                STATUS_SUCCESS -> it.setBackgroundResource(R.drawable.bg_sms_success)
                STATUS_NEUTRAL -> it.setBackgroundResource(R.drawable.bg_sms)
            }
        }
    }

    private fun EditText.setCursorAtStart(){
        this.setSelection(0)
    }

    private fun EditText.setCursorAtTheEnd(){
        this.setSelection(this.text.length)
    }

    private fun EditText.clearText(){ this.setText("") }

    interface SMSListener{
        fun onCodeComplete()
        fun onInvalidText()
    }

    companion object {
        const val STATUS_FAILURE = 0
        const val STATUS_SUCCESS = 1
        const val STATUS_NEUTRAL = 2
    }
}

/*private fun checkLast(): Int{
       var position = 1
       val edited = numbers.filter { it.text.length == 1 }
       edited.forEach {
           val step = it.tag.toString().toInt()
           if(step > position) position = step
       }
       return position
   }

   private fun setFocus(step: Int){
       numbers.forEach { it.clearFocus() }
       when(step){
           1 -> edtFirst.setCursorAtTheEnd()
           2 -> edtSecond.setCursorAtTheEnd()
           3 -> edtThird.setCursorAtTheEnd()
           4 -> edtFourth.setCursorAtTheEnd()
           5 -> edtFifth.setCursorAtTheEnd()
           6 -> edtSixth.setCursorAtTheEnd()
           else -> {
               firstFocus()
           }
       }
   }*/
