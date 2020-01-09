package biz.belcorp.consultoras.domain.entity

import java.util.HashMap

class Contacto {

    var id: Int? = null
    var contactoClienteID: Int? = null
    var clienteID: Int? = null
    var tipoContactoID: Int? = null
    var valor: String? = null
    var estado: Int? = null
    private val additionalProperties = HashMap<String, Any>()

    fun getAdditionalProperties(): Map<String, Any> {
        return this.additionalProperties
    }

    fun setAdditionalProperty(name: String, value: Any) {
        this.additionalProperties.put(name, value)
    }

}
