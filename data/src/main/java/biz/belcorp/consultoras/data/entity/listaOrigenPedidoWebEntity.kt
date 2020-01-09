package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

class listaOrigenPedidoWebEntity {

    @SerializedName("Codigo")
    var codigo: String? = null

    @SerializedName("Valor")
    var valor: Int? = null
}
