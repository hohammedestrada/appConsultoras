package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

class RelatedOfferResponseEntity {

    @SerializedName("Total")
    var total : Int? = null

    @SerializedName("Productos")
    var productos : List<ProductCuvEntity?>? = null

}
