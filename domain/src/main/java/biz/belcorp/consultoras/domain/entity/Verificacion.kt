package biz.belcorp.consultoras.domain.entity

data class Verificacion(
	var mostrarOpcion: Int? = null,
	var opcionVerificacionCorreo: Int? = null,
	var mensajeSaludo: String? = null,
	var idEstadoActividad: Int? = null,
	var correoEnmascarado: String? = null,
	var origenID: Int? = null,
	var horaRestanteCorreo: Int? = null,
	var opcionVerificacionSMS: Int? = null,
	var intentosRestanteSms: Int? = null,
	var primerNombre: String? = null,
	var celularEnmascarado: String? = null,
	var origenDescripcion: String? = null,
	var opcionCambioClave: Int? = null,
	var horaRestanteSms: Int? = null,
    var telefono1: String? = null,
    var telefono2: String? = null
){
    companion object {
        const val MUESTRA_SMS_EMAIL = 1
        const val NO_MUESTRA_SMS = 2
        const val NO_MUESTRA_EMAIL = 3
        const val CAMBIO_CLAVE = 5
    }
}
