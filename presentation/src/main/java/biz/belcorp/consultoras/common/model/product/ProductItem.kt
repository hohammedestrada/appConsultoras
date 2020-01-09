package biz.belcorp.consultoras.common.model.product

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.math.BigDecimal

@Parcelize
data class ProductItem(var id: Int?,
                       var cuv: String?,
                       var descripcionProd: String?,
                       var descripcionCortaProd: String?,
                       var cantidad: Int?,
                       var precioUnidad: BigDecimal?,
                       var importeTotal: BigDecimal?,
                       var clienteID: Int?,
                       var clienteLocalID: Int?,
                       var nombreCliente: String?,
                       var subido: Boolean?,
                       var isEsKitNueva: Boolean?,
                       var tipoEstrategiaID: String?,
                       var tipoOfertaSisID: Int?,
                       var observacionPROL: String?,
                       var observacionPROLType: Int = 0,
                       var observacionPROLList: ArrayList<String>? = ArrayList(),
                       var etiquetaProducto: String?,
                       var indicadorOfertaCUV: String?,
                       var mensajeError: String?,
                       var conjuntoID: Int?,
                       var isEsBackOrder: Boolean?,
                       var isAceptoBackOrder: Boolean?,
                       var isFlagNueva: Boolean?,
                       var isEnRangoProgNuevas: Boolean?,
                       var isEsDuoPerfecto: Boolean?,
                       var isEsPremioElectivo: Boolean?,
                       var isArmaTuPack: Boolean?,
                       var tipoOferta: String?,
                       var components: List<ComponentItem>,
                       var isKitCaminoBrillante: Boolean?,
                       var isDeleteKit: Boolean?,
                       var isPromocion: Boolean?,
                       var observacionPromociones: ArrayList<String>? = ArrayList(),
                       var isFestival: Boolean?,
                       var flagFestival: Int?
) : Parcelable {
    var reemplazarFestival: Boolean = false
}


