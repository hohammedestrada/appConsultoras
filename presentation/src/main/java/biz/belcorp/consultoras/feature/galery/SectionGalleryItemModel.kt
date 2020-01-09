package biz.belcorp.consultoras.feature.galery

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class SectionGalleryItemModel : Parcelable{
    var tab : FiltroGaleriaModel? = null
    var files : ArrayList<ListadoImagenModel>? = null
}
