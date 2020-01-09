package biz.belcorp.consultoras.domain.entity.caminobrillante

class KitCaminoBrillante {

    var estrategiaId: Int? = null
    var codigoEstrategia: String? = null
    var codigoKit: String? = null
    var codigoSap: String? = null
    var cuv: String? = null
    var descripcionCUV: String? = null
    var descripcionCortaCUV: String? = null
    var marcaID: Int? = null
    var descripcionMarca: String? = null
    var codigoNivel: String? = null
    var descripcionNivel: String? = null
    var precioValorizado: Double? = null
    var precioCatalogo: Double? = null
    var ganancia: Double? = null
    var fotoProductoSmall: String? = null
    var fotoProductoMedium: String? = null
    var tipoEstrategiaID: String? = null
    var origenPedidoWebFicha: Int? = null
    var flagSeleccionado: Boolean? = null
    var flagDigitable: Int? = null
    var flagHabilitado: Boolean? = null

    fun getFotoProducto(): String? {
        return if(fotoProductoSmall.isNullOrEmpty() || fotoProductoSmall.isNullOrBlank()) fotoProductoMedium else fotoProductoSmall
    }

}
