package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.data.db.ConsultorasDatabase
import biz.belcorp.consultoras.domain.entity.ProductCUV
import biz.belcorp.consultoras.domain.entity.ProductCUVOpcion
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel
import java.io.Serializable

@Table(database = ConsultorasDatabase::class, name = "ProductoCUV")
class ProductCuvEntity : BaseModel(), Serializable {

    @Column(name = "ID")
    @PrimaryKey(autoincrement = true)
    @SerializedName("ID")
    @Expose
    var id: Int? = null

    @Column(name = "CUV")
    @SerializedName("CUV")
    var cuv: String? = null

    @Column(name = "SAP")
    @SerializedName("SAP")
    var sap: String? = null

    @Column(name = "Descripcion")
    @SerializedName("Descripcion")
    var description: String? = null

    @Column(name = "DescripcionCategoria")
    @SerializedName("DescripcionCategoria")
    var descripcionCategoria: String? = null

    @Column(name = "MarcaID")
    @SerializedName("MarcaID")
    var marcaId: Int? = null

    @Column(name = "DescripcionMarca")
    @SerializedName("DescripcionMarca")
    var descripcionMarca: String? = null

    @Column(name = "PrecioValorizado")
    @SerializedName("PrecioValorizado")
    var precioValorizado: Double? = null

    @Column(name = "PrecioCatalogo")
    @SerializedName("PrecioCatalogo")
    var precioCatalogo: Double? = null

    @Column(name = "FotoProducto")
    @SerializedName("FotoProducto")
    var fotoProducto: String? = null

    @Column(name = "FotoProductoSmall")
    @SerializedName("FotoProductoSmall")
    var fotoProductoSmall: String? = null

    @Column(name = "FotoProductoMedium")
    @SerializedName("FotoProductoMedium")
    var fotoProductoMedium: String? = null

    @Column(name = "CUVRevista")
    @SerializedName("CUVRevista")
    var cuvRevista: String? = null

    @Column(name = "CUVComplemento")
    @SerializedName("CUVComplemento")
    var cuvComplemento: String? = null

    @Column(name = "EstrategiaID")
    @SerializedName("EstrategiaID")
    var estrategiaId: Int? = null

    @Column(name = "TipoEstrategiaID")
    @SerializedName("TipoEstrategiaID")
    var tipoEstrategiaId: String? = null

    @Column(name = "TipoOfertaSisID")
    @SerializedName("TipoOfertaSisID")
    var tipoOfertaSisId: Int? = null

    @Column(name = "ConfiguracionOfertaID")
    @SerializedName("ConfiguracionOfertaID")
    var configuracionOfertaId: Int? = null

    @Column(name = "FlagNueva")
    @SerializedName("FlagNueva")
    var flagNueva: Int? = null

    @Column(name = "IndicadorMontoMinimo")
    @SerializedName("IndicadorMontoMinimo")
    var indicadorMontoMinimo: Int? = null

    @Column(name = "ClienteID")
    @SerializedName("ClienteID")
    var clienteId: Int? = null

    @Column(name = "ClienteLocalID")
    @Expose(serialize = false, deserialize = false)
    var clienteLocalId: Int? = null

    @Column(name = "EsSugerido")
    @SerializedName("EsSugerido")
    var isSugerido: Boolean? = null

    @Column(name = "TipoPersonalizacion")
    @SerializedName("TipoPersonalizacion")
    var tipoPersonalizacion: String? = null

    @Column(name = "CodigoEstrategia")
    @SerializedName("CodigoEstrategia")
    var codigoEstrategia: Int? = null

    @Column(name = "CodigoTipoEstrategia")
    @SerializedName("CodigoTipoEstrategia")
    var codigoTipoEstrategia: String? = null

    @Column(name = "LimiteVenta")
    @SerializedName("LimiteVenta")
    var limiteVenta: Int? = null

    @Column(name = "Stock")
    @SerializedName("Stock")
    var isStock: Boolean? = null

    @Column(name = "DescripcionEstrategia")
    @SerializedName("DescripcionEstrategia")
    var descripcionEstrategia: String? = null

    @Column(name = "OrigenPedidoWeb")
    @SerializedName("OrigenPedidoWeb")
    var origenPedidoWeb: String? = null

    @Column(name = "OrigenPedidoWebFicha")
    @SerializedName("OrigenPedidoWebFicha")
    var origenPedidoWebFicha: String? = null

    @Column(name = "PermiteAgregarPedido")
    @SerializedName("PermiteAgregarPedido")
    var isPermiteAgregarPedido: Boolean? = null

    @Column(name = "TieneOfertasRelacionadas")
    @SerializedName("TieneOfertasRelacionadas")
    var isTieneOfertasRelacionadas: Boolean? = null

