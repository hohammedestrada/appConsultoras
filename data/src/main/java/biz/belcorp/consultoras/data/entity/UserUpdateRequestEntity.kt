package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserUpdateRequestEntity {

    @SerializedName("Correo")
    var email: String? = null
    @SerializedName("Telefono")
    var phone: String? = null
    @SerializedName("Celular")
    var mobile: String? = null
    @SerializedName("OtroTelefono")
    var otherPhone: String? = null
    @SerializedName("AceptoContrato")
    var isAcceptContract: Boolean = false
    @SerializedName("Sobrenombre")
    var sobrenombre: String? = null
    @SerializedName("NombreArchivo")
    var nombreArchivo: String? = null
    @SerializedName("TipoArchivo")
    var tipoArchivo: String? = null
    @Expose
    var urlImagen: String? = null
    @SerializedName("NotificacionesWhatsapp")
    var notificacionesWhatsapp: Boolean? = null
    @SerializedName("ActivaNotificacionesWhatsapp")
    var activaNotificacionesWhatsapp: Boolean? = null
}
