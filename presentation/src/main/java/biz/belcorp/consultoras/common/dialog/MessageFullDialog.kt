package biz.belcorp.consultoras.common.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.WindowManager
import biz.belcorp.consultoras.R
import kotlinx.android.synthetic.main.dialog_information.*

class MessageFullDialog(context: Context,
                        private var sizeTitle: Float,
                        private var sizeMessage: Float,
                        private var message: String,
                        private var messageHtml: Spanned,
                        private var title: String): Dialog(context, R.style.full_screen_dialog) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_information)
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        tvw_title_general.text = title
        tvw_title_general.textSize = sizeTitle

        tvw_message_general.text = if(message.isNullOrBlank()) messageHtml else message
        tvw_message_general.textSize = sizeMessage

        ivw_close_general.setOnClickListener {
            this.dismiss()
        }

    }


    class Builder(private var context: Context){

                  private var sizeTitle: Float = 18.0f
                  private var sizeMessage: Float = 16.0f
                  private var message: String = ""
                  private var title: String = ""
                  private var msgHtml: Spanned = Html.fromHtml("")

        fun withTitle(title: String,sizeTitle: Float): Builder{
            this.title = title
            this.sizeTitle = sizeTitle
            return this
        }

        fun withTitle(title: String): Builder{
            this.title = title
            return  this
        }

        fun withMessage(message: String, sizeMessage: Float):Builder{
            this.message = message
            this.sizeMessage = sizeMessage
            return this
        }

        fun withMessage(message: String):Builder{
            this.message = message
            return this
        }

        fun withHtmlMessage(value: String):Builder{

            val textHtml: Spanned = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                Html.fromHtml(value, Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(value)
            }

            this.msgHtml = textHtml
            return this
        }

        fun withHtmlMessage(value: String, sizeMessage: Float):Builder{

            val textHtml: Spanned = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                Html.fromHtml(value, Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(value)
            }

            this.msgHtml = textHtml

            this.sizeMessage = sizeMessage
            return this
        }

        fun show() = MessageFullDialog(context,
            sizeTitle,
            sizeMessage,
            message,
            msgHtml,
            title).show()

    }

}
