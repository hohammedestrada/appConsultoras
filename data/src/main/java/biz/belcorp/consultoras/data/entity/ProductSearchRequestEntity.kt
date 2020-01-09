package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

class ProductSearchRequestEntity {

    @SerializedName("campania")
    var campaingId: Int = 0

    @SerializedName("zonaId")
    var zoneId: Int = 0

    @SerializedName("cuv")
    var cuv: String? = null

    @SerializedName("descripcion")
    var description: String? = null
}
