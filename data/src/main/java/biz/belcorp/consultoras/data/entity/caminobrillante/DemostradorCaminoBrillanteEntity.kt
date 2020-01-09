package biz.belcorp.consultoras.data.entity.caminobrillante

import com.google.gson.annotations.SerializedName

class DemostradorCaminoBrillanteEntity {

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
    var precioValorizado: Double? = null

    @SerializedName("PrecioFinal")
    var precioFinal: Double? = null

    @SerializedName("PrecioCatalogo")
    var precioCatalogo: Double? = null

    @SerializedName("Ganancia")
    var ganancia: Double? = null

    @SerializedName("FotoProductoSmall")
    var fotoProductoSmall: String? = null

    @SerializedName("FotoProductoMedium")
    var fotoProductoMedium: String? = null

    @SerializedName("TipoEstrategiaID")
    var tipoEstrategiaID: Int? = null

    @SerializedName("OrigenPedidoWebFicha")
    var origenPedidoWebFicha: Int? = null

}
