package biz.belcorp.consultoras.domain.entity

import com.sun.org.apache.xpath.internal.operations.Bool
import java.math.BigDecimal

/**
 *
 */
class OrderListItem {
    var id: Int? = null
    var cuv: String? = null
    var descripcionProd: String? = null
    var descripcionCortaProd: String? = null
    var cantidad: Int? = null
    var precioUnidad: BigDecimal? = null
    var importeTotal: BigDecimal? = null
    var clienteID: Int? = null
    var clienteLocalID: Int? = null
    var nombreCliente: String? = null
    var subido: Boolean? = null
    var isEsKitNueva: Boolean? = false
    var tipoEstrategiaID: String? = null
    var tipoOfertaSisID: Int? = null
    var observacionPROL: String? = null
    var observacionPROLType: Int = 0
    var observacionPROLList: ArrayList<String>? = ArrayList()
    var observacionPromociones: ArrayList<String>? = ArrayList()
    var etiquetaProducto: String? = null
    var indicadorOfertaCUV: String? = null
    var mensajeError: String? = null
    var setID: Int? = null
    var isEsBackOrder: Boolean? = null
    var isAceptoBackOrder: Boolean? = null
    var isFlagNueva: Boolean? = null
    var isEnRangoProgNuevas: Boolean? = null
    var isEsDuoPerfecto: Boolean? = null
    var isEsPremioElectivo: Boolean? = null
    var isArmaTuPack: Boolean? = null
    var tipoOferta: String? = null
    var componentes: List<PedidoComponentes>? = null
    var isDeleteKit: Boolean? = null
    /**CAMINO BRILLANTE**/
    var isKitCaminoBrillante: Boolean? = null
    var isFestival: Boolean? = null
    var flagFestival: Int? = null
    var reemplazarFestival: Boolean = false
    var isPromocion: Boolean? = null
}

class PedidoComponentes{
    var setDetalleId: Int? = null
    var setId: Int? = null
    var cuv: String? = null
    var nombreProducto: String? = null
    var cantidad: Int? = null
    var factorRepetecion: Int? = null
    var precioUnidad: BigDecimal? = null
}
