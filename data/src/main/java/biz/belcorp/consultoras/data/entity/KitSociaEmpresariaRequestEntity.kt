package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

class KitSociaEmpresariaRequestEntity {
    @SerializedName("CampaniaID")
    var campaniaId: Int? = null
    @SerializedName("CodigoPrograma")
    var codigoPrograma: String? = null
    @SerializedName("ConsecutivoNueva")
    var consecutivoNueva: Int? = null
}
