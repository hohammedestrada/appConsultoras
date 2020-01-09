package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

class TermsRequestEntity {

    @SerializedName("Tipo")
    var tipo: Int = 0

    @SerializedName("Aceptado")
    var aceptado: Boolean? = null

}
