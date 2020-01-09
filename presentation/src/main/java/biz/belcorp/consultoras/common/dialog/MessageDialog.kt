package biz.belcorp.consultoras.common.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.util.StringUtil
import biz.belcorp.library.log.BelcorpLogger
import butterknife.ButterKnife
import org.apache.commons.lang3.StringUtils

@SuppressLint("ValidFragment")
class MessageDialog : DialogFragment() {

    private var stringTitle = StringUtils.EMPTY
    private var resTitle = 0

    private var stringMessage = StringUtils.EMPTY
    private var resMessage = 0

    private var messageHtml: Spanned? = null

    private var stringAceptar = StringUtils.EMPTY
    private var resAceptar = 0

    private var stringCancelar = StringUtils.EMPTY
    private var resCancelar = 0

    private var iconResource = -1
    private var iconIsVector = 0
    private var iconDrawable: Drawable? = null

    private var isClose: Boolean? = true
    private var isIcon: Boolean? = false
    private var isButtons: Boolean? = true
    private var isCancel: Boolean? = false

    private var listener: MessageDialogListener? = null

    fun setStringTitle(@StringRes resId: Int): MessageDialog {
        this.resTitle = resId
        return this
    }

    fun setResTitle(value: String): MessageDialog {
        this.stringTitle = value
        return this
    }

    fun setStringMessage(@StringRes resId: Int): MessageDialog {
        this.resMessage = resId
        return this
    }

    fun setResMessage(value: String): MessageDialog {
        this.stringMessage = value
        return this
    }

    fun setMessageHtml(value: String): MessageDialog {

        val textHtml: Spanned
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            textHtml = Html.fromHtml(value, Html.FROM_HTML_MODE_LEGACY)
        } else {
            textHtml = Html.fromHtml(value)
        }

        this.messageHtml = textHtml
        return this
    }

    fun setStringAceptar(@StringRes resId: Int): MessageDialog {
        this.resAceptar = resId
        return this
    }

    fun setResAceptar(value: String): MessageDialog {
        this.stringAceptar = value
        return this
    }

    fun setStringCancelar(@StringRes resId: Int): MessageDialog {
        this.resCancelar = resId
        return this
    }

    fun setResCancelar(value: String): MessageDialog {
        this.stringCancelar = value
        return this
    }

    fun setIcon(@DrawableRes resId: Int, iconIsVector: Int): MessageDialog {
        this.iconResource = resId
        this.iconIsVector = iconIsVector
        return this
    }

    fun setIcon(value: Drawable, iconIsVector: Int): MessageDialog {
        this.iconDrawable = value
        this.iconIsVector = iconIsVector
        return this
    }

    fun showClose(value: Boolean): MessageDialog {
        this.isClose = value
        return this
    }

    fun showIcon(value: Boolean): MessageDialog {
        this.isIcon = value
        return this
    }

    fun showButtons(value: Boolean): MessageDialog {
        this.isButtons = value
        return this
    }

    fun showCancel(value: Boolean): MessageDialog {
        this.isCancel = value
        return this
    }

    fun setListener(listener: MessageDialogListener): MessageDialog {
        this.listener = listener
        return this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_message, container, false)

        val ivwClose = ButterKnife.findById<ImageView>(view, R.id.ivw_close)
        val ivwIcon = ButterKnife.findById<ImageView>(view, R.id.ivw_icon)
        val tvwTitle = ButterKnife.findById<TextView>(view, R.id.tvw_title)
        val tvwMessage = ButterKnife.findById<TextView>(view, R.id.tvw_message)
        val lltButtons = ButterKnife.findById<LinearLayout>(view, R.id.llt_buttons)
        val btnAceptar = ButterKnife.findById<Button>(view, R.id.btn_aceptar)
        val btnCancelar = ButterKnife.findById<Button>(view, R.id.btn_cancelar)

        isClose?.let {
            if(!it)
                ivwClose.visibility = View.GONE
        }

        isIcon?.let{
            if(!it){
                ivwIcon.visibility = View.GONE
            }
            else {
                if (null != iconDrawable) {
                    ivwIcon.setImageDrawable(iconDrawable)
                } else if (-1 != iconResource) {
                    if (1 != iconIsVector)
                        ivwIcon.setImageDrawable(ContextCompat.getDrawable(context!!, iconResource))
                    else
                        ivwIcon.setImageDrawable(VectorDrawableCompat.create(context!!.resources, iconResource, null))
                }
            }
        }


        if (TextUtils.isEmpty(stringTitle) && resTitle == 0)
            stringTitle = "Error"

        if (TextUtils.isEmpty(stringMessage) && resMessage == 0 && TextUtils.isEmpty(messageHtml))
            stringMessage = "No hay conexión a internet"

        if (TextUtils.isEmpty(stringTitle) && resTitle != 0)
            tvwTitle.setText(resTitle)
        else
            tvwTitle.text = stringTitle

        if (TextUtils.isEmpty(stringMessage) && resMessage != 0)
            tvwMessage.setText(resMessage)
        else if (!TextUtils.isEmpty(stringMessage))
            tvwMessage.text = stringMessage
        else
            tvwMessage.text = messageHtml

        isButtons?.let {
            if(!it){
                lltButtons.visibility = View.GONE
            }
            else {
                if (TextUtils.isEmpty(stringAceptar) && resAceptar != 0) {
                    btnAceptar.setText(resAceptar)
                } else if (!TextUtils.isEmpty(stringAceptar))
                    btnAceptar.text = stringAceptar

                isCancel?.let {
                    if(!it){
                        btnCancelar.visibility = View.GONE
                    } else if (TextUtils.isEmpty(stringCancelar) && resCancelar != 0) {
                        btnCancelar.setText(resCancelar)
                    } else if (!TextUtils.isEmpty(stringCancelar))
                        btnCancelar.text = stringCancelar
                }
            }
        }

        ivwClose.setOnClickListener { v -> dialog.dismiss() }

        btnAceptar.setOnClickListener { v ->
            dialog.dismiss()
            listener?.aceptar()
        }

        btnCancelar.setOnClickListener { v ->
            dialog.dismiss()
            listener?.cancelar()
        }

        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.setCanceledOnTouchOutside(false)

        return dialog
    }

    override fun show(manager: FragmentManager, tag: String) {
        try {
            val ft = manager.beginTransaction()
            ft.add(this, tag)
            ft.commit()
        } catch (e: Exception) {
            BelcorpLogger.w("BottomDialog", e.message)
        }

    }

    interface MessageDialogListener {
        fun aceptar()
        fun cancelar()
    }

}
