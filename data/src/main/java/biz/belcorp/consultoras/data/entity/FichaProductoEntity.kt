package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

data class FichaProductoEntity(

    @SerializedName("Code") var code: String?,
    @SerializedName("Message") var message: String?,
    @SerializedName("Data") var data: OfertaEntity?

)
