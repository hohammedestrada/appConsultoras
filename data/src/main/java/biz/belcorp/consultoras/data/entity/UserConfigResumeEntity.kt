package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.data.db.ConsultorasDatabase
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table

/**
 *
 */

@Table(database = ConsultorasDatabase::class, name = "UserConfig")
class UserConfigResumeEntity {

    @PrimaryKey
    @Column(name = "ID")
    @SerializedName("ConfiguracionPaisID")
    var configCountryID: Int? = null

    @Column(name = "Codigo")
    @SerializedName("Codigo")
    var code: String? = null

    @SerializedName("ConfiguracionPaisDatos")
    var configData: List<UserConfigDataEntity>? = null

}
