package biz.belcorp.consultoras.data.entity.contenidoresumen

import biz.belcorp.consultoras.data.db.ConsultorasDatabase
import biz.belcorp.consultoras.data.util.Constant
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table

@Table(database = ConsultorasDatabase::class, name = "ContenidoResumenDetalle")
class ContenidoDetalleEntity {

    @Column(name ="Grupo")
    @SerializedName("Grupo")
    var grupo: String? = null

    @Column(name ="Type")
    @SerializedName("Type")
    var typeContenido: String = Constant.FORMATO_DEFAULT_STORIE

    @PrimaryKey
    @Column(name ="IdContenido")
    @SerializedName("IdContenido")
    var idContenido: String? = null

    @Column(name ="Codigo")
    @SerializedName("Codigo")
    var codigoDetalleResumen: String? = null

    @Column(name ="Url")
    @SerializedName("Url")
    var urlDetalleResumen: String? = null

    @Column(name ="Accion")
    @SerializedName("Accion")
    var accion: String? = null

    @Column(name ="Orden")
    @SerializedName("Orden")
    var ordenDetalleResumen: Int = 0

    @Column(name ="Visto")
    @SerializedName("Visto")
    var isVisto: Boolean = false

    @Column(name ="Descripcion")
    @SerializedName("Descripcion")
    var Descripcion: String? = null
}
