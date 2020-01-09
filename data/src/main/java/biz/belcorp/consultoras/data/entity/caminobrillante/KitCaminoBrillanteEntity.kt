package biz.belcorp.consultoras.data.entity.caminobrillante

import com.google.gson.annotations.SerializedName

class KitCaminoBrillanteEntity {
    
    @SerializedName("EstrategiaID")
    var estrategiaId: Int? = null

    @SerializedName("CodigoEstrategia")
    var codigoEstrategia: String? = null

    @SerializedName("CodigoKit")
    var codigoKit: String? = null

    @SerializedName("CodigoSap")
    var codigoSap: String? = null

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

    @SerializedName("CodigoNivel")
    var codigoNivel: String? = null

    @SerializedName("DescripcionNivel")
    var descripcionNivel: String? = null

    @SerializedName("PrecioValorizado")
    var precioValorizado: Double? = null

    @SerializedName("PrecioCatalogo")
    var precioCatalogo: Double? = null

    @SerializedName("Ganancia")
    var ganancia: Double? = null

    @SerializedName("FotoProductoSmall")
    var fotoProductoSmall: String? = null

    @SerializedName("FotoProductoMedium")
    var fotoProductoMedium: String? = null

    @SerializedName("TipoEstrategiaID")
    var tipoEstrategiaID: String? = null

    @SerializedName("OrigenPedidoWebFicha")
    var origenPedidoWebFicha: Int? = null

    @SerializedName("FlagSeleccionado")
    var flagSeleccionado: Boolean? = null

    @SerializedName("FlagDigitable")
    var flagDigitable: Int? = null

    @SerializedName("FlagHabilitado")
    var flagHabilitado: Boolean? = null

}
