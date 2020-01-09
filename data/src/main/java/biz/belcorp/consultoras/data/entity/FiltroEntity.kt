package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.domain.entity.Filtro
import com.google.gson.annotations.SerializedName

data class FiltroEntity(
    @SerializedName(value = "Codigo") var codigo: String?,
    @SerializedName(value = "Nombre") var nombre: String?,
    @SerializedName(value = "Descripcion") var descripcion: String?,
    @SerializedName(value = "OrdenApp") var ordenApp: Int?,
    @SerializedName(value = "ColorTextoApp") var colorTextoApp: String?,
    @SerializedName(value = "ColorFondoApp") var colorFondoApp: String?,
    @SerializedName(value = "ValorMinimo") var valorMinimo: Float?,
    @SerializedName(value = "ValorMaximo") var valorMaximo: Float?,
    @SerializedName(value = "NombreGrupo") var nombreGrupo: String?,
    @SerializedName(value = "IdFiltro") var idFiltro: Int?,
    @SerializedName(value = "IdPadre") var idPadre: Int?,
    @SerializedName(value = "IdSeccion") var idSeccion: String?
) {

    companion object {

        fun transformList(list: List<FiltroEntity?>?): List<Filtro?>?{
            return mutableListOf<Filtro>().apply {
                list?.forEach {
                    it?.let { it1 -> transform(it1) }?.let { it2 -> add(it2) }
                }
            }.toList()
        }

        fun transform(input: FiltroEntity): Filtro {
            input.run {
                return Filtro(
                    codigo,
                    nombre,
                    descripcion,
                    ordenApp,
                    colorTextoApp,
                    colorFondoApp,
                    valorMinimo,
                    valorMaximo,
                    nombreGrupo,
                    idFiltro,
                    idPadre,
                    idSeccion
                )
            }
        }
    }
}
