package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.Expose
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel

import java.io.Serializable

import biz.belcorp.consultoras.data.db.ConsultorasDatabase

@Table(database = ConsultorasDatabase::class, name = "NotificacionRecordatorio")
class NotificacionRecordatorioEntity : BaseModel(), Serializable {

    @Column(name = "ID")
    @PrimaryKey(autoincrement = true)
    @Expose(serialize = false, deserialize = false)
    var id: Int? = null

    @Column(name = "RecordatorioLocalID")
    var recordatorioLocalID: Int? = null

    @Column(name = "Estado")
    var estado: Int? = null

    var clienteEntity: ClienteEntity? = null

    var recordatorioEntity: RecordatorioEntity? = null
}
