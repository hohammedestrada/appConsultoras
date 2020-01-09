package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.data.db.ConsultorasDatabase
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.OneToMany
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.sql.language.SQLite
import com.raizlabs.android.dbflow.structure.BaseModel
import java.io.Serializable

@Table(database = ConsultorasDatabase::class, name = UserEntity.NAME)
class UserEntity : BaseModel(), Serializable {

    companion object {
        const val ENDPOINT = "User"
        const val NAME = "User"
    }

    @Column(name = "ConsultantId")
    @PrimaryKey
    @SerializedName(value = "consultantId", alternate = ["ConsultantId"])
    var consultantId: String? = null

    @Column(name = "CountryId")
    @SerializedName(value = "countryId", alternate = ["CountryId"])
    var countryId: Int? = 0

    @Column(name = "CountryISO")
    @SerializedName(value = "countryISO", alternate = ["CountryISO"])
    var countryISO: String? = null

    @Column(name = "CountryMoneySymbol")
    @SerializedName(value = "countryMoneySymbol", alternate = ["CountryMoneySymbol"])
    var countryMoneySymbol: String? = null

    @Column(name = "ConsultantCode")
    @SerializedName(value = "consultantCode", alternate = ["ConsultantCode"])
    var consultantCode: String? = null

    @Column(name = "ConsultantAssociateId")
    @SerializedName(value = "consultantAssociateId", alternate = ["ConsultantAssociateId"])
    var consultantAssociateId: String? = null

    @Column(name = "ConsultoraAsociada")
    @SerializedName(value = "consultoraAsociada", alternate = ["ConsultoraAsociada"])
    var consultoraAsociada: String? = null

    @Column(name = "UserCode")
    @SerializedName(value = "userCode", alternate = ["UserCode"])
    var userCode: String? = null

    @Column(name = "UserTest")
    @SerializedName(value = "userTest", alternate = ["UserTest"])
    var isUserTest: Boolean = false

    @Column(name = "UserType")
    @SerializedName(value = "userType", alternate = ["UserType"])
    var userType: Int? = 0

    @Column(name = "ConsultantName")
    @SerializedName(value = "consultantName", alternate = ["ConsultantName"])
    var consultantName: String? = null

    @Column(name = "Alias")
    @SerializedName(value = "alias", alternate = ["Alias"])
    var alias: String? = null

    @Column(name = "Email")
    @SerializedName(value = "email", alternate = ["Email"])
    var email: String? = null

    @Column(name = "Phone")
    @SerializedName(value = "phone", alternate = ["Phone"])
    var phone: String? = null

    @Column(name = "OtherPhone")
    @SerializedName(value = "otherPhone", alternate = ["OtherPhone"])
    var otherPhone: String? = null

    @Column(name = "Mobile")
    @SerializedName(value = "mobile", alternate = ["Mobile"])
    var mobile: String? = null

    @Column(name= "CambioCelularPendiente")
    @SerializedName(value = "CambioCelularPendiente", alternate = ["cambioCelularPendiente"])
    var isCambioCelularPendiente: Boolean? = null

    @Column(name= "CelularPendiente")
    @SerializedName(value = "CelularPendiente", alternate = ["celularPendiente"])
    var celularPendiente: String? = null

    @Column(name = "PhotoProfile")
    @SerializedName(value = "photoProfile", alternate = ["UrlImagen"])
    var photoProfile: String? = null

    @Column(name = "Campaing")
    @SerializedName(value = "campaing", alternate = ["Campaing"])
    var campaing: String? = null

    @Column(name = "NumberOfCampaings")
    @SerializedName(value = "numberOfCampaings", alternate = ["NumberOfCampaings"])
    var numberOfCampaings: String? = null

    @Column(name = "RegionID")
    @SerializedName(value = "regionID", alternate = ["RegionID"])
    var regionID: String? = null

    @Column(name = "RegionCode")
    @SerializedName(value = "regionCode", alternate = ["RegionCode"])
    var regionCode: String? = null

