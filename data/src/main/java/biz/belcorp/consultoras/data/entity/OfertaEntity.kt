package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.data.mapper.OrderEntityDataMapper
import biz.belcorp.consultoras.domain.entity.*
import com.google.gson.annotations.SerializedName

data class OfertaEntity(
    @SerializedName("CUV") var cuv: String?,
    @SerializedName("NombreOferta") var nombreOferta: String?,
    @SerializedName("MarcaID") var marcaID: Int?,
    @SerializedName("NombreMarca") var nombreMarca: String?,
    @SerializedName("PrecioCatalogo") var precioCatalogo: Double?,
    @SerializedName("PrecioValorizado") var precioValorizado: Double?,
    @SerializedName("Ganancia") var ganancia: Double?,
    @SerializedName("ImagenURL") var imagenURL: String?,
    @SerializedName("BannerOferta") var bannerOferta: String?,
    @SerializedName("TipoOferta") var tipoOferta: String?,
    @SerializedName("OrigenPedidoWeb") var origenPedidoWeb: Int?,
    @SerializedName("TipoEstrategiaID") var tipoEstrategiaID: String?,
    @SerializedName("CodigoEstrategia") var codigoEstrategia: String?,
    @SerializedName("EstrategiaID") var estrategiaID: Int?,
    @SerializedName("IndicadorMontoMinimo") var indicadorMontoMinimo: Int?,
    @SerializedName("LimiteVenta") var limiteVenta: Int?,
    @SerializedName("TipoEstrategiaImagenMostrar") var tipoEstrategiaImagenMostrar: Int?,
    @SerializedName("FlagEligeOpcion") var flagEligeOpcion: Boolean?,
    @SerializedName("FlagNueva") var flagNueva: Int?,
    @SerializedName("EsSubCampania") var esSubCampania: Boolean?,
    @SerializedName("FlagIndividual") var flagIndividual: Boolean?,
    @SerializedName("CodigoProducto") var codigoProducto: String?,
    @SerializedName("Agotado") var agotado: Boolean?,
    //@SerializedName("TipoOferta") var tipoOferta: String?,
    @SerializedName("Pum") var pum: String?,
    @SerializedName("ConfiguracionOferta") var ofertaConfiguracion: OfertaConfiguracionEntity?,
    @SerializedName("FichaProductoConfiguracion") var fichaProductoConfiguracion: FichaProductoConfiguracionEntity?,
    @SerializedName("OfertaNiveles") var ofertaNiveles: List<OfertaNivelEntity?>?,
    @SerializedName("Componentes") var componentes: List<ComponenteEntity?>?,
    @SerializedName("OpcionesAgregadas") var opcionesAgregadas: List<OpcionesAgregadasEntity?>?,
    @SerializedName("FlagTieneOfertas") var flagTieneOfertas: Boolean,
    @SerializedName("FlagCantidad") var flagCantidad: Boolean,
    @SerializedName("FlagAgregar") var flagAgregar: Boolean,
    @SerializedName("FlagAgregado") var flagAgregado: Boolean,
    @SerializedName("FlagFestival") var flagFestival: Boolean,
    @SerializedName("CodigoTipoOferta") var codigoTipoOferta: String?,
    @SerializedName("Pedido") var pedido: PedidoGetResponseEntity?,
    @SerializedName("CuvPromocion") var cuvPromocion: String?,

    @SerializedName("FlagBonificacion") var flagBonificacion: Boolean?
) {

    companion object {

        fun transformList(list: List<OfertaEntity?>?): List<Oferta?>? {
            return mutableListOf<Oferta>().apply {
                list?.forEach {
                    it?.let { it1 -> transform(it1) }?.let { it2 -> add(it2) }
                }
            }.toList()
        }

        fun transform(input: OfertaEntity?): Oferta? {
            input?.run {
                val oferta = Oferta(
                    cuv,
                    nombreOferta,
                    marcaID,
                    nombreMarca,
                    precioCatalogo,
                    precioValorizado,
                    ganancia,
                    imagenURL,
                    bannerOferta,
                    origenPedidoWeb,
                    tipoEstrategiaID,
                    codigoEstrategia,
                    estrategiaID,
                    indicadorMontoMinimo,
                    limiteVenta,
                    tipoEstrategiaImagenMostrar,
                    flagEligeOpcion,
                    flagNueva,
                    esSubCampania,
                    flagIndividual,
                    codigoProducto,
                    agotado,
                    pum,
                    OfertaConfiguracion(ofertaConfiguracion?.imgFondoApp,
                        ofertaConfiguracion?.colorTextoApp),
                    FichaProductoConfiguracion(fichaProductoConfiguracion?.tieneNiveles,
                        fichaProductoConfiguracion?.tipoVisualizacion, fichaProductoConfiguracion?.tieneCompartir,
                        fichaProductoConfiguracion?.tieneCarruselUpSelling, fichaProductoConfiguracion?.tieneCarruselCrossSelling,
                        fichaProductoConfiguracion?.tieneCarruselSugeridos, fichaProductoConfiguracion?.tienePromocion),
                    OfertaNivelEntity.transformList(ofertaNiveles),
                    ComponenteEntity.transformList(componentes),
                    OpcionesAgregadasEntity.transformList(opcionesAgregadas),
                    tipoOferta ?: "", // El valor se setea desde la configuracion
                    flagTieneOfertas,
                    flagCantidad,
                    flagAgregar,
                    flagAgregado,
                    codigoTipoOferta = codigoTipoOferta,
                    cuvPromocion = cuvPromocion
                )
                oferta.flagFestival = input.flagFestival
                oferta.pedido = OrderEntityDataMapper.transformFormatted(input.pedido)

                //INI ABT GAB-11
                oferta.flagBonificacion = input.flagBonificacion ?: false
                //END ABT GAB-11

                return oferta
            }
            return null
        }
    }
}

