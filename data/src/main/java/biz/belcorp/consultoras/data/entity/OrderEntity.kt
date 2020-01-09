package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

import java.io.Serializable

class OrderEntity : Serializable {

    @SerializedName("Campania")
    var campania: Int? = null
    @SerializedName("AceptacionConsultoraDA")
    var aceptacionConsultoraDA: Int? = null
    @SerializedName("CUV")
    var cuv: String? = null
    @SerializedName("Cantidad")
    var cantidad: Int? = null
    @SerializedName("OrigenPedidoWeb")
    var origenPedidoWeb: Int? = null

}
