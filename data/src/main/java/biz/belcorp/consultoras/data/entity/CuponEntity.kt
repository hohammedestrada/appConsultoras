package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table

import java.math.BigDecimal

import biz.belcorp.consultoras.data.db.ConsultorasDatabase

@Table(database = ConsultorasDatabase::class, name = "Cupon")
class CuponEntity {

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

    @Column(name = "CodigoCupon")
    @SerializedName("CodigoCupon")
    var codigoCupon: String? = null

    @Column(name = "CodigoVenta")
    @SerializedName("CodigoVenta")
    var codigoVenta: String? = null

    @Column(name = "DescripcionProducto")
    @SerializedName("DescripcionProducto")
    var descripcionProducto: String? = null

    @Column(name = "UnidadesMaximas")
    @SerializedName("UnidadesMaximas")
    var unidadesMaximas: Int? = null

    @Column(name = "IndicadorCuponIndependiente")
    @SerializedName("IndicadorCuponIndependiente")
    var isIndicadorCuponIndependiente: Boolean? = null

    @Column(name = "IndicadorKit")
    @SerializedName("IndicadorKit")
    var isIndicadorKit: Boolean? = null

    @Column(name = "NumeroCampanasVigentes")
    @SerializedName("NumeroCampanasVigentes")
    var numeroCampanasVigentes: Int? = null

    @Column(name = "TextoLibre")
    @SerializedName("TextoLibre")
    var textoLibre: String? = null

    @Column(name = "PrecioUnitario")
    @SerializedName("PrecioUnitario")
    var precioUnitario: BigDecimal? = null

    @Column(name = "Ganancia")
    @SerializedName("Ganancia")
    var ganancia: BigDecimal? = null

    @Column(name = "urlImagenProducto")
    @SerializedName("ImagenURL")
    var urlImagenProducto: String? = null
}