data class OfertaConfiguracionEntity(
    @SerializedName("ImgFondoApp") var imgFondoApp: String?,
    @SerializedName("ColorTextoApp") var colorTextoApp: String?
)

data class FichaProductoConfiguracionEntity(
    @SerializedName("TieneNiveles") var tieneNiveles: Boolean?,
    @SerializedName("TipoVisualizacion") var tipoVisualizacion: Int?,
    @SerializedName("TieneCompartir") var tieneCompartir: Boolean?,
    @SerializedName("TieneCarruselUpSelling") var tieneCarruselUpSelling: Boolean?,
    @SerializedName("TieneCarruselCrossSelling") var tieneCarruselCrossSelling: Boolean?,
    @SerializedName("TieneCarruselSugeridos") var tieneCarruselSugeridos: Boolean?,
    @SerializedName("TienePromocion") var tienePromocion: Boolean?

)

data class OfertaNivelEntity(
    @SerializedName("Cantidad") var cantidad: Int?,
    @SerializedName("Precio") var precio: Double?
) {
    companion object {

        fun transformList(list: List<OfertaNivelEntity?>?): List<OfertaNivel?>? {
            return mutableListOf<OfertaNivel>().apply {
                list?.forEach {
                    it?.let { it1 -> transform(it1) }?.let { it2 -> add(it2) }
                }
            }.toList()
        }

        fun transform(input: OfertaNivelEntity?): OfertaNivel? {
            input?.run {
                return OfertaNivel(
                    cantidad,
                    precio
                )
            }
            return null
        }
    }
}

data class ComponenteEntity(
    @SerializedName("Grupo") var grupo: Int?,
    @SerializedName("MarcaId") var marcaID: Int?,
    @SerializedName("NombreMarca") var nombreMarca: String?,
    @SerializedName("IndicadorDigitable") var indicadorDigitable: Boolean?,
    @SerializedName("FactorRepeticion") var factorRepeticion: Int?,
    @SerializedName("FactorCuadre") var factorCuadre: Int?,
    @SerializedName("NombreComercial") var nombreComercial: String?,
    @SerializedName("Agotado") var agotado: Boolean?,
    @SerializedName("DescripcionPlural") var descripcionPlural: String?,
    @SerializedName("DescripcionSingular") var descripcionSingular: String?,
    @SerializedName("Opciones") var opciones: List<OpcionesEntity>?,
    @SerializedName("CodigoProducto") var codigoProducto: String?,
    @SerializedName("ImagenURL") var imagenURL: String?,
    @SerializedName("ListaImagenURL") var listaImagenURL: List<String?>?,
    @SerializedName("PrecioUnitario") var precioUnitario: Double?,
    @SerializedName("Pum") var pum: String?,
    @SerializedName("Especificaciones") var especificaciones: List<String?>?,
    @SerializedName("Secciones") var secciones: List<SeccionesEntity?>?


) {
    companion object {

        fun transformList(list: List<ComponenteEntity?>?): List<Componente?>? {
            return mutableListOf<Componente>().apply {
                list?.forEach {
                    it?.let { it1 -> transform(it1) }?.let { it2 -> add(it2) }
                }
            }.toList()
        }

        fun transform(input: ComponenteEntity?): Componente? {
            input?.run {
                return Componente(
                    grupo,
                    marcaID,
                    nombreMarca,
                    indicadorDigitable,
                    factorRepeticion,
                    factorCuadre,
                    nombreComercial,
                    agotado,
                    descripcionPlural,
                    descripcionSingular,
                    OpcionesEntity.transformList(opciones),
                    codigoProducto,
                    imagenURL,
                    listaImagenURL,
                    precioUnitario,
                    pum,
                    especificaciones,
                    SeccionesEntity.transformList(secciones)

                )
            }
            return null
        }
    }
}

