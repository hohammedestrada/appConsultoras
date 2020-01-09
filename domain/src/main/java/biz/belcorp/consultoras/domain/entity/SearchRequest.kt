package biz.belcorp.consultoras.domain.entity

class SearchRequest {
    var campaniaId: Int? = null
    var codigoZona: String? = ""
    var textoBusqueda: String? = ""
    var personalizacionesDummy: String = ""
    var fechaInicioFacturacion: String = ""
    var configuracion: SearchConfiguracion? = null
    var paginacion: SearchPaginacion? = null
    var orden: SearchOrden? = null
    var filtros: List<SearchFilter?>? = mutableListOf<SearchFilter>()
}

class SearchConfiguracion {
    var rdEsSuscrita: Boolean? = null
    var rdEsActiva: Boolean? = null
    var lider: Int? = null
    var rdActivoMdo: Boolean? = null
    var rdTieneRDC: Boolean? = null
    var rdTieneRDI: Boolean? = null
    var rdTieneRDCR: Boolean? = null
    var diaFacturacion: Int? = null
    var agrupaPromociones: Boolean? = false
}

class SearchPaginacion {
    var numeroPagina: Int? = null
    var cantidad: Int? = null
}

class SearchOrden {
    var campo: String? = null
    var tipo: String? = null
}

data class SearchFilter(var nombreGrupo: String? = null, var opciones: List<SearchFilterChild?>? = null)

data class SearchFilterChild(var idFiltro: String? = null,
                             var nombreFiltro: String? = null,
                             var cantidad: Int? = null,
                             var marcado: Boolean? = false,
                             var min: Int? = 0,
                             var max: Int? = 0,
                             var idSeccion: String? = null)


data class Promotion(var detalle: Collection<ProductCUV?>? = null, var posicion: Int? = null, var detalleCondition: ArrayList<OfferPromotionDual>? = null)

