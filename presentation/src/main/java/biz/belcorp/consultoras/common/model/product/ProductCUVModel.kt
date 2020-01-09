package biz.belcorp.consultoras.common.model.product

import android.os.Parcelable
import biz.belcorp.consultoras.domain.entity.Componente
import biz.belcorp.consultoras.domain.entity.Origen
import biz.belcorp.consultoras.domain.entity.ProductCUV
import biz.belcorp.consultoras.domain.entity.ProductCUVOpcion
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductCUVModel(
    var id: Int?,
    var cuv: String?,
    var sap: String?,
    var description: String?,
    var descripcionCategoria: String?,
    var marcaId: Int?,
    var descripcionMarca: String?,
    var precioValorizado: Double?,
    var precioCatalogo: Double?,
    var fotoProducto: String?,
    var fotoProductoSmall: String?,
    var fotoProductoMedium: String?,
    var cuvRevista: String?,
    var cuvComplemento: String?,
    var estrategiaId: Int?,
    var tipoEstrategiaId: String?,
    var tipoOfertaSisId: Int?,
    var configuracionOfertaId: Int?,
    var flagNueva: Int?,
    var indicadorMontoMinimo: Int?,
    var clienteId: Int?,
    var clienteLocalId: Int?,
    var cantidad: Int?,
    var identifier: String?,
    var isSugerido: Boolean,
    var tipoPersonalizacion: String?, //tipo de palanca
    var codigoEstrategia: Int?,
    var codigoTipoEstrategia: String?,
    var limiteVenta: Int?,
    var stock: Boolean?,
    var descripcionEstrategia: String?,
    var origenPedidoWeb: String?,
    var agregado: Boolean?,
    var origenPedidoWebFicha: String?,
    var permiteAgregarPedido: Boolean?,
    var tieneOfertasRelacionadas: Boolean?,
    var codigoProducto: String?,
    var isAdded: Boolean?,                        // Flag para validar que fue añadido desde las ofertas relacionadas
    var listaOpciones: List<ProductCUVOpcionModel>?,
    var isMaterialGanancia: Boolean?,
    var origenesPedidoWeb: List<OrigenModel?>?,
    var index: Int = 0  // Posicion en el carrusel
) : Parcelable {

    companion object {

        fun transformList(obj: ProductCUV): ProductCUVModel {

            return ProductCUVModel(
                obj.id,
                obj.cuv,
                obj.sap,
                obj.description,
                obj.descripcionCategoria,
                obj.marcaId,
                obj.descripcionMarca,
                obj.precioValorizado,
                obj.precioCatalogo,
                obj.fotoProducto,
                obj.fotoProductoSmall,
                obj.fotoProductoMedium,
                obj.cuvRevista,
                obj.cuvComplemento,
                obj.estrategiaId,
                obj.tipoEstrategiaId,
                obj.tipoOfertaSisId,
                obj.configuracionOfertaId,
                obj.flagNueva,
                obj.indicadorMontoMinimo,
                obj.clienteId,
                obj.clienteLocalId,
                obj.cantidad,
                obj.identifier,
                obj.isSugerido,
                obj.tipoPersonalizacion,
                obj.codigoEstrategia,
                obj.codigoTipoEstrategia,
                obj.limiteVenta,
                obj.stock,
                obj.descripcionEstrategia,
                obj.origenPedidoWeb,
                obj.agregado,
                obj.origenPedidoWebFicha,
                obj.permiteAgregarPedido,
                obj.tieneOfertasRelacionadas,
                obj.codigoProducto,
                obj.isAdded,
                ProductCUVOpcionModel.transformList(obj.listaOpciones),
                obj.isMaterialGanancia,
                OrigenModel.transformList(obj.origenesPedidoWeb),
                obj.index
            )

        }
    }

}

@Parcelize
data class ProductCUVOpcionModel(
    var cuv: String?,
    var cantidad: Int?,
    var marcaId: Int?,
    var precioUnitario: Double?,
    var grupo: Int?,
    var indicadorDigitable: Int?
) : Parcelable {

    companion object {
        fun transformList(opciones: List<ProductCUVOpcion?>?): List<ProductCUVOpcionModel> {
            return mutableListOf<ProductCUVOpcionModel>().apply {
                opciones?.filterNotNull()?.forEach { item ->

                    add(ProductCUVOpcionModel(
                        item.cuv,
                        item.cantidad,
                        item.marcaId,
                        item.precioUnitario,
                        item.grupo,
                        item.indicadorDigitable
                    ))

                }
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
        fun transformList(opciones: List<Origen?>?): List<OrigenModel> {
            return mutableListOf<OrigenModel>().apply {
                opciones?.filterNotNull()?.forEach { item ->

                    add(OrigenModel(
                        item.codigo ,
                        item.valor
                    ))

                }
            }
        }
    }
}
