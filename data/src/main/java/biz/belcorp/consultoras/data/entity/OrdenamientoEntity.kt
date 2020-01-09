package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.domain.entity.Ordenamiento
import com.google.gson.annotations.SerializedName

class OrdenamientoEntity(
    @SerializedName("TablaLogicaDatosID") var ordenDatosId: Int? = null,

    @SerializedName("TablaLogicaID") var ordenLogicaId: Int? = null,

    @SerializedName("Codigo") var ordenCodigo: String? = null,

    @SerializedName("Valor") var ordenValor: String? = null,

    @SerializedName("Descripcion") var ordenDescripcion: String? = null
){

    companion object {

        fun transformList(list: List<OrdenamientoEntity?>?): List<Ordenamiento?>? {
            return mutableListOf<Ordenamiento>().apply {
                list?.forEach {
                    it?.let { it1 -> transform(it1) }?.let { it2 -> add(it2) }
                }
            }.toList()
        }

        fun transform(input: OrdenamientoEntity): Ordenamiento{
            input.run {
                return Ordenamiento(
                    ordenDatosId,
                    ordenLogicaId,
                    ordenCodigo,
                    ordenValor,
                    ordenDescripcion
                )
            }
        }



    }

}
