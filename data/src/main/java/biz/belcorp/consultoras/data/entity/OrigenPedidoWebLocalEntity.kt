package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.data.db.ConsultorasDatabase
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel
import java.io.Serializable

/**
 * Entidad Origen Pedido Web
 * que crea una tabla en la base de datos o
 * recibe un JSON por parte de un servicio
 *
 * @version 1.0
 * @since 2019-05-21
 */

@Table(database = ConsultorasDatabase::class, name = "OrigenPedidoWebLocal")
class OrigenPedidoWebLocalEntity : BaseModel(), Serializable {

    @PrimaryKey(autoincrement = true)
    @Column
    var uid: Long = 0L

    @Column(name = "tipoOferta")
    @SerializedName("tipoOferta")
    var tipoOferta: String? = null

    @Column(name = "codigo")
    @SerializedName("codigo")
    var codigo: String? = null

    @Column(name = "valor")
    @SerializedName("valor")
    var valor: Int? = null

}
