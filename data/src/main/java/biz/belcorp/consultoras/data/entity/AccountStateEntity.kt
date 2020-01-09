package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table

import java.io.Serializable
import java.math.BigDecimal

import biz.belcorp.consultoras.data.db.ConsultorasDatabase

@Table(database = ConsultorasDatabase::class, name = "AccountState")
class AccountStateEntity : Serializable {

    @Column
    @PrimaryKey(autoincrement = true)
    @SerializedName("ID")
    @Expose(serialize = false, deserialize = false)
    var id: Int? = null

    @Column(name = "FechaRegistro")
    @SerializedName("FechaRegistro")
    var fechaRegistro: String? = null

    @Column(name = "DescripcionOperacion")
    @SerializedName("DescripcionOperacion")
    var descripcionOperacion: String? = null

    @Column(name = "MontoOperacion")
    @SerializedName("MontoOperacion")
    var montoOperacion: BigDecimal? = null

    @Column(name = "Cargo")
    @SerializedName("Cargo")
    var cargo: BigDecimal? = null

    @Column(name = "Abono")
    @SerializedName("Abono")
    var abono: BigDecimal? = null
}