    @Column(name = "CodigoProducto")
    @SerializedName("CodigoProducto")
    var codigoProducto: String? = null

    @Column(name = "MaterialGanancia")
    @SerializedName("MaterialGanancia")
    var isMaterialGanancia: Boolean? = null

    @SerializedName("FlagFestival")
    var flagFestival: Boolean? = null

    @SerializedName("ReemplazarFestival")
    var reemplazarFestival: Boolean? = null

    @Column(name = "CodigoTipoOferta")
    @SerializedName("CodigoTipoOferta")
    var codigoTipoOferta: String? = null

    @SerializedName("FlagPromocion")
    var flagPromocion: Boolean? = null

    @SerializedName("ListaOpciones")
    var listaOpciones: List<ProductCUVOpcionEntity>? = null

    @SerializedName("OrigenesPedidoWeb")
    var origenesPedidoWeb: List<OrigenEntity>? = null


    companion object {

        fun transformList(list: Collection<ProductCuvEntity?>?): List<ProductCUV?>?{
            return mutableListOf<ProductCUV>().apply {
                list?.forEach {
                    it?.let { it1 -> ProductCuvEntity.transform(it1) }?.let { it2 -> add(it2) }
                }
            }.toList()
        }

        fun transform(input: ProductCuvEntity?): ProductCUV?{
            input?.run {
                val output = ProductCUV()
                output.id = id
                output.cuv = cuv
                output.sap = sap
                output.description = description
                output.descripcionCategoria = descripcionCategoria
                output.marcaId = marcaId
                output.descripcionMarca = descripcionMarca
                output.precioValorizado = precioValorizado
                output.precioCatalogo = precioCatalogo
                output.fotoProducto = fotoProducto
                output.fotoProductoSmall = fotoProductoSmall
                output.fotoProductoMedium = fotoProductoMedium
                output.cuvRevista = cuvRevista
                output.cuvComplemento = cuvComplemento
                output.estrategiaId = estrategiaId
                output.tipoEstrategiaId = tipoEstrategiaId
                output.tipoOfertaSisId = tipoOfertaSisId
                output.configuracionOfertaId = configuracionOfertaId
                output.flagNueva = flagNueva
                output.indicadorMontoMinimo = indicadorMontoMinimo
                output.clienteId = clienteId
                output.clienteLocalId = clienteLocalId
                output.isSugerido = isSugerido ?: false
                output.tipoPersonalizacion = tipoPersonalizacion
                output.codigoEstrategia = codigoEstrategia
                output.codigoTipoEstrategia = codigoTipoEstrategia
                output.limiteVenta = limiteVenta
                output.stock = isStock
                output.descripcionEstrategia = descripcionEstrategia
                output.origenPedidoWeb = origenPedidoWeb
                output.origenPedidoWebFicha = origenPedidoWebFicha
                output.permiteAgregarPedido = isPermiteAgregarPedido
                output.tieneOfertasRelacionadas = isTieneOfertasRelacionadas
                output.codigoProducto = codigoProducto
                output.isMaterialGanancia = isMaterialGanancia
                output.origenesPedidoWeb = OrigenEntity.transformList(origenesPedidoWeb)
                output.flagFestival = flagFestival
                output.reemplazarFestival = reemplazarFestival
                return output
            }
            return null
        }

    }

}

class ProductCUVOpcionEntity {
    var cuv: String? = null
    var cantidad: Int? = null
    var marcaId: Int? = null
    var precioUnitario: Double? = null
    var grupo: Int? = null
    var indicadorDigitable: Int? = null

    companion object {

        fun transform(input: ProductCUVOpcion): ProductCUVOpcionEntity{
            return ProductCUVOpcionEntity().apply {
                cuv = input.cuv
                cantidad = input.cantidad
                marcaId = input.marcaId
                precioUnitario = input.precioUnitario
                grupo = input.grupo
                indicadorDigitable = input.indicadorDigitable
            }
        }

        fun transform(input: ProductCUVOpcionEntity): ProductCUVOpcion{
            return ProductCUVOpcion().apply {
                cuv = input.cuv
                cantidad = input.cantidad
                marcaId = input.marcaId
                precioUnitario = input.precioUnitario
                grupo = input.grupo
                indicadorDigitable = input.indicadorDigitable
            }
        }

        fun transformToListEntity(inputList: List<ProductCUVOpcion>): List<ProductCUVOpcionEntity>{
            return mutableListOf<ProductCUVOpcionEntity>().apply {
                inputList.forEach {
                    add(transform(it))
                }
            }
        }

        fun transformToList(inputList: List<ProductCUVOpcionEntity>): List<ProductCUVOpcion>{
            return mutableListOf<ProductCUVOpcion>().apply {
                inputList.forEach {
                    add(transform(it))
                }
            }
        }

    }

}
