package biz.belcorp.consultoras.domain.entity

class PedidoInsertRequest{

    var campaniaId : Int? = null
    var cantidad : Int? = null
    var ipUsuario : String? = null
    var clienteID : Int? = null
    var clienteDescripcion : String? = null
    var identifier : String? = null
    var codigosConcursos : String? = null
    var aceptacionConsultoraDA : Int? = null
    var montoMaximoPedido : Double? = null
    var codigoPrograma : String? = null
    var consecutivoNueva : Int? = null
    var fechaInicioFacturacion : String? = null
    var fechaFinFacturacion : String? = null
    var consultoraNueva : Int? = null
    var nroCampanias : Int? = null
    var producto : ProductCUV? = null
    var isSugerido: Boolean? = null
    var origenPedidoWebCarrusel: Int? = 0
    var nivelCaminoBrillante: Int? = 0

    // Cambio para Pedido Reservado
    var segmentoInternoID: Int? = 0
    var montoMinimoPedido: Double? = 0.0
    var isValidacionAbierta: Boolean? = false
    var isZonaValida: Boolean? = false
    var isValidacionInteractiva : Boolean? = null
    var isDiaProl: Boolean? = false
    var codigoZona: String? = ""
    var codigoRegion: String? = ""
    var isUsuarioPrueba: Boolean? = false
    var simbolo : String? = ""

    // Cambio para Modificar con Ficha Resumida
    var isEditable: Boolean? = false
    var orderID: Int? = 0


}
