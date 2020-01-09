package biz.belcorp.consultoras.domain.entity

import java.io.Serializable

data class ConfiguracionPorPalanca(
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
    var listaOrigenes: List<Origen?>? = arrayListOf(),
    var subCampaniaConfiguracion: SubCampaniaConfiguracion?
)

data class Origen(
    var codigo: String?,
    var valor: String?
) : Serializable


data class SubCampaniaConfiguracion(
    var tieneSubCampania: Boolean?,
    var bannerTextoTitulo: String?,
    var bannerColorTextoTitulo: String?,
    var bannerColorFondoTitulo: String?,
    var bannerTextoSubtitulo: String?,
    var bannerColorTextoSubTitulo: String?,
    var bannerImagenFondo: String?
) : Serializable
