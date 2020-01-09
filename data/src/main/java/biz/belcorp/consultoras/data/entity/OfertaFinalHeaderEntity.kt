package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

class OfertaFinalHeaderEntity {

    @SerializedName("TipoMeta")
    var tipoMeta: String? = null

    @SerializedName("MontoMeta")
    var montoMeta: BigDecimal? = null

    @SerializedName("PorcentajeMeta")
    var porcentajeMeta: BigDecimal? = null

    @SerializedName("DescripcionRegalo")
    var descripcionRegalo: String? = null

    @SerializedName("MensajeTippingPoint")
    var mensajeTipingPoint: String? = null

    @SerializedName("FaltanteTippingPoint")
    var faltanteTipingPoint: BigDecimal? = null

    @SerializedName("CUVRegalo")
    var cuvRegalo: String? = null

}
