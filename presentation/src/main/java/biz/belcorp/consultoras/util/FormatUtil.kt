package biz.belcorp.consultoras.util

import biz.belcorp.consultoras.feature.caminobrillante.feature.ofertasepeciales.kit.KitFragment.Companion.decimalFormat
import biz.belcorp.library.util.CountryUtil

object FormatUtil {

    fun formatWithMoneySymbol(countryISO: String?, moneySymbol: String?, price: String): String {
        decimalFormat = CountryUtil.getDecimalFormatByISO(countryISO, true)
        return "$moneySymbol ${decimalFormat.format(price.toBigDecimal())}"
    }

}
