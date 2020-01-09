package biz.belcorp.consultoras.data.entity.gallery

import com.google.gson.annotations.SerializedName

class GalleryResponseEntity {
    @SerializedName("FiltroGaleria")
    var tabsGaleria : ArrayList<FiltroGaleriaEntity?>? = null

    @SerializedName("ListadoImagen")
    var listadoImagen : ArrayList<ListadoImagenEntity?>? = null
}
