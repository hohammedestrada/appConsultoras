package biz.belcorp.consultoras.common.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.WindowManager
import biz.belcorp.consultoras.R
import kotlinx.android.synthetic.main.dialog_info_list.*

class InfoListFullDialog(context: Context,
                         private var title: String,
                         private var message: String,
                         private var infoList: List<String>,
                         private var buttonMessage: String)
    : Dialog(context, R.style.full_screen_dialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_info_list)
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)

        if (!title.isEmpty()) tvwTitle.text = title
        if (!message.isEmpty()) tvwMessage.text = message
        if (!buttonMessage.isEmpty()) btnDialog.text = buttonMessage

        ivwClose.setOnClickListener {
            this.dismiss()
        }

        btnDialog.setOnClickListener{
            this.dismiss()
        }

        infoList.let {
            rvwCollection.isNestedScrollingEnabled = false
            val adapter = InfoListAdapter(it as java.util.ArrayList<String>)
            rvwCollection.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rvwCollection.adapter = adapter
        }

    }

    class Builder(private var context: Context) {

        private var title: String = ""
        private var message: String = ""
        private var infoList: List<String> = ArrayList()
        private var buttonMessage: String = context.getString(R.string.button_aceptar)

        fun withTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun withMessage(message: String): Builder {
            this.message = message
            return this
        }

        fun withList(list: ArrayList<String>): Builder {
            this.infoList = list
            return this
        }

        fun withButtonMessage(buttonMessage: String): Builder {
            this.buttonMessage = buttonMessage
            return this
        }

        fun show() = InfoListFullDialog(context, title, message, infoList, buttonMessage).show()
    }

}
