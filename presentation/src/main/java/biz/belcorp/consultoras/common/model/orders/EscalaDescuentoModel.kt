package biz.belcorp.consultoras.common.model.orders

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal


@Parcelize
data class EscalaDescuentoModel(var montoDesde: BigDecimal?,
                                var montoHasta: BigDecimal?,
                                var porDescuento: BigDecimal?,
                                var tipoParametriaOfertaFinal: String?,
                                var precioMinimo: BigDecimal?,
                                var algoritmo: String?) : Parcelable
