package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.data.db.ConsultorasDatabase
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import java.io.Serializable

/**
 *
 */
@Table(database = ConsultorasDatabase::class, name = "UserDetail")
class UserDetailEntity : Serializable {

    @PrimaryKey
    @SerializedName("detailType")
    var detailType: Int? = 0

    @Column
    @SerializedName("amount")
    var amount: Int? = 0

    @Column
    @SerializedName("value")
    var value: Double? = 0.toDouble()

    @Column(name = "state")
    @SerializedName("state")
    var isState: Boolean? = false

    @Column
    @SerializedName("name")
    var name: String? = null

    @Column
    @SerializedName("detailDescription")
    var detailDescription: String? = null

    constructor() {
        // EMPTY
    }

    constructor(detailType: Int?, detailDescription: String?) {
        this.detailType = detailType
        this.detailDescription = detailDescription
    }

}
