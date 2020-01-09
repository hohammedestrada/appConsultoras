package biz.belcorp.consultoras.domain.entity

class ResultadoPagoEnLinea {

    /**
     **  @description Codigo de Resultado de Registro de Pago en Linea (Registro Nuevo o Existente)
     **/
    var code: String?= null
    /**
     **  @description Mensaje Descriptivo referente a Codigo
     **/
    var message: String?= null
    var saldoPendiente: Double = 0.0 //TODO MCAPP-14 No debe de iniciar el Saldo Pendiente en 0.0 sino en NULL

}
