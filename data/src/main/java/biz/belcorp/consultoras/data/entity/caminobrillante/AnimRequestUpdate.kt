package biz.belcorp.consultoras.data.entity.caminobrillante

import com.google.gson.annotations.SerializedName

data class AnimRequestUpdate(
    @SerializedName("Key")
    var key: String,
    @SerializedName("Value")
    var value: String,
    @SerializedName("Repeat")
    var repeat: String
)
