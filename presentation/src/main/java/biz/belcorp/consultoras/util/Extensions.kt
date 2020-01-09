package biz.belcorp.consultoras.util

import android.os.Build
import android.text.Html
import android.text.Spanned
import java.math.BigDecimal

fun String.validarFormatoMail(): Boolean {
    return this.matches(
        ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$").toRegex())
}

inline fun <T> Iterable<T>.sumByBigDecimal(selector: (T) -> BigDecimal): BigDecimal {
    var sum = 0.toBigDecimal()
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

fun String.toHtml(): Spanned =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT)
    } else Html.fromHtml(this)