data class OpcionesEntity(
    @SerializedName("CUV") var cuv: String?,
    @SerializedName("CodigoEstrategia") var codigoEstrategia: Int?,
    @SerializedName("CodigoProducto") var codigoProducto: String?,
    @SerializedName("EstrategiaId") var estrategiaId: Int?,
    @SerializedName("NombreOferta") var nombreOferta: String?,
    @SerializedName("Volumen") var volumen: String?,
    @SerializedName("MarcaId") var marcaID: Int?,
    @SerializedName("ImagenURL") var imagenURL: String?,
    @SerializedName("NombreOpcion") var nombreOpcion: String?,
    @SerializedName("Agotado") var agotado: Boolean?,
    @SerializedName("PrecioCatalogo") var precioCatalogo: Double?,
    @SerializedName("PrecioValorizado") var precioValorizado: Double?
) {
    companion object {

        fun transformList(list: List<OpcionesEntity?>?): List<Opciones?>? {
            return mutableListOf<Opciones>().apply {
                list?.forEach {
                    it?.let { it1 -> transform(it1) }?.let { it2 -> add(it2) }
                }
            }.toList()
        }

        fun transform(input: OpcionesEntity?): Opciones? {
            input?.run {
                return Opciones(
                    cuv,
                    codigoEstrategia,
                    codigoProducto,
                    estrategiaId,
                    nombreOferta,
                    volumen,
                    marcaID,
                    imagenURL,
                    nombreOpcion,
                    agotado,
                    precioCatalogo,
                    precioValorizado
                )
            }
            return null
        }
    }
}

data class OpcionesAgregadasEntity(
    @SerializedName("CUV") var cuv: String?,
    @SerializedName("Cantidad") var cantidad: Int?,
    @SerializedName("Grupo") var grupo: String?
) {
    companion object {

        fun transformList(list: List<OpcionesAgregadasEntity?>?): List<OpcionesAgregadas?>? {
            return mutableListOf<OpcionesAgregadas>().apply {
                list?.forEach {
                    it?.let { it1 -> transform(it1) }?.let { it2 -> add(it2) }
                }
            }.toList()
        }

        fun transform(input: OpcionesAgregadasEntity?): OpcionesAgregadas? {
            input?.run {
                return OpcionesAgregadas(
                    cuv,
                    cantidad,
                    grupo
                )
            }
            return null
        }
    }
}

data class SeccionesEntity(
    @SerializedName("Tipo") var tipo: Int?,
    @SerializedName("Titulo") var titulo: String?,
    @SerializedName("Detalles") var detalles: List<DetalleEntity>?
) {
    companion object {

        fun transformList(list: List<SeccionesEntity?>?): List<Secciones?>? {
            return mutableListOf<Secciones>().apply {
                list?.forEach {
                    it?.let { it1 -> transform(it1) }?.let { it2 -> add(it2) }
                }
            }.toList()
        }

        fun transform(input: SeccionesEntity?): Secciones? {
            input?.run {
                return Secciones(
                    tipo,
                    titulo,
                    DetalleEntity.transformList(detalles)
                )
            }
            return null
        }
    }
}

data class DetalleEntity(
    @SerializedName("Titulo") var titulo: String?,
    @SerializedName("Descripcion") var descripcion: String?,
    @SerializedName("Key") var key: String?
) {
    companion object {

        fun transformList(list: List<DetalleEntity?>?): List<Detalle?>? {
            return mutableListOf<Detalle>().apply {
                list?.forEach {
                    it?.let { it1 -> transform(it1) }?.let { it2 -> add(it2) }
                }
            }.toList()
        }

        fun transform(input: DetalleEntity?): Detalle? {
            input?.run {
                return Detalle(
                    titulo,
                    descripcion,
                    key
                )
            }
            return null
        }
    }
}
