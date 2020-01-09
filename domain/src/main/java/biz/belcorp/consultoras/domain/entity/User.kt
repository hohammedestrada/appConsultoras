package biz.belcorp.consultoras.domain.entity

import java.io.Serializable

class User : Serializable {

    var countryId: Int? = 0
    var countryISO: String? = null
    var countryMoneySymbol: String? = null
    var countryShowDecimal: Int = 0

    var consultantId: String? = null
    var consultantUserCode: String? = null
    var consultantCode: String? = null
    var consultantAssociateId: String? = null
    var userCode: String? = null
    var isUserTest: Boolean = false
    var userType: Int? = 0
    var consultantName: String? = null
    var alias: String? = ""
    var email: String? = null
    var phone: String? = null
    var otherPhone: String? = null
    var mobile: String? = null
    var photoProfile: String? = null
    var photoName: String? = null
    var tipoArchivo: String? = null

    var campaing: String? = null
    var numberOfCampaings: String? = null
    var regionID: String? = null
    var regionCode: String? = null
    var zoneID: String? = null
    var zoneCode: String? = null

    var expirationDate: String? = null
    var isDayProl: Boolean = false
    var consultantAcceptDA: Int? = 0
    var urlBelcorpChat: String? = null
    var billingStartDate: String? = null
    var billingEndDate: String? = null
    var endTime: String? = null
    var timeZone: String? = null
    var closingDays: Int? = 0
    var isHasDayOffer: Boolean = false
    var isShowRoom: Boolean = false
    var isAceptaTerminosCondiciones: Boolean = false
    var isAceptaPoliticaPrivacidad: Boolean = false
    var destinatariosFeedback: String? = null

    var isShowBanner: Boolean = false
    var bannerTitle: String? = null
    var bannerMessage: String? = null
    var bannerUrl: String? = null
    var bannerVinculo: String? = null

    var isBirthday: Boolean = false
    var isAnniversary: Boolean = false

    var isPasoSextoPedido: Boolean = false

    var detail: List<LoginDetail?>? = null
    var revistaDigitalSuscripcion: Int? = 0
    var bannerGanaMas: String? = null
    var isTieneGND: Boolean = false
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
    //    var horaFin: String? = null
    var isZonaValida: Boolean = false
    var diasAntes: Int? = null
    var segmentoInternoID: Int? = null
    var isProlSinStock: Boolean = false
    var isValidacionAbierta: Boolean = false
    var isValidacionInteractiva: Boolean = false
    var numeroDocumento: String? = null
    var indicadorGPRSB: Int? = null
    var consultoraAsociada: String? = null
    var horaFinPortal: String? = null
    var esConsultoraOficina: Boolean = false
    var segmentoConstancia: String? = null
    var esLider: Int? = null
    var periodo: String? = null
    var semanaPeriodo: String? = null
    var nivelLider: Int? = null
    var descripcionNivelLider: String? = null
    var campaniaInicioLider: String? = null
    var seccionGestionLider: String? = null
    var indicadorContratoCredito: Int? = null

    var isCambioCorreoPendiente: Boolean? = null
    var correoPendiente: String? = null
    var primerNombre: String? = null
    var isPuedeActualizar: Boolean? = null
    var isPuedeActualizarEmail: Boolean? = null
    var isPuedeActualizarCelular: Boolean? = null

    var isMostrarBuscador: Boolean? = null
    var caracteresBuscador: Int? = null
    var caracteresBuscadorMostrar: Int? = null
    var totalResultadosBuscador: Int? = null
    var lider: Int? = null
    var isRDEsActiva: Boolean? = null
    var isRDEsSuscrita: Boolean? = null
    var isRDActivoMdo: Boolean? = null
    var isRDTieneRDC: Boolean? = null
    var isRDTieneRDI: Boolean? = null
    var isRDTieneRDCR: Boolean? = null
    var diaFacturacion: Int? = null

    var isIndicadorConsultoraDummy: Boolean? = null
    var personalizacionesDummy: String? = null

    var isMostrarBotonVerTodosBuscador: Boolean? = null
    var isAplicarLogicaCantidadBotonVerTodosBuscador: Boolean? = null
    var isMostrarOpcionesOrdenamientoBuscador: Boolean? = null

    var isMostrarFiltrosBuscador: Boolean? = null

    var isTieneMG: Boolean? = null
    var isPagoEnLinea: Boolean? = null
    var isChatBot: Boolean? = null
    var indicadorConsultoraDigital: Int = 0

    var tipoIngreso: String? = null
    var segmentoDatami: String? = null
    var isGanaMasNativo: Boolean = false
    var primerApellido: String? = null
    var fechaNacimiento: String? = null

    var bloqueoPendiente : Boolean = false
    var actualizacionDatos : Int = 0

    var isNotificacionesWhatsapp: Boolean = true //Estatus de las notificaciones
    var isActivaNotificaconesWhatsapp: Boolean = true //Si se muestra o no el check

    var isUltimoDiaFacturacion: Boolean = false
    var isPagoContado : Boolean = false
    var montoMaximoDesviacion: Double? = null

    var isCambioCelularPendiente: Boolean? = null
    var celularPendiente: String? = null
    var isBrillante : Boolean = false
    var isMultipedido : Boolean = false
    var lineaConsultora: String? = null

    var nextCampania: String? = null
    constructor() {
        // EMPTY
    }

    constructor(pais: String, campaing: String, countryZone: String) {
        this.countryISO = pais
        this.campaing = campaing
        this.zoneCode = countryZone
    }
}
