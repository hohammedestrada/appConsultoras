package biz.belcorp.consultoras.domain.entity

import java.io.Serializable

data class Oferta(
    var cuv: String?,
    var nombreOferta: String?,
    var marcaID: Int?,
    var nombreMarca: String?,
    var precioCatalogo: Double?,
    var precioValorizado: Double?,
    var ganancia: Double?,
    var imagenURL: String?,
    var bannerOferta: String?,
    var origenPedidoWeb: Int?,
    var tipoEstrategiaID: String?,
    var codigoEstrategia: String?, //palanca
    var estrategiaID: Int?,
    var indicadorMontoMinimo: Int?,
    var limiteVenta: Int?,
    var tipoEstrategiaImagenMostrar: Int?,
    var flagEligeOpcion: Boolean?,
    var flagNueva: Int?,
    var esSubCampania: Boolean?,
    var flagIndividual: Boolean?,
    var codigoProducto: String?, //  codigo sap
    var agotado: Boolean?,
    var pum: String?,
    var configuracionOferta: OfertaConfiguracion?,
    var fichaProductoConfiguracion: FichaProductoConfiguracion?,
    var ofertaNiveles: List<OfertaNivel?>?,
    var componentes: List<Componente?>?,
    var opcionesAgregadas: List<OpcionesAgregadas?>?,
    var tipoOferta: String?, // Palanca a la que pertenece la oferta
    var flagTieneOfertas: Boolean = true,
    var flagCantidad: Boolean = true,
    var flagAgregar: Boolean = true,
    var flagAgregado: Boolean = false,
    var vencido: Boolean = false,
    var codigoTipoOferta: String? = null,
    var pedido: FormattedOrder? = null,
    var cuvPromocion: String?
) {
    var index: Int = 0
    var tipoPersonalizacion: String? = ""  //tipo de palanca
    var procedencia: String? = null
    var message: String = ""
    var flagFestival: Boolean = false

    // Promociones
    var flagPromocion: Boolean = false

    //INI GAB-11

    var flagBonificacion: Boolean = false

    var varianteA: Boolean = false
    var varianteB: Boolean = false
    var varianteC: Boolean = false

    //END GAB-11
}

data class OfertaConfiguracion(
    var imgFondoApp: String?,
    var colorTextoApp: String?
)

data class FichaProductoConfiguracion(
    var tieneNiveles: Boolean?,
    var tipoVisualizacion: Int?,
    var tieneCompartir: Boolean?,
    var tieneCarruselUpSelling: Boolean?,
    var tieneCarruselCrossSelling: Boolean?,
    var tieneCarruselSugeridos: Boolean?,
    var tienePromocion: Boolean?
)

data class OfertaNivel(
    var cantidad: Int?,
    var precio: Double?
)

data class Componente(
    var grupo: Int?,
    var marcaID: Int?,
    var nombreMarca: String?,
    var indicadorDigitable: Boolean?,
    var factorRepeticion: Int?,
    var factorCuadre: Int?,
    var nombreComercial: String?,
    var agotado: Boolean?,
    var descripcionPlural: String?,
    var descripcionSingular: String?,
    var opciones: List<Opciones?>?,
    var codigoProducto: String?,

    var imagenURL: String?,
    var listaImagenURL: List<String?>?,

    var precioUnitario: Double?,
    var pum: String?,
    var especificaciones: List<String?>?,
    var secciones: List<Secciones?>?,
    var indicarFaltaSeleccion: Boolean = false,
    var selected: Boolean = false
) : Serializable

data class Opciones(
    var cuv: String?,
    var codigoEstrategia: Int?,
    var codigoProducto: String?,
    var estrategiaId: Int?,
    var nombreOferta: String?,
    var volumen: String?,
    var marcaID: Int?,
    var imagenURL: String?,
    var nombreOpcion: String?,
    var agotado: Boolean?,
    var precioCatalogo: Double?,
    var precioValorizado: Double?,
    var selected: Boolean = false,
    var cantidad: Int = 0
) : Serializable, Cloneable {
    public override fun clone(): Any {
        return super.clone()
    }
}

data class OpcionesAgregadas(
    var cuv: String?,
    var cantidad: Int?,
    var grupo: String?
) : Serializable

data class Secciones(
    var tipo: Int?,
    var titulo: String?,
    var detalles: List<Detalle?>?
) : Serializable

data class Detalle(
    var titulo: String?,
    var descripcion: String?,
    var key: String?
) : Serializable
