package biz.belcorp.consultoras.common.model.pagoonline

import java.io.Serializable

class TipoPagoModel: Serializable {
    var urlIcono: String? = null
    var estadoCuenta: PagoOnlineConfigModel.EstadoCuenta? = null
    var tarjeta: PagoOnlineConfigModel.MetodoPago? = null
    var tipoPago: List<PagoOnlineConfigModel.TipoPago> = emptyList()
    var vencimientoDeuda: String = ""
    var simboloMoneda: String = ""

}


