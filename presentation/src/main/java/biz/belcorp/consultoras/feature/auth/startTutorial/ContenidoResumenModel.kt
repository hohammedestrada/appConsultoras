package biz.belcorp.consultoras.feature.auth.startTutorial

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ContenidoResumenModel(
    var codigoResumen: String = "",

    var urlMiniatura: String? = null,

    var totalContenido: Int = 0,

    var contenidoVisto: Int = 0,

    var contenidoDetalle: ArrayList<ContenidoResumenDetalleModel>? = null) : Parcelable
