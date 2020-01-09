package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

class SearchOrderByResponseEntity {

    @SerializedName("TablaLogicaDatosID")
    var tablaLogicaDatosID: Int? = null
    @SerializedName("TablaLogicaID")
    var tablaLogicaID: Int? = null
    @SerializedName("Codigo")
    var codigo: String? = null
    @SerializedName("Valor")
    var valor: String? = null
    @SerializedName("Descripcion")
    var descripcion: String? = null

}
