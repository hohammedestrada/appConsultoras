package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.data.db.ConsultorasDatabase
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table

/**
 *
 */
@Table(database = ConsultorasDatabase::class, name = "UserConfigData")
class UserConfigDataEntity {

    @PrimaryKey
    @Column(name = "Codigo")
    @SerializedName("Codigo")
    var code: String? = null

    @Column(name = "Valor1")
    @SerializedName("Valor1")
    var value1: String? = null

    @Column(name = "Valor2")
    @SerializedName("Valor2")
    var value2: String? = null

    @Column(name = "Valor3")
    @SerializedName("Valor3")
    var value3: String? = null

    @Column(name = "CodigoConfiguracion")
    var codeConfig: String? = null

}
