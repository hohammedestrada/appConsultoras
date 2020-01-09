package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel

import java.io.Serializable

import biz.belcorp.consultoras.data.db.ConsultorasDatabase

@Table(database = ConsultorasDatabase::class, name = "HybrisData")
class HybrisDataEntity : BaseModel(), Serializable {

    @Column(name = "id")
    @PrimaryKey(autoincrement = true)
    @SerializedName("id")
    var id: Int? = null

    @Column(name = "trackingURL")
    @SerializedName("trackingURL")
    var trackingURL: String? = null

}
