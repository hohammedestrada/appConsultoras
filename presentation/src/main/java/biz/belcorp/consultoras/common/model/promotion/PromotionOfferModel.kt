package biz.belcorp.consultoras.common.model.promotion

import android.os.Parcelable
import biz.belcorp.consultoras.domain.entity.EstrategiaCarrusel
import biz.belcorp.consultoras.domain.entity.Oferta
import biz.belcorp.consultoras.domain.entity.PromotionDetail
import biz.belcorp.consultoras.domain.entity.PromotionResponse
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PromotionOfferModel(
    var observacion: String? = null,
    var producto: PromotionDetailModel? = null,
    var listaApoyo: List<PromotionDetailModel?>? = emptyList()
) : Parcelable {

    companion object {
        fun transform(input: PromotionResponse): PromotionOfferModel {
            input.run {
                return PromotionOfferModel(
                    observacion,
                    PromotionDetailModel.transform(producto),
                    PromotionDetailModel.transformList(listaApoyo)
                )
            }
        }
    }

}

@Parcelize
data class PromotionDetailModel(
    var estrategiaId: Int?,
    var codigoEstrategia: String?,
    var activo: Boolean?,
    var cuv: String?,
    var descripcion: String?,
    var imagenURL: String?,
    var glagImagenURL: Boolean?,
    var limiteVenta: Int?,
    var textoLibre: String?,
    var orden: Int?,
    var flagConfig: Boolean?,
    var tipoEstrategiaId: String?,
    var tipoPersonalizacion: String?,
    var descripcionTipoEstrategia: String?,
    var codigoTipoEstrategia: String?,
    var flagActivo: Int?,
    var flagRecoPerfil: Int?,
    var marcaId: Int?,
    var marcaDescripcion: String?,
    var codigoProducto: String?,
    var indicadorMontoMinimo: Boolean?,
    var codigoTipoOferta: String?,
    var tipoEstrategiaImagenMostrar: Int?,
    var flagRevista: Int?,
    var precioTachado: Double?,
    var precioVenta: Double?,
    var gananciaString: Double?,
    var tieneStock: Boolean?,
    var descripcionCortada: String?,
    var descripcionCompleta: String?,
    var codigoCatalogo: String?,
    var factorCuadre: String?
) : Parcelable {

    var flagPromocion: Boolean? = false

    companion object {

        fun transformList(list: List<PromotionDetail?>?): List<PromotionDetailModel?>? {
            return mutableListOf<PromotionDetailModel>().apply {
                list?.forEach {
                    it?.let { it1 -> transform(it1) }?.let { it2 -> add(it2) }
                }
            }.toList()
        }

        fun transform(input: PromotionDetail?): PromotionDetailModel? {
            return input?.let {
                PromotionDetailModel(
                    it.estrategiaId,
                    it.codigoEstrategia,
                    it.activo,
                    it.cuv,
                    it.descripcion,
                    it.imagenURL,
                    it.glagImagenURL,
                    it.limiteVenta,
                    it.textoLibre,
                    it.orden,
                    it.flagConfig,
                    it.tipoEstrategiaId,
                    it.tipoPersonalizacion,
                    it.descripcionTipoEstrategia,
                    it.codigoTipoEstrategia,
                    it.flagActivo,
                    it.flagRecoPerfil,
                    it.marcaId,
                    it.marcaDescripcion,
                    it.codigoProducto,
                    it.indicadorMontoMinimo,
                    it.codigoTipoOferta,
                    it.tipoEstrategiaImagenMostrar,
                    it.flagRevista,
                    it.precioTachado,
                    it.precioVenta,
                    it.gananciaString,
                    it.tieneStock,
                    it.descripcionCortada,
                    it.descripcionCompleta,
                    it.codigoCatalogo,
                    it.factorCuadre
                )
            }
        }

        fun transforToOffer(promotionDetail: PromotionDetailModel?): Oferta? {
            return promotionDetail?.let {
                Oferta(
                    cuv = it.cuv,
                    nombreOferta = it.descripcion,
                    marcaID = it.marcaId,
                    nombreMarca = it.marcaDescripcion,
                    precioCatalogo = it.precioVenta,
                    precioValorizado = it.precioTachado,
                    ganancia = it.gananciaString,
                    imagenURL = it.imagenURL,
                    bannerOferta = null,
                    origenPedidoWeb = null,
                    tipoEstrategiaID = it.tipoEstrategiaId,
                    codigoEstrategia = it.codigoEstrategia,
                    estrategiaID = it.estrategiaId,
                    indicadorMontoMinimo = 0, // it.indicadorMontoMinimo,
                    limiteVenta = it.limiteVenta,
                    tipoEstrategiaImagenMostrar = null,
                    flagEligeOpcion = null,
                    flagNueva = null,
                    esSubCampania = null,
                    flagIndividual = null,
                    codigoProducto = null,
                    agotado = null,
                    pum = null,
                    configuracionOferta = null,
                    fichaProductoConfiguracion = null,
                    ofertaNiveles = null,

                    componentes = null,

                    opcionesAgregadas = null,
                    tipoOferta = it.tipoPersonalizacion,

                    cuvPromocion = null
                )
            }
        }

        fun transforToOfferEC(promotionDetail: PromotionDetailModel?): EstrategiaCarrusel? {
            return promotionDetail?.let {
                EstrategiaCarrusel().apply {

                    cuv = it.cuv
                    descripcionCUV = it.descripcion
                    descripcionMarca = it.marcaDescripcion
                    marcaID = it.marcaId
                    precioFinal = it.precioVenta?.toBigDecimal()
                    precioValorizado = it.precioTachado?.toBigDecimal()
                    fotoProductoMedium = it.imagenURL
                    fotoProductoSmall = it.imagenURL
                    tipoEstrategiaID = it.tipoEstrategiaId
                    codigoEstrategia = it.codigoEstrategia
                    estrategiaID = it.estrategiaId
                    indicadorMontoMinimo = 0
                    tipoPersonalizacion = it.tipoPersonalizacion
                    flagPromocion = it.flagPromocion

                }

            }
        }

    }

}
