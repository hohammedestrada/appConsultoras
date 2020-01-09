package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

/**
 *
 */
class UserOrdersResumeEntity {

    @SerializedName("ClientesCampana")
    var clientesCampana: Int? = null
    @SerializedName("Mensaje")
    var mensaje: String? = null
    @SerializedName("CantidadProductos")
    var cantidadProductos: Int? = null
}
