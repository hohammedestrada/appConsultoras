package biz.belcorp.consultoras.util

import java.text.Normalizer

object StringUtil {

    private val REGEX_UNACCENT = "\\p{InCombiningDiacriticalMarks}+".toRegex()
    private val HEX_PATTERN = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$".toRegex()

    fun unAccent(word: String?): String {

        word?.let {

            val temp = Normalizer.normalize(it, Normalizer.Form.NFD)
            val wordFormatted = REGEX_UNACCENT.replace(temp, "")
            return wordFormatted.replace("'", "")

        } ?: return word.toString()

    }

    fun isHexColor(text: String?) : Boolean {
        return if (text.isNullOrEmpty())
            false
        else
            HEX_PATTERN.matches(text)
    }

}
