package biz.belcorp.consultoras.common.model.search

import android.os.Parcelable
import biz.belcorp.consultoras.domain.entity.Origen
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrigenModel(var codigo: String?, var valor: String?): Parcelable{

    companion object {

        fun transformList(list: List<Origen?>?): ArrayList<OrigenModel>{
            return arrayListOf<OrigenModel>().apply {
                list?.forEach {
                    it?.let { it1 -> transform(it1) }?.let { it2 -> add(it2) }
                }
            }
        }

        fun transform(input: Origen): OrigenModel{
            input.run {
                return OrigenModel(codigo, valor)
            }
        }
    }
}

