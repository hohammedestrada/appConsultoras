package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

class ConditionsGetResponseEntity {

    @SerializedName("MensajePromocion")
    var messagePromotion: String? = null

    @SerializedName("CuvCondicion")
    var cuvCondition: String? = null

    @SerializedName("CuvPromocion")
    var cuvPromotion: String? = null

    @SerializedName("ImagenURLCondicion")
    var imageURLCondition: String? = null

    @SerializedName("ImagenURLPromocion")
    var imageURLPromotion: String? = null

    @SerializedName("TipoPersonalizacionCondicion")
    var typeCondition: String? = null

    @SerializedName("TipoPersonalizacionPromocion")
    var typePromotion: String? = null

    @SerializedName("DescripcionCondicion")
    var descriptionCondition: String? = null

    @SerializedName("DescripcionPromocion")
    var descriptionPromotion: String? = null

    @SerializedName("PrecioCondicion")
    var priceCondition: Double? = null

    @SerializedName("PrecioPromocion")
    var pricePromotion: Double? = null

}

