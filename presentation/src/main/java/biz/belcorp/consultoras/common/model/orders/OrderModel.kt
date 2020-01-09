package biz.belcorp.consultoras.common.model.orders

import android.os.Parcelable
import biz.belcorp.consultoras.common.model.client.ClienteModel
import biz.belcorp.consultoras.common.model.product.ProductItem
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Parcelize
data class OrderModel (var pedidoID: Int?,
                       var gananciaEstimada: BigDecimal?,
                       var montoEscala: BigDecimal?,
                       var descuentoProl: BigDecimal?,
                       var importeTotal: BigDecimal?,
                       var importeTotalDescuento: BigDecimal?,
                       var montoAhorroCatalogo: BigDecimal?,
                       var montoAhorroRevista: BigDecimal?,
                       var cantidadProductos: Int?,
                       var cantidadCuv: Int?,
                       var tippingPoint: BigDecimal?,
                       var pedidoValidado: Boolean,
                       var muestraRegalo: Boolean,
                       var precioPorNivel: Boolean,
                       var productosDetalle: List<ProductItem?>?,
                       var clientesDetalle: List<ClienteModel?>?,
                       var identifier : String?,
                       var isDiaProl : Boolean?,
                       var gananciaDetalle: List<GananciaListItemModel?>?,
                       var isPagoContado : Boolean,
                       var montoPagoContadoSIC : BigDecimal?,
                       var montoDeudaAnteriorSIC : BigDecimal?,
                       var montoDescuentoSIC : BigDecimal?,
                       var montoFleteSIC : BigDecimal?,
                       var precioRegalo : Boolean?,
                       var montoMaximoDesviacion : BigDecimal?,
                       var facturarPedidoFM : Boolean?,
                       var activaMultiPedido : Boolean?
                       ) : Parcelable
