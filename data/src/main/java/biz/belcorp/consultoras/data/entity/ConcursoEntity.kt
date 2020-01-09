package biz.belcorp.consultoras.data.entity

import java.io.Serializable
import java.math.BigDecimal

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.OneToMany
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.sql.language.SQLite

import biz.belcorp.consultoras.data.db.ConsultorasDatabase

@Table(database = ConsultorasDatabase::class, name = "Concurso")
class ConcursoEntity : Serializable {

    @Column
    @PrimaryKey(autoincrement = true)
    @SerializedName("ID")
    @Expose(serialize = false, deserialize = false)
    var id: Int? = null

    @Column(name = "CampaniaID")
    @SerializedName("CampaniaID")
    var campaniaId: String? = null

    @Column(name = "CampaniaIDInicio")
    @SerializedName("CampaniaIDInicio")
    var campaniaIDInicio: Int? = null

    @Column(name = "CampaniaIDFin")
    @SerializedName("CampaniaIDFin")
    var campaniaIDFin: Int? = null

    @Column(name = "CodigoConcurso")
    @SerializedName("CodigoConcurso")
    var codigoConcurso: String? = null

    @Column(name = "TipoConcurso")
    @SerializedName("TipoConcurso")
    var tipoConcurso: String? = null

    @Column(name = "PuntosAcumulados")
    @SerializedName("PuntosAcumulados")
    var puntosAcumulados: Int? = null

    @Column(name = "IndicadorPremioAcumulativo")
    @SerializedName("IndicadorPremioAcumulativo")
    var isIndicadorPremioAcumulativo: Boolean? = null

    @Column(name = "NivelAlcanzado")
    @SerializedName("NivelAlcanzado")
    var nivelAlcanzado: Int? = null

    @Column(name = "NivelSiguiente")
    @SerializedName("NivelSiguiente")
    var nivelSiguiente: Int? = null

    @Column(name = "CampaniaIDPremiacion")
    @SerializedName("CampaniaIDPremiacion")
    var campaniaIDPremiacion: String? = null

    @Column(name = "PuntajeExigido")
    @SerializedName("PuntajeExigido")
    var puntajeExigido: Int? = null

    @Column(name = "DescripcionConcurso")
    @SerializedName("DescripcionConcurso")
    var descripcionConcurso: String? = null

    @Column(name = "EstadoConcurso")
    @SerializedName("EstadoConcurso")
    var estadoConcurso: String? = null

    @Column(name = "UrlBannerPremiosProgramaNuevas")
    @SerializedName("UrlBannerPremiosProgramaNuevas")
    var urlBannerPremiosProgramaNuevas: String? = null

    @Column(name = "UrlBannerCuponesProgramaNuevas")
    @SerializedName("UrlBannerCuponesProgramaNuevas")
    var urlBannerCuponesProgramaNuevas: String? = null

    @Column(name = "CodigoNivelProgramaNuevas")
    @SerializedName("CodigoNivelProgramaNuevas")
    var codigoNivelProgramaNuevas: String? = null

    @Column(name = "ImportePedido")
    @SerializedName("ImportePedido")
    var importePedido: BigDecimal? = null

    @Column(name = "TextoCupon")
    @SerializedName("TextoCupon")
    var textoCupon: String? = null

    @Column(name = "TextoCuponIndependiente")
    @SerializedName("TextoCuponIndependiente")
    var textoCuponIndependiente: String? = null

    var error: Throwable? = null

    @SerializedName("Niveles")
    var niveles: List<NivelEntity?>? = null

    @SerializedName("NivelesProgramaNuevas")
    var nivelesProgramaNuevas: List<NivelProgramaNuevaEntity?>? = null

    val nivelesDB: List<NivelEntity>
        @OneToMany(methods = [(OneToMany.Method.ALL)], variableName = "niveles", isVariablePrivate = true)
        get() {
            if (niveles == null || niveles!!.isEmpty()) {
                niveles = SQLite.select()
                        .from(NivelEntity::class.java)
                        .where(NivelEntity_Table.concursoLocalId.eq(id))
                        .queryList()
            }
            return niveles as List<NivelEntity>
        }

    val nivelesProgramaNuevasDB: List<NivelProgramaNuevaEntity>
        @OneToMany(methods = [(OneToMany.Method.ALL)], variableName = "nivelesProgramaNuevas", isVariablePrivate = true)
        get() {
            if (nivelesProgramaNuevas == null || nivelesProgramaNuevas!!.isEmpty()) {
                nivelesProgramaNuevas = SQLite.select()
                        .from(NivelProgramaNuevaEntity::class.java)
                        .where(NivelProgramaNuevaEntity_Table.concursoLocalId.eq(id))
                        .queryList()
            }
            return nivelesProgramaNuevas as List<NivelProgramaNuevaEntity>
        }
}
