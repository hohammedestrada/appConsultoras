package biz.belcorp.consultoras.domain.entity

class AssociateRequest {

    var codigoUsuario: String? = null
    var claveSecreta: String? = null
    var idAplicacion: String? = null
    var login: String? = null
    var nombres: String? = null
    var apellidos: String? = null
    var fechaNacimiento: String? = null
    var correo: String? = null
    var genero: String? = null
    var ubicacion: String? = null
    var linkPerfil: String? = null
    var fotoPerfil: String? = null
    var paisISO: String? = null
    var proveedor: String? = null
    @Transient
    var refreshToken: String? = null
    @Transient
    var grandType: String? = null
    @Transient
    var tipoAutenticacion: String? = null
    @Transient
    var authorization: String? = null
}
