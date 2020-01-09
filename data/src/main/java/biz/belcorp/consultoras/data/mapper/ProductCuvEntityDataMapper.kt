package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.*
import biz.belcorp.consultoras.domain.entity.ProductCUV
import biz.belcorp.consultoras.domain.entity.SearchOrderByResponse
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ProductCuvEntityDataMapper @Inject
internal constructor() {

    fun transform(input: ProductCuvEntity?): ProductCUV? {
        return input?.let {
            ProductCUV().apply {
                id = it.id
                cuv = it.cuv
                sap = it.sap
                description = it.description
                descripcionCategoria = it.descripcionCategoria
                marcaId = it.marcaId
                descripcionMarca = it.descripcionMarca
                precioValorizado = it.precioValorizado
                precioCatalogo = it.precioCatalogo
                fotoProducto = it.fotoProducto
                fotoProductoSmall = it.fotoProductoSmall
                fotoProductoMedium = it.fotoProductoMedium
                cuvRevista = it.cuvRevista
                cuvComplemento = it.cuvComplemento
                estrategiaId = it.estrategiaId
                tipoEstrategiaId = it.tipoEstrategiaId
                tipoOfertaSisId = it.tipoOfertaSisId
                configuracionOfertaId = it.configuracionOfertaId
                flagNueva = it.flagNueva
                indicadorMontoMinimo = it.indicadorMontoMinimo
                clienteId = it.clienteId
                clienteLocalId = it.clienteLocalId
                isSugerido = it.isSugerido ?: false
                isMaterialGanancia = it.isMaterialGanancia ?: false
                tipoPersonalizacion = it.tipoPersonalizacion
                codigoEstrategia = it.codigoEstrategia
                codigoTipoEstrategia = it.codigoTipoEstrategia
                limiteVenta = it.limiteVenta
                stock = it.isStock
                descripcionEstrategia = it.descripcionEstrategia
                origenPedidoWeb = it.origenPedidoWeb
                origenPedidoWebFicha = it.origenPedidoWebFicha
                permiteAgregarPedido = it.isPermiteAgregarPedido
                tieneOfertasRelacionadas = it.isTieneOfertasRelacionadas
                codigoProducto = it.codigoProducto
                codigoTipoOferta = it.codigoTipoOferta
                flagPromocion = it.flagPromocion
                flagFestival = it.flagFestival
                reemplazarFestival = it.reemplazarFestival
                origenesPedidoWeb = it.origenesPedidoWeb?.let { it1 -> OrigenEntity.transformList(it1) }
            }
        }
    }

    fun transform(input: ProductCUV?): ProductCuvEntity? {
        return transformToEntity(input)
    }

    fun transform(input: SearchOrderByResponseEntity?): SearchOrderByResponse? {
        return input?.let {
            SearchOrderByResponse().apply {
                tablaLogicaDatosID = it.tablaLogicaDatosID
                tablaLogicaID = it.tablaLogicaID
                descripcion = it.descripcion
                codigo = it.codigo
                valor = it.valor
            }
        }
    }

    fun transform(input: List<ProductCuvEntity?>?): Collection<ProductCUV?>? {
        return input?.asSequence()?.map { it1 -> transform(it1) }?.filter { it1 -> null != it1 }?.toList()
            ?: run {
            emptyList<ProductCUV>()
        }
    }

    fun transformOrderBy(input: List<SearchOrderByResponseEntity?>?): Collection<SearchOrderByResponse?>? {
        return input?.asSequence()?.map { it1 -> transform(it1) }?.filter { it1 -> null != it1 }?.toList()
            ?: run {
            emptyList<SearchOrderByResponse>()
        }
    }

    companion object {

        fun transformToEntity(input: ProductCUV?): ProductCuvEntity? {
            return input?.let {
                ProductCuvEntity().apply {
                    id = it.id
                    cuv = it.cuv
                    sap = it.sap
                    description = it.description
                    descripcionCategoria = it.descripcionCategoria
                    marcaId = it.marcaId
                    descripcionMarca = it.descripcionMarca
                    precioValorizado = it.precioValorizado
                    precioCatalogo = it.precioCatalogo
                    fotoProducto = it.fotoProducto
                    fotoProductoSmall = it.fotoProductoSmall
                    fotoProductoMedium = it.fotoProductoMedium
                    cuvRevista = it.cuvRevista
                    cuvComplemento = it.cuvComplemento
                    estrategiaId = it.estrategiaId
                    tipoEstrategiaId = it.tipoEstrategiaId
                    tipoOfertaSisId = it.tipoOfertaSisId
                    configuracionOfertaId = it.configuracionOfertaId
                    flagNueva = it.flagNueva
                    indicadorMontoMinimo = it.indicadorMontoMinimo
                    clienteId = it.clienteId
                    clienteLocalId = it.clienteLocalId
                    isSugerido = it.isSugerido

                    tipoPersonalizacion = it.tipoPersonalizacion
                    codigoEstrategia = it.codigoEstrategia
                    codigoTipoEstrategia = it.codigoTipoEstrategia
                    limiteVenta = it.limiteVenta
                    isStock = it.stock
                    descripcionEstrategia = it.descripcionEstrategia
                    origenPedidoWeb = it.origenPedidoWeb
                    origenPedidoWebFicha = it.origenPedidoWebFicha
                    isPermiteAgregarPedido = it.permiteAgregarPedido
                    isTieneOfertasRelacionadas = it.tieneOfertasRelacionadas
                    codigoProducto = it.codigoProducto
                    flagFestival = it.flagFestival
                    reemplazarFestival = it.reemplazarFestival
                    flagPromocion = it.flagPromocion
                    origenesPedidoWeb = it.origenesPedidoWeb?.let { it1 -> OrigenEntity.transformToListEntity(it1) }
                    listaOpciones = it.listaOpciones?.let { it1 -> ProductCUVOpcionEntity.transformToListEntity(it1) }
                }
            }
        }

    }

}
