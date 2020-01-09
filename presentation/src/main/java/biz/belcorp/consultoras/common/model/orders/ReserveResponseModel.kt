package biz.belcorp.consultoras.common.model.orders

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Parcelize
class ReserveResponseModel (var reserva: Boolean?,
                            var codigoMensaje: String?,
                            var informativas: Boolean?,
                            var montoEscala: BigDecimal?,
                            var montoTotal: BigDecimal?,
                            var observaciones: List<ObservacionPedidoModel?>? ) : Parcelable

