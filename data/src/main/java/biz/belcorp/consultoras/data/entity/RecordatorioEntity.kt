package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel

import java.io.Serializable

import biz.belcorp.consultoras.data.db.ConsultorasDatabase

/**
 * Entidad Recordatorio
 * que crea una tabla en la base de datos o
 * recibe un JSON por parte de un servicio
 *
 * @version 1.0
 * @since 2017-04-14
 */

@Table(database = ConsultorasDatabase::class, name = "Recordatorio")
class RecordatorioEntity : BaseModel(), Serializable {

    @Column(name = "Id")
    @PrimaryKey(autoincrement = true)
    @SerializedName("Id")
    @Expose(serialize = false, deserialize = false)
    var id: Int? = null

    @Column(name = "RecordatorioId")
    @SerializedName(value = "RecordatorioId", alternate = ["ClienteRecordatorioId"])
    var recordatorioId: Int? = null

    @Column(name = "ClienteID")
    @SerializedName(value = "ClienteId", alternate = ["ClienteID"])
    var clienteID: Int? = null

    @Column(name = "ClienteLocalID")
    @SerializedName("ClienteLocalID")
    @Expose
    var clienteLocalID: Int? = null

    @Column(name = "Fecha")
    @SerializedName("Fecha")
    var fecha: String? = null

    @Column(name = "Descripcion")
    @SerializedName("Descripcion")
    var descripcion: String? = null

    @Column(name = "Sincronizado")
    @SerializedName("Sincronizado")
    @Expose
    var sincronizado: Int? = null

    @Column(name = "Estado")
    @SerializedName("Status")
    var estado: Int? = null

}
