package biz.belcorp.consultoras.feature.auth.startTutorial

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ContenidoResumenDetalleModel(
    var grupo: String? = null,

    var typeContenido: String = "",

    var idContenido: String = "",

    var codigoDetalleResumen: String? = null,

    var urlDetalleResumen: String? = null,

    var accion: String? = null,

    var ordenDetalleResumen: Int = 0,

    var visto: Boolean = false,

    var descripcion: String? = null
) : Parcelable
