package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

class BackOrderRequest{

    @SerializedName("CampaniaID")
    var campaniaId : Int? = null

    @SerializedName("AceptacionConsultoraDA")
    var aceptacionConsultoraDA : Int? = null

    @SerializedName("PedidoID")
    var pedidoID : Int? = null

    @SerializedName("PedidoDetalleID")
    var pedidoDetalleID : Int? = null

    @SerializedName("CUV")
    var cuv : String? = null

    @SerializedName("Identifier")
    var identifier : String? = null

    @SerializedName("CodigoPrograma")
    var codigoPrograma : String? = null

}
