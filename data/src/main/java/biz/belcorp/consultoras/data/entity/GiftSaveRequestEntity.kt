package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

class GiftSaveRequestEntity {
    @SerializedName("CampaniaID")
    var campaniaID: Int? = null
    @SerializedName("nroCampanias")
    var nroCampanias: Int? = null
    @SerializedName("codigoPrograma")
    var codigoPrograma: String? = null
    @SerializedName("consecutivoNueva")
    var consecutivoNueva: Int? = null
    @SerializedName("Identifier")
    var identifier : String? = null
}
