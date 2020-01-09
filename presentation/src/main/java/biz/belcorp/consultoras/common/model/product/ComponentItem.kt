package biz.belcorp.consultoras.common.model.product

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Parcelize
data class ComponentItem(var setDetalleId: Int?,
                         var setId: Int?,
                         var cuv: String?,
                         var nombreProducto: String?,
                         var cantidad: Int? = null,
                         var factorRepetecion: Int?,
                         var precioUnidad: BigDecimal?) : Parcelable
