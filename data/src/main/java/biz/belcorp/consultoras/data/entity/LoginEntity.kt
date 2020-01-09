package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

/**
 * Entidad de Login datos devueltos por el servicio
 */
class LoginEntity {

    @SerializedName(value = "access_token", alternate = ["Access_token"])
    var accessToken: String? = null

    @SerializedName(value = "token_type", alternate = ["Token_type"])
    var tokenType: String? = null

    @SerializedName(value = "expires_in", alternate = ["Expires_in"])
    var expiresIn: Int? = 0

    @SerializedName(value = "refresh_token", alternate = ["Refresh_token"])
    var refreshToken: String? = null

    @SerializedName(value = "paisID", alternate = ["PaisID"])
    var countryId: Int? = 0

    @SerializedName(value = "paisISO", alternate = ["PaisISO"])
    var countryISO: String? = null

    @SerializedName(value = "simbolo", alternate = ["Simbolo"])
    var countryMoneySymbol: String? = null

    @SerializedName(value = "consultoraID", alternate = ["ConsultoraID"])
    var consultantID: Int? = 0

    @SerializedName(value = "codigoUsuario", alternate = ["CodigoUsuario"])
    var userCode: String? = null

    @SerializedName(value = "codigoConsultora", alternate = ["CodigoConsultora"])
    var consultantCode: String? = null

    @SerializedName(value = "usuarioPrueba", alternate = ["UsuarioPrueba"])
    var userTest: String? = null

    @SerializedName(value = "campania", alternate = ["Campania"])
    var campaing: String? = null

    @SerializedName(value = "nroCampanias", alternate = ["NroCampanias"])
    var numberOfCampaings: String? = null

    @SerializedName(value = "regionID", alternate = ["RegionID"])
    var regionID: String? = null

    @SerializedName(value = "codigoRegion", alternate = ["CodigoRegion"])
    var regionCode: String? = null

    @SerializedName(value = "zonaID", alternate = ["ZonaID"])
    var zoneID: String? = null

    @SerializedName(value = "codigoZona", alternate = ["CodigoZona"])
    var zoneCode: String? = null

    @SerializedName(value = "nombreConsultora", alternate = ["NombreConsultora"])
    var consultantName: String? = null

    @SerializedName(value = "sobrenombre", alternate = ["Sobrenombre"])
    var alias: String? = null

    @SerializedName(value = "fechaVencimiento", alternate = ["FechaVencimiento"])
    var expirationDate: String? = null

    @SerializedName(value = "diaProl", alternate = ["DiaProl"])
    var isDayProl: Boolean? = false

    @SerializedName(value = "aceptacionConsultoraDA", alternate = ["AceptacionConsultoraDA"])
    var consultantAcceptDA: Int? = 0

    @SerializedName(value = "urlBelcorpChat", alternate = ["UrlBelcorpChat"])
    var urlBelcorpChat: String? = null

    @SerializedName(value = "tipoUsuario", alternate = ["TipoUsuario"])
    var userType: Int? = 0

    @SerializedName(value = "fechaInicioFacturacion", alternate = ["FechaInicioFacturacion"])
    var billingStartDate: String? = null

    @SerializedName(value = "fechaFinFacturacion", alternate = ["FechaFinFacturacion"])
    var billingEndDate: String? = null

    @SerializedName(value = "horaFin", alternate = ["HoraFin"])
    var endTime: String? = null

    @SerializedName(value = "zonaHoraria", alternate = ["ZonaHoraria"])
    var timeZone: String? = null

    @SerializedName(value = "diasCierre", alternate = ["DiasCierre"])
    var closingDays: Int? = 0

    @SerializedName(value = "correo", alternate = ["Correo"])
    var email: String? = null

    @SerializedName(value = "telefono", alternate = ["Telefono"])
    var phone: String? = null

    @SerializedName(value = "celular", alternate = ["Celular"])
    var mobile: String? = null

    @SerializedName(value = "CambioCelularPendiente", alternate = ["cambioCelularPendiente"])
     var isCambioCelularPendiente: Boolean? = null

    @SerializedName(value = "CelularPendiente", alternate = ["celularPendiente"])
    var celularPendiente: String? = null

    @SerializedName(value = "tieneOfertaDelDia", alternate = ["TieneOfertaDelDia"])
    var hasDayOffer: String? = null

    @SerializedName(value = "consultoraAsociadaID", alternate = ["ConsultoraAsociadaID"])
    var consultantAssociateID: String? = null

    @SerializedName(value = "consultoraAsociada", alternate = ["ConsultoraAsociada"])
    var consultoraAsociada: String? = null

