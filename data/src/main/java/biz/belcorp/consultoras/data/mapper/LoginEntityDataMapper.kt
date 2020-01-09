package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.LoginEntity
import biz.belcorp.consultoras.data.entity.UserDetailEntity
import biz.belcorp.consultoras.data.entity.UserEntity
import biz.belcorp.consultoras.data.entity.UserResumeEntity
import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.consultoras.domain.entity.Login
import biz.belcorp.consultoras.domain.entity.LoginDetail
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Clase encarga de realizar el mapeo de la entidad loginOnline(tabla o json) a
 * una entidad del dominio
 *
 * @version 1.0
 * @since 2017-04-14
 */
@Singleton
class LoginEntityDataMapper @Inject
internal constructor() {

    /**
     * Transforma la entidad a una entidad del dominio.
     *
     * @param input Entidad tipo tabla o json
     * @return object Entidad de dominio
     */
    fun transform(input: LoginEntity?): Login? {
        return input?.let {
            Login().apply {
                accessToken = it.accessToken
                tokenType = it.tokenType
                expiresIn = it.expiresIn
                refreshToken = it.refreshToken
                countryId = it.countryId
                countryISO = it.countryISO
                countryMoneySymbol = it.countryMoneySymbol
                consultantID = it.consultantID
                userCode = it.userCode
                consultantCode = it.consultantCode
                userTest = it.userTest
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
                hasDayOffer = it.hasDayOffer
                consultantAssociateID = it.consultantAssociateID
                otherPhone = it.otherPhone
                photoProfile = it.photoProfile
                issued = it.issued
                expires = it.expires
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

                cuponEstado = it.cuponEstado
                cuponPctDescuento = it.cuponPctDescuento
                cuponMontoMaxDscto = it.cuponMontoMaxDscto
                isTieneGND = it.isTieneGND
                codigoSeccion = it.codigoSeccion
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
                indicadorContratoCredito = it.indicadorContratoCredito ?: -1

                isCambioCorreoPendiente = it.isCambioCorreoPendiente
                correoPendiente = it.correoPendiente
                primerNombre = it.primerNombre
                isPuedeActualizar = it.isPuedeActualizar
                isPuedeActualizarEmail = it.isPuedeActualizarEmail
                isPuedeActualizarCelular = it.isPuedeActualizarCelular

                isMostrarBuscador = it.isMostrarBuscador ?: false
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
                isPagoEnLinea = it.isPagoEnLinea
                isTieneMG = it.isTieneMG
                isChatBot = it.isChatBot
                indicadorConsultoraDigital = it.indicadorConsultoraDigital
                tipoIngreso = it.tipoIngreso

                isMostrarFiltrosBuscador = it.isMostrarFiltrosBuscador
                segmentoDatami = it.segmentoDatami
                isGanaMasNativo = it.isGanaMasNativo
                primerApellido = it.primerApellido
                fechaNacimiento = it.fechaNacimiento
                checkEnviarWhatsaap = if (it.isNotificacionesWhatsapp) 1 else 0
                showCheckWhatsapp = if (it.isShowCheckWhatsapp) 1 else 0
                isUltimoDiaFacturacion = it.isUltimoDiaFacturacion
                isPagoContado = it.isPagoContado
                celularPendiente = it.celularPendiente
                isCambioCelularPendiente = it.isCambioCelularPendiente
                isBrillante = it.isBrillante
                montoMaximoDesviacion = it.montoMaximoDesviacion
                isMultipedido = it.isMultipedido
                lineaConsultora = it.lineaConsultora
                logSiguienteCampania = it.loginSiguienteCampania

            }
        }
    }

    /**
     * Transforma la entidad a una entidad del dominio.
     *
     * @param entity Entidad tipo tabla o json
     * @return object Entidad de dominio
     */
    fun transform(entity: Login?, userResumeEntity: UserResumeEntity?): Login {

        entity?.let {
            val detailList = ArrayList<LoginDetail>()

            val incentivesDetail = LoginDetail(0, userResumeEntity?.incentivos?.mensaje)
            incentivesDetail.name = userResumeEntity?.incentivos?.giftName
            incentivesDetail.amount = userResumeEntity?.incentivos?.puntosFaltantes
            incentivesDetail.isState =
                if (userResumeEntity?.incentivos?.hayIncentivos == null)
                    false
                else {
                    userResumeEntity.incentivos?.hayIncentivos
                }
            detailList.add(incentivesDetail)

            val debtsDetail = LoginDetail(1, userResumeEntity?.deudas?.mensaje)
            debtsDetail.amount = userResumeEntity?.deudas?.deudores
            debtsDetail.value = userResumeEntity?.deudas?.montoCobro
            detailList.add(debtsDetail)

            val ordersDetail = LoginDetail(2, userResumeEntity?.pedidos?.mensaje)
            ordersDetail.amount = userResumeEntity?.pedidos?.clientesCampana
            ordersDetail.name = entity.campaing
            ordersDetail.count = userResumeEntity?.pedidos?.cantidadProductos
            detailList.add(ordersDetail)

            val clientsDetail = LoginDetail(3, userResumeEntity?.clientes?.mensaje)
            clientsDetail.amount = userResumeEntity?.clientes?.total
            detailList.add(clientsDetail)

            val gananciaDetail = LoginDetail(4, userResumeEntity?.ganancias?.mensajeCabecera)
            gananciaDetail.value = userResumeEntity?.ganancias?.monto
            detailList.add(gananciaDetail)

            val estadoCuentaDetail = LoginDetail(5, it.expirationDate)
            estadoCuentaDetail.value = userResumeEntity?.estadoCuenta?.deuda
            detailList.add(estadoCuentaDetail)

            //catalogo digital y banner
            userResumeEntity?.config?.forEach { it1 ->
                if (it1.code == Constant.ACTIVAR_CATALOGO_DIGITAL) {
                    val catalogoDigitalDetail = LoginDetail(6, it1.configData?.get(Constant.CATALOGO_DIGITAL_SUB_INDEX)?.codeConfig)
                    catalogoDigitalDetail.name = it1.configData?.get(Constant.CATALOGO_DIGITAL_SUB_INDEX)?.value1
                    detailList.add(catalogoDigitalDetail)

                    val cantPedidosPendientes = LoginDetail(7, it1?.configData?.get(Constant.PEDIDOS_PENDIENTES_SUB_INDEX)?.codeConfig)
                    catalogoDigitalDetail.value = it1?.configData?.get(Constant.PEDIDOS_PENDIENTES_SUB_INDEX)?.value1?.toDouble()
                    detailList.add(cantPedidosPendientes)
                } else if (it1.code?.toLowerCase() == Constant.ACTIVAR_BANNER.toLowerCase()) {
                    it1.let { userConfig ->
                        userConfig.configData?.forEach { item ->
                            item.value1?.toInt()?.let {it1->
                                it.mostrarEnBanner = it1
                            }

                        }
                    }
                }
            }

            entity.detail = detailList

            return entity
        } ?: throw NullPointerException()

    }

    /**
     * Transforma la entidad a una entidad del dominio.
     *
     * @param input Entidad tipo tabla o json
     * @return object Entidad de dominio
     */
    fun transform(input: LoginEntity?, userResumeEntity: UserResumeEntity?): Login? {
        var mostrar = -1
        return input?.let {
            Login().apply {

                accessToken = it.accessToken
                tokenType = it.tokenType
                expiresIn = it.expiresIn
                refreshToken = it.refreshToken
                countryId = it.countryId
                countryISO = it.countryISO
                countryMoneySymbol = it.countryMoneySymbol
                consultantID = it.consultantID
                userCode = it.userCode
                consultantCode = it.consultantCode
                userTest = it.userTest
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
                hasDayOffer = it.hasDayOffer
                consultantAssociateID = it.consultantAssociateID
                otherPhone = it.otherPhone
                photoProfile = it.photoProfile
                issued = it.issued
                expires = it.expires
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

                cuponEstado = it.cuponEstado
                cuponPctDescuento = it.cuponPctDescuento
                cuponMontoMaxDscto = it.cuponMontoMaxDscto
                isTieneGND = it.isTieneGND
                codigoSeccion = it.codigoSeccion
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

                indicadorContratoCredito = it.indicadorContratoCredito ?: -1
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
                isPagoEnLinea = it.isPagoEnLinea
                isChatBot = it.isChatBot
                indicadorConsultoraDigital = it.indicadorConsultoraDigital
                isTieneMG = it.isTieneMG
                tipoIngreso = it.tipoIngreso

                isMostrarFiltrosBuscador = it.isMostrarFiltrosBuscador
                segmentoDatami = it.segmentoDatami
                isGanaMasNativo = it.isGanaMasNativo
                primerApellido = it.primerApellido
                fechaNacimiento = it.fechaNacimiento

                isUltimoDiaFacturacion = it.isUltimoDiaFacturacion
                checkEnviarWhatsaap = if (it.isNotificacionesWhatsapp) 1 else 0
                showCheckWhatsapp = if (it.isShowCheckWhatsapp) 1 else 0
                isPagoContado = it.isPagoContado
                montoMaximoDesviacion = it.montoMaximoDesviacion

                celularPendiente = it.celularPendiente
                isCambioCelularPendiente = it.isCambioCelularPendiente
                isBrillante = it.isBrillante
                logSiguienteCampania = it.loginSiguienteCampania

                isMultipedido = it.isMultipedido
                lineaConsultora = it.lineaConsultora

                val detailList = ArrayList<LoginDetail>()

                val incentivesDetail = LoginDetail(0, userResumeEntity?.incentivos?.mensaje)
                incentivesDetail.name = userResumeEntity?.incentivos?.giftName
                incentivesDetail.amount = userResumeEntity?.incentivos?.puntosFaltantes
                incentivesDetail.isState = when {
                    userResumeEntity?.incentivos?.hayIncentivos == null -> false
                    else -> userResumeEntity.incentivos?.hayIncentivos
                }
                detailList.add(incentivesDetail)

                val debtsDetail = LoginDetail(1, userResumeEntity?.deudas?.mensaje)
                debtsDetail.amount = userResumeEntity?.deudas?.deudores
                debtsDetail.value = userResumeEntity?.deudas?.montoCobro
                detailList.add(debtsDetail)

                val ordersDetail = LoginDetail(2, userResumeEntity?.pedidos?.mensaje)
                ordersDetail.amount = userResumeEntity?.pedidos?.clientesCampana
                ordersDetail.name = it.campaing
                ordersDetail.count = userResumeEntity?.pedidos?.cantidadProductos
                detailList.add(ordersDetail)

                val clientsDetail = LoginDetail(3, userResumeEntity?.clientes?.mensaje)
                clientsDetail.amount = userResumeEntity?.clientes?.total
                detailList.add(clientsDetail)

                val gananciaDetail = LoginDetail(4, userResumeEntity?.ganancias?.mensajeCabecera)
                gananciaDetail.value = userResumeEntity?.ganancias?.monto
                detailList.add(gananciaDetail)

                val estadoCuentaDetail = LoginDetail(5, it.expirationDate)
                estadoCuentaDetail.value = userResumeEntity?.estadoCuenta?.deuda
                detailList.add(estadoCuentaDetail)


                userResumeEntity?.config?.forEach { it1 ->
                    if (it1.code == Constant.ACTIVAR_CATALOGO_DIGITAL) {
                        if (it1.configData?.size ?: 0 >= 1) {
                            val catalogoDigitalDetail = LoginDetail(6, it1.configData?.get(Constant.CATALOGO_DIGITAL_SUB_INDEX)?.codeConfig)
                            catalogoDigitalDetail.name = it1.configData?.get(Constant.CATALOGO_DIGITAL_SUB_INDEX)?.value1
                            detailList.add(catalogoDigitalDetail)
                        }

                        if (it1.configData?.size ?: 0 >= 2) {
                            val cantPedidosPendientes = LoginDetail(7, it1.configData?.get(Constant.PEDIDOS_PENDIENTES_SUB_INDEX)?.codeConfig)
                            val cant: Int = it1.configData?.get(Constant.PEDIDOS_PENDIENTES_SUB_INDEX)?.value1?.toInt()?.let { it }
                                ?: -1
                            cantPedidosPendientes.value = cant.toDouble()
                            detailList.add(cantPedidosPendientes)
                        }
                    } else if (it1.code == Constant.ACTIVAR_ADC) {
                        if (it1.configData?.size ?: 0 >= 1) {
                            val puedeActualizarCelular = LoginDetail(8, it1.configData?.get(Constant.ACTUALIZAR_CELULAR_SUB_INDEX)?.code)
                            puedeActualizarCelular.value = it1.configData?.get(Constant.ACTUALIZAR_CELULAR_SUB_INDEX)?.value1?.toDouble()
                            detailList.add(puedeActualizarCelular)
                        }
                    } else if (it1.code?.toLowerCase() == Constant.ACTIVAR_BANNER.toLowerCase()) {
                        it1.let { userConfig ->
                            userConfig.configData?.forEach { item ->
                                item.value1?.toInt()?.let {it1->
                                    mostrar = it1
                                }
                            }
                        }
                    } else if (it1.code?.toLowerCase() == Constant.BLOQUEO_PENDIENTE.toLowerCase()) {
                        if (it1.configData?.size ?: 0 > 0) {
                            bloqueoPendiente = it1.configData?.get(0) != null
                        }
                    } else if (it1.code?.toLowerCase() == Constant.ACTIVAR_ACTUALIZACION_DATOS.toLowerCase()) {
                        if (it1.configData?.size ?: 0 > 0) {
                            it1.configData?.get(0)?.let{
                                actualizacionDatos = 1
                            }

                        }
                    }
                }
                mostrarEnBanner = mostrar
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
    fun transform(input: Login?): LoginEntity? {
        return input?.let {
            LoginEntity().apply {
                accessToken = it.accessToken
                tokenType = it.tokenType
                expiresIn = it.expiresIn
                refreshToken = it.refreshToken
                countryId = it.countryId
                countryISO = it.countryISO
                countryMoneySymbol = it.countryMoneySymbol
                consultantID = it.consultantID
                userCode = it.userCode
                consultantCode = it.consultantCode
                userTest = it.userTest
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
                hasDayOffer = it.hasDayOffer
                consultantAssociateID = it.consultantAssociateID
                otherPhone = it.otherPhone
                photoProfile = it.photoProfile
                issued = it.issued
                expires = it.expires
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

                cuponEstado = it.cuponEstado
                cuponPctDescuento = it.cuponPctDescuento
                cuponMontoMaxDscto = it.cuponMontoMaxDscto
                isTieneGND = it.isTieneGND
                codigoSeccion = it.codigoSeccion
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
                indicadorGPRSB = it.indicadorGPRSB
                consultoraAsociada = it.consultoraAsociada
                horaFinPortal = it.horaFinPortal
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
                isPagoEnLinea = it.isPagoEnLinea
                isChatBot = it.isChatBot
                indicadorConsultoraDigital = it.indicadorConsultoraDigital
                isTieneMG = it.isTieneMG
                tipoIngreso = it.tipoIngreso

                isMostrarFiltrosBuscador = it.isMostrarFiltrosBuscador
                segmentoDatami = it.segmentoDatami
                isGanaMasNativo = it.isGanaMasNativo
                primerApellido = it.primerApellido
                isPagoContado = it.isPagoContado
                fechaNacimiento = it.fechaNacimiento
                isBloqueoPendiente = it.bloqueoPendiente
                isActualizacionDatos = it.actualizacionDatos > 0
                isShowCheckWhatsapp = it.showCheckWhatsapp > 0
                isNotificacionesWhatsapp = it.checkEnviarWhatsaap > 0
                isUltimoDiaFacturacion = it.isUltimoDiaFacturacion
                isCambioCelularPendiente = it.isCambioCelularPendiente
                celularPendiente = it.celularPendiente
                isBrillante = it.isBrillante
                montoMaximoDesviacion = it.montoMaximoDesviacion
                isMultipedido = it.isMultipedido
                lineaConsultora = it.lineaConsultora
                loginSiguienteCampania = it.logSiguienteCampania
            }
        }
    }

    fun transformUser(input: Login?): UserEntity? {
        return input?.let {
            UserEntity().apply {

                countryId = it.countryId
                countryISO = it.countryISO
                countryMoneySymbol = it.countryMoneySymbol
                consultantId = it.consultantID.toString()
                userCode = it.userCode
                consultantCode = it.consultantCode
                isUserTest = "true" == it.userTest
                campaing = it.campaing
                numberOfCampaings = it.numberOfCampaings
                regionID = it.regionID
                regionCode = it.regionCode
                zoneID = it.zoneID
                zoneCode = it.zoneCode
                consultantName = it.consultantName
                alias = it.alias
                expirationDate = it.expirationDate
                isDayProl = it.isDayProl ?: false
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
                isHasDayOffer = "true" == it.hasDayOffer
                consultantAssociateId = it.consultantAssociateID
                otherPhone = it.otherPhone
                photoProfile = it.photoProfile
                isShowRoom = it.isShowRoom ?: false
                isAceptaTerminosCondiciones = it.isAceptaTerminosCondiciones ?: false
                isAceptaPoliticaPrivacidad = it.isAceptaPoliticaPrivacidad ?: false
                destinatariosFeedback = it.destinatariosFeedback

                isShowBanner = it.isShowBanner ?: false
                bannerTitle = it.bannerTitle
                bannerMessage = it.bannerMessage
                bannerUrl = it.bannerUrl
                bannerVinculo = it.bannerVinculo

                isBirthday = it.isBirthday ?: false
                isAnniversary = it.isAnniversary ?: false
                isPasoSextoPedido = it.isPasoSextoPedido ?: false
                revistaDigitalSuscripcion = it.revistaDigitalSuscripcion
                bannerGanaMas = it.bannerGanaMas

                cuponEstado = it.cuponEstado
                cuponPctDescuento = it.cuponPctDescuento
                cuponMontoMaxDscto = it.cuponMontoMaxDscto
                isTieneGND = it.isTieneGND ?: false
                codigoSeccion = it.codigoSeccion
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
                isZonaValida = it.isZonaValida ?: false
                diasAntes = it.diasAntes
                segmentoInternoID = it.segmentoInternoID
                isProlSinStock = it.isProlSinStock ?: false
                isValidacionAbierta = it.isValidacionAbierta ?: false
                isValidacionInteractiva = it.isValidacionInteractiva ?: false
                numeroDocumento = it.numeroDocumento
                indicadorGPRSB = it.indicadorGPRSB
                consultoraAsociada = it.consultoraAsociada
                horaFinPortal = it.horaFinPortal
                isConsultoraOficina = it.esConsultoraOficina ?: false
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
                isPagoEnLinea = it.isPagoEnLinea
                isChatBot = it.isChatBot
                indicadorConsultoraDigital = it.indicadorConsultoraDigital
                isTieneMG = it.isTieneMG
                tipoIngreso = it.tipoIngreso

                isMostrarFiltrosBuscador = it.isMostrarFiltrosBuscador
                segmentoDatami = it.segmentoDatami
                isGanaMasNativo = it.isGanaMasNativo
                primerApellido = it.primerApellido
                fechaNacimiento = it.fechaNacimiento
                isBloqueoPendiente = it.bloqueoPendiente
                actualizacionDatos = it.actualizacionDatos
                isUltimoDiaFacturacion = it.isUltimoDiaFacturacion
                isNotificacionesWhatsapp = it.checkEnviarWhatsaap > 0
                isShowCheckWhatsapp = it.showCheckWhatsapp > 0
                isPagoContado = it.isPagoContado
                isCambioCelularPendiente = it.isCambioCelularPendiente
                celularPendiente = it.celularPendiente
                isBrillante = it.isBrillante
                montoMaximoDesviacion = it.montoMaximoDesviacion
                nextCampania = it.logSiguienteCampania
                isMultipedido = it.isMultipedido
                lineaConsultora = it.lineaConsultora

                it.detail?.let { li ->
                    val detailList = ArrayList<UserDetailEntity>()
                    li.forEach { it1 ->
                        val userDetail = UserDetailEntity(it1?.detailType, it1?.detailDescription)
                        userDetail.value = it1?.value
                        userDetail.isState = it1?.isState
                        userDetail.amount = it1?.amount
                        userDetail.name = it1?.name

                        detailList.add(userDetail)

                    }
                    userResume = detailList
                } ?: run {
                    userResume = null
                }

            }
        }
    }

    /**
     * Transforma una lista de entidades a una lista de entidades de dominio
     *
     * @param list Lista de entidades
     * @return list Lista de entidades del dominio
     */
    fun transform(list: Collection<LoginEntity?>?): List<Login?>? {
        return list?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<Login>()
        }
    }

}
