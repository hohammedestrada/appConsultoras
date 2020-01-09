package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

import java.io.Serializable
import java.math.BigDecimal

/**
 * Created by andres.escobar on 16/10/2017.
 */

class ProductoPedidoEntity : Serializable {

    @SerializedName("PedidoWebFacturadoID")
    var pedidoWebFacturadoID: Int? = null

    @SerializedName("Cantidad")
    var cantidad: Int? = null

    @SerializedName("PrecioUnidad")
    var precioUnidad: BigDecimal? = null
}
