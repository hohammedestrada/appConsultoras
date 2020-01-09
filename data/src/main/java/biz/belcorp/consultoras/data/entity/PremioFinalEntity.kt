package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

class PremioFinalEntity{

    @SerializedName("UpSellingDetalleId")
    var upSellingDetalleId: Int? = null

    @SerializedName("CUV")
    var cuv: String? = null

    @SerializedName("Nombre")
    var nombre: String? = null

    @SerializedName("Descripcion")
    var descripcion: String? = null

    @SerializedName("Imagen")
    var imagen: String? = null

    @SerializedName("Stock")
    var stock: Int? = null

    @SerializedName("Orden")
    var orden: Int? = null

    @SerializedName("Activo")
    var activo: Boolean? = null

    @SerializedName("UpSellingId")
    var upSellingId: Int? = null

    @SerializedName("StockActual")
    var stockActual: Int? = null

    @SerializedName("Seleccionado")
    var seleccionado: Boolean? = null

    @SerializedName("Habilitado")
    var habilitado: Boolean? = null

}
