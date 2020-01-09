package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.domain.entity.Categoria
import com.google.gson.annotations.SerializedName

data class CategoriaEntity(
    @SerializedName(value = "Codigo") var codigo: String?,
    @SerializedName(value = "Nombre") var nombre: String?,
    @SerializedName(value = "Descripcion") var descripcion: String?,
    @SerializedName(value = "OrdenApp") var ordenApp: Int?,
    @SerializedName(value = "ColorTextoApp") var colorTextoApp: String?,
    @SerializedName(value = "ColorFondoApp") var colorFondoApp: String?,
    @SerializedName(value = "Cantidad") var cantidad: Int?,
    @SerializedName(value = "UrlImagenGrupo") var urlImagenGrupo: String?
) {

    companion object {

        fun transformList(list: List<CategoriaEntity?>?): List<Categoria?>?{
            return mutableListOf<Categoria>().apply {
                list?.forEach {
                    it?.let { it1 -> transform(it1) }?.let { it2 -> add(it2) }
                }
            }.toList()
        }

        fun transform(input: CategoriaEntity): Categoria {
            input.run {
                return Categoria(
                    codigo,
                    nombre,
                    descripcion,
                    ordenApp,
                    colorTextoApp,
                    colorFondoApp,
                    cantidad,
                    urlImagenGrupo
                )
            }
        }
    }
}
