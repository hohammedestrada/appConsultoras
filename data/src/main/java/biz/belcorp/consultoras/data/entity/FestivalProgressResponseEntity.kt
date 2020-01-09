package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.domain.entity.FestivalProgressResponse
import com.google.gson.annotations.SerializedName

data class FestivalProgressResponseEntity(
    @SerializedName("CuvPremio") var cuvPremio: String?,
    @SerializedName("PorcentajeProgreso") var porcentajeProgreso: Int?,
    @SerializedName("MontoRestante") var montoRestante: Double?,
    @SerializedName("FlagPremioAgregado") var flagPremioAgregado: Boolean?
) {
    companion object {

        fun transformList(list: List<FestivalProgressResponseEntity?>?): List<FestivalProgressResponse>?{
            return mutableListOf<FestivalProgressResponse>().apply {
                list?.forEach {
                    it?.let{ it1 -> transform(it1) }?.let { it2 -> add(it2)  }
                }
            }.toList()
        }

        fun transform(input: FestivalProgressResponseEntity?): FestivalProgressResponse?{
            return input?.let{
                FestivalProgressResponse(
                    input.cuvPremio,
                    input.porcentajeProgreso,
                    input.montoRestante,
                    input.flagPremioAgregado)
            }
        }
    }
}
