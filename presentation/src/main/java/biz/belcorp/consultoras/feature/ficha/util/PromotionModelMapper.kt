package biz.belcorp.consultoras.feature.ficha.util

import biz.belcorp.consultoras.base.SafeLet
import biz.belcorp.consultoras.common.model.promotion.PromotionDetailModel
import biz.belcorp.mobile.components.offers.Offer
import biz.belcorp.mobile.components.offers.model.OfferDuoModel
import biz.belcorp.mobile.components.offers.model.OfferModel
import java.text.DecimalFormat

class PromotionModelMapper : SafeLet {

    var symbol = ""
    var df = DecimalFormat()

    fun transformToOfferDuoModel(condition: PromotionDetailModel?, promotions: List<PromotionDetailModel?>?) : ArrayList<OfferDuoModel> {
        return arrayListOf<OfferDuoModel>().apply {
            condition?.let { it1 ->
                transformPromotion(it1)?.let { it2 ->
                    promotions?.forEach { it3 ->
                        it3?.let { it4 -> transformPromotion(it4) }?.let { it5 -> add(OfferDuoModel(it2, it5)) }
                    }
                }
            }
        }
    }

    fun transformListPromotion(promotions: List<PromotionDetailModel?>?): ArrayList<OfferModel> {
        return arrayListOf<OfferModel>().apply {
            promotions?.forEach {
                it?.let { it1 -> transformPromotion(it1) }?.let { it2 -> add(it2) }
            }
        }
    }

    fun transformPromotion(promo: PromotionDetailModel?): OfferModel? {
        return promo?.let {
            safeLet(it.cuv, it.marcaDescripcion
                ?: "", it.descripcionCortada, it.precioVenta ?: 0, it.marcaId) { cuv, nombreMarca, nombreOferta, precioVenta, marcaID ->
                Offer.transform(
                    id = cuv,
                    brand = nombreMarca,
                    productName = nombreOferta,
                    personalAmount = formatWithMoneySymbol(symbol, df, precioVenta.toString()),
                    clientAmount = "",
                    imageURL = it.imagenURL ?: "",
                    leverName = "",
                    isSelectionType = false,
                    marcaID = marcaID,
                    imageBgURL = "",
                    textColor = "",
                    showClientAmount = true,
                    procedencia = null,
                    tipoPersonalizacion = null,
                    soldOut = !(it.tieneStock ?: true)
                )
            }
        }
    }

    private fun formatWithMoneySymbol(symbol: String, df: DecimalFormat, price: String): String {
        return "$symbol ${df.format(price.toBigDecimal())}"
    }

}
