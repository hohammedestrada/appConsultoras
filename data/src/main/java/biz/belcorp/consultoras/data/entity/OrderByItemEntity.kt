package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.data.db.ConsultorasDatabase
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table

@Table(database = ConsultorasDatabase::class, name = "OrderByItem")
class OrderByItemEntity {

    @Column(name = "Code")
    @PrimaryKey
    @SerializedName("Codigo")
    @Expose
    var code: String? = null

    @Column(name = "Description")
    @SerializedName("Descripcion")
    var description: String? = null

    @Column(name = "Value")
    @SerializedName("Valor")
    var value: String? = null

    @Column(name = "Selected")
    var isSelected: Boolean? = false
}
