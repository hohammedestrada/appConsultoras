package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table

import java.io.Serializable
import java.math.BigDecimal

import biz.belcorp.consultoras.data.db.ConsultorasDatabase

@Table(database = ConsultorasDatabase::class, name = "MyOrderEntity")
class MyOrderEntity : Serializable {

    @Column
    @PrimaryKey(autoincrement = true)
    @SerializedName("ID")
    @Expose(serialize = false, deserialize = false)
    var id: Int? = null

    @Column(name = "EstadoPedidoDesc")
    @SerializedName("EstadoPedidoDesc")
    var estadoPedidoDesc: String? = null

    @Column(name = "CampaniaID")
    @SerializedName("CampaniaID")
    var campaniaID: Int? = null

    @Column(name = "FechaRegistro")
    @SerializedName("FechaRegistro")
    var fechaRegistro: String? = null

    @Column(name = "ImporteTotal")
    @SerializedName("ImporteTotal")
    var importeTotal: BigDecimal? = null

    @Column(name = "RutaPaqueteDocumentario")
    @SerializedName("RutaPaqueteDocumentario")
    var rutaPaqueteDocumentario: String? = null

    @Column(name = "NumeroPedido")
    @SerializedName("NumeroPedido")
    var numeroPedido: Int? = null

    @Column(name = "EstadoEncuesta")
    @SerializedName("EstadoEncuesta")
    var estadoEncuesta: Int? = null
}
