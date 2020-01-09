package biz.belcorp.consultoras.common.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import biz.belcorp.consultoras.R
import kotlinx.android.synthetic.main.view_call_center.*

class CallCenterDialog(context: Context,
                       private var listener: Listener,
                       private var numberList: List<String>)
    : Dialog(context), CallCenterListAdapter.Listener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_call_center)

        numberList.let {
            rvwList.isNestedScrollingEnabled = false
            val adapter = CallCenterListAdapter(this, it as java.util.ArrayList<String>)
            rvwList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rvwList.adapter = adapter
        }

    }

    override fun onCall(number: String) {
        listener?.onCall(number)
    }

    /** */

    class Builder(private var context: Context, private var listener: Listener) {

        private var numberList: List<String> = ArrayList()


        fun withList(list: ArrayList<String>): Builder {
            this.numberList = list
            return this
        }

        fun show() = CallCenterDialog(context, listener, numberList).show()
    }

    /** */

    interface Listener {
        fun onCall(number: String)
    }

}
