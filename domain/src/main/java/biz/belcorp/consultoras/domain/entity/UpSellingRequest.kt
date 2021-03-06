package biz.belcorp.consultoras.domain.entity

class UpSellingRequest {
    var campaniaId: Int? = null
    var codigoProducto: List<String?> = mutableListOf()
    var precioCatalogo: Double? = null
    var codigoZona: String? = ""
    var personalizacionesDummy: String = ""
    var cuv: String = ""
    var fechaInicioFacturacion: String = ""
    var configuracion: UpSellingConfiguracion? = null
}

class UpSellingConfiguracion {
    var lider: Int? = null
    var rdEsActiva: Boolean? = null
    var rdActivoMdo: Boolean? = null
    var rdTieneRDC: Boolean? = null
    var rdTieneRDI: Boolean? = null
    var rdTieneRDCR: Boolean? = null
    var diaFacturacion: Int? = null
}
