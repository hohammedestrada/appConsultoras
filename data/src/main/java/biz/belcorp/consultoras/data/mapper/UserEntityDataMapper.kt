package biz.belcorp.consultoras.data.mapper

import java.util.ArrayList

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.entity.UserEntity
import biz.belcorp.consultoras.domain.entity.Login
import biz.belcorp.consultoras.domain.entity.LoginDetail
import biz.belcorp.consultoras.domain.entity.User

/**
 * Clase encarga de realizar el mapeo de la entidad loginOnline(tabla o json) a
 * una entidad del dominio
 *
 * @version 1.0
 * @since 2017-04-14
 */
@Singleton
class UserEntityDataMapper @Inject
internal constructor() {

    /**
     * Transforma la entidad a una entidad del dominio.
     *
     * @param input Entidad tipo tabla o json
     * @return object Entidad de dominio
     */
    fun transform(input: UserEntity?): User? {
        return input?.let {
            User().apply {
                countryId = it.countryId
                countryISO = it.countryISO
                countryMoneySymbol = it.countryMoneySymbol
                consultantId = it.consultantId
                userCode = it.userCode
                consultantCode = it.consultantCode
                isUserTest = it.isUserTest
                campaing = it.campaing
                numberOfCampaings = it.numberOfCampaings
                regionID = it.regionID
                regionCode = it.regionCode
                zoneID = it.zoneID
                zoneCode = it.zoneCode
                consultantName = it.consultantName
                alias = it.alias
                expirationDate = it.expirationDate
                isDayProl = it.isDayProl
                consultantAcceptDA = it.consultantAcceptDA
                urlBelcorpChat = it.urlBelcorpChat
                userType = it.userType
                billingStartDate = it.billingStartDate
                billingEndDate = it.billingEndDate
                endTime = it.endTime
                timeZone = it.timeZone
                closingDays = it.closingDays
                email = it.email
                phone = it.phone
                mobile = it.mobile
                isHasDayOffer = it.isHasDayOffer
                consultantAssociateId = it.consultantAssociateId
                otherPhone = it.otherPhone
                photoProfile = it.photoProfile
                isShowRoom = it.isShowRoom
                isAceptaTerminosCondiciones = it.isAceptaTerminosCondiciones
                isAceptaPoliticaPrivacidad = it.isAceptaPoliticaPrivacidad
                destinatariosFeedback = it.destinatariosFeedback

                isShowBanner = it.isShowBanner
                bannerTitle = it.bannerTitle
                bannerMessage = it.bannerMessage
                bannerUrl = it.bannerUrl
                bannerVinculo = it.bannerVinculo

                isBirthday = it.isBirthday
                isAnniversary = it.isAnniversary
                isPasoSextoPedido = it.isPasoSextoPedido

                revistaDigitalSuscripcion = it.revistaDigitalSuscripcion
                bannerGanaMas = it.bannerGanaMas
                isTieneGND = it.isTieneGND
                tipoCondicion = it.tipoCondicion

                codigoPrograma = it.codigoPrograma
                consecutivoNueva = it.consecutivoNueva
                consultoraNueva = it.consultoraNueva
                montoMinimoPedido = it.montoMinimoPedido
                montoMaximoPedido = it.montoMaximoPedido
                horaInicioNoFacturable = it.horaInicioNoFacturable
                horaCierreNoFacturable = it.horaCierreNoFacturable
                horaInicio = it.horaInicio
                codigosConcursos = it.codigosConcursos
                isZonaValida = it.isZonaValida
                diasAntes = it.diasAntes
                segmentoInternoID = it.segmentoInternoID
                isProlSinStock = it.isProlSinStock
                isValidacionAbierta = it.isValidacionAbierta
                isValidacionInteractiva = it.isValidacionInteractiva
                numeroDocumento = it.numeroDocumento
                indicadorGPRSB = it.indicadorGPRSB
                consultoraAsociada = it.consultoraAsociada
                horaFinPortal = it.horaFinPortal
                esConsultoraOficina = it.isConsultoraOficina
                segmentoConstancia = it.segmentoConstancia
                esLider = it.esLider
                periodo = it.periodo
                semanaPeriodo = it.semanaPeriodo
                descripcionNivelLider = it.descripcionNivelLider
                nivelLider = it.nivelLider
                campaniaInicioLider = it.campaniaInicioLider
                seccionGestionLider = it.seccionGestionLider
                indicadorContratoCredito = it.indicadorContratoCredito

                codigoSeccion = it.codigoSeccion

                isCambioCorreoPendiente = it.isCambioCorreoPendiente
                correoPendiente = it.correoPendiente
                primerNombre = it.primerNombre
                isPuedeActualizar = it.isPuedeActualizar
                isPuedeActualizarEmail = it.isPuedeActualizarEmail
                isPuedeActualizarCelular = it.isPuedeActualizarCelular

                isMostrarBuscador = it.isMostrarBuscador
                caracteresBuscador = it.caracteresBuscador
                caracteresBuscadorMostrar = it.caracteresBuscadorMostrar
                totalResultadosBuscador = it.totalResultadosBuscador
                lider = it.lider
                isRDEsActiva = it.isRDEsActiva
                isRDEsSuscrita = it.isRDEsSuscrita
                isRDActivoMdo = it.isRDActivoMdo
                isRDTieneRDC = it.isRDTieneRDC
                isRDTieneRDI = it.isRDTieneRDI
                isRDTieneRDCR = it.isRDTieneRDCR
                diaFacturacion = it.diaFacturacion
                isIndicadorConsultoraDummy = it.isIndicadorConsultoraDummy
                personalizacionesDummy = it.personalizacionesDummy
                isMostrarBotonVerTodosBuscador = it.isMostrarBotonVerTodosBuscador
                isAplicarLogicaCantidadBotonVerTodosBuscador = it.isAplicarLogicaCantidadBotonVerTodosBuscador
                isMostrarOpcionesOrdenamientoBuscador = it.isMostrarOpcionesOrdenamientoBuscador

                isTieneMG = it.isTieneMG

                isMostrarFiltrosBuscador = it.isMostrarFiltrosBuscador

                isPagoEnLinea = it.isPagoEnLinea
                isChatBot = it.isChatBot
                indicadorConsultoraDigital = it.indicadorConsultoraDigital
                tipoIngreso = it.tipoIngreso
                segmentoDatami = it.segmentoDatami
                isGanaMasNativo = it.isGanaMasNativo
                primerApellido = it.primerApellido
                fechaNacimiento = it.fechaNacimiento
                bloqueoPendiente = it.isBloqueoPendiente
                actualizacionDatos = it.actualizacionDatos
                isUltimoDiaFacturacion = it.isUltimoDiaFacturacion
                isNotificacionesWhatsapp = it.isNotificacionesWhatsapp
                isActivaNotificaconesWhatsapp = it.isShowCheckWhatsapp
                celularPendiente = it.celularPendiente
                isCambioCelularPendiente = it.isCambioCelularPendiente
                isPagoContado = it.isPagoContado
                isBrillante = it.isBrillante
                isMultipedido = it.isMultipedido
                lineaConsultora = it.lineaConsultora
                montoMaximoDesviacion = it.montoMaximoDesviacion
                nextCampania = it.nextCampania

                val detailList = ArrayList<LoginDetail>()

                it.userResume?.forEach { loginDetail ->
                    val userDetail = LoginDetail(loginDetail.detailType, loginDetail.detailDescription)
                    userDetail.value = loginDetail.value
                    userDetail.isState = loginDetail.isState
                    userDetail.amount = loginDetail.amount
                    userDetail.name = loginDetail.name

                    detailList.add(userDetail)
                }

                detail = detailList

            }
        }
    }

    /**
     * Transforma la entidad de dominio a una entidad.
     *
     * @param input Entidad de dominio
     * @return object Entidad
     */
    fun transform(input: User?): UserEntity? {
        return input?.let {
            UserEntity().apply {
                countryId = it.countryId
                countryISO = it.countryISO
                countryMoneySymbol = it.countryMoneySymbol
                consultantId = it.consultantId
                userCode = it.userCode
                consultantCode = it.consultantCode
                isUserTest = it.isUserTest
                campaing = it.campaing
                numberOfCampaings = it.numberOfCampaings
                regionID = it.regionID
                regionCode = it.regionCode
                zoneID = it.zoneID
                zoneCode = it.zoneCode
                consultantName = it.consultantName
                alias = it.alias
                expirationDate = it.expirationDate
                isDayProl = it.isDayProl
                consultantAcceptDA = it.consultantAcceptDA
                urlBelcorpChat = it.urlBelcorpChat
                userType = it.userType
                billingStartDate = it.billingStartDate
                billingEndDate = it.billingEndDate
                endTime = it.endTime
                timeZone = it.timeZone
                closingDays = it.closingDays
                email = it.email
                phone = it.phone
                mobile = it.mobile
                isHasDayOffer = it.isHasDayOffer
                consultantAssociateId = it.consultantAssociateId
                otherPhone = it.otherPhone
                photoProfile = it.photoProfile
                isShowRoom = it.isShowRoom
                isAceptaTerminosCondiciones = it.isAceptaTerminosCondiciones
                isAceptaPoliticaPrivacidad = it.isAceptaPoliticaPrivacidad
                destinatariosFeedback = it.destinatariosFeedback

                isShowBanner = it.isShowBanner
                bannerTitle = it.bannerTitle
                bannerMessage = it.bannerMessage
                bannerUrl = it.bannerUrl
                bannerVinculo = it.bannerVinculo

                isBirthday = it.isBirthday
                isAnniversary = it.isAnniversary
                isPasoSextoPedido = it.isPasoSextoPedido

                revistaDigitalSuscripcion = it.revistaDigitalSuscripcion
                bannerGanaMas = it.bannerGanaMas
                isTieneGND = it.isTieneGND
                tipoCondicion = it.tipoCondicion

                codigoPrograma = it.codigoPrograma
                consecutivoNueva = it.consecutivoNueva
                consultoraNueva = it.consultoraNueva
                montoMinimoPedido = it.montoMinimoPedido
                montoMaximoPedido = it.montoMaximoPedido
                horaInicioNoFacturable = it.horaInicioNoFacturable
                horaCierreNoFacturable = it.horaCierreNoFacturable
                horaInicio = it.horaInicio
                codigosConcursos = it.codigosConcursos
                isZonaValida = it.isZonaValida
                diasAntes = it.diasAntes
                segmentoInternoID = it.segmentoInternoID
                isProlSinStock = it.isProlSinStock
                isValidacionAbierta = it.isValidacionAbierta
                isValidacionInteractiva = it.isValidacionInteractiva
                numeroDocumento = it.numeroDocumento
                indicadorGPRSB = it.indicadorGPRSB
                consultoraAsociada = it.consultoraAsociada
                horaFinPortal = it.horaFinPortal

                codigoSeccion = it.codigoSeccion
                isConsultoraOficina = it.esConsultoraOficina
                segmentoConstancia = it.segmentoConstancia
                esLider = it.esLider
                periodo = it.periodo
                semanaPeriodo = it.semanaPeriodo
                descripcionNivelLider = it.descripcionNivelLider
                nivelLider = it.nivelLider
                campaniaInicioLider = it.campaniaInicioLider
                seccionGestionLider = it.seccionGestionLider
                indicadorContratoCredito = it.indicadorContratoCredito

                isCambioCorreoPendiente = it.isCambioCorreoPendiente
                correoPendiente = it.correoPendiente
                primerNombre = it.primerNombre
                isPuedeActualizar = it.isPuedeActualizar
                isPuedeActualizarEmail = it.isPuedeActualizarEmail
                isPuedeActualizarCelular = it.isPuedeActualizarCelular

                isMostrarBuscador = it.isMostrarBuscador
                caracteresBuscador = it.caracteresBuscador
                caracteresBuscadorMostrar = it.caracteresBuscadorMostrar
                totalResultadosBuscador = it.totalResultadosBuscador
                lider = it.lider
                isRDEsActiva = it.isRDEsActiva
                isRDEsSuscrita = it.isRDEsSuscrita
                isRDActivoMdo = it.isRDActivoMdo
                isRDTieneRDC = it.isRDTieneRDC
                isRDTieneRDI = it.isRDTieneRDI
                isRDTieneRDCR = it.isRDTieneRDCR
                diaFacturacion = it.diaFacturacion

                isIndicadorConsultoraDummy = it.isIndicadorConsultoraDummy
                personalizacionesDummy = it.personalizacionesDummy

                isMostrarBotonVerTodosBuscador = it.isMostrarBotonVerTodosBuscador
                isAplicarLogicaCantidadBotonVerTodosBuscador = it.isAplicarLogicaCantidadBotonVerTodosBuscador
                isMostrarOpcionesOrdenamientoBuscador = it.isMostrarOpcionesOrdenamientoBuscador
                isChatBot = it.isChatBot
                indicadorConsultoraDigital = it.indicadorConsultoraDigital
                isTieneMG = it.isTieneMG

                isMostrarFiltrosBuscador = it.isMostrarFiltrosBuscador

                isPagoEnLinea = it.isPagoEnLinea

                tipoIngreso = it.tipoIngreso
                segmentoDatami = it.segmentoDatami
                isGanaMasNativo = it.isGanaMasNativo
                primerApellido = it.primerApellido
                fechaNacimiento = it.fechaNacimiento
                isBloqueoPendiente = it.bloqueoPendiente
                actualizacionDatos = it.actualizacionDatos
                isNotificacionesWhatsapp = it.isNotificacionesWhatsapp
                isShowCheckWhatsapp = it.isActivaNotificaconesWhatsapp

                isUltimoDiaFacturacion = it.isUltimoDiaFacturacion
                isCambioCelularPendiente = it.isCambioCelularPendiente
                celularPendiente = it.celularPendiente
                isPagoContado = it.isPagoContado
                isBrillante = it.isBrillante
                isMultipedido = it.isMultipedido
                lineaConsultora = it.lineaConsultora
                montoMaximoDesviacion = it.montoMaximoDesviacion
                nextCampania = it.nextCampania

            }
        }
    }

    /**
     * Transforma la entidad a una entidad del dominio.
     *
     * @param input Entidad tipo tabla o json
     * @return object Entidad de dominio
     */
    fun transformToLogin(input: UserEntity?): Login? {

        return input?.let {
            Login().apply {
                countryId = it.countryId
                countryISO = it.countryISO
                countryMoneySymbol = it.countryMoneySymbol
                consultantID = it.consultantId?.toInt()
                userCode = it.userCode
                consultantCode = it.consultantCode
                userTest = it.isUserTest.toString()
                campaing = it.campaing
                numberOfCampaings = it.numberOfCampaings
                regionID = it.regionID
                regionCode = it.regionCode
                zoneID = it.zoneID
                zoneCode = it.zoneCode
                consultantName = it.consultantName
                alias = it.alias ?: ""
                expirationDate = it.expirationDate
                isDayProl = it.isDayProl
                consultantAcceptDA = it.consultantAcceptDA
                urlBelcorpChat = it.urlBelcorpChat
                userType = it.userType
                billingStartDate = it.billingStartDate
                billingEndDate = it.billingEndDate
                endTime = it.endTime
                timeZone = it.timeZone
                closingDays = it.closingDays
                email = it.email
                phone = it.phone
                mobile = it.mobile
                isCambioCelularPendiente = it.isCambioCelularPendiente
                celularPendiente = it.celularPendiente
                hasDayOffer = it.isHasDayOffer.toString()
                consultantAssociateID = it.consultantAssociateId
                otherPhone = it.otherPhone
                photoProfile = it.photoProfile
                isShowRoom = it.isShowRoom
                isAceptaTerminosCondiciones = it.isAceptaTerminosCondiciones
                isAceptaPoliticaPrivacidad = it.isAceptaPoliticaPrivacidad
                destinatariosFeedback = it.destinatariosFeedback

                isShowBanner = it.isShowBanner
                bannerTitle = it.bannerTitle
                bannerMessage = it.bannerMessage
                bannerUrl = it.bannerUrl
                bannerVinculo = it.bannerVinculo

                isBirthday = it.isBirthday
                isAnniversary = it.isAnniversary
                isPasoSextoPedido = it.isPasoSextoPedido

                revistaDigitalSuscripcion = it.revistaDigitalSuscripcion
                bannerGanaMas = it.bannerGanaMas
                isTieneGND = it.isTieneGND

                tipoCondicion = it.tipoCondicion
                cuponEstado = it.cuponEstado
                cuponPctDescuento = it.cuponPctDescuento
                cuponMontoMaxDscto = it.cuponMontoMaxDscto

                codigoPrograma = it.codigoPrograma
                consecutivoNueva = it.consecutivoNueva
                consultoraNueva = it.consultoraNueva
                montoMinimoPedido = it.montoMinimoPedido
                montoMaximoPedido = it.montoMaximoPedido
                horaInicioNoFacturable = it.horaInicioNoFacturable
                horaCierreNoFacturable = it.horaCierreNoFacturable
                horaInicio = it.horaInicio
                codigosConcursos = it.codigosConcursos
                isZonaValida = it.isZonaValida
                diasAntes = it.diasAntes
                segmentoInternoID = it.segmentoInternoID
                isProlSinStock = it.isProlSinStock
                isValidacionAbierta = it.isValidacionAbierta
                isValidacionInteractiva = it.isValidacionInteractiva
                numeroDocumento = it.numeroDocumento
                indicadorGPRSB = it.indicadorGPRSB
                consultoraAsociada = it.consultoraAsociada
                horaFinPortal = it.horaFinPortal
                esConsultoraOficina = it.isConsultoraOficina
                segmentoConstancia = it.segmentoConstancia
                esLider = it.esLider
                periodo = it.periodo
                semanaPeriodo = it.semanaPeriodo
                descripcionNivelLider = it.descripcionNivelLider
                nivelLider = it.nivelLider
                campaniaInicioLider = it.campaniaInicioLider
                seccionGestionLider = it.seccionGestionLider
                indicadorContratoCredito = it.indicadorContratoCredito

                codigoSeccion = it.codigoSeccion

                isCambioCorreoPendiente = it.isCambioCorreoPendiente
                correoPendiente = it.correoPendiente
                primerNombre = it.primerNombre
                isPuedeActualizar = it.isPuedeActualizar
                isPuedeActualizarEmail = it.isPuedeActualizarEmail
                isPuedeActualizarCelular = it.isPuedeActualizarCelular

                isMostrarBuscador = it.isMostrarBuscador
                caracteresBuscador = it.caracteresBuscador
                caracteresBuscadorMostrar = it.caracteresBuscadorMostrar
                totalResultadosBuscador = it.totalResultadosBuscador
                lider = it.lider
                isRDEsActiva = it.isRDEsActiva
                isRDEsSuscrita = it.isRDEsSuscrita
                isRDActivoMdo = it.isRDActivoMdo
                isRDTieneRDC = it.isRDTieneRDC
                isRDTieneRDI = it.isRDTieneRDI
                isRDTieneRDCR = it.isRDTieneRDCR
                diaFacturacion = it.diaFacturacion

                isIndicadorConsultoraDummy = it.isIndicadorConsultoraDummy
                personalizacionesDummy = it.personalizacionesDummy

                isMostrarBotonVerTodosBuscador = it.isMostrarBotonVerTodosBuscador
                isAplicarLogicaCantidadBotonVerTodosBuscador = it.isAplicarLogicaCantidadBotonVerTodosBuscador
                isMostrarOpcionesOrdenamientoBuscador = it.isMostrarOpcionesOrdenamientoBuscador
                isChatBot = it.isChatBot
                indicadorConsultoraDigital = it.indicadorConsultoraDigital
                isTieneMG = it.isTieneMG
                isPagoEnLinea = it.isPagoEnLinea
                tipoIngreso = it.tipoIngreso

                isMostrarFiltrosBuscador = it.isMostrarFiltrosBuscador
                segmentoDatami = it.segmentoDatami

                isGanaMasNativo = it.isGanaMasNativo
                primerApellido = it.primerApellido
                fechaNacimiento = it.fechaNacimiento
                bloqueoPendiente = it.isBloqueoPendiente
                actualizacionDatos = it.actualizacionDatos
                checkEnviarWhatsaap = if (it.isNotificacionesWhatsapp) 1 else 0
                showCheckWhatsapp = if (it.isShowCheckWhatsapp) 1 else 0
                isUltimoDiaFacturacion = it.isUltimoDiaFacturacion
                isPagoContado = it.isPagoContado
                isBrillante = it.isBrillante
                isMultipedido = it.isMultipedido
                lineaConsultora = it.lineaConsultora
                montoMaximoDesviacion = it.montoMaximoDesviacion
                logSiguienteCampania = it.nextCampania
                if (it.userResume != null && !it.userResume!!.isEmpty()) {

                    val detailList = ArrayList<LoginDetail>()

                    for (loginDetail in it.userResume!!) {
                        val userDetail = LoginDetail(loginDetail.detailType, loginDetail.detailDescription)
                        userDetail.value = loginDetail.value
                        userDetail.isState = loginDetail.isState
                        userDetail.amount = loginDetail.amount
                        userDetail.name = loginDetail.name

                        detailList.add(userDetail)
                    }

                    detail = detailList
                }
            }
        }
    }

}
