package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

class EnviarCorreoRequest{

    @SerializedName("Correo")
    var correo: String? = null

    @SerializedName("NombreConsultora")
    var nombreConsultora: String? = null

    @SerializedName("Sobrenombre")
    var sobrenombre: String? = null

    @SerializedName("PrimerNombre")
    var primerNombre: String? = null

    @SerializedName("PuedeActualizar")
    var puedeActualizar: Boolean? = null

    @SerializedName("CorreoNuevo")
    var correoNuevo: String? = null

}
