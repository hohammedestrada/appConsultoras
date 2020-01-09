package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel

import java.io.Serializable

import biz.belcorp.consultoras.data.db.ConsultorasDatabase

@Table(database = ConsultorasDatabase::class, name = "Config")
class ConfigEntity : BaseModel(), Serializable {

    @Column(name = "ID")
    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0

    @Column(name = "ConnectivityType")
    @SerializedName("connectivityType")
    var connectivityType: Int = 0

    @Column(name = "Notification")
    @SerializedName("notification")
    var isNotification: Boolean = false

    @Column(name = "Sonido")
    @SerializedName("sonido")
    var isSonido: Boolean = false
}
