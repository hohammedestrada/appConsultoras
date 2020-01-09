package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.data.db.ConsultorasDatabase
import biz.belcorp.consultoras.domain.entity.OfertaFinalEstado
import biz.belcorp.consultoras.domain.entity.SearchRecentOffer
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel
import java.io.Serializable

/**
 * Entidad Origen Pedido Web
 * que crea una tabla en la base de datos o
 * recibe un JSON por parte de un servicio
 *
 * @version 1.0
 * @since 2019-05-21
 */

@Table(database = ConsultorasDatabase::class, name = "OfertaFinalEstado")
class OfertaFinalEstadoEntity : BaseModel(), Serializable {

    @PrimaryKey(autoincrement = true)
    @Column
    var uid: Long = 0L

    @Column(name = "codigoConsultora")
    @SerializedName("codigoConsultora")
    var codigoConsultora: String? = null

    @Column(name = "countryIso")
    @SerializedName("countryIso")
    var countryIso: String? = null

    @Column(name = "montoInicial")
    @SerializedName("montoInicial")
    var montoInicial: Double? = null


    @Column(name = "estadoPremio")
    @SerializedName("estadoPremio")
    var estadoPremio: Int? = null


    companion object {

        fun transformList(list: List<OfertaFinalEstadoEntity?>?): List<OfertaFinalEstado?>? {
            return mutableListOf<OfertaFinalEstado>().apply {
                list?.forEach {
                    it?.let { it1 -> transform(it1) }?.let { it2 -> add(it2) }
                }
            }.toList()
        }


        fun transform(input: OfertaFinalEstadoEntity?): OfertaFinalEstado? {
            input?.run {
                return OfertaFinalEstado(
                    codigoConsultora,
                    countryIso,
                    montoInicial,
                    estadoPremio
                )
            }
            return null
        }


        fun transform(codConsultora: String, codeIso: String, monto: Double, estadoPremio : Int ): OfertaFinalEstadoEntity{
            return OfertaFinalEstadoEntity().apply {
                this.codigoConsultora = codConsultora
                this.countryIso = codeIso
                this.montoInicial = monto
                this.estadoPremio = estadoPremio
            }
        }
    }
}
