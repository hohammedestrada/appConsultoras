package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

class MensajeProlEntity {
    @SerializedName("CodigoMensajeRxP")
    var code: String ? = null
    @SerializedName("MensajeRxP")
    var message: String ? = null
    @SerializedName("UrlImagen")
    var imageUrl: String? = null
}