    @Column(name = "ZoneID")
    @SerializedName(value = "zoneID", alternate = ["ZoneID"])
    var zoneID: String? = null

    @Column(name = "ZoneCode")
    @SerializedName(value = "zoneCode", alternate = ["ZoneCode"])
    var zoneCode: String? = null

    @Column(name = "ExpirationDate")
    @SerializedName(value = "expirationDate", alternate = ["ExpirationDate"])
    var expirationDate: String? = null

    @Column(name = "DayProl")
    @SerializedName(value = "dayProl", alternate = ["DayProl"])
    var isDayProl: Boolean = false

    @Column(name = "ConsultantAcceptDA")
    @SerializedName(value = "consultantAcceptDA", alternate = ["ConsultantAcceptDA"])
    var consultantAcceptDA: Int? = 0

    @Column(name = "UrlBelcorpChat")
    @SerializedName(value = "urlBelcorpChat", alternate = ["UrlBelcorpChat"])
    var urlBelcorpChat: String? = null

    @Column(name = "BillingStartDate")
    @SerializedName(value = "billingStartDate", alternate = ["BillingStartDate"])
    var billingStartDate: String? = null

    @Column(name = "BillingEndDate")
    @SerializedName(value = "fechaFinFacturacion", alternate = ["FechaFinFacturacion"])
    var billingEndDate: String? = null

    @Column(name = "EndTime")
    @SerializedName(value = "endTime", alternate = ["EndTime"])
    var endTime: String? = null

    @Column(name = "TimeZone")
    @SerializedName(value = "timeZone", alternate = ["TimeZone"])
    var timeZone: String? = null

    @Column(name = "ClosingDays")
    @SerializedName(value = "closingDays", alternate = ["ClosingDays"])
    var closingDays: Int? = 0

    @Column(name = "HasDayOffer")
    @SerializedName(value = "hasDayOffer", alternate = ["HasDayOffer"])
    var isHasDayOffer: Boolean = false

    @Column(name = "ShowRoom")
    @SerializedName(value = "showRoom", alternate = ["ShowRoom"])
    var isShowRoom: Boolean = false

    @Column(name = "AceptaTerminosCondiciones")
    @SerializedName(value = "aceptaTerminosCondiciones", alternate = ["AceptaTerminosCondiciones"])
    var isAceptaTerminosCondiciones: Boolean = false

    @Column(name = "AceptaPoliticaPrivacidad")
    @SerializedName(value = "aceptaPoliticaPrivacidad", alternate = ["AceptaPoliticaPrivacidad"])
    var isAceptaPoliticaPrivacidad: Boolean = false

    @Column(name = "DestinatariosFeedback")
    @SerializedName(value = "destinatariosFeedback", alternate = ["DestinatariosFeedback"])
    var destinatariosFeedback: String? = null

    @Column(name = "GPRMostrarBannerRechazo")
    @SerializedName(value = "GPRMostrarBannerRechazo", alternate = ["gprMostrarBannerRechazo"])
    var isShowBanner: Boolean = false

    @Column(name = "GPRBannerTitulo")
    @SerializedName(value = "GPRBannerTitulo", alternate = ["gprBannerTitulo"])
    var bannerTitle: String? = null

    @Column(name = "GPRBannerMensaje")
    @SerializedName(value = "GPRBannerMensaje", alternate = ["gprBannerMensaje"])
    var bannerMessage: String? = null

    @Column(name = "GPRBannerUrl")
    @SerializedName(value = "GPRBannerUrl", alternate = ["gprBannerUrl"])
    var bannerUrl: String? = null

    @Column(name = "GPRTextovinculo")
    @SerializedName(value = "GPRTextovinculo", alternate = ["gprTextovinculo"])
    var bannerVinculo: String? = null

    @Column(name = "Birthday")
    @SerializedName(value = "esCumpleanio", alternate = ["EsCumpleanio"])
    var isBirthday: Boolean = false

    @Column(name = "Anniversary")
    @SerializedName(value = "esAniversario", alternate = ["EsAniversario"])
    var isAnniversary: Boolean = false

