package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.data.db.ConsultorasDatabase
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel
import java.io.Serializable


@Table(database = ConsultorasDatabase::class, name = "SubseccionWebLocal")
class SubseccionEntity : BaseModel(), Serializable {

    @PrimaryKey(autoincrement = true)
    @Column
    var uid: Long = 0L

    @Column(name = "codigo")
    @SerializedName("codigo")
    var codigo: String? = null

    @Column(name = "valor")
    @SerializedName("valor")
    var valor: String? = null

}
