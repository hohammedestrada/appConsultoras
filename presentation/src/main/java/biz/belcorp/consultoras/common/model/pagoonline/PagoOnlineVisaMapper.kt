package biz.belcorp.consultoras.common.model.pagoonline

import biz.belcorp.consultoras.domain.entity.ResultadoPagoEnLinea
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.entity.VisaConfig
import biz.belcorp.consultoras.domain.entity.VisaPayment
import pe.com.visanet.lib.VisaNetPaymentInfo
import javax.inject.Inject
import biz.belcorp.consultoras.domain.util.replaceByEmptyIs
import biz.belcorp.consultoras.domain.util.toDateAndFormat

class PagoOnlineVisaMapper @Inject internal constructor() {

    fun transform(visaNetPaymentInfo: VisaNetPaymentInfo, confirm: ConfirmacionPagoModel, config: VisaConfig, user: User?): VisaPayment {
        var visaPayment = VisaPayment()
        visaPayment.let {
            it.montoGastosAdministrativos = confirm.porcentajeBruto
            it.montoPago = confirm.montoBruto
            it.montoDeudaConGastos = confirm.totalPagar!!.toDouble() //TODO Cambiar a Double
            it.merchantId = config.merchantId
            it.userTokenId = config.tokenTarjetaGuardada
            it.externalTransactionId = config.sessionToken
            //if (visaNetPaymentInfo != null)
            //{
                it.paymentStatus = visaNetPaymentInfo.paymentStatus
                it.paymentDescription = visaNetPaymentInfo.paymentDescription
                it.transactionUUID = visaNetPaymentInfo.transactionId
                it.transactionDateTime = visaNetPaymentInfo.transactionDate
                it.aliasNameTarjeta = null // TODO NOMBRE DE LA TARJETA DE CREDITO NO SE PUEDE OBTENER POR LO LEGAL
                it.visa = VisaPayment.Visa()
                it.visa!!.let {
                    it.numorden = visaNetPaymentInfo.data["NUMORDEN"]?.replaceByEmptyIs("null")                                   // NUMORDEN: 1602
                    it.impcuotaaprox = visaNetPaymentInfo.data["IMPCUOTAAPROX"]?.replaceByEmptyIs("null")                         // IMPCUOTAAPROX: 0.00
                    it.pan = visaNetPaymentInfo.data["PAN"]?.replaceByEmptyIs("null")                                             // PAN: 450034******0016
                    it.codAutoriza = visaNetPaymentInfo.data["COD_AUTORIZA"]?.replaceByEmptyIs("null")                            // COD_AUTORIZA: 123047
                    it.nrocuota = visaNetPaymentInfo.data["NROCUOTA"]?.replaceByEmptyIs("null")                                   // NROCUOTA: 00
                    it.eticket = visaNetPaymentInfo.data["ETICKET"]?.replaceByEmptyIs("null")                                     // ETICKET: 4106040291071811141232252656
                    it.csicodigoprograma = visaNetPaymentInfo.data["CSICODIGOPROGRAMA"]?.replaceByEmptyIs("null")                 // CSICODIGOPROGRAMA: null
                    it.numreferencia = visaNetPaymentInfo.data["NUMREFERENCIA"]?.replaceByEmptyIs("null")                         // NUMREFERENCIA: null
                    it.eci = visaNetPaymentInfo.data["ECI"]?.replaceByEmptyIs("null")                                             // ECI: 07
                    it.codaccion = visaNetPaymentInfo.data["CODACCION"]?.replaceByEmptyIs("null")                                 // CODACCION: 000
                    it.dscEci = visaNetPaymentInfo.data["DSC_ECI"]?.replaceByEmptyIs("null")                                      // DSC_ECI: No encontrado
                    it.respuesta = visaNetPaymentInfo.data["RESPUESTA"]?.replaceByEmptyIs("null")                                 // RESPUESTA: 1
                    it.cardtokenuuid = visaNetPaymentInfo.data["cardTokenUUID"]?.replaceByEmptyIs("null")                         // cardTokenUUID: 385c1b9a-6888-4436-97ec-e210383d6875
                    it.csiimportecomercio = visaNetPaymentInfo.data["CSIIMPORTECOMERCIO"]?.replaceByEmptyIs("null")               // CSIIMPORTECOMERCIO: null
                    it.iDUnico = visaNetPaymentInfo.data["ID_UNICO"]?.replaceByEmptyIs("null")                                    // ID_UNICO: 991183180241731
                    it.nomEmisor = visaNetPaymentInfo.data["NOM_EMISOR"]?.replaceByEmptyIs("null")                                // NOM_EMISOR: No encontrado
                    it.usertokenuuid = visaNetPaymentInfo.data["userTokenUUID"]?.replaceByEmptyIs("null")                         // userTokenUUID: 35c67466-b221-4f11-a6a6-cdd4276ea3f1
                    it.csimensaje = visaNetPaymentInfo.data["CSIMENSAJE"]?.replaceByEmptyIs("null")                               // CSIMENSAJE: null
                    it.csiporcentajedescuento = visaNetPaymentInfo.data["CSIPORCENTAJEDESCUENTO"]?.replaceByEmptyIs("null")       // CSIPORCENTAJEDESCUENTO: null
                    it.codtienda = visaNetPaymentInfo.data["CODTIENDA"]?.replaceByEmptyIs("null")                                 // CODTIENDA: 148009103
                    it.oriTarjeta = visaNetPaymentInfo.data["ORI_TARJETA"]?.replaceByEmptyIs("null")                              // ORI_TARJETA:
                    it.resCv2 = visaNetPaymentInfo.data["RES_CVV2"]?.replaceByEmptyIs("null")                                     // RES_CVV2: N
                    it.reviewtransaction = visaNetPaymentInfo.data["reviewTransaction"]?.replaceByEmptyIs("null")                 // reviewTransaction: null
                    it.csitipocobro = visaNetPaymentInfo.data["CSITIPOCOBRO"]?.replaceByEmptyIs("null")                           // CSITIPOCOBRO: null
                    it.dscCOdAccion = visaNetPaymentInfo.data["DSC_COD_ACCION"]?.replaceByEmptyIs("null")                         // DSC_COD_ACCION: Operacion Autorizada
                    it.imPAutorizado = visaNetPaymentInfo.data["IMP_AUTORIZADO"]?.replaceByEmptyIs("null")                        // IMP_AUTORIZADO: 400.14
                    it.decisioncs = visaNetPaymentInfo.data["DECISIONCS"]?.replaceByEmptyIs("null")                               // DECISIONCS: null
                    it.fechayhoraTx = visaNetPaymentInfo.data["FECHAYHORA_TX"]?.replaceByEmptyIs("null")                          // FECHAYHORA_TX: 14/11/2018 12:32
                }
                visaPayment.transactionId = visaNetPaymentInfo.data["ID_UNICO"]?.replaceByEmptyIs("null")
                visaPayment.transactionId = visaNetPaymentInfo.data["ID_UNICO"]?.replaceByEmptyIs("null")

                //it.userTokenId = config.tokenTarjetaGuardada

                if(it.userTokenId.isNullOrEmpty()){
                    it.userTokenId = visaNetPaymentInfo.data["userTokenUUID"]?.replaceByEmptyIs("null")
                }

            //}
            user!!.let {
                visaPayment.email = it.email
                //visaPayment.fechaLimPago = it.expirationDate
                //visaPayment.fechaLimPago = "2018-10-25" // TODO CAMBIAR
                visaPayment.fechaLimPago = it.expirationDate.toDateAndFormat("dd/mm/yyyy","yyyy-mm-dd")
                visaPayment.campaniaID = it.campaing
                visaPayment.documentoIdentidad = it.numeroDocumento
                visaPayment.codigoUsuario = it.consultantCode
                visaPayment.simbolo = it.countryMoneySymbol
                visaPayment.primerNombre = it.primerNombre
                visaPayment.nombre = it.alias
                visaPayment.primerApellido = it.consultantName
            }
        }
        return visaPayment
    }

