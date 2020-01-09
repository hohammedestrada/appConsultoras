package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

class FestivalCategoriaEntity {

    @SerializedName("IdFestivalCategoria")
    var idFestivalCategoria: Int? = null

    @SerializedName("IdFestival")
    var idFestival: Int? = null

    @SerializedName("CodigoCategoria")
    var CodigoCategoria: String? = null

    @SerializedName("Activo")
    var Activo: Boolean? = null


}
