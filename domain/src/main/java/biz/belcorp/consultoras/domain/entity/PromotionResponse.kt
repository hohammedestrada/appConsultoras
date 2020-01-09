package biz.belcorp.consultoras.domain.entity

data class PromotionResponse (
    var observacion: String?,
    var producto: PromotionDetail?,
    var listaApoyo: List<PromotionDetail?>?
)

data class PromotionDetail (
    var estrategiaId: Int?,
    var codigoEstrategia: String?,
    var activo: Boolean?,
    var cuv: String?,
    var descripcion: String?,
    var imagenURL: String?,
    var glagImagenURL: Boolean?,
    var limiteVenta: Int?,
    var textoLibre: String?,
    var orden: Int?,
    var flagConfig: Boolean?,
    var tipoEstrategiaId: String?,
    var tipoPersonalizacion: String?,
    var descripcionTipoEstrategia: String?,
    var codigoTipoEstrategia: String?,
    var flagActivo: Int?,
    var flagRecoPerfil: Int?,
    var marcaId: Int?,
    var marcaDescripcion: String?,
    var codigoProducto: String?,
    var indicadorMontoMinimo: Boolean?,
    var codigoTipoOferta: String?,
    var tipoEstrategiaImagenMostrar: Int?,
    var flagRevista: Int?,
    var precioTachado: Double?,
    var precioVenta: Double?,
    var gananciaString: Double?,
    var tieneStock: Boolean?,
    var descripcionCortada: String?,
    var descripcionCompleta: String?,
    var codigoCatalogo: String?,
    var factorCuadre: String?
)
