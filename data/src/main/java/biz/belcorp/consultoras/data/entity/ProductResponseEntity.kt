package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table

import java.io.Serializable

import biz.belcorp.consultoras.data.db.ConsultorasDatabase

/**
 *
 */
@Table(database = ConsultorasDatabase::class, name = "ProductMovement")
class ProductResponseEntity : Serializable {

    @PrimaryKey
    @Column(name = "PedidoID")
    @SerializedName(value = "PedidoDetalleID", alternate = ["pedidoDetalleID"])
    var productMovementID: Int = 0
    @Column(name = "MovimientoId")
    @SerializedName(value = "MovimientoId", alternate = ["movementId"])
    var movementId: Int = 0
    @Column(name = "ClienteID")
    @SerializedName(value = "ClienteID", alternate = ["clienteID"])
    var clientID: Int = 0
    @Column(name = "Quantity")
    @SerializedName(value = "Cantidad", alternate = ["cantidad"])
    var quantity: Int = 0
    @Column(name = "Code")
    @SerializedName(value = "CUV", alternate = ["cuv"])
    var code: String? = null
    @Column(name = "Name")
    @SerializedName(value = "Descripcion", alternate = ["descripcion"])
    var name: String? = null
    @Column(name = "Price")
    @SerializedName(value = "PrecioUnidad", alternate = ["precioUnidad"])
    var price: Double = 0.toDouble()
}
