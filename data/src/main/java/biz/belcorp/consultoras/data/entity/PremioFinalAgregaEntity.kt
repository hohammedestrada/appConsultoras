package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName


class PremioFinalAgregaEntity {

    @SerializedName("CampaniaId")
    var campaniaId: Int? = null

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

    @SerializedName("CUV")
    var cuv: String? = null

    @SerializedName("TipoRango")
    var tipoRango: String? = null

    @SerializedName("MontoPedidoFinal")
    var montoPedidoFinal: Double? = null

    @SerializedName("UpSellingDetalleId")
    var upSellingDetalleId: Int? = null





}
