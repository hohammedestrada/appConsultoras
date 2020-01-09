package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.data.db.ConsultorasDatabase
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel
import java.io.Serializable

@Table(database = ConsultorasDatabase::class, name = ProductoMasivoEntity.NAME)
class ProductoMasivoEntity : BaseModel(), Serializable {

    companion object {
        const val ENDPOINT = "Order"
        const val NAME = "ProductoCP"
    }

    @Column(name = "ID")
    @PrimaryKey(autoincrement = true)
    @SerializedName("ID")
    @Expose(serialize = false, deserialize = false)
    var id: Int? = null

    @Column(name = "CUV")
    @SerializedName("CUV")
    var cuv: String? = null

    @Column(name = "Descripcion")
    @Expose(serialize = false, deserialize = false)
    var descripcion: String? = null

    @Column(name = "Cantidad")
    @SerializedName("Cantidad")
    var cantidad: Int? = null

    @Column(name = "MarcaID")
    @SerializedName("MarcaID")
    var marcaId: Int? = null

    @Column(name = "MarcaDescripcion")
    @Expose(serialize = false, deserialize = false)
    var marcaDescripcion: String? = null

    @Column(name = "ClienteID")
    @SerializedName("ClienteID")
    var clienteId: Int? = null

    @Column(name = "ClienteDescripcion")
    @SerializedName("ClienteDescripcion")
    var clienteDescripcion: String? = null

    @Column(name = "PrecioCatalogo")
    @SerializedName("PrecioCatalogo")
    var precioCatalogo: Double? = null

    @Column(name = "Tono")
    @Expose(serialize = false, deserialize = false)
    var tono: String? = null

    @Column(name = "UrlImagen")
    @Expose(serialize = false, deserialize = false)
    var urlImagen: String? = null

    @Column(name = "CodigoRespuesta")
    @Expose(serialize = false, deserialize = false)
    var codigoRespuesta : String? = null

    @Column(name = "MensajeRespuesta")
    @Expose(serialize = false, deserialize = false)
    var mensajeRespuesta : String? = null

    @Column(name = "IdOrder")
    @Expose(serialize = false, deserialize = false)
    var idOrder: Long? = null

}
