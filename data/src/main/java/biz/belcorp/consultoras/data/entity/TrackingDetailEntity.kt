package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table

import java.io.Serializable

import biz.belcorp.consultoras.data.db.ConsultorasDatabase

@Table(database = ConsultorasDatabase::class, name = "TrackingDetail")
class TrackingDetailEntity : Serializable {

    @Column
    @PrimaryKey(autoincrement = true)
    @SerializedName("ID")
    @Expose(serialize = false, deserialize = false)
    var id: Int? = null

    @Column(name = "TrackingDetailId")
    @SerializedName("TrackingDetailId")
    var trackingDetailId: Int? = null

    @Column(name = "Etapa")
    @SerializedName("Etapa")
    var etapa: Int? = null

    @Column(name = "Situacion")
    @SerializedName("Situacion")
    var situacion: String? = null

    @Column(name = "Fecha")
    @SerializedName("Fecha")
    var fecha: String? = null

    @Column(name = "FechaFormatted")
    @SerializedName("FechaFormatted")
    var fechaFormatted: String? = null

    @Column(name = "Alcanzado")
    @SerializedName("Alcanzado")
    var isAlcanzado: Boolean? = null

    @Column(name = "Observacion")
    @SerializedName("Observacion")
    var observacion: String? = null
}
