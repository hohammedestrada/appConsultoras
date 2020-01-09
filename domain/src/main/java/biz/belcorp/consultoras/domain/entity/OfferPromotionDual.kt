package biz.belcorp.consultoras.domain.entity

import java.io.Serializable

class OfferPromotionDual : Serializable {
    var cuvCondition: String? = null

    var cuvPromotion: String?= null

    var imageURLCondition: String?= null

    var imageURLPromotion: String?= null

    var descriptionCondition: String?= null

    var descriptionPromotion: String?= null

    var messagePromotion: String?= null

    var typeCondition: String?= null

    var typePromotion: String?= null

    var priceCondition: Double?= null

    var pricePromotion: Double?= null

}

