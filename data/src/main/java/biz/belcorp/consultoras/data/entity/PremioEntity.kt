package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table

import biz.belcorp.consultoras.data.db.ConsultorasDatabase

@Table(database = ConsultorasDatabase::class, name = "Premio")
class PremioEntity {

    @Column
    @PrimaryKey(autoincrement = true)
    @SerializedName("Id")
    @Expose(serialize = false, deserialize = false)
    var id: Int? = null

    @Column
    @SerializedName("NivelLocalId")
    @Expose(serialize = false, deserialize = false)
    var nivelLocalId: Int? = null

    @Column(name = "CodigoPremio")
    @SerializedName("CodigoPremio")
    var codigoPremio: String? = null

    @Column(name = "DescripcionPremio")
    @SerializedName("DescripcionPremio")
    var descripcionPremio: String? = null

    @Column(name = "NumeroPremio")
    @SerializedName("NumeroPremio")
    var numeroPremio: Int? = null

    @Column(name = "ImagenPremio")
    @SerializedName("ImagenPremio")
    var imagenPremio: String? = null

}
