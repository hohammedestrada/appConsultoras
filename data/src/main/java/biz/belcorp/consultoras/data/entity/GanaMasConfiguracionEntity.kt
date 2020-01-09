package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.domain.entity.ConfiguracionPorPalanca
import biz.belcorp.consultoras.domain.entity.Origen
import biz.belcorp.consultoras.domain.entity.SubCampaniaConfiguracion
import biz.belcorp.consultoras.domain.util.OfferTypes
import com.google.gson.annotations.SerializedName


data class GanaMasConfiguracionEntity(
    @SerializedName("OfertaConfiguracion") var ofertaConfiguracion: List<ConfiguracionPorPalancaEntity?>?,
    @SerializedName("ListadoOrigenesPedidoWeb") var listadoOrigenesPedidoWeb: List<ListadoOrigenesPedidoWebEntity?>?
){
    companion object {
        fun transform(input: GanaMasConfiguracionEntity?): List<ConfiguracionPorPalanca?>? {
            input.run {
                return ConfiguracionPorPalancaEntity.transformList(input)
            }
        }
    }
}


data class subCampaniaConfiguracion(
    @SerializedName("TieneSubCampania") var tieneSubCampania: Boolean?,
    @SerializedName("BannerTextoTituloSubCampania") var bannerTextoTitulo: String?,
    @SerializedName("BannerColorTextoTituloSubCampania") var bannerColorTextoTitulo: String?,
    @SerializedName("BannerColorFondoTituloSubCampania") var bannerFondoTextoTitulo: String?,
    @SerializedName("BannerTextoSubTituloSubCampania") var bannerTextoSubtitulo: String?,
    @SerializedName("BannerColorTextoSubTituloSubCampania") var bannerColorTextoSubTitulo: String?,
    @SerializedName("BannerImagenFondoSubCampania") var bannerImagenFondo: String?
)

data class ConfiguracionPorPalancaEntity(
    @SerializedName("TipoOferta") var tipoOferta: String?,
    @SerializedName("FlagActivo") var flagActivo: Boolean?,
    @SerializedName("Titulo") var titulo: String?,
    @SerializedName("SubTitulo") var subTitulo: String?,
    @SerializedName("ColorTexto") var colorTexto: String?,
    @SerializedName("ColorFondo") var colorFondo: String?,
    @SerializedName("Orden") var orden: Int?,
    @SerializedName("CantidadMostrarCarrusel") var cantidadMostrarCarrusel: Int?,
    @SerializedName("BannerOferta") var bannerOferta: String?,
    @SerializedName("TieneEvento") var tieneEvento: Boolean?,
    @SerializedName("TieneCompartir") var tieneCompartir: Boolean?,
    @SerializedName("TextoInicio") var textoInicio: String?,
    @SerializedName("TextoModificar") var textoModificar: String?,
    @SerializedName("ColorFondoBoton") var colorFondoBoton: String?,
    @SerializedName("ColorTextoBoton") var colorTextoBoton: String?,
    @SerializedName("SubCampaniaConfiguracion") var subCampaniaConfiguracion: subCampaniaConfiguracion?
) {

    companion object {
        fun transformList(input: GanaMasConfiguracionEntity?): List<ConfiguracionPorPalanca?>? {
            val listConfigXPalanca =  mutableListOf<ConfiguracionPorPalanca>().apply {
                input?.ofertaConfiguracion?.forEach outer@ {ofertaConfig ->
                    ofertaConfig?.let { ofertaConfig -> transform(ofertaConfig) }?.let {configXPalanca ->

                        when (configXPalanca.tipoOferta) {
                            OfferTypes.PN, OfferTypes.DP, OfferTypes.OPT -> add(configXPalanca)
                        }

                        input.listadoOrigenesPedidoWeb?.forEach {

                            if(it?.tipoOferta == configXPalanca.tipoOferta){
                                configXPalanca.apply {
                                    listaOrigenes = OrigenEntity.transformList(it?.listaOrigenPedidoWeb)
                                }?.let { add(configXPalanca) }
                                return@outer
                            }
                        }}
                }
            }.toList()
            return listConfigXPalanca
        }

        fun transform(input: ConfiguracionPorPalancaEntity): ConfiguracionPorPalanca {
            input.run {
                return ConfiguracionPorPalanca(
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
                    null,
                    subCampaniaConfiguracion?.let { transform(it) }
                )
            }
        }


        fun transform(input:subCampaniaConfiguracion): SubCampaniaConfiguracion {
            input.run {
                return SubCampaniaConfiguracion(
                    tieneSubCampania,
                    bannerTextoTitulo,
                    bannerColorTextoTitulo,
                    bannerFondoTextoTitulo,
                    bannerTextoSubtitulo,
                    bannerColorTextoSubTitulo,
                    bannerImagenFondo
                )
            }
        }


    }

}

data class ListadoOrigenesPedidoWebEntity(
    @SerializedName("TipoOferta") var tipoOferta: String?,
    @SerializedName("ListaOrigenPedidoWeb") var listaOrigenPedidoWeb: List<OrigenEntity?>?
)




data class OrigenEntity(
    @SerializedName("Codigo") var codigo: String?,
    @SerializedName("Valor")  var valor: String?
){
    companion object {

        fun transformList(list: List<OrigenEntity?>?): List<Origen?>?{
            return mutableListOf<Origen>().apply {
                list?.forEach {
                    it?.let { it1 -> transform(it1) }?.let { it2 -> add(it2) }
                }
            }.toList()
        }

        fun transform(input: OrigenEntity): Origen{
            input.run {
                return Origen(codigo, valor)
            }
        }

        fun transform(input: Origen?): OrigenEntity{
            return OrigenEntity(input?.codigo, input?.valor)
        }

        fun transformToListEntity(inputList: List<Origen?>): List<OrigenEntity>{
            return mutableListOf<OrigenEntity>().apply {
                inputList.forEach {
                    add(transform(it))
                }
            }
        }

    }
}

