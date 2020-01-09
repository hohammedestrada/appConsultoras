package biz.belcorp.consultoras.feature.galery

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class FiltroGaleriaModel : Parcelable {
    var Codigo : String = ""
    var Descripcion : String = ""
    var Tipo : Int? = -1
    var Orden : Int = -2
    var Activo : Boolean = false
    var IdPadre : Int = 0
    var CodigoPadre : String = ""
    var OrdenPadre : Int = 0
    var Otros : String = ""
    var OtrosAdd : String = ""
    var EsSeccion : Boolean = false
    var EsExcluyente : Boolean = false
    var EsSeleccionado : Boolean = false
}
