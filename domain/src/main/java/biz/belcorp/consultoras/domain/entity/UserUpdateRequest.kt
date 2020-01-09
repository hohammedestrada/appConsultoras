package biz.belcorp.consultoras.domain.entity

class UserUpdateRequest {

    var email: String? = null
    var phone: String? = null
    var mobile: String? = null
    var otherPhone: String? = null
    var isAcceptContract: Boolean = false
    var sobreNombre: String? = null
    var urlImagen: String? = null
    var nombreArchivo: String? = null
    var tipoArchivo: String? = null
    var isNotificacionesWhatsapp: Boolean = true
    var isActivaNotificaconesWhatsapp: Boolean = true
}