    @Column(name = "PasoSextoPedido")
    @SerializedName(value = "pasoSextoPedido", alternate = ["PasoSextoPedido"])
    var isPasoSextoPedido: Boolean = false

    @Column(name = "RevistaDigitalSuscripcion")
    @SerializedName(value = "revistaDigitalSuscripcion", alternate = ["RevistaDigitalSuscripcion"])
    var revistaDigitalSuscripcion: Int? = 0

    @Column(name = "UrlBannerGanaMas")
    @SerializedName(value = "UrlBannerGanaMas", alternate = ["urlBannerGanaMas"])
    var bannerGanaMas: String? = null

    @Column(name = "CuponEstado")
    @SerializedName(value = "cuponEstado", alternate = ["CuponEstado"])
    var cuponEstado: Int? = 0

    @Column(name = "CuponPctDescuento")
    @SerializedName(value = "cuponPctDescuento", alternate = ["CuponPctDescuento"])
    var cuponPctDescuento: Double? = 0.0

    @Column(name = "CuponMontoMaxDscto")
    @SerializedName(value = "cuponMontoMaxDscto", alternate = ["CuponMontoMaxDscto"])
    var cuponMontoMaxDscto: Double? = 0.0

    @Column(name = "TieneGND")
    @SerializedName(value = "tieneGND", alternate = ["TieneGND"])
    var isTieneGND: Boolean = false

    @Column(name = "CodigoSeccion")
    @SerializedName(value = "codigoSeccion", alternate = ["CodigoSeccion"])
    var codigoSeccion: String? = null

    @Column(name = "CuponTipoCondicion")
    @SerializedName(value = "cuponTipoCondicion", alternate = ["CuponTipoCondicion"])
    var tipoCondicion: Int? = 0

    @Column(name = "CodigoPrograma")
    @SerializedName(value = "codigoPrograma", alternate = ["CodigoPrograma"])
    var codigoPrograma: String? = null

    @Column(name = "ConsecutivoNueva")
    @SerializedName(value = "consecutivoNueva", alternate = ["ConsecutivoNueva"])
    var consecutivoNueva: Int? = null

    @Column(name = "ConsultoraNueva")
    @SerializedName(value = "consultoraNueva", alternate = ["ConsultoraNueva"])
    var consultoraNueva: Int? = null

    @Column(name = "MontoMinimoPedido")
    @SerializedName(value = "montoMinimoPedido", alternate = ["MontoMinimoPedido"])
    var montoMinimoPedido: Double? = null

    @Column(name = "MontoMaximoPedido")
    @SerializedName(value = "montoMaximoPedido", alternate = ["MontoMaximoPedido"])
    var montoMaximoPedido: Double? = null

    @Column(name = "HoraInicioNoFacturable")
    @SerializedName(value = "horaInicioNoFacturable", alternate = ["HoraInicioNoFacturable"])
    var horaInicioNoFacturable: String? = null

    @Column(name = "HoraCierreNoFacturable")
    @SerializedName(value = "horaCierreNoFacturable", alternate = ["HoraCierreNoFacturable"])
    var horaCierreNoFacturable: String? = null

    @Column(name = "HoraInicio")
    @SerializedName(value = "horaInicio", alternate = ["HoraInicio"])
    var horaInicio: String? = null

    @Column(name = "CodigosConcursos")
    @SerializedName(value = "codigosConcursos", alternate = ["CodigosConcursos"])
    var codigosConcursos: String? = null

    @Column(name = "ZonaValida")
    @SerializedName(value = "zonaValida", alternate = ["ZonaValida"])
    var isZonaValida: Boolean = false

    @Column(name = "DiasAntes")
    @SerializedName(value = "diasAntes", alternate = ["DiasAntes"])
    var diasAntes: Int? = null

    @Column(name = "segmentoInternoID")
    @SerializedName(value = "segmentoInternoID", alternate = ["SegmentoInternoID"])
    var segmentoInternoID: Int? = null

    @Column(name = "PROLSinStock")
    @SerializedName(value = "prolSinStock", alternate = ["PROLSinStock"])
    var isProlSinStock: Boolean = false

