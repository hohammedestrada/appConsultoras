package biz.belcorp.consultoras.data.entity


import com.google.gson.annotations.SerializedName

class FestivalConfiguracionEntity{

    @SerializedName("IdFestival")
    var idFestival: Int? = null

    @SerializedName("CampaniaId")
    var campaniaId: Int? = null

    @SerializedName("PremioGratis")
    var PremioGratis: Boolean? = null

    @SerializedName("Tipo")
    var Tipo: Int? = null

    @SerializedName("Activo")
    var Activo: Boolean? = false

    @SerializedName("Titulo")
    var Titulo: String? = null

    @SerializedName("DescripcionCorta")
    var DescripcionCorta: String? = null

    @SerializedName("DescripcionLarga")
    var DescripcionLarga: String? = null

    @SerializedName("Banner")
    var Banner:FestivalBannerEntity? = null

    @SerializedName("Sello")
    var Sello:FestivalSelloEntity? = null

    @SerializedName("Categoria")
    var Categoria : List<FestivalCategoriaEntity?>?=null



}
