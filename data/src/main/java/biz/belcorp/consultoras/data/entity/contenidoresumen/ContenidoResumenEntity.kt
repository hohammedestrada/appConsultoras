package biz.belcorp.consultoras.data.entity.contenidoresumen

import biz.belcorp.consultoras.data.db.ConsultorasDatabase
import biz.belcorp.library.util.StringUtil
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.OneToMany
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.sql.language.SQLite

@Table(database = ConsultorasDatabase::class, name = "ContenidoResumen")
class ContenidoResumenEntity {
    @PrimaryKey
    @Column(name = "Codigo")
    @SerializedName("Codigo")
    var codigoResumen: String = StringUtil.Empty

    @Column(name = "UrlMiniatura")
    @SerializedName("UrlMiniatura")
    var urlMiniatura: String? = null

    @Column(name = "TotalContenido")
    @SerializedName("TotalContenido")
    var totalContenido: Int = 0

    @Column(name = "ContenidoVisto")
    @SerializedName("ContenidoVisto")
    var contenidoVisto: Int = 0

    @SerializedName("ContenidoDetalle")
    var contenidoDetalleEntity: List<ContenidoDetalleEntity>? = null
        @OneToMany(methods = [(OneToMany.Method.ALL)], isVariablePrivate = true)
        get() {

            field?.let {
                if (it.isEmpty()) {
                    this.contenidoDetalleEntity = SQLite.select()
                        .from(ContenidoDetalleEntity::class.java)
                        .queryList()
                }
            } ?: kotlin.run {
                this.contenidoDetalleEntity = SQLite.select()
                    .from(ContenidoDetalleEntity::class.java)
                    .queryList()
            }
            return field
        }
}

