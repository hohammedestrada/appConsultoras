package biz.belcorp.consultoras.domain.entity

class VisaPayment {

    var montoGastosAdministrativos: Double? = null
    var email: String? = null
    var paymentStatus: String? = null
    var montoPago: Double? = null
    var fechaLimPago: String? = null
    var transactionId: String? = null
    var visa: Visa? = null
    var campaniaID: String? = null
    var documentoIdentidad: String? = null
    var codigoUsuario: String? = null
    var simbolo: String? = null
    var primerNombre: String? = null
    var montoDeudaConGastos: Double? = null
    var transactionDateTime: Long? = null
    var merchantId: String? = null
    var nombre: String? = null
    var paymentDescription: String? = null
    var userTokenId: String? = null
    var aliasNameTarjeta: String? = null
    var primerApellido: String? = null
    var externalTransactionId: String? = null
    var transactionUUID: String? = null

    class Visa {

        var pan: String? = null
        var cardtokenuuid: String? = null
        var imPAutorizado: String? = null
        var csicodigoprograma: String? = null
        var decisioncs: String? = null
        var resCv2: String? = null
        var csiporcentajedescuento: String? = null
        var nrocuota: String? = null
        var iDUnico: String? = null
        var eci: String? = null
        var respuesta: String? = null
        var dscEci: String? = null
        var dscCOdAccion: String? = null
        var codAutoriza: String? = null
        var reviewtransaction: String? = null
        var codtienda: String? = null
        var numorden: String? = null
        var codaccion: String? = null
        var usertokenuuid: String? = null
        var fechayhoraTx: String? = null
        var impcuotaaprox: String? = null
        var csiimportecomercio: String? = null
        var csimensaje: String? = null
        var nomEmisor: String? = null
        var oriTarjeta: String? = null
        var csitipocobro: String? = null
        var numreferencia: String? = null
        var eticket: String? = null

    }
}
