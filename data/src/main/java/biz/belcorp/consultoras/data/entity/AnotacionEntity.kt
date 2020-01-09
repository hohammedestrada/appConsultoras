package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel

import java.io.Serializable

import biz.belcorp.consultoras.data.db.ConsultorasDatabase

@Table(database = ConsultorasDatabase::class, name = "Anotacion")
class AnotacionEntity : BaseModel(), Serializable {

    @Column(name = "ID")
    @PrimaryKey(autoincrement = true)
    @SerializedName("ID")
    @Expose(serialize = false, deserialize = false)
    var id: Int? = null

    @Column(name = "AnotacionID")
    @SerializedName("NotaId")
    var anotacionID: Int? = null

    @Column(name = "Descripcion")
    @SerializedName("Descripcion")
    var descripcion: String? = null

    @Column(name = "Estado", defaultValue = "0")
    @SerializedName("Status")
    var estado: Int? = null

    @Column(name = "ClienteID")
    @SerializedName("ClienteId")
    var clienteID: Int? = null

    @Column(name = "ClienteLocalID")
    @SerializedName("ClienteLocalID")
    @Expose
    var clienteLocalID: Int? = null

    @Column(name = "Fecha")
    @SerializedName("Fecha")
    var fecha: String? = null

    @Column(name = "Sincronizado")
    @SerializedName("Sincronizado")
    @Expose
    var sincronizado: Int? = null
}
