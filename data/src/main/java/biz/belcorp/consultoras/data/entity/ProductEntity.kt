package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel

import java.io.Serializable

import biz.belcorp.consultoras.data.db.ConsultorasDatabase

@Table(database = ConsultorasDatabase::class, name = "Producto")
class ProductEntity : BaseModel(), Serializable {

    @Column(name = "ID")
    @PrimaryKey(autoincrement = true)
    @SerializedName("ID")
    @Expose
    var id: Int? = null

    @Column(name = "CUV")
    @SerializedName("CUV")
    var cuv: String? = null

    @Column(name = "Descripcion")
    @SerializedName("Descripcion")
    var description: String? = null

    @Column(name = "Catalogo")
    @SerializedName("Catalogo")
    var catalogo: String? = null

    @Column(name = "Categoria")
    @SerializedName("Categoria")
    var categoria: String? = null

    @Column(name = "NumeroPagina")
    @SerializedName("NumeroPagina")
    var numeroPagina: Int? = null
}
