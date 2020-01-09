package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

class PedidoMasivoResponseEntity {

    @SerializedName("CodigoRespuesta")
    var codigoRespuesta : String? = null

    @SerializedName("MensajeRespuesta")
    var mensajeRespuesta : String? = null

    @SerializedName("CUV")
    var cuv : String? = null

    @SerializedName("ClienteID")
    var clienteID : Int? = null

}
