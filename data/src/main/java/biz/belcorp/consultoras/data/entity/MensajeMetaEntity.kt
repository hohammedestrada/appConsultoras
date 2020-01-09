package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

class MensajeMetaEntity {

    @SerializedName("TipoMensaje")
    var tipoMensaje: String? = null

    @SerializedName("Titulo")
    var titulo: String? = null

    @SerializedName("Mensaje")
    var mensaje: String? = null
}
