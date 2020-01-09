package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.data.db.ConsultorasDatabase
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table

@Table(database = ConsultorasDatabase::class, name = "Verificacion")
data class VerificacionEntity(

    @Column(name = "ID")
    @PrimaryKey(autoincrement = true)
    @SerializedName("ID")
    @Expose(serialize = false, deserialize = false)
    var id: Int? = null,

    @Column(name = "MostrarOpcion")
    @field:SerializedName("MostrarOpcion")
	var mostrarOpcion: Int? = null,

    @Column(name = "OpcionVerificacionCorreo")
	@field:SerializedName("OpcionVerificacionCorreo")
	var opcionVerificacionCorreo: Int? = null,

    @Column(name = "MensajeSaludo")
	@field:SerializedName("MensajeSaludo")
	var mensajeSaludo: String? = null,

    @Column(name = "IdEstadoActividad")
	@field:SerializedName("IdEstadoActividad")
	var idEstadoActividad: Int? = null,

    @Column(name = "CorreoEnmascarado")
	@field:SerializedName("CorreoEnmascarado")
	var correoEnmascarado: String? = null,

    @Column(name = "OrigenID")
	@field:SerializedName("OrigenID")
	var origenID: Int? = null,

    @Column(name = "HoraRestanteCorreo")
	@field:SerializedName("HoraRestanteCorreo")
	var horaRestanteCorreo: Int? = null,

    @Column(name = "OpcionVerificacionSMS")
	@field:SerializedName("OpcionVerificacionSMS")
	var opcionVerificacionSMS: Int? = null,

    @Column(name = "IntentosRestanteSms")
	@field:SerializedName("IntentosRestanteSms")
	var intentosRestanteSms: Int? = null,

    @Column(name = "PrimerNombre")
	@field:SerializedName("PrimerNombre")
	var primerNombre: String? = null,

    @Column(name = "CelularEnmascarado")
	@field:SerializedName("CelularEnmascarado")
	var celularEnmascarado: String? = null,

    @Column(name = "OrigenDescripcion")
	@field:SerializedName("OrigenDescripcion")
	var origenDescripcion: String? = null,

    @Column(name = "OpcionCambioClave")
	@field:SerializedName("OpcionCambioClave")
	var opcionCambioClave: Int? = null,

    @Column(name = "HoraRestanteSms")
	@field:SerializedName("HoraRestanteSms")
	var horaRestanteSms: Int? = null
)
