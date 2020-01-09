package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.data.db.ConsultorasDatabase
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

@Table(database = ConsultorasDatabase::class, name = "SearchRecentOffer")
class SearchRecentOfferEntity : BaseModel(), Serializable {

    @PrimaryKey(autoincrement = true)
    @Column
    var uid: Long = 0L

    @Column(name = "codigoConsultora")
    @SerializedName("codigoConsultora")
    var codigoConsultora: String? = null

    @Column(name = "countryIso")
    @SerializedName("countryIso")
    var countryIso: String? = null

    @Column(name = "cuv")
    @SerializedName("cuv")
    var cuv: String? = null

    @Column(name = "nombreOferta")
    @SerializedName("nombreOferta")
    var nombreOferta: String? = null

    @Column(name = "precioCatalogo")
    @SerializedName("precioCatalogo")
    var precioCatalogo: Double? = null

    @Column(name = "precioValorizado")
    @SerializedName("precioValorizado")
    var precioValorizado: Double? = null

    @Column(name = "imagenURL")
    @SerializedName("imagenURL")
    var imagenURL: String? = null

    @Column(name = "tipoOferta")
    @SerializedName("tipoOferta")
    var tipoOferta: String? = null

    @Column(name = "flagFestival")
    @SerializedName("flagFestival")
    var flagFestival: Boolean? = null

    fun isFlagFestival() = flagFestival

    @Column(name = "flagPromocion")
    @SerializedName("flagPromocion")
    var flagPromocion: Boolean? = null

    fun isFlagPromocion() = flagPromocion

    @Column(name = "agotado")
    @SerializedName("agotado")
    var agotado: Boolean? = null

    fun isAgotado() = agotado

    @Column(name = "flagCatalogo")
    @SerializedName("flagCatalogo")
    var flagCatalogo: Boolean? = null

    fun isFlagCatalogo() = flagCatalogo

    /* TODO */

    companion object {

        fun transformList(list: List<SearchRecentOfferEntity?>?): List<SearchRecentOffer?>? {
            return mutableListOf<SearchRecentOffer>().apply {
                list?.forEach {
                    it?.let { it1 -> transform(it1) }?.let { it2 -> add(it2) }
                }
            }.toList()
        }


        fun transform(input: SearchRecentOfferEntity?): SearchRecentOffer? {
            input?.run {
                return SearchRecentOffer(
                    cuv,
                    nombreOferta,
                    precioCatalogo,
                    precioValorizado,
                    imagenURL,
                    tipoOferta ?: "",
                    flagFestival,
                    flagPromocion,
                    agotado,
                    flagCatalogo
                )
            }
            return null
        }

        fun transform(input: SearchRecentOffer, codConsultora: String, countryId: String): SearchRecentOfferEntity {
            input?.run {
                return SearchRecentOfferEntity().apply {
                    codigoConsultora = codConsultora
                    countryIso = countryId
                    cuv = input.cuv
                    nombreOferta = input.nombreOferta
                    precioCatalogo = input.precioCatalogo
                    precioValorizado = input.precioValorizado
                    imagenURL = input.imagenURL
                    tipoOferta = input.tipoOferta
                    flagFestival = input.flagFestival
                    flagPromocion = input.flagPromocion
                    agotado = input.agotado
                    flagCatalogo = input.flagCatalogo
                }
            }
        }
    }
}
