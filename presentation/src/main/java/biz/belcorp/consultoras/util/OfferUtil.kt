package biz.belcorp.consultoras.util

import biz.belcorp.mobile.components.offers.model.OfferModel

object OfferUtil {

    const val COD_EST_COMPUESTA_VARIABLE = 2003

    private fun isDigitalOffer(strategyTypeCode: String?): Boolean {
        return (strategyTypeCode in listOf("030", "005", "001", "007", "008", "009", "010", "011"))
    }

    fun checkOfferSelectionType(strategyTypeCode: String?, strategyCode: Int): Boolean {
        return (isDigitalOffer(strategyTypeCode) && strategyCode == COD_EST_COMPUESTA_VARIABLE)
    }

    fun exists(cuv: String?, list: ArrayList<OfferModel>): Boolean {
        val model = list.firstOrNull { it.key == cuv }
        return model != null
    }
}