    @SerializedName(value = "otroTelefono", alternate = ["OtroTelefono"])
    var otherPhone: String? = null

    @SerializedName(value = "fotoPerfil", alternate = ["FotoPerfil"])
    var photoProfile: String? = null

    @SerializedName(value = ".issued", alternate = [".Issued"])
    var issued: String? = null

    @SerializedName(value = ".expires", alternate = [".Expires"])
    var expires: String? = null

    @SerializedName(value = "tieneEntidadesShowRoom", alternate = ["TieneEntidadesShowRoom"])
    var isShowRoom: Boolean? = false

    @SerializedName(value = "AceptaTerminosCondiciones", alternate = ["aceptaTerminosCondiciones"])
    var isAceptaTerminosCondiciones: Boolean? = false

    @SerializedName(value = "AceptaPoliticaPrivacidad", alternate = ["aceptaPoliticaPrivacidad"])
    var isAceptaPoliticaPrivacidad: Boolean? = false

    @SerializedName(value = "DestinatariosFeedback", alternate = ["destinatariosFeedback"])
    var destinatariosFeedback: String? = null

    @SerializedName(value = "GPRMostrarBannerRechazo", alternate = ["gprMostrarBannerRechazo"])
    var isShowBanner: Boolean? = false

    @SerializedName(value = "GPRBannerTitulo", alternate = ["gprBannerTitulo"])
    var bannerTitle: String? = null

    @SerializedName(value = "GPRBannerMensaje", alternate = ["gprBannerMensaje"])
    var bannerMessage: String? = null

    @SerializedName(value = "GPRBannerUrl", alternate = ["gprBannerUrl"])
    var bannerUrl: String? = null

    @SerializedName(value = "GPRTextovinculo", alternate = ["gprTextovinculo"])
    var bannerVinculo: String? = null

    @SerializedName(value = "esCumpleanio", alternate = ["EsCumpleanio"])
    var isBirthday: Boolean? = false

    @SerializedName(value = "esAniversario", alternate = ["EsAniversario"])
    var isAnniversary: Boolean? = false

    @SerializedName(value = "pasoSextoPedido", alternate = ["PasoSextoPedido"])
    var isPasoSextoPedido: Boolean? = false

    @SerializedName(value = "revistaDigitalSuscripcion", alternate = ["RevistaDigitalSuscripcion"])
    var revistaDigitalSuscripcion: Int? = 0

    @SerializedName(value = "UrlBannerGanaMas", alternate = ["urlBannerGanaMas"])
    var bannerGanaMas: String? = null

    @SerializedName(value = "cuponEstado", alternate = ["CuponEstado"])
    var cuponEstado: Int? = 0

    @SerializedName(value = "cuponPctDescuento", alternate = ["CuponPctDescuento"])
    var cuponPctDescuento: Double? = 0.0

    @SerializedName(value = "cuponMontoMaxDscto", alternate = ["CuponMontoMaxDscto"])
    var cuponMontoMaxDscto: Double? = 0.0

    @SerializedName(value = "tieneGND", alternate = ["TieneGND"])
    var isTieneGND: Boolean? = false

    @SerializedName(value = "codigoSeccion", alternate = ["CodigoSeccion"])
    var codigoSeccion: String? = null

    @SerializedName(value = "cuponTipoCondicion", alternate = ["CuponTipoCondicion"])
    var tipoCondicion: Int? = 0

    /****/

    @SerializedName(value = "codigoPrograma", alternate = ["CodigoPrograma"])
    var codigoPrograma: String? = null

    @SerializedName(value = "consecutivoNueva", alternate = ["ConsecutivoNueva"])
    var consecutivoNueva: Int? = null

    @SerializedName(value = "consultoraNueva", alternate = ["ConsultoraNueva"])
    var consultoraNueva: Int? = null

    @SerializedName(value = "montoMinimoPedido", alternate = ["MontoMinimoPedido"])
    var montoMinimoPedido: Double? = null

    @SerializedName(value = "montoMaximoPedido", alternate = ["MontoMaximoPedido"])
    var montoMaximoPedido: Double? = null

    @SerializedName(value = "horaInicioNoFacturable", alternate = ["HoraInicioNoFacturable"])
    var horaInicioNoFacturable: String? = null

    @SerializedName(value = "horaCierreNoFacturable", alternate = ["HoraCierreNoFacturable"])
    var horaCierreNoFacturable: String? = null

    @SerializedName(value = "horaInicio", alternate = ["HoraInicio"])
    var horaInicio: String? = null

    @SerializedName(value = "codigosConcursos", alternate = ["CodigosConcursos"])
    var codigosConcursos: String? = null

