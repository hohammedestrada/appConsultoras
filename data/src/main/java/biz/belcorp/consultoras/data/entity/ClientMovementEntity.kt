package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.sql.language.SQLite
import com.raizlabs.android.dbflow.structure.BaseModel

import java.io.Serializable
import java.math.BigDecimal

import biz.belcorp.consultoras.data.db.ConsultorasDatabase

@Table(database = ConsultorasDatabase::class, name = "ClientMovement")
class ClientMovementEntity : BaseModel(), Serializable {

    @Column(name = "ID")
    @PrimaryKey(autoincrement = true)
    @SerializedName("ID")
    @Expose(serialize = false, deserialize = false)
    var id: Int? = null

    @Column(name = "MovementID")
    @SerializedName("MovimientoId")
    var movementID: Int? = null

    @Column(name = "Amount")
    @SerializedName("Monto")
    var amount: BigDecimal? = null

    @Column(name = "Description")
    @SerializedName("Descripcion")
    var description: String? = null

    @Column(name = "CampaingCode")
    @SerializedName("CodigoCampania")
    var campaingCode: String? = null

    @Column(name = "MovementType")
    @SerializedName("TipoMovimiento")
    var movementType: String? = null

    @Column(name = "Note")
    @SerializedName("Nota")
    var note: String? = null

    @Column(name = "Date")
    @SerializedName("Fecha")
    var date: String? = null

    @Column(name = "ClienteID")
    @SerializedName(value = "ClienteId", alternate = ["ClienteID"])
    var clientID: Int? = null

    @Column(name = "ClienteLocalID")
    @SerializedName("ClienteLocalID")
    @Expose(serialize = false, deserialize = false)
    var clienteLocalID: Int? = null

    @Column(name = "ClienteCode")
    @SerializedName("CodigoCliente")
    var clientCode: Int? = null

    @Column(name = "Sincronizado")
    @SerializedName("Sincronizado")
    @Expose(serialize = false, deserialize = false)
    var sincronizado: Int? = null

    @SerializedName("Saldo")
    @Expose
    var saldo: BigDecimal? = null

    @Column(name = "Estado")
    @SerializedName("Status")
    var estado: Int? = null

    @Column(name = "Message")
    @SerializedName("Message")
    var message: String? = null

    @Column(name = "Code")
    @SerializedName("Code")
    var code: String? = null

    @SerializedName(value = "Pedidos", alternate = ["pedidos"])
    @Expose
    var productList: List<ProductResponseEntity>? = null
        get() {
            if (field == null || field!!.isEmpty()) {
                this.productList = SQLite.select()
                        .from(ProductResponseEntity::class.java)
                        .where(ProductResponseEntity_Table.MovimientoId.eq(movementID))
                        .queryList()
            }
            return field
        }
}
