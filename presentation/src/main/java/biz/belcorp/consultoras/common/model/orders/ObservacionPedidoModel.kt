package biz.belcorp.consultoras.common.model.orders

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ObservacionPedidoModel(var cuv: String?,
                                  var descripcion: String?,
                                  var caso: Int?,
                                  var cuvObs: String?,
                                  var conjuntoID: Int?,
                                  var pedidoDetalleID: Int?) : Parcelable
