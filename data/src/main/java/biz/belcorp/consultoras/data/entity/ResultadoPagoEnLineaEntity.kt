package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 **  ResultadoPagoEnLineaEntity.kt
 **
 **  Copyright 2017 Belcorp Perú. All rights reserved.
 **
 **  @description Entity de Registro del Resultado de Pago en Linea VISA
 **  @created     29/11/2017
 **  @author      Emanuel Ortega Silva
 **  @version     1.0
 **/
class ResultadoPagoEnLineaEntity : Serializable {

    /**
     **  @description Codigo de Resultado de Registro de Pago en Linea (Registro Nuevo o Existente)
     **/
    @SerializedName("Code")
    var code: String? = null
    /**
     **  @description Mensaje Descriptivo referente a Codigo
     **/
    @SerializedName("Message")
    var message: String? = null
    @SerializedName("SaldoPendiente")
    var saldoPendiente: Double? = null
    @SerializedName("PagoEnLineaResultadoLogId")
    var pagoEnLineaResultadoLogId: Int? = null
    var error: Throwable? = null

}
