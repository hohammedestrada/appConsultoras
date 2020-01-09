package biz.belcorp.consultoras.common.model.ganamas

import android.os.Parcelable
import biz.belcorp.consultoras.domain.entity.ConfiguracionPorPalanca
import biz.belcorp.consultoras.domain.entity.Origen
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ConfiguracionPorPalancaModel (
    var tipoOferta: String?,
    var flagActivo: Boolean?,
    var titulo: String?,
    var subTitulo: String?,
    var colorTexto: String?,
    var colorFondo: String?,
    var orden: Int?,
    var cantidadMostrarCarrusel: Int?,
    var bannerOferta: String?,
    var tieneEvento: Boolean?,
    var tieneCompartir: Boolean?,
    var textoInicio: String?,
    var textoModificar: String?,
    var colorFondoBoton: String?,
    var colorTextoBoton: String?,
    var listaOrigenes: List<OrigenModel?>? = arrayListOf()
) : Parcelable {

    companion object {

        fun transformList(list: List<ConfiguracionPorPalanca?>?): List<ConfiguracionPorPalancaModel?>?{
            return mutableListOf<ConfiguracionPorPalancaModel>().apply {
                list?.forEach {
                    it?.let { it1 -> transform(it1) }?.let { it2 -> add(it2) }
                }
            }.toList()
        }

        fun transform(input: ConfiguracionPorPalanca): ConfiguracionPorPalancaModel {
            input.run {
                return ConfiguracionPorPalancaModel(
                    tipoOferta,
                    flagActivo,
                    titulo,
                    subTitulo,
                    colorTexto,
                    colorFondo,
                    orden,
                    cantidadMostrarCarrusel,
                    bannerOferta,
                    tieneEvento,
                    tieneCompartir,
                    textoInicio,
                    textoModificar,
                    colorFondoBoton,
                    colorTextoBoton,
                    OrigenModel.transformList(listaOrigenes)
                )
            }
        }

    }
}

@Parcelize
data class OrigenModel(
    var codigo: String?,
    var valor: String?
) : Parcelable {

    companion object {
        fun transformList(list: List<Origen?>?): List<OrigenModel?>?{
            return mutableListOf<OrigenModel>().apply {
                list?.forEach {
                    it?.let { it1 -> transform(it1) }?.let { it2 -> add(it2) }
                }
            }.toList()
        }

        fun transform(input: Origen): OrigenModel {
            input.run {
                return OrigenModel(
                    codigo,
                    valor
                )
            }
        }
    }

}

