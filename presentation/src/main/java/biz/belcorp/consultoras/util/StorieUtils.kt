package biz.belcorp.consultoras.util

import android.os.Bundle
import biz.belcorp.consultoras.common.model.stories.StorieModel
import biz.belcorp.consultoras.util.anotation.RedirectionStories
import java.util.*

class StorieUtils {
    companion object {
        val PALANCASGANAMAS = ArrayList(
            Arrays.asList(RedirectionStories.GANA_MAS,
                RedirectionStories.GANA_MAS_ODD,
                RedirectionStories.GANA_MAS_SR,
                RedirectionStories.GANA_MAS_MG,
                RedirectionStories.GANA_MAS_OPT,
                RedirectionStories.GANA_MAS_RD,
                RedirectionStories.GANA_MAS_HV,
                RedirectionStories.GANA_MAS_DP,
                RedirectionStories.GANA_MAS_PN,
                RedirectionStories.GANA_MAS_ATP,
                RedirectionStories.GANA_MAS_LAN,
                RedirectionStories.GANA_MAS_OPM))

        fun calcularIndiceInicio(storiesModel: StorieModel): Int {
            storiesModel.contenidoDetalle?.let {
                return if (it.size > 1) {
                    val indice = it.indexOfFirst { it2 -> it2?.visto == false }
                    if (indice < 0)
                        0
                    else
                        indice
                } else {
                    0
                }
            }?:run{
                return 0
            }
        }

         fun getRedirectionGanaMas(bundle: Bundle): String {
            val palancas = PALANCASGANAMAS
            for (i in palancas.indices) {
                bundle.getString(palancas[i])?.let{
                    if (it.isNotEmpty()){
                        return palancas[i]
                    }
                }
            }
             return GlobalConstant.PALANCADEFAULT
        }

    }
}
