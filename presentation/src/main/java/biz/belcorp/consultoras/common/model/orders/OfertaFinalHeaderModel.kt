package biz.belcorp.consultoras.common.model.orders

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal


@Parcelize
data class OfertaFinalHeaderModel(var tipoMeta: String?,
                                  var montoMeta: BigDecimal?,
                                  var porcentajeMeta: BigDecimal?,
                                  var descripcionRegalo: String?,
                                  var mensajeTipingPoint: String?,
                                  var faltanteTipingPoint: BigDecimal?,
                                  var cuvRegalo: String?) : Parcelable
