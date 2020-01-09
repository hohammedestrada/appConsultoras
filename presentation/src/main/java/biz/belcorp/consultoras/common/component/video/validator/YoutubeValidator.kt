package biz.belcorp.consultoras.common.component.video.validator

import java.util.regex.Pattern

class YoutubeValidator {
    companion object {
        val patternFormat = "^[a-zA-Z0-9_-]{11}\$"

        fun isIdVideo(idVideo: String): Boolean {
            val pattern = Pattern.compile(patternFormat)
            val matcher = pattern.matcher(idVideo)
            return matcher.matches()
        }
    }
}
