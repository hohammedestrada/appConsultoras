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

@Table(database = ConsultorasDatabase::class, name = "NivelProgramaNueva")
class NivelProgramaNuevaEntity {

    @Column
    @PrimaryKey(autoincrement = true)
    @SerializedName("Id")
    @Expose(serialize = false, deserialize = false)
    var id: Int? = null

    @Column
    @SerializedName("concursoLocalId")
    @Expose(serialize = false, deserialize = false)
    var concursoLocalId: Int? = null

    @Column(name = "CodigoConcurso")
    @SerializedName("CodigoConcurso")
    var codigoConcurso: String? = null

    @Column(name = "CodigoNivel")
    @SerializedName("CodigoNivel")
    var codigoNivel: String? = null

    @Column(name = "MontoExigidoPremio")
    @SerializedName("MontoExigidoPremio")
    var montoExigidoPremio: BigDecimal? = null

    @Column(name = "MontoExigidoCupon")
    @SerializedName("MontoExigidoCupon")
    var montoExigidoCupon: BigDecimal? = null

    @SerializedName("PremiosProgramaNuevas")
    var premiosNuevas: List<PremioNuevasEntity?>? = null

    @SerializedName("CuponesProgramaNuevas")
    var cupones: List<CuponEntity?>? = null

    val premiosNuevasDB: List<PremioNuevasEntity>
        @OneToMany(methods = [(OneToMany.Method.ALL)], variableName = "premiosNuevas",  isVariablePrivate = true)
        get() {
            if (premiosNuevas == null || premiosNuevas!!.isEmpty()) {
                premiosNuevas = SQLite.select()
                        .from(PremioNuevasEntity::class.java)
                        .where(PremioNuevasEntity_Table.nivelProgramaLocalId.eq(id))
                        .queryList()
            }
            return premiosNuevas as List<PremioNuevasEntity>
        }

    val cuponesDB: List<CuponEntity>
        @OneToMany(methods = [(OneToMany.Method.ALL)], variableName = "cupones", isVariablePrivate = true)
        get() {
            if (cupones == null || cupones!!.isEmpty()) {
                cupones = SQLite.select()
                        .from(CuponEntity::class.java)
                        .where(CuponEntity_Table.nivelProgramaLocalId.eq(id))
                        .queryList()
            }
            return cupones as List<CuponEntity>
        }
}
