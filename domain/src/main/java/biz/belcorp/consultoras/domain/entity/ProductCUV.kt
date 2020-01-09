package biz.belcorp.consultoras.domain.entity

import java.io.Serializable

class ProductCUV : Serializable {

    var id: Int? = null
    var cuv: String? = null
    var sap: String? = null
    var description: String? = null
    var descripcionCategoria: String? = null
    var marcaId: Int? = null
    var descripcionMarca: String? = null
    var precioValorizado: Double? = null
    var precioCatalogo: Double? = null
    var fotoProducto: String? = null
    var fotoProductoSmall: String? = null
    var fotoProductoMedium: String? = null
    var cuvRevista: String? = null
    var cuvComplemento: String? = null
    var estrategiaId: Int? = null
    var tipoEstrategiaId: String? = null
    var tipoOfertaSisId: Int? = null
    var configuracionOfertaId: Int? = null
    var flagNueva: Int? = null
    var indicadorMontoMinimo: Int? = null
    var clienteId: Int? = null
    var clienteLocalId: Int? = null
    var cantidad: Int? = null
    var identifier: String? = null
    var isSugerido: Boolean = false
    var tipoPersonalizacion: String? = null //tipo de palanca
    var codigoEstrategia: Int? = null
    var codigoTipoEstrategia: String? = null
    var limiteVenta: Int? = null
    var stock: Boolean? = null
    var descripcionEstrategia: String? = null
    var origenPedidoWeb: String? = null
    var agregado: Boolean? = false
    var origenPedidoWebFicha: String? = null
    var permiteAgregarPedido: Boolean? = false
    var tieneOfertasRelacionadas: Boolean? = false
    var codigoProducto: String? = null
    var isAdded: Boolean? = false                         // Flag para validar que fue añadido desde las ofertas relacionadas
    var listaOpciones: List<ProductCUVOpcion>? = null
    var isMaterialGanancia: Boolean? = null
    var origenesPedidoWeb: List<Origen?>? = null
    var index: Int = 0  // Posicion en el carrusel
    var flagFestival: Boolean? = null
    var reemplazarFestival: Boolean? = null
    var pedido: FormattedOrder? = null
    var codigoTipoOferta: String? = null
    var flagPromocion: Boolean? = null
}

class ProductCUVOpcion : Serializable {
    var cuv: String? = null
    var cantidad: Int? = null
    var marcaId: Int? = null
    var precioUnitario: Double? = null
    var grupo: Int? = null
    var indicadorDigitable: Int? = null

    companion object {

        fun transformList(componentes: List<Componente?>?): List<ProductCUVOpcion>{
            return mutableListOf<ProductCUVOpcion>().apply {
                componentes?.filterNotNull()?.forEach { componente ->
                    componente.opciones?.filterNotNull()?.forEach { opcion ->
                        add(ProductCUVOpcion().apply {
                            cuv = opcion.cuv
                            cantidad = opcion.cantidad
                            marcaId = opcion.marcaID
                            precioUnitario = opcion.precioCatalogo
                            grupo = componente.grupo
                            indicadorDigitable = if(componente.indicadorDigitable == true) 1 else 0
                        })
                    }
                }
            }
        }

    }

}

class PromocionCUVCondiciones : Serializable {
    var cuvPromocion: String? = null
    var condiciones: List<String>? = null
}
