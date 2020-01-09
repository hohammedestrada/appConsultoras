package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.data.db.ConsultorasDatabase
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import java.util.*

@Table(database = ConsultorasDatabase::class, name = "Notificacion")
class NotificacionEntity {

    @Column(name = "ID")
    @PrimaryKey(autoincrement = true)
    var id: Int? = null

    @Column(name = "Descripcion")
    var descripcion: String? = null

    @Column(name = "Emoji")
    var emoji: Int? = null

    @Column(name = "Estado")
    var estado: Int? = null

    @Column(name = "Fecha")
    var fecha: Date? = null

    @Column(name = "Codigo")
    var codigo: String? = null

    @Column(name = "ConsultoraId")
    var consultoraId: String? = null

    @Column(name = "NotificationID")
    var notificationId: Int? = null

}
