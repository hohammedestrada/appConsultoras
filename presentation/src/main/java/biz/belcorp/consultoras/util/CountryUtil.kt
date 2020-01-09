package biz.belcorp.consultoras.util

import biz.belcorp.library.annotation.Country

class CountryUtil {

    fun getHashtagBelcorp50Years(iso: String): String {

        when (iso) {
            Country.BO -> return "#MiHistoriaBelcorpBolivia"
            Country.CL -> return "#MiHistoriaBelcorpChile"
            Country.CO -> return "#MiHistoriaBelcorpColombia"
            Country.DO -> return "#MiHistoriaBelcorpDO"
            Country.EC -> return "#MiHistoriaBelcorpEcuador"
            Country.GT -> return "#MiHistoriaBelcorpGuatemala"
            Country.PE -> return "#MiHistoriaBelcorpPerú"
            Country.SV -> return "#MiHistoriaBelcorpElSalvador"
            Country.VE -> return "#MiHistoriaBelcorpVenezuela"
            Country.CR -> return "#MiHistoriaBelcorpCostaRica"
            Country.MX -> return "#MiHistoriaBelcorpMéxico"
            Country.PA -> return "#MiHistoriaBelcorpPanamá"
            Country.PR -> return "#MiHistoriaBelcorpPR"
            else -> return ""
        }
    }

}