    @SerializedName(value = "zonaValida", alternate = ["ZonaValida"])
    var isZonaValida: Boolean? = null

    @SerializedName(value = "diasAntes", alternate = ["DiasAntes"])
    var diasAntes: Int? = null

    @SerializedName(value = "segmentoInternoID", alternate = ["SegmentoInternoID"])
    var segmentoInternoID: Int? = null

    @SerializedName(value = "prolSinStock", alternate = ["PROLSinStock"])
    var isProlSinStock: Boolean? = null

    @SerializedName(value = "validacionAbierta", alternate = ["ValidacionAbierta"])
    var isValidacionAbierta: Boolean? = null

    @SerializedName(value = "validacionInteractiva", alternate = ["ValidacionInteractiva"])
    var isValidacionInteractiva: Boolean? = null

    @SerializedName(value = "numeroDocumento", alternate = ["NumeroDocumento"])
    var numeroDocumento: String? = null

    @SerializedName(value = "indicadorGPRSB", alternate = ["IndicadorGPRSB"])
    var indicadorGPRSB: Int? = null

    @SerializedName(value = "horaFinPortal", alternate = ["HoraFinPortal"])
    var horaFinPortal: String? = null

    @SerializedName(value = "esConsultoraOficina", alternate = ["EsConsultoraOficina"])
    var isConsultoraOficina: Boolean? = null

    @SerializedName(value = "SegmentoConstancia", alternate = ["segmentoConstancia"])
    var segmentoConstancia: String? = null

    @SerializedName(value = "EsLider", alternate = ["esLider"])
    var esLider: Int? = null

    @SerializedName(value = "NivelLider", alternate = ["nivelLider"])
    var nivelLider: Int? = null

    @SerializedName(value = "DescripcionNivelLider", alternate = ["descripcionNivelLider"])
    var descripcionNivelLider: String? = null

    @SerializedName(value = "CampaniaInicioLider", alternate = ["campaniaInicioLider"])
    var campaniaInicioLider: String? = null

    @SerializedName(value = "SeccionGestionLider", alternate = ["seccionGestionLider"])
    var seccionGestionLider: String? = null

    @SerializedName(value = "IndicadorContratoCredito", alternate = ["indicadorContratoCredito"])
    var indicadorContratoCredito: Int? = null

    @SerializedName(value = "CambioCorreoPendiente", alternate = ["cambioCorreoPendiente"])
    var isCambioCorreoPendiente: Boolean? = null

    @SerializedName(value = "CorreoPendiente", alternate = ["correoPendiente"])
    var correoPendiente: String? = null

    @SerializedName(value = "PrimerNombre", alternate = ["primerNombre"])
    var primerNombre: String? = null

    @SerializedName(value = "PuedeActualizar", alternate = ["puedeActualizar"])
    var isPuedeActualizar: Boolean? = null

    @SerializedName(value = "PuedeActualizarEmail", alternate = ["puedeActualizarEmail"])
    var isPuedeActualizarEmail: Boolean? = null

    @SerializedName(value = "PuedeActualizarCelular", alternate = ["puedeActualizarCelular"])
    var isPuedeActualizarCelular: Boolean? = null

    @SerializedName(value = "MostrarBuscador", alternate = ["mostrarBuscador"])
    var isMostrarBuscador: Boolean? = null

    @SerializedName(value = "CaracteresBuscador", alternate = ["caracteresBuscador"])
    var caracteresBuscador: Int? = null

    @SerializedName(value = "CaracteresBuscadorMostrar", alternate = ["caracteresBuscadorMostrar"])
    var caracteresBuscadorMostrar: Int? = null

    @SerializedName(value = "TotalResultadosBuscador", alternate = ["totalResultadosBuscador"])
    var totalResultadosBuscador: Int? = null

    @SerializedName(value = "Lider", alternate = ["lider"])
    var lider: Int? = null

    @SerializedName(value = "RDEsActiva", alternate = ["rdEsActiva"])
    var isRDEsActiva: Boolean? = null

    @SerializedName(value = "RDEsSuscrita", alternate = ["rdEsSuscrita"])
    var isRDEsSuscrita: Boolean? = null

    @SerializedName(value = "RDActivoMdo", alternate = ["rdActivoMdo"])
    var isRDActivoMdo: Boolean? = null

    @SerializedName(value = "RDTieneRDC", alternate = ["rdTieneRDC"])
    var isRDTieneRDC: Boolean? = null

    @SerializedName(value = "RDTieneRDI", alternate = ["rdTieneRDI"])
    var isRDTieneRDI: Boolean? = null

