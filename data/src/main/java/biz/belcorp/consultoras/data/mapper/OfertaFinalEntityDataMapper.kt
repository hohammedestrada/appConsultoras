package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.OfertaFinalEntity
import biz.belcorp.consultoras.data.entity.OfertaFinalHeaderEntity
import biz.belcorp.consultoras.data.entity.OfertaFinalResponseEntity
import biz.belcorp.consultoras.domain.entity.OfertaFinal
import biz.belcorp.consultoras.domain.entity.OfertaFinalHeader
import biz.belcorp.consultoras.domain.entity.OfertaFinalResponse
import biz.belcorp.consultoras.domain.entity.BasicDto
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.net.dto.ServiceDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class OfertaFinalEntityDataMapper @Inject
constructor()// EMPTY
{
    fun transform(input: OfertaFinalEntity?): OfertaFinal? {
        return input?.let {
            OfertaFinal().apply {
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
            }
        }
    }

    fun transform(input: OfertaFinal?): OfertaFinalEntity? {
        return input?.let {
            OfertaFinalEntity().apply {
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
            }
        }
    }

    /**
     * Transforma una lista de entidades a una lista de entidades de dominio
     *
     * @param list Lista de entidades
     * @return Lista de entidades del dominio
     */
    fun transform(list: Collection<OfertaFinalEntity?>?): List<OfertaFinal?>? {
        return list?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<OfertaFinal>()
        }
    }

    fun transformResponse(input: ServiceDto<OfertaFinalResponseEntity>?): BasicDto<OfertaFinalResponse>? {
        return input?.let {
            BasicDto<OfertaFinalResponse>().apply {
                code = it.code
                val aux: OfertaFinalResponseEntity
                val json = Gson().toJson(it.data)
                val type = object : TypeToken<OfertaFinalResponseEntity>() {}.type
                try {
                    aux = Gson().fromJson(json, type)
                    data = transform(aux)
                } catch (e: Exception) {
                    BelcorpLogger.d("Lista de Oferta Fina: Data de respuesta de pedido nula")
                }
                message = it.message
            }
        }
    }

    fun transform(input: OfertaFinalHeaderEntity?): OfertaFinalHeader? {
        return input?.let {
            OfertaFinalHeader().apply {
                tipoMeta = it.tipoMeta
                montoMeta = it.montoMeta
                porcentajeMeta = it.porcentajeMeta
                descripcionRegalo = it.descripcionRegalo
                mensajeTipingPoint = it.mensajeTipingPoint
                faltanteTipingPoint = it.faltanteTipingPoint
                cuvRegalo = it.cuvRegalo

            }
        }
    }

    fun transform(input: OfertaFinalHeader?): OfertaFinalHeaderEntity? {
        return input?.let {
            OfertaFinalHeaderEntity().apply {
                tipoMeta = it.tipoMeta
                montoMeta = it.montoMeta
                porcentajeMeta = it.porcentajeMeta
                descripcionRegalo = it.descripcionRegalo
                mensajeTipingPoint = it.mensajeTipingPoint
                faltanteTipingPoint = it.faltanteTipingPoint
                cuvRegalo = it.cuvRegalo
            }
        }
    }

    fun transform(input: OfertaFinalResponseEntity?): OfertaFinalResponse? {
        return input?.let {
            OfertaFinalResponse().apply {
                ofertaFinalHeader = transform(it.ofertaFinalHeader)
                productosOfertaFinal = transform(it.productosOfertaFinal)
            }
        }
    }
}
