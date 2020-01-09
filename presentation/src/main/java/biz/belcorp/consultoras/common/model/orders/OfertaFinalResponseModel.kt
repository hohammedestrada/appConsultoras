package biz.belcorp.consultoras.common.model.orders

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class OfertaFinalResponseModel( var ofertaFinalHeader: OfertaFinalHeaderModel?,
                                     var productosOfertaFinal: List<OfertaFinalModel?>?) : Parcelable


