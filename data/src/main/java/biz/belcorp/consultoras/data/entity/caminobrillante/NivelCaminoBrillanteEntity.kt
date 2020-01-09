package biz.belcorp.consultoras.data.entity.caminobrillante

import biz.belcorp.consultoras.data.db.ConsultorasDatabase
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import java.math.BigDecimal
import java.math.BigInteger

@Table(database = ConsultorasDatabase::class, name = "NivelCaminoBrillante")
class NivelCaminoBrillanteEntity {

    @PrimaryKey(autoincrement = true)
    @Column(name = "id")
    var id: Long? = null

    @Column(name = "CodigoNivel")
    @SerializedName("CodigoNivel")
    var codigoNivel: String? = null

    @Column(name = "DescripcionNivel")
    @SerializedName("DescripcionNivel")
    var descripcionNivel: String? = null

    @Column(name = "MontoMinimo")
    @SerializedName("MontoMinimo")
    var montoMinimo: Double? = null

    @Column(name = "MontoMaximo")
    @SerializedName("MontoMaximo")
    var montoMaximo: Double? = null

    @Column(name = "isTieneOfertasEspeciales")
    @SerializedName("TieneOfertasEspeciales")
    var isTieneOfertasEspeciales: Boolean? = null

    @Column(name = "MontoFaltante")
    @SerializedName("MontoFaltante")
    var montoFaltante: BigDecimal? = BigDecimal.ONE

    @Column(name = "UrlImagenNivel")
    @SerializedName("UrlImagenNivel")
    var urlImagenNivel: String? = null

    @SerializedName("Beneficios")
    var beneficios: List<BeneficioCaminoBrillanteEntity>? = null

    @Column(name = "EnterateMas")
    @SerializedName("EnterateMas")
    var enterateMas: Int? = 0

    @Column(name = "EnterateMasParam")
    @SerializedName("EnterateMasParam")
    var enterateMasParam: String? = null

    @Column(name = "Puntaje")
    @SerializedName("Puntaje")
    var puntaje: Int? = null

    @Column(name = "PuntajeAcumulado")
    @SerializedName("PuntajeAcumulado")
    var puntajeAcumulado: Int? = null

    @Column(name = "Mensaje")
    @SerializedName("Mensaje")
    var mensaje: String? = null

    @Table(database = ConsultorasDatabase::class, name = "BeneficioCaminoBrillante")
    class BeneficioCaminoBrillanteEntity {

        @PrimaryKey(autoincrement = true)
        @Column(name = "Id")
        var id: Long? = null

        @Column(name = "CodigoNivel")
        @SerializedName("CodigoNivel")
        var codigoNivel: String? = null

        @Column(name = "CodigoBeneficio")
        @SerializedName("CodigoBeneficio")
        var codigoBeneficio: String? = null

        @Column(name = "NombreBeneficio")
        @SerializedName("NombreBeneficio")
        var nombreBeneficio: String? = null

        @Column(name = "Descripcion")
        @SerializedName("Descripcion")
        var descripcion: String? = null

        @Column(name = "UrlIcono")
        @SerializedName("Icono")
        var urlIcono: String? = null

    }

}
