package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

class EscalaDescuentoEntity {

    @SerializedName("MontoDesde")
    var montoDesde: BigDecimal? = null

    @SerializedName("MontoHasta")
    var montoHasta: BigDecimal? = null

    @SerializedName("PorDescuento")
    var porDescuento: BigDecimal? = null

    @SerializedName("TipoParametriaOfertaFinal")
    var tipoParametriaOfertaFinal: String? = null

    @SerializedName("PrecioMinimo")
    var precioMinimo: BigDecimal? = null

    @SerializedName("Algoritmo")
    var algoritmo: String? = null
}
