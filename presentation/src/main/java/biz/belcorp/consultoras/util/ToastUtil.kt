package biz.belcorp.consultoras.util

import android.content.Context
import android.widget.Toast


object ToastUtil {

    private var toast: Toast? = null

    /**
     * Use this to prevent multiple Toasts from spamming the UI for a long time.
     */
    fun show(context: Context, text: CharSequence, duration: Int = Toast.LENGTH_LONG) : Toast? {
        if (toast == null)
            toast = Toast.makeText(context, text, duration)
        else
            toast?.setText(text)
        toast?.show()
        return toast
    }

    fun show(context: Context, resId: Int, duration: Int = Toast.LENGTH_LONG) : Toast?{
        if (toast == null)
            toast = Toast.makeText(context, context.resources.getText(resId), duration)
        else
            toast?.setText(context.resources.getText(resId))
        toast?.show()
        return toast
    }
}
