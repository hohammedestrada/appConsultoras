package biz.belcorp.consultoras.feature.galery

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ListadoImagenModel(var titulo : String = "",
                              var nombreArchivo : String = "",
                              var urlImagenThumb : String = "",
                              var urlImagenVisualiza : String = "",
                              var urlImagenDescarga : String = "",
                              var categoria : String = "",
                              var orden : Int = -1,
                              var seccion : String = "") : Parcelable
