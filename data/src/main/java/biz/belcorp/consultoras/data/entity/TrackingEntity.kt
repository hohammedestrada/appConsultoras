package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.OneToMany
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.sql.language.SQLite

import java.io.Serializable

import biz.belcorp.consultoras.data.db.ConsultorasDatabase

@Table(database = ConsultorasDatabase::class, name = "Tracking")
class TrackingEntity : Serializable {

    @Column
    @PrimaryKey(autoincrement = true)
    @SerializedName("ID")
    @Expose(serialize = false, deserialize = false)
    var id: Int? = null

    @Column(name = "NumeroPedido")
    @SerializedName("NumeroPedido")
    var numeroPedido: String? = null

    @Column(name = "Campania")
    @SerializedName("Campania")
    var campania: Int? = null

    @Column(name = "Estado")
    @SerializedName("Estado")
    var estado: String? = null

    @Column(name = "Fecha")
    @SerializedName("Fecha")
    var fecha: String? = null

    @SerializedName("Detalles")
    var detalles: List<TrackingDetailEntity?>? = null

    val detailDB: List<TrackingDetailEntity>
        @OneToMany(methods = [(OneToMany.Method.ALL)], variableName = "detalles", isVariablePrivate = true)
        get() {
            if (detalles == null || detalles!!.isEmpty()) {
                detalles = SQLite.select()
                        .from(TrackingDetailEntity::class.java)
                        .where(TrackingDetailEntity_Table.TrackingDetailId.eq(id))
                        .queryList()
            }
            return detalles as List<TrackingDetailEntity>
        }
}