    @SerializedName(value = "RDTieneRDCR", alternate = ["rdTieneRDCR"])
    var isRDTieneRDCR: Boolean? = null

    @SerializedName(value = "DiaFacturacion", alternate = ["diaFacturacion"])
    var diaFacturacion: Int? = null

    @SerializedName(value = "IndicadorConsultoraDummy", alternate = ["indicadorConsultoraDummy"])
    var isIndicadorConsultoraDummy: Boolean? = null

    @SerializedName(value = "PersonalizacionesDummy", alternate = ["personalizacionesDummy"])
    var personalizacionesDummy: String? = null

    @SerializedName(value = "MostrarBotonVerTodosBuscador", alternate = ["mostrarBotonVerTodosBuscador"])
    var isMostrarBotonVerTodosBuscador: Boolean? = null

    @SerializedName(value = "AplicarLogicaCantidadBotonVerTodosBuscador", alternate = ["aplicarLogicaCantidadBotonVerTodosBuscador"])
    var isAplicarLogicaCantidadBotonVerTodosBuscador: Boolean? = null

    @SerializedName(value = "MostrarOpcionesOrdenamientoBuscador", alternate = ["mostrarOpcionesOrdenamientoBuscador"])
    var isMostrarOpcionesOrdenamientoBuscador: Boolean? = null

    @SerializedName(value = "TienePagoEnLinea", alternate = ["tienePagoEnLinea"])
    var isPagoEnLinea: Boolean? = null

    @SerializedName(value = "TieneChatbot", alternate = ["tieneChatbot"])
    var isChatBot: Boolean? = null

    @SerializedName(value = "indicadorConsultoraDigital", alternate = ["IndicadorConsultoraDigital"])
    var indicadorConsultoraDigital: Int = 0

    @SerializedName(value = "TieneMG", alternate = ["tieneMG"])
    var isTieneMG: Boolean? = null

    @SerializedName(value = "MostrarFiltrosBuscador", alternate = ["mostrarFiltrosBuscador"])
    var isMostrarFiltrosBuscador: Boolean? = null

    @SerializedName(value = "TipoIngreso", alternate = ["tipoIngreso"])
    var tipoIngreso: String? = null

    @SerializedName(value = "SegmentoDatami", alternate = ["segmentoDatami"])
    var segmentoDatami: String? = null

    @SerializedName(value = "GanaMasNativo", alternate = ["ganaMasNativo"])
    var isGanaMasNativo: Boolean = false

    @SerializedName(value = "PrimerApellido", alternate = ["primerApellido"])
    var primerApellido: String? = null

    @SerializedName(value = "FechaNacimiento", alternate = ["fechaNacimiento"])
    var fechaNacimiento: String? = null

    @SerializedName(value = "BloqueoPendiente", alternate = ["bloqueoPendiente"])
    var isBloqueoPendiente: Boolean = false

    @SerializedName(value = "ActualizacionDatos", alternate = ["actualizacionDatos"])
    var isActualizacionDatos: Boolean = false

    @SerializedName(value = "NotificacionesWhatsapp", alternate = ["notificacionesWhatsapp"])
    var isNotificacionesWhatsapp: Boolean = true

    @SerializedName(value = "activaNotificacionesWhatsapp",alternate =["ActivaNotificacionesWhatsapp"] )
    var isShowCheckWhatsapp: Boolean = true

    @SerializedName(value = "EsUltimoDiaFacturacion", alternate = ["esUltimoDiaFacturacion"])
    var isUltimoDiaFacturacion: Boolean = false

    @SerializedName(value = "PagoContado", alternate = ["pagoContado"])
    var isPagoContado: Boolean = false

    @SerializedName(value = "esBrillante", alternate = ["EsBrillante"])
    var isBrillante: Boolean = false

    @SerializedName(value = "MontoMaximoDesviacion", alternate = ["montoMaximoDesviacion"])
    var montoMaximoDesviacion: Double? = null

    @SerializedName(value = "LoginSiguienteCampania", alternate = ["loginSiguienteCampania"])
    var loginSiguienteCampania: String? = null

    @SerializedName(value = "facturarPedidoFM", alternate = ["FacturarPedidoFM"])
    var isMultipedido: Boolean = false

    @SerializedName(value = "lineaConsultora", alternate = ["LineaConsultora"])
    var lineaConsultora: String? = null

    @SerializedName(value = "Periodo", alternate = ["periodo"])
    var periodo: String? = null

    @SerializedName(value = "SemanaPeriodo", alternate = ["semanaPeriodo"])
    var semanaPeriodo: String? = null
}
