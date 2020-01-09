package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.data.db.ConsultorasDatabase
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel
import java.io.Serializable

@Table(database = ConsultorasDatabase::class, name = "BeneficioNivel")
class BeneficioNivelEntity : BaseModel(), Serializable {

    @PrimaryKey
    @Column(name = "CodigoNivel")
    @SerializedName("CodigoNivel")
    var codigoNivel: String? = null

    @PrimaryKey
    @Column(name = "CodigoBeneficio")
    @SerializedName("CodigoBeneficio")
    var codigoBeneficio: String? = null

    @Column(name = "Titulo")
    @SerializedName("Titulo")
    var titulo: String? = null

    @Column(name = "Descripcion")
    @SerializedName("Descripcion")
    var descripcion: String? = null

    @Column(name = "UrlImagen")
    @SerializedName("UrlImagen")
    var urlImagen: String? = null

}
