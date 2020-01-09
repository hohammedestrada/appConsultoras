package biz.belcorp.consultoras.domain.entity.caminobrillante

class Carousel {

    var verMas: Boolean? = null

    var items: List<OfertaCarousel>? = null

    class OfertaCarousel {

        var tipoOferta: Int? = null

        var estrategiaID: Int? = null

        var codigoEstrategia: String? = null

        var tipoEstrategiaID: Int? = null

        var CUV: String? = null

        var descripcionCUV: String? = null

        var descripcionCortaCUV: String? = null

        var marcaID: Int? = null

        var codigoMarca: String? = null

        var descripcionMarca: String? = null

        var codigoNivel: String? = null

        var descripcionNivel: String? = null

        var precioValorizado: Double? = null

        var precioCatalogo: Double? = null

        var ganancia: Double? = null

        var fotoProductoSmall: String? = null

        var fotoProductoMedium: String? = null

        var flagSeleccionado: Boolean? = null

        var flagDigitable: Int? = null

        var flagHabilitado: Boolean? = null

        var flagHistorico: Boolean? = null

        var isCatalogo: Int? = null

        fun getFotoProducto(): String? {
            return if(fotoProductoSmall.isNullOrEmpty() || fotoProductoSmall.isNullOrBlank()) fotoProductoMedium else fotoProductoSmall
        }

    }

}
