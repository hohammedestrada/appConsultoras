package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

class OfertaFinalEntity {

    @SerializedName("CUV")
    var cuv: String? = null

    @SerializedName("NombreComercial")
    var nombreComercial: String? = null

    @SerializedName("NombreComercialCorto")
    var nombreComercialCorto: String? = null

    @SerializedName("PrecioCatalogo")
    var precioCatalogo: BigDecimal? = null

    @SerializedName("PrecioValorizado")
    var precioValorizado: BigDecimal? = null

    @SerializedName("MarcaID")
    var marcaID: Int? = null

    @SerializedName("NombreMarca")
    var nombreMarca: String? = null

    @SerializedName("FotoProducto")
    var fotoProducto: String? = null

    @SerializedName("FotoProductoSmall")
    var fotoProductoSmall: String? = null

    @SerializedName("FotoProductoMedium")
    var fotoProductoMedium: String? = null

    @SerializedName("TipoMeta")
    var tipoMeta: String? = null

    @SerializedName("IndicadorMontoMinimo")
    var indicadorMontoMinimo: Int? = null

    @SerializedName("TipoEstrategiaID")
    var tipoEstrategiaID: Int? = null

    @SerializedName("TipoOfertaSisID")
    var tipoOfertaSisID: Int? = null

    @SerializedName("ConfiguracionOfertaID")
    var configuracionOfertaID: Int? = null

}
