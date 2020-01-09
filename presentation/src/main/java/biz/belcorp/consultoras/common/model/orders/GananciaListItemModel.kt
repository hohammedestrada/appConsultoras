package biz.belcorp.consultoras.common.model.orders

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Parcelize
data class GananciaListItemModel (var descripcion: String?,
                                  var montoGanancia: BigDecimal?): Parcelable
