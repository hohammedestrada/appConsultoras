package biz.belcorp.consultoras.domain.entity.caminobrillante

class DemostradorCaminoBrillante {

    var estrategiaID: Int? = null
    var codigoEstrategia: String? = null
    var cuv: String? = null
    var descripcionCUV: String? = null
    var descripcionCortaCUV: String? = null
    var marcaID: Int? = null
    var descripcionMarca: String? = null
    var precioValorizado: Double? = null
    var precioCatalogo: Double? = null
    var precioFinal: Double? = null
    var ganancia: Double? = null
    var fotoProductoSmall: String? = null
    var fotoProductoMedium: String? = null
    var tipoEstrategiaID: Int? = null
    var origenPedidoWebFicha: Int? = null

    fun getFotoProducto(): String? {
        return if(fotoProductoSmall.isNullOrEmpty() || fotoProductoSmall.isNullOrBlank()) fotoProductoMedium else fotoProductoSmall
    }

}
