package biz.belcorp.consultoras.common.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.WindowManager
import biz.belcorp.consultoras.R
import biz.belcorp.library.util.StringUtil
import kotlinx.android.synthetic.main.dialog_info_list_obs.*

class InfoListFullObsDialog(context: Context,
                            private var title: String,
                            private var message: String,
                            private var infoList: List<String>,
                            private var buttonMessage: String, private var Call: (() -> Unit)?)
    : Dialog(context, R.style.full_screen_dialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_info_list_obs)
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)

        if (!title.isEmpty()) tvwTitle.text = title

        if (!buttonMessage.isEmpty()) btnDialog.text = buttonMessage

        ivwClose.setOnClickListener {
            this.dismiss()
        }

        btnDialog.setOnClickListener {
            Call?.invoke()
            this.dismiss()
        }

        infoList.let {
            rvwCollection.isNestedScrollingEnabled = false
            val adapter = InfoListObsAdapter(it as java.util.ArrayList<String>)
            rvwCollection.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rvwCollection.adapter = adapter
        }
    }

    class Builder(private var context: Context) {

        private var title: String = StringUtil.Empty
        private var message: String = StringUtil.Empty
        private var infoList: List<String> = ArrayList()
        private var buttonMessage: String = context.getString(R.string.button_obs_promo)

        private var Callback: (() -> Unit)? = null

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

        fun withCallback(Call: (() -> Unit)?): Builder {
            this.Callback = Call
            return this
        }

        fun withButtonMessage(buttonMessage: String): Builder {
            this.buttonMessage = buttonMessage
            return this
        }


        fun show() = InfoListFullObsDialog(context, title, message, infoList, buttonMessage, Callback).show()
    }

}
