package biz.belcorp.consultoras.domain.entity

data class FestivalResponse(
    var listAwards: List<FestivalAward?>?,
    var listConditions: List<FestivalProduct?>?,
    var filters: List<FestivalFilter?>?
)

data class FestivalAward(
    var totalAmountLevel: Double?,
    var remainingAmount: Double?,
    var progressLevel: Int?,
    var flagPremioAgregado: Boolean?,
    var product: FestivalProduct?
)

data class FestivalProduct(
    var cuv: String?,
    var description: String?,
    var brandId: Int?,
    var brandName: String?,
    var catalogPrice: Double?,
    var valuedPrice: Double?,
    var profit: Double?,
    var imageUrl: String?,
    var strategyTypeId: String?,
    var strategyCode: String?,
    var strategyId: Int?,
    var minAmountIndicator: Int?,
    var limitSale: Int?,
    var strategyTypeImageShow: Int?,
    var flagChoiceOption: Boolean?,
    var flagNew: Boolean?,
    var isSubCampaign: Boolean?,
    var flagIndividual: Boolean?,
    var productCode: String?,
    var isSoldOut: Boolean?,
    var offerType: String?
) {
    companion object {
        fun transformFestivalProductToProductCUV(festival: FestivalProduct): ProductCUV {
            return ProductCUV().apply {
                id = null
                cuv = festival.cuv
                sap = null
                description = festival.description
                descripcionCategoria = null
                marcaId = festival.brandId
                descripcionMarca = festival.brandName
                precioValorizado = festival.valuedPrice
                precioCatalogo = festival.catalogPrice
                fotoProducto = null
                fotoProductoSmall = festival.imageUrl
                fotoProductoMedium = null
                cuvRevista = null
                cuvComplemento = null
                estrategiaId = null
                tipoEstrategiaId = festival.strategyTypeId
                tipoOfertaSisId = null
                configuracionOfertaId = null
                flagNueva = null
                indicadorMontoMinimo = null
                clienteId = null
                clienteLocalId = null
                cantidad = null
                identifier = null
                isSugerido = false
                tipoPersonalizacion = festival.offerType
                codigoEstrategia = festival.strategyCode?.toInt()
                codigoTipoEstrategia = festival.strategyTypeId
                limiteVenta = null
                stock = !(festival.isSoldOut ?: false)
                descripcionEstrategia = festival.brandName
                origenPedidoWeb = null
                agregado = false
                origenPedidoWebFicha = null
                permiteAgregarPedido = false
                tieneOfertasRelacionadas = false
                codigoProducto = null
                isAdded = false
                listaOpciones = null
                isMaterialGanancia = null
                origenesPedidoWeb = null
                index = 0
                flagFestival = true
                reemplazarFestival = null
            }
        }
    }
}

data class FestivalFilter(
    var groupName: String? = null,
    var options: List<FestivalFilterChild?>? = null)

data class FestivalFilterChild(
    var filterId: String? = null,
    var filterName: String? = null,
    var quantity: Int? = null,
    var checked: Boolean? = false)


