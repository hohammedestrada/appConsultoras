package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.data.mapper.ProductCuvEntityDataMapper
import biz.belcorp.consultoras.domain.entity.PedidoInsertRequest
import com.google.gson.annotations.SerializedName

class PedidoInsertRequestEntity {

    @SerializedName("CampaniaID")
    var campaniaId: Int? = null

    @SerializedName("Cantidad")
    var cantidad: Int? = null

    @SerializedName("IPUsuario")
    var ipUsuario: String? = null

    @SerializedName("ClienteID")
    var clienteID: Int? = null

    @SerializedName("ClienteDescripcion")
    var clienteDescripcion: String? = null

    @SerializedName("Identifier")
    var identifier: String? = null

    @SerializedName("CodigosConcursos")
    var codigosConcursos: String? = null

    @SerializedName("AceptacionConsultoraDA")
    var aceptacionConsultoraDA: Int? = null

    @SerializedName("MontoMaximoPedido")
    var montoMaximoPedido: Double? = null

    @SerializedName("CodigoPrograma")
    var codigoPrograma: String? = null

    @SerializedName("ConsecutivoNueva")
    var consecutivoNueva: Int? = null

    @SerializedName("FechaInicioFacturacion")
    var fechaInicioFacturacion: String? = null

    @SerializedName("FechaFinFacturacion")
    var fechaFinFacturacion: String? = null

    @SerializedName("ConsultoraNueva")
    var consultoraNueva: Int? = null

    @SerializedName("NroCampanias")
    var nroCampanias: Int? = null

    @SerializedName("Producto")
    var producto: ProductCuvEntity? = null

    @SerializedName("EsSugerido")
    var isSugerido: Boolean? = null

    @SerializedName(value = "OrigenPedidoWeb")
    var origenPedidoWebCarrusel: Int? = 0

    @SerializedName(value = "NivelCaminoBrillante")
    var nivelCaminoBrillante: Int? = 0

    // Cambios para Pedido Reservado
    @SerializedName(value = "SegmentoInternoID")
    var segmentoInternoID: Int? = 0

    @SerializedName(value = "MontoMinimoPedido")
    var montoMinimoPedido: Double? = 0.0

    @SerializedName(value = "ValidacionAbierta")
    var isValidacionAbierta: Boolean? = false

    @SerializedName(value = "ZonaValida")
    var isZonaValida: Boolean? = false

    @SerializedName(value = "ValidacionInteractiva")
    var isValidacionInteractiva: Boolean? = false

    @SerializedName(value = "DiaPROL")
    var isDiaProl: Boolean? = false

    @SerializedName(value = "CodigoZona")
    var codigoZona: String? = ""

    @SerializedName(value = "CodigoRegion")
    var codigoRegion: String? = ""

    @SerializedName(value = "UsuarioPrueba")
    var isUsuarioPrueba: Boolean? = false

    @SerializedName(value = "Simbolo")
    var simbolo: String? = ""

    // Cambios para modificar desde Ficha Resumida
    @SerializedName(value = "EsEditable")
    var isEditable: Boolean? = false

    @SerializedName(value = "SetID")
    var orderID: Int? = 0

    companion object {

        fun transform(request: PedidoInsertRequest): PedidoInsertRequestEntity {
            return PedidoInsertRequestEntity().apply {
                campaniaId = request.campaniaId
                cantidad = request.cantidad
                ipUsuario = request.ipUsuario
                clienteID = request.clienteID
                clienteDescripcion = request.clienteDescripcion
                identifier = request.identifier
                codigosConcursos = request.codigosConcursos
                aceptacionConsultoraDA = request.aceptacionConsultoraDA
                montoMaximoPedido = request.montoMaximoPedido
                codigoPrograma = request.codigoPrograma
                consecutivoNueva = request.consecutivoNueva
                fechaInicioFacturacion = request.fechaInicioFacturacion
                fechaFinFacturacion = request.fechaFinFacturacion
                consultoraNueva = request.consultoraNueva
                nroCampanias = request.nroCampanias
                producto = ProductCuvEntityDataMapper.transformToEntity(request.producto)
                isSugerido = request.isSugerido
                origenPedidoWebCarrusel = request.origenPedidoWebCarrusel
                nivelCaminoBrillante = request.nivelCaminoBrillante

                // Cambios para Pedido Reservado
                segmentoInternoID = request.segmentoInternoID
                montoMinimoPedido = request.montoMinimoPedido
                isValidacionAbierta = request.isValidacionAbierta
                isZonaValida = request.isZonaValida
                isValidacionInteractiva = request.isValidacionInteractiva
                isDiaProl = request.isDiaProl
                codigoZona = request.codigoZona
                codigoRegion = request.codigoRegion
                isUsuarioPrueba = request.isUsuarioPrueba
                simbolo = request.simbolo

                // Cambios para modificar desde Ficha Resumida
                isEditable = request.isEditable
                orderID = request.orderID
            }
        }

    }
}
