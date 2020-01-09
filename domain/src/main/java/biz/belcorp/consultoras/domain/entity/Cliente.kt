package biz.belcorp.consultoras.domain.entity

import java.math.BigDecimal
import java.util.HashMap

class Cliente {

    var id: Int? = null
    var clienteID: Int? = null
    var apellidos: String? = null
    var nombres: String? = null
    var alias: String? = null
    var foto: String? = null
    var fechaNacimiento: String? = null
    var sexo: String? = null
    var documento: String? = null
    var origen: String? = null
    var favorito: Int? = null
    var estado: Int? = null
    var tipoRegistro: Int? = null
    var tipoContactoFavorito: Int? = null
    var totalDeuda: BigDecimal? = null
    var sincronizado: Int? = null
    var cantidadProductos: Int? = null
    var cantidadPedido: Int? = null
    var montoPedido: BigDecimal? = null
    var codigoRespuesta: String? = null
    var mensajeRespuesta: String? = null

    var contactos: List<Contacto?>? = null
    var anotaciones: List<Anotacion?>? = null
    var recordatorios: List<Recordatorio?>? = null
    var movements: List<ClientMovement?>? = null
    var orderList: List<OrderListItem?>? = null

    private val additionalProperties = HashMap<String, Any>()

    fun getAdditionalProperties(): Map<String, Any> {
        return this.additionalProperties
    }

    fun setAdditionalProperty(name: String, value: Any) {
        this.additionalProperties.put(name, value)
    }
}
