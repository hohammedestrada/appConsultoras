package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel

import java.io.Serializable

import biz.belcorp.consultoras.data.db.ConsultorasDatabase

/**
 *
 */
@Table(database = ConsultorasDatabase::class, name = "Catalogo")
class CatalogoEntity : BaseModel(), Serializable {

    @Column(name = "ID")
    @PrimaryKey(autoincrement = true)
    var id: Int? = null

    @Column(name = "MarcaId")
    @SerializedName("MarcaId")
    var marcaId: Int? = null

    @Column(name = "CampaniaId")
    @SerializedName("CampaniaId")
    var campaniaID: Int? = null

    @Column(name = "MarcaDescripcion")
    @SerializedName("MarcaDescripcion")
    var marcaDescripcion: String? = null

    @Column(name = "UrlImagen")
    @SerializedName("UrlImagen")
    var urlImagen: String? = null

    @Column(name = "UrlCatalogo")
    @SerializedName("UrlCatalogo")
    var urlCatalogo: String? = null

    @Column(name = "Titulo")
    @SerializedName("Titulo")
    var titulo: String? = null

    @Column(name = "Descripcion")
    @SerializedName("Descripcion")
    var descripcion: String? = null

    @Column(name = "UrlDescargaEstado")
    @SerializedName("UrlDescargaEstado")
    var urlDescargaEstado : Int? = null

}
