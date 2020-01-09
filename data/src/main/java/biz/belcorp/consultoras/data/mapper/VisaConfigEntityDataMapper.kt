package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.DataVisaConfigEntity
import biz.belcorp.consultoras.data.entity.ResultadoPagoEnLineaEntity
import biz.belcorp.consultoras.data.entity.VisaEntity
import biz.belcorp.consultoras.data.entity.VisaLogPaymentEntity
import biz.belcorp.consultoras.domain.entity.BasicDto
import biz.belcorp.consultoras.domain.entity.ResultadoPagoEnLinea
import biz.belcorp.consultoras.domain.entity.VisaConfig
import biz.belcorp.consultoras.domain.entity.VisaPayment
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.net.dto.ServiceDto
import biz.belcorp.library.security.AesEncryption
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class VisaConfigEntityDataMapper @Inject internal constructor() {

    fun transform(input: VisaConfig, key: String): VisaConfig {
        return input.let {
            VisaConfig().apply {
                var encryption = AesEncryption.newInstance()
                this.accessKeyId = encryption.decrypt(key, it.accessKeyId)
                this.secretAccessKey = encryption.decrypt(key, it.secretAccessKey)
                this.endPointUrl = encryption.decrypt(key, it.endPointUrl)
                this.sessionToken = encryption.decrypt(key, it.sessionToken)
                this.merchantId = encryption.decrypt(key, it.merchantId)
                this.nextCounterUrl = encryption.decrypt(key, it.nextCounterUrl)
                this.recurrence = encryption.decrypt(key, it.recurrence)
                this.recurrenceType = encryption.decrypt(key, it.recurrenceType)
                this.recurrenceFrecuency = encryption.decrypt(key, it.recurrenceFrecuency)
                this.recurrenceAmount = encryption.decrypt(key, it.recurrenceAmount)
                this.tokenTarjetaGuardada = encryption.decrypt(key, it.tokenTarjetaGuardada)
            }
        }
    }

    fun transform(visaConfig: DataVisaConfigEntity?): VisaConfig? {
        return visaConfig?.let {
            VisaConfig().apply {
                accessKeyId = it.accessKeyId
                secretAccessKey = it.secretAccessKey
                endPointUrl = it.endPointUrl
                sessionToken = it.sessionToken
                merchantId = it.merchantId
                nextCounterUrl = it.nextCounterUrl
                recurrence = it.recurrence
                recurrenceType = it.recurrenceType
                recurrenceFrecuency = it.recurrenceFrecuency
                recurrenceAmount = it.recurrenceAmount
                tokenTarjetaGuardada = it.tokenTarjetaGuardada
            }
        }
    }

    fun transformVisaConfig(input: ServiceDto<DataVisaConfigEntity>?): BasicDto<VisaConfig>? {
        return input?.let {
            BasicDto<VisaConfig>().apply {
                code = it.code
                message = it.message
                val json = Gson().toJson(it.data)
                val type = object : TypeToken<DataVisaConfigEntity>() {}.type
                try {
                    val predata: DataVisaConfigEntity = Gson().fromJson(json, type)
                    data = transform(predata)
                } catch (e: Exception) {
                    BelcorpLogger.d("Visa: obtencion de configuracion inicial nulo")
                }
            }
        }
    }

    fun transform(visaSave: VisaPayment?): VisaLogPaymentEntity? {
        return visaSave?.let {
            VisaLogPaymentEntity().apply {
                this.aliasNameTarjeta = it.aliasNameTarjeta
                this.campaniaID = it.campaniaID
                this.codigoUsuario = it.codigoUsuario
                this.documentoIdentidad = it.documentoIdentidad
                this.email = it.email
                this.externalTransactionId = it.externalTransactionId
                this.fechaLimPago = it.fechaLimPago
                this.merchantId = it.merchantId
                this.montoDeudaConGastos = it.montoDeudaConGastos
                this.montoGastosAdministrativos = it.montoGastosAdministrativos
                this.montoPago = it.montoPago
                this.nombre = it.nombre
                this.paymentDescription = it.paymentDescription
                this.paymentStatus = it.paymentStatus
                this.primerApellido = it.primerApellido
                this.primerNombre = it.primerNombre
                this.simbolo = it.simbolo
                this.transactionDateTime = it.transactionDateTime
                this.transactionId = it.transactionId
                this.transactionUUID = it.transactionUUID
                this.userTokenId = it.userTokenId
                this.visa = transform(it.visa)
            }
        }
    }

    private fun transform(visa: VisaPayment.Visa?): VisaEntity? {
        return visa?.let {
            VisaEntity().apply {
                this.pan = it.pan
                this.cardtokenuuid = it.cardtokenuuid
                this.imPAutorizado = it.imPAutorizado
                this.csicodigoprograma = it.csicodigoprograma
                this.decisioncs = it.decisioncs
                this.resCv2 = it.resCv2
                this.csiporcentajedescuento = it.csiporcentajedescuento
                this.nrocuota = it.nrocuota
                this.iDUnico = it.iDUnico
                this.eci = it.eci
                this.respuesta = it.respuesta
                this.dscEci = it.dscEci
                this.dscCOdAccion = it.dscCOdAccion
                this.codAutoriza = it.codAutoriza
                this.reviewtransaction = it.reviewtransaction
                this.codtienda = it.codtienda
                this.numorden = it.numorden
                this.codaccion = it.codaccion
                this.usertokenuuid = it.usertokenuuid
                this.fechayhoraTx = it.fechayhoraTx
                this.impcuotaaprox = it.impcuotaaprox
                this.csiimportecomercio = it.csiimportecomercio
                this.csimensaje = it.csimensaje
                this.nomEmisor = it.nomEmisor
                this.oriTarjeta = it.oriTarjeta
                this.csitipocobro = it.csitipocobro
                this.numreferencia = it.numreferencia
                this.eticket = it.eticket
            }
        }
    }

    fun transform(input: ResultadoPagoEnLineaEntity?): ResultadoPagoEnLinea? {
        return input?.let {
            ResultadoPagoEnLinea().apply {
                this.saldoPendiente = if (it.saldoPendiente != null) it.saldoPendiente!! else 0.0
                this.message = it.message
                this.code = it.code
            }
        }
    }

    fun transformSaldo(input: ServiceDto<ResultadoPagoEnLineaEntity>): BasicDto<ResultadoPagoEnLinea> {
        return input.let {
            BasicDto<ResultadoPagoEnLinea>().apply {
                code = it.code
                message = it.message
                val json = Gson().toJson(it.data)
                val type = object : TypeToken<ResultadoPagoEnLineaEntity>() {}.type
                try {
                    val predata: ResultadoPagoEnLineaEntity = Gson().fromJson(json, type)
                    data = transform(predata)
                } catch (e: Exception) {
                    BelcorpLogger.d("Visa: Saldo actual error")
                }

            }
        }
    }

}
