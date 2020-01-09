package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

import java.util.ArrayList

class OrigenPedidoWebEntity {
    @SerializedName("TipoOferta")
    var tipoOferta: String? = null

    @SerializedName("ListaOrigenPedidoWeb")
    var listaOrigenPedidoWeb: List<listaOrigenPedidoWebEntity?>? = null
}
