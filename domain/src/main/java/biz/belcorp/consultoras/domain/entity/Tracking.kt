package biz.belcorp.consultoras.domain.entity

class Tracking {

    var id: Int? = null
    var numeroPedido: String? = null
    var campania: Int? = null
    var estado: String? = null
    var fecha: String? = null
    var detalles: List<TrackingDetail>? = null
}
