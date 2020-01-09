package biz.belcorp.consultoras.common.dialog

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.util.FestivityAnimationUtil
import biz.belcorp.library.log.BelcorpLogger
import kotlinx.android.synthetic.main.dialog_fullscreen.*
import android.view.KeyEvent.KEYCODE_BACK
import android.content.DialogInterface
import android.text.Spanned
import android.view.KeyEvent


class FullScreenDialog(context: Context,
                       private var icon: Int,
                       private var iconAnimation: Boolean,
                       private var title: String,
                       private var titleHtml: Spanned?,
                       private var titleAllCaps: Boolean,
                       private var sizeTitle: Float,
                       private var message: String,
                       private var messageHtml: Spanned?,
                       private var botonClose:Boolean,
                       private var sizeMessage: Float,
                       private var buttonMessage: String,
                       private var buttonStyleDefault: Boolean,
                       private var vanish: Boolean,
                       private var action: String,
                       private var screenDismiss: Boolean,
                       private var scr: List<Int>?,
                       private var resources: Resources?,
                       private var type: Int,
                       private var timeInMillis: Long,
                       private var listener: FullScreenDialogListener?)
    : Dialog(context, R.style.full_screen_dialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_fullscreen)
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        if(botonClose){
            ivwDialogCloseGeneral.visibility=View.VISIBLE
        }
        if(icon != -1){
            ivDialog.setImageResource(icon)
            ivDialogRipple.setImageResource(icon)
        }

        ivwDialogCloseGeneral.setOnClickListener{
            dismiss()
        }

        if(iconAnimation){
            ivDialog.visibility = View.GONE
            rvwAnimation.visibility = View.VISIBLE
        } else {
            ivDialog.visibility = View.VISIBLE
            rvwAnimation.visibility = View.GONE
        }

        tvwTitle.text = if(title.isNotEmpty() || titleHtml.isNullOrEmpty()) title else titleHtml
        tvwTitle.textSize = sizeTitle
        tvwTitle.setAllCaps(titleAllCaps)
        tvwMessage.text = if(message.isNotEmpty() || messageHtml.isNullOrEmpty()) message else messageHtml
        tvwMessage.textSize = sizeMessage

        setOnKeyListener { arg0, keyCode, event ->
            // TODO Auto-generated method stub
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                listener?.onBackPressed(this@FullScreenDialog)
            }
            true
        }

        if(!buttonMessage.isEmpty()){
            btnDialog.text = buttonMessage
            btnDialog.visibility = View.VISIBLE
            if(buttonStyleDefault){
                btnDialog.setBackgroundResource(R.drawable.btn_selector_primary)
            }
            else{
                btnDialog.setBackgroundResource(R.drawable.border_black)
                btnDialog.setTextColor(resources!!.getColor(R.color.black))
            }

            btnDialog.setOnClickListener{
                listener?.onClickAceptar(this)
            }
        }

        if(!action.isEmpty()){
            tvwAction.paintFlags = tvwAction.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            tvwAction.visibility = View.VISIBLE
            tvwAction.text = action
            tvwAction.setOnClickListener {
                listener?.onClickAction(this)
            }
        }

        if(screenDismiss){
            frlScreen.setOnClickListener {
                dismiss()
            }
        }


        scr?.let { images ->
            resources?.let {
                lnlContainer.postDelayed({
                    if (type == CUSTOM_ANIMATION){
                        FestivityAnimationUtil.imageConfetti(it, lnlContainer, images)
                    }else{
                        if(images.size >= 2){
                            FestivityAnimationUtil.getCommonConfetti(
                                images[0],
                                images[1],
                                it, lnlContainer)
                        }else{
                            BelcorpLogger.w("Se debe añadir al menos dos colores para mostrar")
                        }
                    }
                }, 300)
                if(vanish)
                    finishDialog()
            }
        } ?: run {
            if(vanish)
                finishDialog()
        }
    }

    private fun finishDialog(){
        Handler().postDelayed({
            val fadeOut = AlphaAnimation(1f, 0f)
            fadeOut.duration = 1000
            frlScreen.animation = fadeOut
            frlScreen.startAnimation(fadeOut)
            fadeOut.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    listener?.onDismiss()
                    dismiss()
                }
                override fun onAnimationRepeat(animation: Animation) {}
            })
        }, timeInMillis)
    }

    class Builder(private var context: Context) {

        private var icon: Int = -1
        private var iconAnimation: Boolean = false
        private var title: String = ""
        private var titleHtml: Spanned? = null
        private var titleAllCaps: Boolean = true
        private var message: String = ""
        private var messageHtml: Spanned? = null
        private var buttonMessage: String = ""
        private var action: String = ""
        private var listener: FullScreenDialogListener? = null
        private var scr: List<Int>? = null
        private var resources: Resources? = null
        private var type: Int = SIMPLE_ANIMATION
        private var screenDismiss = false
        private var sizeTitle : Float = 20f
        private var sizeMessage : Float = 18f
        private var buttonStyleDefault: Boolean = true
        private var vanish: Boolean = true
        private var botonClose: Boolean = false
        private var timeInMillis: Long = 6500

        fun withButtonStyleDefault(default: Boolean=true): Builder{
            this.buttonStyleDefault = default
            return this
        }

        fun withButtonClose(default: Boolean = false): Builder{
            this.botonClose = default
            return this

        }

        fun withVanish(default: Boolean = true): Builder{
            this.vanish = default
            return this
        }

        fun withIcon(icon: Int): Builder {
            this.icon = icon
            return this
        }

        fun withIconAnimation(): Builder {
            this.iconAnimation = true
            return this
        }

        fun withTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun withTitle(title: String, size: Float): Builder {
            this.sizeTitle = size
            this.title = title
            return this
        }

        fun withTitleHtml(titleHtml: Spanned, size: Float): Builder {
            this.sizeTitle = size
            this.titleHtml = titleHtml
            return this
        }

        fun withMessage(message: String, size: Float): Builder {
            this.sizeMessage = size
            this.message = message
            return this
        }

        fun withMessageHtml(messageHtml: Spanned, size: Float): Builder {
            this.sizeMessage = size
            this.messageHtml = messageHtml
            return this
        }

        fun withMessage(message: String): Builder {
            this.message = message
            return this
        }


        fun withButtonMessage(buttonMessage: String): Builder {
            this.buttonMessage = buttonMessage
            return this
        }

        fun setOnItemClick(listener: FullScreenDialogListener): Builder {
            this.listener = listener
            return this
        }

        fun withAction(text: String): Builder {
            this.action = text
            return this
        }

        fun withAnimation(resources: Resources, type: Int, vararg scr: Int): Builder{
            this.scr = scr.toList()
            this.resources = resources
            this.type = type
            return this
        }

        fun withScreenDismiss(screenDismiss: Boolean): Builder{
            this.screenDismiss = screenDismiss
            return this
        }

        fun setTime(timeInMillis: Long) : Builder {
            this.timeInMillis = timeInMillis
            return this
        }

        fun show() = FullScreenDialog(context,
            icon,
            iconAnimation,
            title,
            titleHtml,
            titleAllCaps,
            sizeTitle,
            message,
            messageHtml,
            botonClose,
            sizeMessage,
            buttonMessage,
            buttonStyleDefault,
            vanish,
            action,
            screenDismiss,
            scr,
            resources,
            type,
            timeInMillis,
            listener).show()
    }


    interface FullScreenDialogListener {
        fun onClickAceptar(dialog: FullScreenDialog)
        fun onClickAction(dialog: FullScreenDialog)
        fun onDismiss()
        fun onBackPressed(dialog: FullScreenDialog) {
            dialog.dismiss()
        }
    }

    companion object {
        const val SIMPLE_ANIMATION = 1
        const val CUSTOM_ANIMATION = 2
    }

}
