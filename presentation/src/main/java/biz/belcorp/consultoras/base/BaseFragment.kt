package biz.belcorp.consultoras.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import com.orhanobut.logger.Logger
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.dialog.DialogRegaloProducto
import biz.belcorp.consultoras.common.dialog.FullScreenDialog
import biz.belcorp.consultoras.common.dialog.MessageDialog
import biz.belcorp.consultoras.common.dialog.Tooltip
import biz.belcorp.consultoras.common.model.error.ErrorModel
import biz.belcorp.consultoras.common.view.LoadingDialogView
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.data.net.RestApi
import biz.belcorp.consultoras.data.net.dto.AuthErrorDto
import biz.belcorp.consultoras.data.net.dto.GeneralErrorDto
import biz.belcorp.consultoras.di.HasComponent
import biz.belcorp.consultoras.domain.entity.MensajeProl
import biz.belcorp.consultoras.exception.ErrorFactory
import biz.belcorp.consultoras.util.ToastUtil
import biz.belcorp.consultoras.util.anotation.ErrorCode
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.util.StringUtil
import biz.belcorp.mobile.components.dialogs.bottom.BottomDialog

abstract class BaseFragment : Fragment(), LoadingView, LoadingDialogView, biz.belcorp.consultoras.base.View {

    private var mIsInjected = false
    protected var loadingView: LoadingView? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is LoadingView) {
            this.loadingView = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        try {
            mIsInjected = onInjectView()
        } catch (e: IllegalStateException) {
            Logger.e(e.message)
            mIsInjected = false
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (mIsInjected) onViewInjected(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (!mIsInjected) {
            mIsInjected = onInjectView()
            if (mIsInjected) onViewInjected(savedInstanceState)
        }
    }

    /**
     * Gets a component for dependency injection by its type.
     *
     * @throws IllegalStateException if component has not been initialized yet.
     */
    @Throws(IllegalStateException::class)
    protected fun <C> getComponent(componentType: Class<C>): C {
        return componentType.cast((activity as HasComponent<C>).component)
                ?: throw IllegalStateException(componentType.simpleName + " has not been initialized yet.")
    }

    /**
     * Called to do an optional injection. This will be called on [.onCreate] and if
     * an exception is thrown or false returned, on [.onActivityCreated] again.
     * Within this method getIncentives the injection component and inject the view. Based on returned value
     * [.onViewInjected] will be called. Check [.onViewInjected]
     * documentation for more info.
     *
     * @return True, if injection was successful, false otherwise. Returns false by default.
     * @throws IllegalStateException If there is a failure in getting injection component or
     * injection process itself. This can occur if activity holding
     * component instance has been killed by the system and has not
     * been initialized yet.
     */
    @Throws(IllegalStateException::class)
    protected open fun onInjectView(): Boolean {
        // Return false by default.
        return false
    }

    /**
     * Called when the fragment has been injected and the field injected can be initialized. This
     * will be called on [.onViewCreated] if [.onInjectView] returned
     * true when executed on [.onCreate], otherwise it will be called on
     * [.onActivityCreated] if [.onInjectView] returned true right before.
     *
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @CallSuper
    protected open fun onViewInjected(savedInstanceState: Bundle?) {
        // Intentionally left empty.
    }

    /** Messages */

    protected fun showToastMessage(message: String) {
        ToastUtil.show(context!!, message, Toast.LENGTH_SHORT)
    }

    protected fun showSnackBarMessage(v: View, message: String) {
        val snackbar = Snackbar.make(v, message, 4000)
        snackbar.view.setBackgroundColor(ContextCompat.getColor(context(), R.color.lograste_puntaje))
        snackbar.show()
    }

    /** BottomDialog **/

    protected fun showBottomDialog(context: Context, message: String, imageLink: String?, messageColor: Int){

        imageLink?.let { image ->
            BottomDialog.Builder(context)
                .setImage(image)
                .setImageDefault(R.drawable.ic_container_placeholder)
                .setImageSize(100, 100)
                .setContentTextSize(16)
                .setContentTextColor(messageColor)
                .setContentBold()
                .setContent(message)
                .autoDismiss(true, 2500)
                .show()
        } ?: run {
            BottomDialog.Builder(context)
                .setImage(null)
                .setContentTextSize(16)
                .setContentBold()
                .setContentTextColor(messageColor)
                .setContent(message)
                .autoDismiss(true, 2500)
                .show()
        }
    }

    protected fun showBottomDialogAction(context: Context, message: String?, positiveAction: () -> Unit) {

        var textTitle = context.getString(R.string.fest_award_title)
        var textMessage= context.getString(R.string.fest_award_message)

        message?.let {
            if (it.contains("|")) {
                val msg = it.split("|")
                textTitle = msg[0]
                textMessage = msg[1]
            } else {
                textMessage = it
            }
        }

        BottomDialog.Builder(context)
            .setIcon(R.drawable.ic_anima_por)
            .setTitle(textTitle)
            .setTitleBold()
            .setContent(textMessage)
            .setNegativeText(context.getString(R.string.fest_win_alert_cancel))
            .setPositiveText(context.getString(R.string.fest_win_alert_ok))
            .setNegativeTextColor(R.color.black)
            .setNegativeBorderColor(R.color.black)
            .setNegativeBackgroundColor(R.color.white)
            .onNegative(object : BottomDialog.ButtonCallback {
                override fun onClick(dialog: BottomDialog) {
                    dialog.dismiss()
                }
            })
            .onPositive(object : BottomDialog.ButtonCallback {
                override fun onClick(dialog: BottomDialog, chbxConfirmacion: CheckBox) {
                    positiveAction.invoke()
                }
            })
            .setPositiveBackgroundColor(R.color.magenta)
            .show()
    }

    /**  Loading */

    override fun showLoading() {
        loadingView?.showLoading()
    }

    override fun hideLoading() {
        loadingView?.hideLoading()
    }

    override fun showLoadingDialog(){
        loadingView?.showLoading()
    }

    override fun hideLoadingDialog() {
        loadingView?.hideLoading()
    }

    override fun onVersionError(required: Boolean, url: String) {
        if (!isVisible) return

        val messageDialog = MessageDialog()
        if (required) {
            try {
                messageDialog.isCancelable = false
                messageDialog
                        .setIcon(R.drawable.ic_alerta, 0)
                        .setStringTitle(R.string.error_update_version_title)
                        .setStringMessage(R.string.error_update_version_message)
                        .setStringAceptar(R.string.button_aceptar)
                        .showCancel(false)
                        .showIcon(true)
                        .showClose(false)
                        .setListener(object : MessageDialog.MessageDialogListener {
                            override fun aceptar() {
                                showPlayStore(url)
                            }

                            override fun cancelar() {
                                messageDialog.dismiss()
                            }
                        })
                        .show(childFragmentManager, "modalAceptar")
            } catch (e: IllegalStateException) {
                BelcorpLogger.w("modalAceptar", e)
            }

        } else {
            try {
                messageDialog.isCancelable = false
                messageDialog
                        .setIcon(R.drawable.ic_alerta, 0)
                        .setStringTitle(R.string.error_update_version_title)
                        .setStringMessage(R.string.error_update_version_message)
                        .setStringAceptar(R.string.button_aceptar)
                        .setStringCancelar(R.string.button_omitir)
                        .showCancel(true)
                        .showIcon(true)
                        .showClose(false)
                        .setListener(object : MessageDialog.MessageDialogListener {
                            override fun aceptar() {
                                showPlayStore(url)
                            }

                            override fun cancelar() {
                                messageDialog.dismiss()
                            }
                        })
                        .show(childFragmentManager, "modalAceptar")
            } catch (e: IllegalStateException) {
                BelcorpLogger.w("modalAceptar", e)
            }

        }

    }

    private fun showPlayStore(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
        activity?.finishAffinity()
    }

    @JvmOverloads
    protected fun showTooltip(view: View, message: String, gravity: Int, middle: Boolean = false) {
        try {
            val location = IntArray(2)
            view.getLocationInWindow(location)

            location[1] = location[1] + view.height / 2

            if (middle)
                location[0] = location[0] + view.width / 3

            val layout = activity?.layoutInflater?.inflate(R.layout.layout_tooltip, null)
            val tvwMessage = layout?.findViewById<TextView>(R.id.tvw_tooltip_message)

            Tooltip(activity)
                    .setLayout(layout)
                    .setLocation(location)
                    .setGravity(gravity)
                    .setAnimationAlphaShow(1000, 0.3f, 1.0f)
                    .setAnimationAlphaDismiss(500, 1.0f, 0.0f)
                    .setTouchOutsideDismiss(true)
                    .setMatchParent(false)
                    .setOutsideColor(ContextCompat.getColor(context!!, R.color.tooltip_outside))
                    .show()

            tvwMessage?.setTextColor(ContextCompat.getColor(context!!, R.color.white))
            tvwMessage?.text = message
        } catch (e: Exception) {
            BelcorpLogger.w("showTooltip", e)
        }

    }

    /**
     * Dibuja un tooltip a la izquierda de un view que se manda por entrada.
     *
     * @param view    la vista donde va a aparecer el tooltip
     * @param message el mensaje donde se va a pintar el tooltip
     */

    protected fun drawTooltipToLeft(view: View, message: String, textColorResourceId: Int, tooltipColorResourceId: Int) {
        try {
            val location = IntArray(2)
            view.getLocationInWindow(location)
            location[1] = location[1] + view.height / 2

            val tooltipLayout = (context as Activity).layoutInflater.inflate(R.layout.layout_tooltip, null)
            val tvwTooltipMessage = tooltipLayout.findViewById<TextView>(R.id.tvw_tooltip_message)

            Tooltip(context)
                    .setLayout(tooltipLayout)
                    .setBackgroundColor(ContextCompat.getColor(activity!!, tooltipColorResourceId))
                    .setLocation(location)
                    .setGravity(Tooltip.GRAVITY_LEFT)
                    .setAnimationAlphaShow(1000, 0.3f, 1.0f)
                    .setAnimationAlphaDismiss(500, 1.0f, 0.0f)
                    .setTouchOutsideDismiss(true)
                    .setMatchParent(false)
                    .setOutsideColor(ContextCompat.getColor(context!!, R.color.tooltip_outside))
                    .show()

            tvwTooltipMessage.setTextColor(ContextCompat.getColor(context!!, textColorResourceId))
            tvwTooltipMessage.text = message

        } catch (e: Exception) {
            BelcorpLogger.w("drawTooltipToLeft", e)
        }

    }

    /**
     * Dibuja un tooltip a la izquierda de un view que se manda por entrada.
     *
     * @param view    la vista donde va a aparecer el tooltip
     * @param message el mensaje donde se va a pintar el tooltip
     */
    protected fun drawTooltipThemeToLeft(view: View, message: String, textColorResourceId: Int, gravity: Int, middle: Int) {
        try {
            val location = IntArray(2)
            view.getLocationInWindow(location)
            location[1] = location[1] + view.height / 2
            if (middle == 1)
                location[0] = location[0] + view.width / 3


            val tooltipLayout = (context as Activity).layoutInflater.inflate(
                    R.layout.layout_tooltip, null)
            val tvwTooltipMessage = tooltipLayout.findViewById<TextView>(R.id.tvw_tooltip_message)

            Tooltip(context)
                    .setLayout(tooltipLayout)
                    .setLocation(location)
                    .setGravity(gravity)
                    .setAnimationAlphaShow(1000, 0.3f, 1.0f)
                    .setAnimationAlphaDismiss(500, 1.0f, 0.0f)
                    .setTouchOutsideDismiss(true)
                    .setMatchParent(false)
                    .setOutsideColor(ContextCompat.getColor(context!!, R.color.tooltip_outside))
                    .show()

            tvwTooltipMessage.setTextColor(ContextCompat.getColor(context!!, textColorResourceId))
            tvwTooltipMessage.text = message
        } catch (e: Exception) {
            BelcorpLogger.w("drawTooltipThemeToLeft", e)
        }

    }

    protected fun processError(exception: Throwable) {
        val errorModel = ErrorFactory.create(exception)
        processError(errorModel)
    }

    protected fun processError(errorModel: ErrorModel) {
        when (errorModel.code) {
            ErrorCode.HTTP -> showToastMessage(errorModel.message)
            ErrorCode.NETWORK -> showNetworkError()
            ErrorCode.BAD_REQUEST, ErrorCode.SERVICE -> {
                if (errorModel.params == null)
                    showErrorDefault()
                else {
                    val response = RestApi.readError(errorModel.params, AuthErrorDto::class.java)
                    if (StringUtil.isNullOrEmpty(response.description))
                        showErrorDefault()
                    else
                        showErrorMessage(response.description)
                }
            }
            else -> showError("ERROR", errorModel.message)
        }
    }

    protected fun processGeneralError(errorModel: ErrorModel) {
        when (errorModel.code) {
            ErrorCode.HTTP -> showToastMessage(errorModel.message)
            ErrorCode.NETWORK -> showNetworkError()
            ErrorCode.BAD_REQUEST, ErrorCode.SERVICE -> {
                if (errorModel.params == null)
                    showErrorDefault()
                else {
                    val response = RestApi.readError(errorModel.params, GeneralErrorDto::class.java)
                    if (response == null || StringUtil.isNullOrEmpty(response.message))
                        showErrorDefault()
                    else
                        showErrorMessage(response.message)
                }
            }
            else -> showError("ERROR", errorModel.message)
        }
    }



    fun showNetworkError() {
        if (isVisible) {
            try {
                MessageDialog()
                        .setIcon(R.drawable.ic_network_error, 1)
                        .setResTitle(getString(R.string.home_error_network_title))
                        .setResMessage(getString(R.string.home_error_network_message))
                        .setStringAceptar(R.string.button_aceptar)
                        .showIcon(true)
                        .showClose(false)
                        .show(childFragmentManager, BaseFragment.MODAL_TAG)
            } catch (e: Exception) {
                BelcorpLogger.w("showNetworkError", e)
            }

        }
    }

    fun showNetworkErrorWithListener(listener: MessageDialog.MessageDialogListener) {
        if (isVisible) {
            try {
                MessageDialog()
                    .setIcon(R.drawable.ic_network_error, 1)
                    .setResTitle(getString(R.string.home_error_network_title))
                    .setResMessage(getString(R.string.home_error_network_message))
                    .setStringAceptar(R.string.button_aceptar)
                    .showIcon(true)
                    .showClose(false)
                    .setListener(listener)
                    .show(childFragmentManager, BaseFragment.MODAL_TAG)
            } catch (e: Exception) {
                BelcorpLogger.w("showNetworkError", e)
            }
        }
    }


    protected open fun showError(title: String, message: String) {
        if (isVisible) {
            try {
                MessageDialog()
                        .setIcon(R.drawable.ic_alerta, 0)
                        .setResTitle(title)
                        .setResMessage(message)
                        .setStringAceptar(R.string.button_aceptar)
                        .showIcon(true)
                        .showClose(false)
                        .show(childFragmentManager, BaseFragment.MODAL_TAG)
            } catch (e: Exception) {
                BelcorpLogger.w("showError", e)
            }

        }
    }

    protected open fun showFullScreenError(title: String, message: String, icon: Int = -1) {
        if (isVisible) {
            try {
                FullScreenDialog.Builder(context!!)
                    .withTitle(title)
                    .withMessage(message)
                    .withIcon(icon)
                    .withButtonMessage(resources.getString(R.string.button_aceptar))
                    .setOnItemClick(object : FullScreenDialog.FullScreenDialogListener {
                        override fun onBackPressed(dialog: FullScreenDialog) {
                            dialog.dismiss()
                        }

                        override fun onDismiss() {
                            // EMPTY
                        }
                        override fun onClickAceptar(dialog: FullScreenDialog) { dialog.dismiss() }
                        override fun onClickAction(dialog: FullScreenDialog) {
                            // EMPTY
                        }
                    })
                    .show()
            } catch (e: Exception) {
                BelcorpLogger.w("showFullScreenError", e)
            }

        }
    }

    protected open fun showFullScreenErrorWithListener(title: String, message: String, listener: FullScreenDialog.FullScreenDialogListener) {
        if (isVisible) {
            try {
                FullScreenDialog.Builder(context!!)
                    .withTitle(title)
                    .withMessage(message)
                    .withIcon(R.drawable.ic_error)
                    .withButtonMessage(resources.getString(R.string.button_aceptar))
                    .setOnItemClick(listener)
                    .show()
            } catch (e: Exception) {
                BelcorpLogger.w("showFullScreenError", e)
            }

        }
    }




    protected open fun showErrorMessage(message: String?) {
        if (isVisible) {
            try {
                MessageDialog()
                        .setIcon(R.drawable.ic_alerta, 0)
                        .setStringTitle(R.string.incentives_error)
                        .setResMessage(message!!)
                        .setStringAceptar(R.string.button_aceptar)
                        .showIcon(true)
                        .showClose(false)
                        .show(childFragmentManager, BaseFragment.MODAL_TAG)
            } catch (e: IllegalStateException) {
                BelcorpLogger.w(BaseFragment.MODAL_TAG, e)
            }

        }
    }


    protected open fun showErrorMessageWithListener(message: String?, listener: MessageDialog.MessageDialogListener) {
        if (isVisible) {
            try {
                MessageDialog()
                    .setIcon(R.drawable.ic_alerta, 0)
                    .setStringTitle(R.string.incentives_error)
                    .setResMessage(message ?: "")
                    .setStringAceptar(R.string.button_aceptar)
                    .showIcon(true)
                    .showClose(false)
                    .setListener(listener)
                    .show(childFragmentManager, BaseFragment.MODAL_TAG)
            } catch (e: IllegalStateException) {
                BelcorpLogger.w(BaseFragment.MODAL_TAG, e)
            }

        }
    }

    protected fun showErrorDefault() {
        if (isVisible) {
            try {
                MessageDialog()
                        .setIcon(R.drawable.ic_alerta, 0)
                        .setStringTitle(R.string.login_error_title)
                        .setStringMessage(R.string.incentives_error_message_default)
                        .setStringAceptar(R.string.button_aceptar)
                        .showIcon(true)
                        .showClose(false)
                        .show(childFragmentManager, BaseFragment.MODAL_TAG)
            } catch (e: IllegalStateException) {
                BelcorpLogger.w(BaseFragment.MODAL_TAG, e)
            }

        }
    }




    companion object {
        const val MODAL_TAG = "modalError"
    }
}
