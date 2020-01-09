package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.domain.entity.FestivalSello
import com.google.gson.annotations.SerializedName

class FestivalSelloEntity {

    @SerializedName("SelloColorInicio")
    var selloColorInicio: String? = null

    @SerializedName("SelloColorFin")
    var selloColorFin: String? = null

    @SerializedName("SelloColorDireccion")
    var selloColorDireccion: Int? = null

    @SerializedName("SelloTexto")
    var selloTexto: String? = null

    @SerializedName("SelloColorTexto")
    var selloColorTexto: String? = null

    companion object {
            fun transform(input: FestivalSelloEntity?): FestivalSello?{
                return FestivalSello().apply {
                    selloColorInicio = input?.selloColorInicio
                    selloColorFin = input?.selloColorFin
                    selloColorDireccion = input?.selloColorDireccion
                    selloTexto = input?.selloTexto
                    selloColorTexto = input?.selloColorTexto
                }

            }
    }
}
