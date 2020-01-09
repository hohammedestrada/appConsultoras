package biz.belcorp.consultoras.domain.entity

/**
 * Entidad de Login datos devueltos por el servicio
 */
class Login {

    var accessToken: String? = null
    var tokenType: String? = null
    var expiresIn: Int? = 0
    var refreshToken: String? = null
    var countryId: Int? = 0
    var countryISO: String? = null
    var countryMoneySymbol: String? = null
    var consultantID: Int? = 0
    var userCode: String? = null
    var consultantCode: String? = null
    var userTest: String? = null
    var campaing: String? = null
    var numberOfCampaings: String? = null
    var regionID: String? = null
    var regionCode: String? = null
    var zoneID: String? = null
    var zoneCode: String? = null
    var consultantName: String? = null
    var alias: String? = null
    var expirationDate: String? = null
    var isDayProl: Boolean? = false
    var consultantAcceptDA: Int? = 0
    var urlBelcorpChat: String? = null
    var userType: Int? = 0
    var billingStartDate: String? = null
    var billingEndDate: String? = null
    var endTime: String? = null
    var timeZone: String? = null
    var closingDays: Int? = 0
    var email: String? = null
    var phone: String? = null
    var mobile: String? = null
    var isCambioCelularPendiente: Boolean? = null
    var celularPendiente: String? = null

    var hasDayOffer: String? = null
    var consultantAssociateID: String? = null
    var otherPhone: String? = null
    var photoProfile: String? = null
    var issued: String? = null
    var expires: String? = null
    var isShowRoom: Boolean? = false
    var isAceptaTerminosCondiciones: Boolean? = false
    var isAceptaPoliticaPrivacidad: Boolean? = false
    var destinatariosFeedback: String? = null

    var isShowBanner: Boolean? = false
    var bannerTitle: String? = null
    var bannerMessage: String? = null
    var bannerUrl: String? = null
    var bannerVinculo: String? = null
    var isBirthday: Boolean? = false
    var isAnniversary: Boolean? = false

    var isPasoSextoPedido: Boolean? = false

    var detail: List<LoginDetail?>? = null
    var revistaDigitalSuscripcion: Int? = 0
    var bannerGanaMas: String? = null

    var cuponEstado: Int? = 0
    var cuponPctDescuento: Double? = 0.0
    var cuponMontoMaxDscto: Double? = 0.0
    var isTieneGND: Boolean? = false
    var codigoSeccion: String? = null
    var tipoCondicion: Int? = 0

    var codigoPrograma: String? = null
    var consecutivoNueva: Int? = null
    var consultoraNueva: Int? = null
    var montoMinimoPedido: Double? = null
    var montoMaximoPedido: Double? = null
    var horaInicioNoFacturable: String? = null
    var horaCierreNoFacturable: String? = null
    var horaInicio: String? = null
    var codigosConcursos: String? = null
    var isZonaValida: Boolean? = null

    var diasAntes: Int? = null
    var segmentoInternoID: Int? = null
    var isProlSinStock: Boolean? = null
    var isValidacionAbierta: Boolean? = null
    var isValidacionInteractiva: Boolean? = null
    var numeroDocumento: String? = null
    var indicadorGPRSB: Int? = null
    var consultoraAsociada: String? = null
    var horaFinPortal: String? = null
    var esConsultoraOficina: Boolean? = null

    var segmentoConstancia: String? = null
    var esLider: Int? = null
    var nivelLider: Int? = null
    var campaniaInicioLider: String? = null
    var seccionGestionLider: String? = null

    var indicadorContratoCredito : Int? = null

    var isCambioCorreoPendiente :Boolean? = null
    var correoPendiente: String? = null
    var primerNombre: String? = null
    var isPuedeActualizar: Boolean? = null
    var isPuedeActualizarEmail: Boolean? = null
    var isPuedeActualizarCelular: Boolean? = null

    var isMostrarBuscador: Boolean? = false
    var caracteresBuscador: Int? = 0
    var caracteresBuscadorMostrar: Int? = 0
    var totalResultadosBuscador: Int? = 0
    var lider: Int? = 0
    var isRDEsActiva: Boolean? = false
    var isRDEsSuscrita: Boolean? = false
    var isRDActivoMdo: Boolean? = false
    var isRDTieneRDC: Boolean? = false
    var isRDTieneRDI: Boolean? = false
    var isRDTieneRDCR: Boolean? = false
    var diaFacturacion: Int? = 0

    var isIndicadorConsultoraDummy: Boolean? = false
    var personalizacionesDummy: String? = null

    var isMostrarBotonVerTodosBuscador: Boolean? = null
    var isAplicarLogicaCantidadBotonVerTodosBuscador: Boolean? = null
    var isMostrarOpcionesOrdenamientoBuscador: Boolean? = null

    var isMostrarFiltrosBuscador: Boolean? = null

    var isTieneMG: Boolean? = null
    var isPagoEnLinea: Boolean?=false
    var isChatBot: Boolean? = false
    var indicadorConsultoraDigital: Int = 0
    var exception: Throwable? = null

    var tipoIngreso: String? = null
    var segmentoDatami: String? = null
    var isGanaMasNativo: Boolean = false

    var isUltimoDiaFacturacion: Boolean = false
    var mostrarEnBanner: Int = -1
    var primerApellido: String? = null
    var fechaNacimiento: String? = null

    var bloqueoPendiente : Boolean = false
    var actualizacionDatos : Int = 0
    var checkEnviarWhatsaap: Int= -1
    var showCheckWhatsapp: Int = -1
    var isPagoContado : Boolean = false
    var isBrillante : Boolean = false
    var montoMaximoDesviacion: Double? = null
    var isMultipedido : Boolean = false
    var lineaConsultora : String? = null
    var periodo: String? = null
    var semanaPeriodo: String? = null
    var descripcionNivelLider: String? = null

    var logSiguienteCampania: String? = null
}
