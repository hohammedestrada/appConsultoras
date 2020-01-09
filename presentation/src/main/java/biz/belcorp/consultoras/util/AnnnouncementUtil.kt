package biz.belcorp.consultoras.util

import android.content.Context
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.domain.entity.Announcement
import biz.belcorp.consultoras.util.anotation.AnnouncementType

object AnnnouncementUtil {

    fun get(announcement: Int, context: Context): Announcement{
        // Note the block
        when (announcement) {
            AnnouncementType.ANNOUNCEMENT_1F60B -> {
                return Announcement().apply {
                    header = context.getString(R.string.announcement_header_1f40l)
                    titulo = context.getString(R.string.announcement_titulo_1f40l)
                    subtitulo = context.getString(R.string.announcement_subtitulo_1f40l)
                    numero = context.getString(R.string.announcement_numero_1f40l)
                    extra = context.getString(R.string.announcement_mensaje_adicional_1f40l)
                }
            }
            AnnouncementType.ANNOUNCEMENT_1F40L -> {
                return Announcement().apply {
                    header = context.getString(R.string.announcement_header_1f60b)
                    titulo = context.getString(R.string.announcement_titulo_1f60b)
                    subtitulo = context.getString(R.string.announcement_subtitulo_1f60b)
                    numero = context.getString(R.string.announcement_numero_1f60b)
                    extra = context.getString(R.string.announcement_mensaje_adicional_1f60b)
                }
            } else -> return Announcement().apply {
                header = context.getString(R.string.announcement_header_1f40l)
                titulo = context.getString(R.string.announcement_titulo_1f40l)
                subtitulo = context.getString(R.string.announcement_subtitulo_1f40l)
                numero = context.getString(R.string.announcement_numero_1f40l)
                extra = context.getString(R.string.announcement_mensaje_adicional_1f40l)
            }

        }

    }

}
