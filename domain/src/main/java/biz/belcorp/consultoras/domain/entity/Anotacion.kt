package biz.belcorp.consultoras.domain.entity

import java.util.HashMap

class Anotacion {

    var id: Int? = null
    var anotacionID: Int? = null
    var descripcion: String? = null
    var estado: Int? = null
    var clienteID: Int? = null
    var clienteLocalID: Int? = null
    var sincronizado: Int? = null
    var fecha: String? = null
    private val additionalProperties = HashMap<String, Any>()

    fun getAdditionalProperties(): Map<String, Any> {
        return this.additionalProperties
    }

    fun setAdditionalProperty(name: String, value: Any) {
        this.additionalProperties.put(name, value)
    }
}