    @Column(name = "ValidacionAbierta")
    @SerializedName(value = "validacionAbierta", alternate = ["ValidacionAbierta"])
    var isValidacionAbierta: Boolean = false

    @Column(name = "ValidacionInteractiva")
    @SerializedName(value = "validacionInteractiva", alternate = ["ValidacionInteractiva"])
    var isValidacionInteractiva: Boolean = false

    @Column(name = "NumeroDocumento")
    @SerializedName(value = "numeroDocumento", alternate = ["NumeroDocumento"])
    var numeroDocumento: String? = null

    @Column(name = "indicadorGPRSB")
    @SerializedName(value = "indicadorGPRSB", alternate = ["IndicadorGPRSB"])
    var indicadorGPRSB: Int? = null

    @Column(name = "horaFinPortal")
    @SerializedName(value = "horaFinPortal", alternate = ["HoraFinPortal"])
    var horaFinPortal: String? = null

    @Column(name = "EsConsultoraOficina")
    @SerializedName(value = "esConsultoraOficina", alternate = ["EsConsultoraOficina"])
    var isConsultoraOficina: Boolean = false

    @Column(name = "SegmentoConstancia")
    @SerializedName(value = "SegmentoConstancia", alternate = ["segmentoConstancia"])
    var segmentoConstancia: String? = null

    @Column(name = "EsLider")
    @SerializedName(value = "EsLider", alternate = ["esLider"])
    var esLider: Int? = null

    @Column(name= "Periodo")
    @SerializedName(value = "Periodo", alternate = ["periodo"])
    var periodo: String? = null

    @Column(name= "SemanaPeriodo")
    @SerializedName(value = "SemanaPeriodo", alternate = ["semanaPeriodo"])
    var semanaPeriodo: String? = null

    @Column(name = "NivelLider")
    @SerializedName(value = "NivelLider", alternate = ["nivelLider"])
    var nivelLider: Int? = null

    @Column(name = "DescripcionNivelLider")
    @SerializedName(value = "DescripcionNivelLider", alternate = ["descripcionNivelLider"])
    var descripcionNivelLider: String? = null

    @Column(name = "CampaniaInicioLider")
    @SerializedName(value = "CampaniaInicioLider", alternate = ["campaniaInicioLider"])
    var campaniaInicioLider: String? = null

    @Column(name = "SeccionGestionLider")
    @SerializedName(value = "SeccionGestionLider", alternate = ["seccionGestionLider"])
    var seccionGestionLider: String? = null

    @Column(name = "IndicadorContratoCredito", defaultValue = "-1")
    @SerializedName(value = "IndicadorContratoCredito", alternate = ["indicadorContratoCredito"])
    var indicadorContratoCredito: Int? = null

    @Column(name = "CambioCorreoPendiente")
    @SerializedName(value = "CambioCorreoPendiente", alternate = ["cambioCorreoPendiente"])
    var isCambioCorreoPendiente: Boolean? = null

    @Column(name = "CorreoPendiente")
    @SerializedName(value = "CorreoPendiente", alternate = ["correoPendiente"])
    var correoPendiente: String? = null

    @Column(name = "PrimerNombre")
    @SerializedName(value = "PrimerNombre", alternate = ["primerNombre"])
    var primerNombre: String? = null

    @Column(name = "PuedeActualizar")
    @SerializedName(value = "PuedeActualizar", alternate = ["puedeActualizar"])
    var isPuedeActualizar: Boolean? = null

    @Column(name = "PuedeActualizarEmail")
    @SerializedName(value = "PuedeActualizarEmail", alternate = ["puedeActualizarEmail"])
    var isPuedeActualizarEmail: Boolean? = null

    @Column(name = "PuedeActualizarCelular")
    @SerializedName(value = "PuedeActualizarCelular", alternate = ["puedeActualizarCelular"])
    var isPuedeActualizarCelular: Boolean? = null

    @Column(name = "MostrarBuscador", defaultValue = "0")
    @SerializedName(value = "MostrarBuscador", alternate = ["mostrarBuscador"])
    var isMostrarBuscador: Boolean? = null

