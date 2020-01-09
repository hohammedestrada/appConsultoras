package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.domain.entity.BasicDto
import biz.belcorp.consultoras.domain.entity.PromotionDetail
import biz.belcorp.consultoras.domain.entity.PromotionResponse
import biz.belcorp.library.net.dto.ServiceDto
import com.google.gson.annotations.SerializedName

data class PromotionResponseEntity(
    @SerializedName("Observacion") var observacion: String?,
    @SerializedName("Producto") var producto: PromotionDetailEntity?,
    @SerializedName("ListaApoyo") var listaApoyo: List<PromotionDetailEntity?>?
){
    companion object {

        fun transform(input: PromotionResponseEntity?): PromotionResponse?{
            input?.run {
                return PromotionResponse(
                    observacion,
                    PromotionDetailEntity.transform(producto),
                    PromotionDetailEntity.transformList(listaApoyo)
                )
            }
            return null
        }

        fun transformPromotion(service: ServiceDto<PromotionResponseEntity?>?): BasicDto<PromotionResponse?> {
            return BasicDto<PromotionResponse?>().apply {
                code = service?.code
                data = transform(service?.data)
                message = service?.message
            }
        }
    }
}

data class PromotionDetailEntity(
    @SerializedName("EstrategiaId") var estrategiaId: Int?,
    @SerializedName("CodigoEstrategia") var codigoEstrategia: String?,
    @SerializedName("Activo") var activo: Boolean?,
    @SerializedName("CUV") var cuv: String?,
    @SerializedName("Descripcion") var descripcion: String?,
    @SerializedName("ImagenURL") var imagenUrl: String?,
    @SerializedName("FlagImagenURL") var flagImagenUrl: Boolean?,
    @SerializedName("LimiteVenta") var limiteVenta: Int?,
    @SerializedName("TextoLibre") var textoLibre: String?,
    @SerializedName("Orden") var orden: Int?,
    @SerializedName("FlagConfig") var flagConfig: Boolean?,
    @SerializedName("TipoEstrategiaId") var tipoEstrategiaId: String?,
    @SerializedName("TipoPersonalizacion") var tipoPersonalizacion: String?,
    @SerializedName("DescripcionTipoEstrategia") var descripcionTipoEstrategia: String?,
    @SerializedName("CodigoTipoEstrategia") var codigoTipoEstrategia: String?,
    @SerializedName("FlagActivo") var flagActivo: Int?,
    @SerializedName("FlagRecoPerfil") var flagRecoPerfil: Int?,
    @SerializedName("MarcaId") var marcaId: Int?,
    @SerializedName("MarcaDescripcion") var marcaDescripcion: String?,
    @SerializedName("CodigoProducto") var codigoProducto: String?,
    @SerializedName("IndicadorMontoMinimo") var indicadorMontoMinimo: Boolean?,
    @SerializedName("CodigoTipoOferta") var codigoTipoOferta: String?,
    @SerializedName("TipoEstrategiaImagenMostrar") var tipoEstrategiaImagenMostrar: Int?,
    @SerializedName("FlagRevista") var flagRevista: Int?,
    @SerializedName("PrecioTachado") var precioTachado: Double?,
    @SerializedName("PrecioVenta") var precioVenta: Double?,
    @SerializedName("GananciaString") var gananciaString: Double?,
    @SerializedName("TieneStock") var tieneStock: Boolean?,
    @SerializedName("DescripcionCortada") var descripcionCortada: String?,
    @SerializedName("DescripcionCompleta") var descripcionCompleta: String?,
    @SerializedName("CodigoCatalogo") var codigoCatalogo: String?,
    @SerializedName("FactorCuadre") var factorCuadre: String?
){
    companion object {

        fun transformList(list: List<PromotionDetailEntity?>?): List<PromotionDetail?>?{
            return mutableListOf<PromotionDetail>().apply {
                list?.forEach {
                    it?.let { it1 -> transform(it1) }?.let { it2 -> add(it2) }
                }
            }.toList()
        }

        fun transform(input: PromotionDetailEntity?): PromotionDetail? {
            return input?.let {
                PromotionDetail(
                    it.estrategiaId,
                    it.codigoEstrategia,
                    it.activo,
                    it.cuv,
                    it.descripcion,
                    it.imagenUrl,
                    it.flagImagenUrl,
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
            return null
        }
    }
}