    fun transform(visaPayment:VisaPayment, saldoActual: ResultadoPagoEnLinea): ResultadoPagoModel{
        return ResultadoPagoModel().apply {
            this.nombre = visaPayment.nombre
            this.numeroOperacion = visaPayment.visa!!.numorden
            this.numeroTarjeta = visaPayment.visa!!.pan
            this.fechaPago = visaPayment.visa!!.fechayhoraTx
            this.monto = visaPayment.montoPago.toString() //TODO Monto no String
            this.cargo = visaPayment.montoGastosAdministrativos.toString() //TODO Monto no String
            this.totalPagado = visaPayment.montoDeudaConGastos.toString() //TODO Monto no String
            this.saldoPendiente = saldoActual.saldoPendiente.toString() //TODO Monto no String
            this.fechaVencimiento = visaPayment.fechaLimPago.toDateAndFormat("yyyy-mm-dd", "dd/mm")
            this.gastosAdmin = visaPayment.montoGastosAdministrativos.toString() //TODO Monto no String ???
            this.simboloMoneda = visaPayment.simbolo
        }
    }

    fun transform(visaPayment:VisaPayment) : ResultadoPagoModel.ResultadoPagoRechazadoModel{
        return ResultadoPagoModel.ResultadoPagoRechazadoModel().apply {
            this.operacion = visaPayment.visa!!.numorden  //TODO OJO
            this.mensaje = visaPayment.visa!!.dscCOdAccion
            this.fecha = visaPayment.visa!!.fechayhoraTx
        }
    }

}
