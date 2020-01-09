package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.util.ArrayList

class ReservaResponseEntity {

    @SerializedName("Reserva")
    var reserva: Boolean? = null

    @SerializedName("CodigoMensaje")
    var codigoMensaje: String? = null

    @SerializedName("Informativas")
    var informativas: Boolean? = null

    @SerializedName("MontoEscala")
    var montoEscala: BigDecimal? = null

    @SerializedName("MontoTotal")
    var montoTotal: BigDecimal? = null

    @SerializedName("Observaciones")
    var observaciones: List<ObservacionPedidoEntity?>? = null

    @SerializedName("ListaMensajeCondicional")
    var mensajesProl: List<MensajeProlEntity?>? = null

    init {
        this.observaciones = ArrayList()
    }
}
