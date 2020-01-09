package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

class EstrategiaCarruselEntity {

    @SerializedName("EstrategiaID")
    var estrategiaID: Int? = null

    @SerializedName("CodigoEstrategia")
    var codigoEstrategia: String? = null

    @SerializedName("CUV")
    var cuv: String? = null

    @SerializedName("DescripcionCUV")
    var descripcionCUV: String? = null

    @SerializedName("DescripcionCortaCUV")
    var descripcionCortaCUV: String? = null

    @SerializedName("MarcaID")
    var marcaID: Int? = null

    @SerializedName("DescripcionMarca")
    var descripcionMarca: String? = null

    @SerializedName("PrecioValorizado")
    var precioValorizado: BigDecimal? = null

    @SerializedName("PrecioFinal")
    var precioFinal: BigDecimal? = null

    @SerializedName("Ganancia")
    var ganancia: BigDecimal? = null

    @SerializedName("FotoProductoSmall")
    var fotoProductoSmall: String? = null

    @SerializedName("FotoProductoMedium")
    var fotoProductoMedium: String? = null

    @SerializedName("FlagNueva")
    var flagNueva: Int? = null

    @SerializedName("TipoEstrategiaID")
    var tipoEstrategiaID: String? = null

    @SerializedName("IndicadorMontoMinimo")
    var indicadorMontoMinimo: Int? = null

    @SerializedName("OrigenPedidoWebFicha")
    var origenPedidoWebFicha: Int? = null

    @SerializedName("OrigenPedidoWeb")
    var origenPedidoWeb: Int? = null

    @SerializedName("ProductoDetalle")
    var productoDetalle: List<ProductoDetalleEntity>? = null

    @SerializedName("TieneStock")
    var tieneStock: Boolean = true

    @SerializedName("FlagSeleccionado")
    var flagSeleccionado: Int? = null

    @SerializedName("FlagPremioDefault")
    var flagPremioDefault: Boolean = false

    @SerializedName("FlagFestival")
    var flagFestival: Boolean = false

    @SerializedName("TipoPersonalizacion")
    var tipoPersonalizacion: String? = null
}
