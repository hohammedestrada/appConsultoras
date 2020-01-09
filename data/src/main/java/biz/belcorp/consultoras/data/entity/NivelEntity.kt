package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.OneToMany
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.sql.language.SQLite

import java.math.BigDecimal

import biz.belcorp.consultoras.data.db.ConsultorasDatabase

@Table(database = ConsultorasDatabase::class, name = "ConcursoNivel")
class NivelEntity {

    @Column
    @PrimaryKey(autoincrement = true)
    @SerializedName("Id")
    @Expose(serialize = false, deserialize = false)
    var id: Int? = null

    @Column
    @SerializedName("ConcursoLocalId")
    @Expose(serialize = false, deserialize = false)
    var concursoLocalId: Int? = null

    @Column(name = "CodigoConcurso")
    @SerializedName("CodigoConcurso")
    var codigoConcurso: String? = null

    @Column(name = "CodigoNivel")
    @SerializedName("CodigoNivel")
    var codigoNivel: Int? = null

    @Column(name = "PuntosNivel")
    @SerializedName("PuntosNivel")
    var puntosNivel: Int? = null

    @Column(name = "PuntosFaltantes")
    @SerializedName("PuntosFaltantes")
    var puntosFaltantes: Int? = null

    @Column(name = "IndicadorPremiacionPedido")
    @SerializedName("IndicadorPremiacionPedido")
    var isIndicadorPremiacionPedido: Boolean? = null

    @Column(name = "MontoPremiacionPedido")
    @SerializedName("MontoPremiacionPedido")
    var montoPremiacionPedido: BigDecimal? = null

    @Column(name = "IndicadorBelCenter")
    @SerializedName("IndicadorBelCenter")
    var isIndicadorBelCenter: Boolean? = null

    @Column(name = "FechaVentaRetail")
    @SerializedName("FechaVentaRetail")
    var fechaVentaRetail: String? = null

    @Column(name = "IndicadorNivelElectivo")
    @SerializedName("IndicadorNivelElectivo")
    var isIndicadorNivelElectivo: Boolean? = null

    @Column(name = "PuntosExigidos")
    @SerializedName("PuntosExigidos")
    var puntosExigidos: Int? = null

    @Column(name = "PuntosExigidosFaltantes")
    @SerializedName("PuntosExigidosFaltantes")
    var puntosExigidosFaltantes: Int? = null

    @SerializedName("Premios")
    var premios: List<PremioEntity?>? = null

    val premiosNuevasDB: List<PremioEntity>
        @OneToMany(methods = [(OneToMany.Method.ALL)], variableName = "premios",  isVariablePrivate = true)
        get() {
            if (premios == null || premios!!.isEmpty()) {
                premios = SQLite.select()
                        .from(PremioEntity::class.java)
                        .where(PremioEntity_Table.nivelLocalId.eq(id))
                        .queryList()
            }
            return premios as List<PremioEntity>
        }

}
