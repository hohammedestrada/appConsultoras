package biz.belcorp.consultoras.common.model.orders

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.OfertaFinal
import biz.belcorp.consultoras.domain.entity.OfertaFinalHeader
import biz.belcorp.consultoras.domain.entity.OfertaFinalResponse
import java.math.BigDecimal
import javax.inject.Inject

@PerActivity
class OfertaFinalModelDataMapper @Inject
internal constructor() {

    fun transform(input: OfertaFinal?): OfertaFinalModel? {
        return input?.let {
            OfertaFinalModel(it.cuv, it.nombreComercial, it.nombreComercialCorto,
                it.precioCatalogo, it.precioValorizado, it.marcaID, it.nombreMarca,
                it.fotoProducto, it.fotoProductoSmall, it.fotoProductoMedium,
                it.tipoMeta, it.indicadorMontoMinimo, it.tipoEstrategiaID,
                it.tipoOfertaSisID, it.configuracionOfertaID, it.cantidad ?: 1, it.added )
        }
    }

    fun transform(input: OfertaFinalModel?): OfertaFinal? {
        return input?.let {
            OfertaFinal().apply{
                cuv = it.cuv
                nombreComercial = it.nombreComercial
                nombreComercialCorto = it.nombreComercialCorto
                precioCatalogo = it.precioCatalogo
                precioValorizado = it.precioValorizado
                marcaID = it.marcaID
                nombreMarca = it.nombreMarca
                fotoProducto = it.fotoProducto
                fotoProductoSmall = it.fotoProductoSmall
                fotoProductoMedium = it.fotoProductoMedium
                tipoMeta = it.tipoMeta
                indicadorMontoMinimo = it.indicadorMontoMinimo
                tipoEstrategiaID = it.tipoEstrategiaID
                tipoOfertaSisID = it.tipoOfertaSisID
                configuracionOfertaID = it.configuracionOfertaID
                cantidad = it.cantidad
                added = it.added
            }
        }
    }

    fun transform(input: Collection<OfertaFinal?>?): List<OfertaFinalModel?>? {
        return input?.let {
            it
                .map { transform(it) }
                .filter { null != it }
        } ?: run {
            emptyList<OfertaFinalModel>()
        }
    }

    fun transform(input: List<OfertaFinalModel?>?): Collection<OfertaFinal?>? {
        return input?.let {
            it
                .map { transform(it) }
                .filter { null != it }
        } ?: run {
            emptyList<OfertaFinal>()
        }
    }

    /** Oferta Final Header */

    fun transform(input: OfertaFinalHeader?): OfertaFinalHeaderModel? {
        return input?.let {
            OfertaFinalHeaderModel(it.tipoMeta, it.montoMeta,
                it.porcentajeMeta ?: BigDecimal(0),
                it.descripcionRegalo,it.mensajeTipingPoint, it.faltanteTipingPoint, it.cuvRegalo)

        }
    }

    /** Oferta Final Response*/

    fun transform(input: OfertaFinalResponse?): OfertaFinalResponseModel? {
        return input?.let {
            OfertaFinalResponseModel(
                transform(it.ofertaFinalHeader),
                transform(it.productosOfertaFinal))
        }
    }

}
