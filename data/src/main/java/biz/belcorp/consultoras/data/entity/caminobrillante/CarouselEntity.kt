package biz.belcorp.consultoras.data.entity.caminobrillante

import com.google.gson.annotations.SerializedName

class CarouselEntity {

    @SerializedName("VerMas")
    var verMas: Boolean? = null

    @SerializedName("Items")
    var items: List<OfertaCarouselEntity>? = null

    class OfertaCarouselEntity {

        @SerializedName("TipoOferta")
        var tipoOferta: Int? = null

        @SerializedName("EstrategiaID")
        var estrategiaID: Int? = null


        @SerializedName("CodigoEstrategia")
        var codigoEstrategia: String? = null

        @SerializedName("TipoEstrategiaID")
        var tipoEstrategiaID: Int? = null

        @SerializedName("CUV")
        var CUV: String? = null

        @SerializedName("DescripcionCUV")
        var descripcionCUV: String? = null

        @SerializedName("DescripcionCortaCUV")
        var descripcionCortaCUV: String? = null

        @SerializedName("MarcaID")
        var marcaID: Int? = null

        @SerializedName("CodigoMarca")
        var codigoMarca: String? = null

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

        @SerializedName("FlagSeleccionado")
        var flagSeleccionado: Boolean? = null

        @SerializedName("FlagDigitable")
        var flagDigitable: Int? = null

        @SerializedName("FlagHabilitado")
        var flagHabilitado: Boolean? = null

        @SerializedName("FlagHistorico")
        var flagHistorico: Boolean? = null

        @SerializedName("EsCatalogo")
        var isCatalogo: Int? = null

    }

}
