package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

class PedidoConfigEntity {

    @SerializedName("ListaEscalaDescuento")
    var escalaDescuentoEntity: List<EscalaDescuentoEntity?>? = null

    @SerializedName("ListaMensajeMeta")
    var mensajeMetaEntity: List<MensajeMetaEntity?>? = null
}
