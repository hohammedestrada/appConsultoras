package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.data.db.ConsultorasDatabase
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel
import java.io.Serializable

@Table(database = ConsultorasDatabase::class, name = "Params")
class ParamsEntity : BaseModel(), Serializable {

    @Column(name = "ID")
    @PrimaryKey(autoincrement = true)
    var id: Int? = null

    @Column(name = "CampaniaActual")
    @SerializedName("CampaniaActual")
    var campaniaActual: String? = null

    @Column(name = "CodigoZona")
    @SerializedName("CodigoZona")
    var codigoZona: String? = null

    @Column(name = "TopAnterior")
    @SerializedName("TopAnterior")
    var topAnterior: Int? = null

    @Column(name = "TopSiguiente")
    @SerializedName("TopSiguiente")
    var topSiguiente: Int? = null

    @Column(name = "NroCampanias")
    @SerializedName("NroCampanias")
    var nroCampanias: Int? = null

    @Column(name = "MostrarCampaniaActual")
    @SerializedName("MostrarCampaniaActual")
    var isMostrarCampaniaActual: Boolean? = null

    @Column(name = "esBrillante")
    @SerializedName("esBrillante")
    var isBrillante: Boolean? = null

}
