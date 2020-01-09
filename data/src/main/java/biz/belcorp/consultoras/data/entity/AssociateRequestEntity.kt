package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

class AssociateRequestEntity {

    @SerializedName("CodigoUsuario")
    var codigoUsuario: String? = null
    @SerializedName("ClaveSecreta")
    var claveSecreta: String? = null
    @SerializedName("IdAplicacion")
    var idAplicacion: String? = null
    @SerializedName("Login")
    var login: String? = null
    @SerializedName("Nombres")
    var nombres: String? = null
    @SerializedName("Apellidos")
    var apellidos: String? = null
    @SerializedName("FechaNacimiento")
    var fechaNacimiento: String? = null
    @SerializedName("Correo")
    var correo: String? = null
    @SerializedName("Genero")
    var genero: String? = null
    @SerializedName("Ubicacion")
    var ubicacion: String? = null
    @SerializedName("LinkPerfil")
    var linkPerfil: String? = null
    @SerializedName("FotoPerfil")
    var fotoPerfil: String? = null
    @SerializedName("PaisISO")
    var paisISO: String? = null
    @SerializedName("Proveedor")
    var proveedor: String? = null

    @Transient
    var authorization: String? = null

    @Transient
    var grandType: String? = null

    @Transient
    var tipoAutenticacion: String? = null

    @Transient
    var refreshToken: String? = null
}
