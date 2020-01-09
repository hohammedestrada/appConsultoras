package biz.belcorp.consultoras.data.entity.caminobrillante

import biz.belcorp.consultoras.data.db.ConsultorasDatabase
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel
import java.io.Serializable

@Table(database = ConsultorasDatabase::class, name = "LogrosCaminoBrillante")
class LogroCaminoBrillanteEntity : BaseModel(), Serializable {

    @PrimaryKey
    @SerializedName("Id")
    @Column(name = "Id")
    var id: String? = null

    @Column(name = "Titulo")
    @SerializedName("Titulo")
    var titulo: String? = null

    @Column(name = "Descripcion")
    @SerializedName("Descripcion")
    var descripcion: String? = null

    @SerializedName("Indicadores")
    var indicadores: List<IndicadorEntity>? = null

    @Table(database = ConsultorasDatabase::class, name = "IndicadoresLogroCaminoBrillante")
    class IndicadorEntity : BaseModel(), Serializable  {

        @PrimaryKey(autoincrement = true)
        @Column(name = "Id")
        var id: Long? = null

        @SerializedName("Codigo")
        @Column(name = "Codigo")
        var codigo: String? = null

        @Column(name = "IdLogro")
        var idLogro: String? = null

        @Column(name = "Orden")
        @SerializedName("Orden")
        var orden: Int? = null

        @Column(name = "Titulo")
        @SerializedName("Titulo")
        var titulo: String? = null

        @Column(name = "Descripcion")
        @SerializedName("Descripcion")
        var descripcion: String? = null

        @SerializedName("Medallas")
        var medallas: List<MedallaEntity>? = null


        @Table(database = ConsultorasDatabase::class, name = "MedallasLogroCaminoBrillante")
        class MedallaEntity {

            @PrimaryKey(autoincrement = true)
            @Column(name = "Id")
            var id: Long? = null

            @Column(name = "IdIndicador")
            var idIndicador: Long? = null

            @Column(name = "Orden")
            @SerializedName("Orden")
            var orden: Int? = null

            @Column(name = "Tipo")
            @SerializedName("Tipo")
            var tipo: String? = null

            @Column(name = "Titulo")
            @SerializedName("Titulo")
            var titulo: String? = null

            @Column(name = "Subtitulo")
            @SerializedName("Subtitulo")
            var subtitulo: String? = null

            @Column(name = "Valor")
            @SerializedName("Valor")
            var valor: String? = null

            @Column(name = "isEstado")
            @SerializedName("Estado")
            var isEstado: Boolean = false

            @Column(name = "isDestacado")
            @SerializedName("Destacar")
            var isDestacado: Boolean = false

            @Column(name = "ModalTitulo")
            @SerializedName("ModalTitulo")
            var modalTitulo: String? = null

            @Column(name = "ModalDescripcion")
            @SerializedName("ModalDescripcion")
            var modalDescripcion: String? = null

        }

    }

}


