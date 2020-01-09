package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

class PremioFinalMetaEntity {

    @SerializedName("MontoPedido")
    var montoPedido: Double? = null

    @SerializedName("GapMinimo")
    var gapMinimo: Double? = null

    @SerializedName("GapMaximo")
    var gapMaximo: Double? = null

    @SerializedName("GapAgregar")
    var gapAgregar: Double? = null

    @SerializedName("MontoMeta")
    var montoMeta: Double? = null

    @SerializedName("TipoRango")
    var tipoRango: String? = null

}
