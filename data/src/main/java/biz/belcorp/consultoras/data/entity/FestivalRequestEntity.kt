package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.domain.entity.FestivalRequest
import com.google.gson.annotations.SerializedName

data class FestivalRequestEntity(

    @SerializedName("FlagMostrarBuscador")
    var isShowSearch: Boolean? = null,
    @SerializedName("BuscarFestival")
    var festivalSearch: SearchRequestEntity? = null,
    @SerializedName("ZonaID")
    var zoneId: Int? = null,
    @SerializedName("CodigoRegion")
    var regionCode: String? = null,
    @SerializedName("diaInicio")
    var startDay: Int? = null
) {
    companion object{

        fun transformToEntity(input: FestivalRequest): FestivalRequestEntity{
            return input.let {
                FestivalRequestEntity(it.isShowSearch,
                    SearchRequestEntity.transform(it.festivalSearch),
                    it.zoneId,
                    it.regionCode,
                    it.startDay)
            }
        }

    }
}
