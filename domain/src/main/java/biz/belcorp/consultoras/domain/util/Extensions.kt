package biz.belcorp.consultoras.domain.util

import java.text.SimpleDateFormat
import java.util.*

fun String.replaceIs(find : String, replace: String) : String {
    return when(this){
        find -> replace
        else -> this
    }
}

fun List<String?>?.join(delimiter: String) : String? {
    var joined = ""
    this?.forEachIndexed { index, s ->
        joined = if(index+1 != this.size) joined.plus(s).plus(delimiter)
                 else joined.plus(s)
    }
    return joined
}

fun String.replaceByEmptyIs(find : String) : String {
    return this.replaceIs(find, "")
}

fun String?.toDateAndFormat(srcFormat : String, outFormat : String) : String {
    return when (this){
        null, "" -> ""
        else -> {
            val inFormat = SimpleDateFormat(srcFormat, Locale.getDefault())
            val fecha = inFormat.parse(this)
            val ouFormat = SimpleDateFormat(outFormat, Locale.getDefault())
            return ouFormat.format(fecha)
        }
    }
}

fun Double.toStringOpcionalDecimal() : String{
    val iDec = this.toInt()
    val dif:Double = this - iDec.toDouble()
    return when(dif){
        0.0 -> this.toInt().toString()
        else -> this.toString()
    }
}

fun String.convertToDouble(cadena: String): Double{
    return try {
        cadena.toDouble()
    } catch (e: Exception) {
        0.0

    }
}

