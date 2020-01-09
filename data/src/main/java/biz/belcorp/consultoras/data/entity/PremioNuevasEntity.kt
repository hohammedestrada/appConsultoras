package biz.belcorp.consultoras.data.entity


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table

import java.math.BigDecimal

import biz.belcorp.consultoras.data.db.ConsultorasDatabase

@Table(database = ConsultorasDatabase::class, name = "PremioNuevas")
class PremioNuevasEntity {

    @Column
    @SerializedName("Id")
    @PrimaryKey(autoincrement = true)
    @Expose(serialize = false, deserialize = false)
    var id: Int? = null

    @Column
    @SerializedName("nivelProgramaLocalId")
    @Expose(serialize = false, deserialize = false)
    var nivelProgramaLocalId: Int? = null

    @Column(name = "CodigoConcurso")
    @SerializedName("CodigoConcurso")
    var codigoConcurso: String? = null

    @Column(name = "CodigoNivel")
    @SerializedName("CodigoNivel")
    var codigoNivel: String? = null

    @Column(name = "CUV")
    @SerializedName("CUV")
    var cuv: String? = null

    @Column(name = "DescripcionProducto")
    @SerializedName("DescripcionProducto")
    var descripcionProducto: String? = null

    @Column(name = "IndicadorKitNuevas")
    @SerializedName("IndicadorKitNuevas")
    var isIndicadorKitNuevas: Boolean? = null

    @Column(name = "PrecioUnitario")
    @SerializedName("PrecioUnitario")
    var precioUnitario: BigDecimal? = null

    @Column(name = "urlImagenProducto")
    @SerializedName("ImagenURL")
    var urlImagenProducto: String? = null
}