    @Column(name = "CaracteresBuscador", defaultValue = "3")
    @SerializedName(value = "CaracteresBuscador", alternate = ["caracteresBuscador"])
    var caracteresBuscador: Int? = null

    @Column(name = "CaracteresBuscadorMostrar", defaultValue = "25")
    @SerializedName(value = "CaracteresBuscadorMostrar", alternate = ["caracteresBuscadorMostrar"])
    var caracteresBuscadorMostrar: Int? = null

    @Column(name = "TotalResultadosBuscador", defaultValue = "20")
    @SerializedName(value = "TotalResultadosBuscador", alternate = ["totalResultadosBuscador"])
    var totalResultadosBuscador: Int? = null

    @Column(name = "Lider", defaultValue = "0")
    @SerializedName(value = "Lider", alternate = ["lider"])
    var lider: Int? = null

    @Column(name = "RDEsActiva", defaultValue = "0")
    @SerializedName(value = "RDEsActiva", alternate = ["rdEsActiva"])
    var isRDEsActiva: Boolean? = null

    @Column(name = "RDEsSuscrita", defaultValue = "0")
    @SerializedName(value = "RDEsSuscrita", alternate = ["rdEsSuscrita"])
    var isRDEsSuscrita: Boolean? = null

    @Column(name = "RDActivoMdo", defaultValue = "0")
    @SerializedName(value = "RDActivoMdo", alternate = ["rdActivoMdo"])
    var isRDActivoMdo: Boolean? = null

    @Column(name = "RDTieneRDC", defaultValue = "0")
    @SerializedName(value = "RDTieneRDC", alternate = ["rdTieneRDC"])
    var isRDTieneRDC: Boolean? = null

    @Column(name = "RDTieneRDI", defaultValue = "0")
    @SerializedName(value = "RDTieneRDI", alternate = ["rdTieneRDI"])
    var isRDTieneRDI: Boolean? = null

    @Column(name = "RDTieneRDCR", defaultValue = "0")
    @SerializedName(value = "RDTieneRDCR", alternate = ["rdTieneRDCR"])
    var isRDTieneRDCR: Boolean? = null

    @Column(name = "DiaFacturacion")
    @SerializedName(value = "DiaFacturacion", alternate = ["diaFacturacion"])
    var diaFacturacion: Int? = null

    @Column(name = "IndicadorConsultoraDummy")
    @SerializedName(value = "IndicadorConsultoraDummy", alternate = ["indicadorConsultoraDummy"])
    var isIndicadorConsultoraDummy: Boolean? = null

    @Column(name = "PersonalizacionesDummy")
    @SerializedName(value = "PersonalizacionesDummy", alternate = ["personalizacionesDummy"])
    var personalizacionesDummy: String? = null

    @Column(name = "MostrarBotonVerTodosBuscador")
    @SerializedName(value = "MostrarBotonVerTodosBuscador", alternate = ["mostrarBotonVerTodosBuscador"])
    var isMostrarBotonVerTodosBuscador: Boolean? = null

    @Column(name = "AplicarLogicaCantidadBotonVerTodosBuscador")
    @SerializedName(value = "AplicarLogicaCantidadBotonVerTodosBuscador", alternate = ["aplicarLogicaCantidadBotonVerTodosBuscador"])
    var isAplicarLogicaCantidadBotonVerTodosBuscador: Boolean? = null

    @Column(name = "MostrarOpcionesOrdenamientoBuscador")
    @SerializedName(value = "MostrarOpcionesOrdenamientoBuscador", alternate = ["mostrarOpcionesOrdenamientoBuscador"])
    var isMostrarOpcionesOrdenamientoBuscador: Boolean? = null

    @Column(name = "TieneMG")
    @SerializedName(value = "TieneMG", alternate = ["tieneMG"])
    var isTieneMG: Boolean? = null

