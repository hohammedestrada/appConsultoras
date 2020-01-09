package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

class ProductoDetalleEntity {

    @SerializedName("ID")
    var id: Int? = null

    @SerializedName("CUV")
    var cuv: String? = null

    @SerializedName("NombreComercial")
    var nombreComercial: String? = null

    @SerializedName("FactorCuadre")
    var factorCuadre: Int? = null

    @SerializedName("Orden")
    var orden: Int? = null

    @SerializedName("CodigoProducto")
    var codigoProducto: String? = null

    @SerializedName("Grupo")
    var grupo: String? = null

    @SerializedName("PrecioCatalogo")
    var precioCatalogo: BigDecimal? = null

    @SerializedName("Digitable")
    var digitable: Int? = null

    @SerializedName("Cantidad")
    var cantidad: Int? = null
}
