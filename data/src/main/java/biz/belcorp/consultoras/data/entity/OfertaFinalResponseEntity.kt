package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class OfertaFinalResponseEntity {

    @SerializedName("OfertaFinalCabecera")
    var ofertaFinalHeader: OfertaFinalHeaderEntity? = null

    @SerializedName("OfertaFinalDetalle")
    var productosOfertaFinal: List<OfertaFinalEntity?>? = null

    init {
        this.productosOfertaFinal = ArrayList()
    }

}
