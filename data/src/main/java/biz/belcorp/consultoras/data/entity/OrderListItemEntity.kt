package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.data.db.ConsultorasDatabase
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import java.math.BigDecimal

/**
 *
 */
@Table(database = ConsultorasDatabase::class, name = "Order")
class OrderListItemEntity {

    @Column
    @PrimaryKey(autoincrement = true)
    @SerializedName("PedidoDetalleID")
    @Expose(serialize = false, deserialize = false)
    var id: Int? = null

    @Column(name = "CUV")
    @SerializedName("CUV")
    var cuv: String? = null

    @Column(name = "DescripcionProd")
    @SerializedName("DescripcionProd")
    var descripcionProd: String? = null

    @Column(name = "DescripcionCortaProd")
    @SerializedName("DescripcionCortaProd")
    var descripcionCortaProd: String? = null

    @Column(name = "Cantidad")
    @SerializedName("Cantidad")
    var cantidad: Int? = null

    @Column(name = "PrecioUnidad")
    @SerializedName("PrecioUnidad")
    var precioUnidad: BigDecimal? = null

    @Column(name = "ImporteTotal")
    @SerializedName("ImporteTotal")
    var importeTotal: BigDecimal? = null

    @Column(name = "ClienteID")
    @SerializedName("ClienteID")
    var clienteID: Int? = null

    @Column(name = "ClienteLocalID")
    @SerializedName("ClienteLocalID")
    @Expose(serialize = false, deserialize = false)
    var clienteLocalID: Int? = null

    @Column(name = "NombreCliente")
    @SerializedName("NombreCliente")
    var nombreCliente: String? = null

    @Column(name = "EsKitNueva")
    @SerializedName("EsKitNueva")
    var isEsKitNueva: Boolean? = null

    @Column(name = "Subido")
    @Expose(serialize = false, deserialize = false)
    var isSubido: Boolean? = null

    @Column(name = "UserCode")
    @Expose(serialize = false, deserialize = false)
    var userCode: String? = null

    @Column(name = "TipoEstrategiaID")
    @SerializedName("TipoEstrategiaID")
    var tipoEstrategiaID: String? = null

    @Column(name = "TipoOfertaSisID")
    @SerializedName("TipoOfertaSisID")
    @Expose(serialize = false, deserialize = false)
    var tipoOfertaSisID: Int? = null

    @Column(name = "ObservacionPROL")
    @SerializedName("ObservacionPROL")
    var observacionPROL: String? = null

    @SerializedName("ObservacionPromociones")
    var observacionPromociones: List<String?>? = null

    @Column(name = "EtiquetaProducto")
    @SerializedName("EtiquetaProducto")
    var etiquetaProducto: String? = null

    @Column(name = "IndicadorOfertaCUV")
    @SerializedName("IndicadorOfertaCUV")
    var indicadorOfertaCUV: String? = null

    @Column(name = "SetID")
    @SerializedName("SetID")
    @Expose(serialize = false, deserialize = false)
    var conjuntoID: Int? = null

    @Column(name = "EsBackOrder")
    @SerializedName("EsBackOrder")
    @Expose(serialize = false, deserialize = false)
    var isEsBackOrder: Boolean? = null

    @Column(name = "AceptoBackOrder")
    @SerializedName("AceptoBackOrder")
    @Expose(serialize = false, deserialize = false)
    var isAceptoBackOrder: Boolean? = null

    @Column(name = "FlagNueva")
    @SerializedName("FlagNueva")
    @Expose(serialize = false, deserialize = false)
    var isFlagNueva: Boolean? = null

    @Column(name = "EnRangoProgNuevas")
    @SerializedName("EnRangoProgNuevas")
    @Expose(serialize = false, deserialize = false)
    var isEnRangoProgNuevas: Boolean? = null

    @Column(name = "EsDuoPerfecto")
    @SerializedName("EsDuoPerfecto")
    @Expose(serialize = false, deserialize = false)
    var isEsDuoPerfecto: Boolean? = null

    @Column(name = "EsPremioElectivo")
    @SerializedName("EsPremioElectivo")
    var isEsPremioElectivo: Boolean? = null

    @Column(name = "EsArmaTuPack")
    @SerializedName("EsArmaTuPack")
    var isArmaTuPack: Boolean? = null

    @SerializedName("Componentes")
    var componentes: List<PedidoComponentesEntity?>? = null

    fun getSetID(): Int? {
        return conjuntoID
    }

    fun setSetID(setID: Int?) {
        this.conjuntoID = setID
    }

    @Column(name = "TipoOferta")
    @SerializedName("TipoOferta")
    @Expose(serialize = false, deserialize = false)
    var tipoOferta: String? = null

    /**CAMINO BRILLANTE**/

    @Column(name="EsKitCaminoBrillante")
    @SerializedName("EsKitCaminoBrillante")
    @Expose(serialize = false, deserialize = false)
    var isKitCaminoBrillante: Boolean? = null

    @Column(name="EliminableSE")
    @SerializedName("EliminableSE")
    var isDeleteKit: Boolean? = null

    @Column(name="EsFestival")
    @SerializedName("EsFestival")
    @Expose(serialize = false, deserialize = false)
    var isFestival: Boolean? = null

    @Column(name="FlagFestival")
    @SerializedName("FlagFestival")
    @Expose(serialize = false, deserialize = false)
    var flagFestival: Int? = null

    @Column(name="EsPromocion")
    @SerializedName("EsPromocion")
    @Expose(serialize = false, deserialize = false)
    var isPromocion: Boolean? = null

}


class PedidoComponentesEntity{

    @SerializedName("SetDetalleId")
    var setDetalleId: Int? = null

    @SerializedName("SetId")
    var setId: Int? = null

    @SerializedName("CUV")
    var cuv: String? = null

    @SerializedName("NombreProducto")
    var nombreProducto: String? = null

    @SerializedName("Cantidad")
    var cantidad: Int? = null

    @SerializedName("FactorRepetecion")
    var factorRepetecion: Int? = null

    @SerializedName("PrecioUnidad")
    var precioUnidad: BigDecimal? = null

}