    @Column(name = "MostrarFiltrosBuscador")
    @SerializedName(value = "MostrarFiltrosBuscador", alternate = ["mostrarFiltrosBuscador"])
    var isMostrarFiltrosBuscador: Boolean? = null

    @Column(name = "TienePagoEnLinea")
    @SerializedName(value = "TienePagoEnLinea", alternate = ["tienePagoEnLinea"])
    var isPagoEnLinea: Boolean? = null

    @Column(name = "TieneChatBot")
    @SerializedName(value = "TieneChatbot", alternate = ["tieneChatbot"])
    var isChatBot: Boolean? = null

    @Column(name = "indicadorConsultoraDigital")
    @SerializedName(value = "indicadorConsultoraDigital", alternate = ["indConsultoraDigital"])
    var indicadorConsultoraDigital: Int = 0

    @Column(name = "TipoIngreso")
    @SerializedName(value = "TipoIngreso", alternate = ["tipoIngreso"])
    var tipoIngreso: String? = null

    @Column(name= "SegmentoDatami")
    @SerializedName(value = "SegmentoDatami", alternate = ["segmentoDatami"])
    var segmentoDatami: String? = null

    @Column(name= "GanaMasNativo")
    @SerializedName(value = "GanaMasNativo", alternate = ["ganaMasNativo"])
    var isGanaMasNativo: Boolean = false

    @Column(name= "PrimerApellido")
    @SerializedName(value = "PrimerApellido", alternate = ["primerApellido"])
    var primerApellido: String? = null

    @Column(name= "FechaNacimiento")
    @SerializedName(value = "FechaNacimiento", alternate = ["fechaNacimiento"])
    var fechaNacimiento: String? = null

    @Column(name= "BloqueoPendiente")
    @SerializedName(value = "BloqueoPendiente", alternate = ["bloqueoPendiente"])
    var isBloqueoPendiente: Boolean = false

    @Column(name= "ActualizacionDatos")
    @SerializedName(value = "ActualizacionDatos", alternate = ["actualizacionDatos"])
    var actualizacionDatos: Int = 0

    @Column(name = "NotificacionesWhatsapp")
    @SerializedName(value = "NotificacionesWhatsapp", alternate = ["notificacionesWhatsapp"])
    var isNotificacionesWhatsapp: Boolean = true

    @Column(name = "ShowCheckWhatsapp")
    @SerializedName(value = "ShowCheckWhatsapp",alternate =["showcheckwhatsapp"] )
    var isShowCheckWhatsapp: Boolean = true

    @Column(name= "EsUltimoDiaFacturacion")
    @SerializedName(value = "EsUltimoDiaFacturacion", alternate = ["esUltimoDiaFacturacion"])
    var isUltimoDiaFacturacion: Boolean = false

    @Column(name= "PagoContado")
    @SerializedName(value = "PagoContado", alternate = ["pagoContado"])
    var isPagoContado: Boolean = false

    @Column(name= "esBrillante")
    @SerializedName(value = "esBrillante", alternate = ["EsBrillante"])
    var isBrillante: Boolean = false

    @Column(name= "flagMultipedido")
    @SerializedName(value = "facturarPedidoFM", alternate = ["FacturarPedidoFM"])
    var isMultipedido: Boolean = false

    @Column(name= "lineaConsultora")
    @SerializedName(value = "lineaConsultora", alternate = ["LineaConsultora"])
    var lineaConsultora: String? = null

    @Column(name= "MontoMaximoDesviacion")
    @SerializedName(value = "MontoMaximoDesviacion", alternate = ["montoMaximoDesviacion"])
    var montoMaximoDesviacion: Double? = null

    @Column(name= "SiguienteCampania")
    @SerializedName(value = "SiguienteCampania", alternate = ["siguienteCampania"])
    var nextCampania: String? = null

    var userResume: List<UserDetailEntity>? = null
        @OneToMany(methods = [(OneToMany.Method.ALL)], isVariablePrivate = true)
        get() {
            if (field == null || field!!.isEmpty()) {
                this.userResume = SQLite.select()
                    .from(UserDetailEntity::class.java)
                    .queryList()
            }
            return field
        }
}
