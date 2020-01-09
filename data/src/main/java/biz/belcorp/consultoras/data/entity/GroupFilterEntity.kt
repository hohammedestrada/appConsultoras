package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.domain.entity.GroupFilter
import com.google.gson.annotations.SerializedName

data class GroupFilterEntity(
    @SerializedName(value = "NombreGrupo") var nombreGrupo: String?,
    @SerializedName(value = "Excluyente") var excluyente: Boolean,
    @SerializedName(value = "Opciones") var filtros: List<FiltroEntity?>?
) {

    companion object {

        fun transformList(list: List<GroupFilterEntity?>?): List<GroupFilter?>?{
            return mutableListOf<GroupFilter>().apply {
                list?.forEach {
                    it?.let { it1 -> transform(it1) }?.let { it2 -> add(it2) }
                }
            }.toList()
        }

        fun transform(input: GroupFilterEntity): GroupFilter {
            input.run {
                return GroupFilter(
                    nombreGrupo,
                    excluyente,
                    FiltroEntity.transformList(filtros)
                )
            }
        }
    }
}
